# CLAUDE.md

Guidance for Claude Code when working in this repository.

---

## 1. What this project is

**胡椒 MAP / PepperNoodles** — a Taiwanese restaurant-discovery and food e-commerce platform,
originally built ~2021 as a bootcamp capstone project (資策會 EEIT23) by a team of students.

Feature areas:

| Domain | Description |
|---|---|
| 地圖 Map | OSM + Leaflet restaurant map, radius/name/tag search, LINE bot query |
| 使用者 User | Registration, email verification, CAPTCHA, friends, WebSocket chat, favourites |
| 企業 Company | Company-account registration, restaurant CRUD, menus, business hours, events |
| 餐廳 Restaurant | Reviews, replies, scores, food tags |
| 商城 Shop | Product catalogue, cart, orders, ECPay (綠界) payment, sales reports |
| 後台 Admin | Restaurant/member management, suspension, notification email, contact-us inbox |

**A refactor of the entire codebase is in progress.** See §5.

---

## 2. Legacy architecture (as committed, pre-refactor)

```
Spring Boot 2.4.4  ·  Java 11  ·  Maven
JSP + JSTL + jQuery 3.5 + Bootstrap 4.6  (server-rendered monolith, 64 JSPs)
Spring Data JPA / Hibernate 5.4  →  MS SQL Server 2019
Spring Security 5.4 (form login, BCrypt, session-based)
Spring WebSocket (STOMP) for friend chat
Vendored ECPay payment SDK
```

### Layout

```
src/main/java/com/infotran/springboot/
├── commonmodel/          25 JPA entities + AuthUserDetails(Service)  ← all entities live here
├── config/               WebSecurityConfig, WebAppConfig, ExecutorConfig
├── loginsystem/          auth, registration, food tags        (dao/ + service/Impl/)
├── userAccsystem/        profile, friends, forum, messages    (repository/ + service/inplement/)
├── companysystem/        restaurant/menu/event/hours CRUD     (dao/ + service/impl/ + validator/)
├── restaurantSearchSystem/  map + geo search                  (repository/ + service/impl/)
├── restaurantMessage/    restaurant reviews                   (dao/ + service/impl/)
├── rearsystem/           admin back office                    (dao/)
├── rearMessage/          contact-us inbox                     (dao/ + service/impl/)
├── shoppingmall/         products, cart, orders, ECPay        (dao/ + model/ + service/Impl/ + util/)
├── websocket/            STOMP chat                           (model/repository/, model/service/)
├── LineBot/              LINE messaging webhook
└── welcome/              landing page + demo data seeding

src/main/java/ecpay/payment/integration/   36 files — vendored 綠界 ECPay SDK (do not hand-edit)
src/main/webapp/WEB-INF/views/             64 JSPs
src/main/webapp/{plugins,images,css,dist,vendor,fonts}   ~130 MB of committed static assets
src/main/resources/application.properties  all config + secrets, single profile
bin/                                       committed Eclipse build output (278 files, 35 MB)
```

### Runtime config (legacy)

- Context path `/PepperNoodles`, **port 433**
- `spring.jpa.hibernate.ddl-auto=update` — **schema exists only as Hibernate-generated DDL; there is no SQL migration script anywhere in the repo**
- View resolver: `/WEB-INF/views/*.jsp`
- Requires ngrok on port 443 for the LINE bot webhook

---

## 3. Known problems (the reason for the refactor)

These are established facts about the current code, verified in-tree. Do not re-derive them.

### 3.1 Security — leaked credentials (resolved 2026-08-16)

The 2021 codebase committed live credentials to a **public** repo. All of them were
**revoked by the owner and purged from git history** on 2026-08-16 via
`git filter-repo --replace-text`, which rewrote all 454 commits across all 25 branches.
Their former values now read `***REMOVED-…***` throughout history.

| Secret | Was in | Status |
|---|---|---|
| Gmail app password | `commonmodel/SendEmail.java`, `rearsystem/dao/RearSendEmail.java` | revoked + purged |
| LINE bot channel token + secret | `application.properties` | purged — **reissue before enabling the LINE bot** |
| reCAPTCHA site key + secret | `application.properties` | purged — **regenerate before enabling reCAPTCHA** |
| MSSQL `watcher` password | `application.properties` | purged (MSSQL is gone entirely) |
| Spring Security in-memory `sa/sa123456` | `application.properties` | gone with the legacy config |

> ECPay's `2000132` / `5294y06JbISpM5x9` / `v77hoKGq4kWxNNIS` in `payment_conf.xml` are
> **not** a leak — they are ECPay's published stage-test credentials. Replace them with
> real merchant credentials only when going live.

Anyone with a pre-2026-08-16 clone or fork still holds the old values, and GitHub may
retain unreferenced commits until it garbage-collects. That is acceptable here **because
the credentials were revoked first** — purging history is hygiene, not the mitigation.

Other security defects in the legacy code:
- `csrf().disable()` in `WebSecurityConfig` on a cookie-session app
- `WebSecurityConfigurerAdapter` — removed in Spring Security 6
- Authorization is a hand-maintained `antMatchers` list with a commented-out rule mid-chain
- ECPay `HashKey`/`HashIV` in `src/main/resources/payment_conf.xml`

### 3.2 Persistence

- **Entities are the only schema definition.** `ddl-auto=update` never drops or alters; the real MSSQL
  schema has drifted from the entities (the old README documents manual `ALTER`s for `varbinary(max)`
  and manual `roles` seed rows).
- **MSSQL-specific SQL inside JPQL** — breaks immediately on PostgreSQL:
  `convert(varchar, x, 111)` / `convert(nvarchar, x, 111)` in
  `rearMessage/dao/RearMessageBoxRepository.java`, `shoppingmall/dao/ProductRepository.java`,
  `shoppingmall/dao/OrderListRepository.java`
- **`CascadeType.ALL` on `@ManyToOne` and `@ManyToMany`** throughout — e.g. `Restaurant.userAccount`
  cascades a restaurant delete into the owning `UserAccount`. This is a data-loss bug, not a style issue.
- **Fields carrying both `@Column` and `@Transient`** (`UserAccount.accountType`, `.accountDetailId`,
  `.companyDetailId`, `.levelDetailId`; `Restaurant.userAccountId`) — the `@Column` is dead.
- **`FetchType.EAGER` on collections** (`UserAccount.roles`, `.userTags`, `Restaurant.foodTag`,
  `.RestaurantBusinHour`) — N+1 and cartesian-product fetches.
- **Images stored as `java.sql.Blob` columns** in 6 entities (`Restaurant`, `UserDetail`, `MenuDetail`,
  `EventList`, `Forum`, `LevelDetail`, `Product`) and streamed through controllers.
- `UserAccount.accountIndex` is misspelled and is actually the login e-mail; column
  `acoount_index` is misspelled in the DB too.
- Hibernate `Session` used directly in `loginsystem/dao/impl/UserAccountDAOImpl.java`.
- `@PersistenceContext EntityManager` injected straight into controllers
  (`RestaurantCRUDController`, `UserSysController`).

### 3.3 Code quality

- **No tests.** `SpringPepperNoodleApplicationTests` is an empty context-load stub.
- **209 `System.out.println` calls**; no SLF4J usage in application code. `log4j 1.2.17` (EOL, CVE-ridden) is a declared dependency, used only by the vendored ECPay SDK.
- **`UserSysController` is 1148 lines.**
- Entities annotated `@Component` (`UserAccount`, `Restaurant`, …) — they are not beans.
- Inconsistent layer naming across features: `dao/` vs `repository/`; `service/impl/` vs `service/Impl/` vs `service/inplement/` (typo).
- Class names violating Java convention: `menuController`, `inform`.
- Field names violating Java convention: `Restaurant.Menus`, `.RestaurantBusinHour`, `UserAccount.Likes`.
- Large blocks of commented-out dead code in nearly every entity and config class.
- Hardcoded test data in production paths — `"chris@gmail.com"` in `ShoppingMallController.java:171,237,275`; `"chrislodouchebag@gmail.com"` / `"stevenzchao439@gmail.com"` in `UserSysController.java:251,278`.
- No Lombok, no MapStruct, no DTOs — entities are serialised straight to JSON, guarded ad-hoc with `@JsonIgnore`.
- `bin/` (35 MB of stale `.class` and duplicated sources) is tracked by git and must be deleted + gitignored.

### 3.4 Build / environment

- **The project cannot build on this machine as-is.** Local toolchain is JDK 25 (`java`) and Maven 3.9.9
  running on JDK 23. Spring Boot 2.4.4 / Hibernate 5.4 do not support JDK 17+, let alone 23.
  Either a JDK 11/17 must be installed, or the framework must be upgraded (the refactor does the latter).
- `spring-boot-devtools` ships in the dependency list; `mssql-jdbc` has no pinned version.
- Two conflicting LINE bot starters are declared (`com.linecorp.bot:line-bot-spring-boot:4.3.0`
  and `io.github.jistol:line-bot-spring-boot:1.6.0.JISTOL`).

---

## 4. Commands

```bash
# --- database -------------------------------------------------------------
supabase start                 # boots the local stack (needs Docker Desktop)
supabase status                # prints URLs and keys
supabase db reset              # re-apply every migration, then seed.sql
supabase migration new <name>
supabase stop

# --- backend --------------------------------------------------------------
cd backend
JAVA_HOME=$(/usr/libexec/java_home -v 25) ./mvnw spring-boot:run
JAVA_HOME=$(/usr/libexec/java_home -v 25) ./mvnw test
JAVA_HOME=$(/usr/libexec/java_home -v 25) ./mvnw clean package

# --- psql shortcut --------------------------------------------------------
psql "postgresql://postgres:postgres@127.0.0.1:55322/postgres"
```

### Local ports

This project's Supabase stack runs on **553xx**, not the default 543xx, because
another unrelated Supabase project is already running on this machine at 543xx.
Never `supabase stop` without `--project-id PepperNoodles` — you would kill the
other project's stack.

| Service | URL |
|---|---|
| Postgres | `127.0.0.1:55322` (`postgres` / `postgres`) |
| API gateway | http://127.0.0.1:55321 |
| Studio | http://127.0.0.1:55323 |
| Mailpit (web) | http://127.0.0.1:55324 |
| Mailpit (SMTP) | `127.0.0.1:55325` |
| Backend API | http://localhost:8080 |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| Frontend | http://localhost:3000 |

### Seeded accounts

All use the password `Password123!`.

| Account | Role |
|---|---|
| `admin@peppernoodles.local` | ROLE_ADMIN |
| `owner.din@` / `owner.chan@` / `owner.chun@peppernoodles.local` | ROLE_COMPANY |
| `mei@` / `wei@` / `ling@example.com` | ROLE_USER |
| `pending@example.com` | ROLE_USER, unverified — cannot log in |

Installed locally: Supabase CLI 2.109.1, Node v22.17.0, Maven 3.9.9,
JDKs 25.0.1 / 24 / 17 / 11. **Maven's own `java` is JDK 23**, so always pass
`JAVA_HOME` as shown above.

---

## 4a. Gotchas already hit (do not rediscover these)

- **Spring Boot 4 renamed the starters.** `spring-boot-starter-web` →
  `spring-boot-starter-webmvc`; `oauth2-resource-server` →
  `security-oauth2-resource-server`; test support is now per-module
  (`spring-boot-starter-webmvc-test`, …); Testcontainers artifacts are
  `testcontainers-junit-jupiter` / `testcontainers-postgresql`. When in doubt,
  generate a throwaway project from start.spring.io rather than guessing.
- **Boot 4 ships Jackson 3** (`tools.jackson.*`). `SerializationFeature`
  no longer has `WRITE_DATES_AS_TIMESTAMPS`; ISO-8601 is the default, so the
  setting is simply unnecessary.
- **`RestClient.Builder` is not auto-configured** by the webmvc starter in
  Boot 4. Use `RestClient.create()`.
- **Do not use `citext`.** Hibernate's schema validator reports it as
  `Types#OTHER` and rejects it against a `String` field, and it does not exist
  in a plain Postgres test container. E-mail is a plain `text` column stored
  lower-cased, with a CHECK constraint and `EmailAddress.normalise` on every
  write path.
- **Revocation that must survive a rollback needs `REQUIRES_NEW`.** Refresh
  token replay detection revokes the whole family and *then* throws; in one
  transaction the throw rolls the revocation back and the leaked family stays
  live. That is what `RefreshTokenRevoker` is for. There is a test for it.
- **`supabase db reset` runs `seed.sql`,** which ends in a block of row-count
  assertions. Most seed inserts are `insert … select … join`, which silently
  drops rows when a natural-key lookup misses; the assertions turn that into a
  loud failure. Update the expected counts when adding seed rows.
- The `restaurants.geo` column is `GENERATED ALWAYS`. It is deliberately **not
  mapped** in the `Restaurant` entity — radius search uses a native query.
- **`@Lock(PESSIMISTIC_WRITE)` does not refresh an already-loaded entity.**
  Checkout loads cart rows with an entity graph that pulls in `Product`; a
  subsequent `SELECT … FOR UPDATE` then returns the *cached* instance, so a
  stock check reads a stale quantity. Three concurrent two-unit orders all
  passed a check against a stock of three. Stock is therefore changed only by
  `ProductRepository#reserveStock` / `#releaseStock`, which do the comparison
  and the write in one atomic UPDATE. Never reintroduce an in-memory
  decrement-then-save.
- **Never derive a unique value with `count(*) + 1`.** The first order-number
  generator did, and two simultaneous checkouts produced the same number.
  `order_no` now comes from the `order_no_seq` sequence.
- **Untyped nulls in JPQL break PostgreSQL.** The `(:param is null or col =
  :param)` idiom binds NULL as `bytea`, so `lower(:q)` fails with *function
  lower(bytea) does not exist* — and even when it works it is not sargable. The
  shop catalogue filter uses `ProductSpecifications` and builds only the
  predicates the caller supplied.
- **Do not set `hibernate.jdbc.time_zone`.** It makes the driver convert
  temporal values against that zone. `Instant`/`timestamptz` is unaffected
  (absolute), but `LocalTime` is wall-clock with no zone and gets shifted — a
  10:00 opening time came back as 18:00 on this UTC+8 host. Business hours are
  `time` columns, so the setting is deliberately absent.
- **Two collections cannot be fetched in one query.** An `@EntityGraph` naming
  both `foodTags` and `businessHours` returned each opening row once per tag —
  seven rows became twenty-one. Fetch one eagerly and let the other load inside
  the same transaction.
- **Schema-qualify PostGIS.** The extension is installed into `extensions`, and
  unqualified `st_dwithin(...)` / `::geography` resolve on Supabase only because
  that schema is on the role's search_path — they fail on a plain test
  container. Functions take `extensions.` and the `&&` overlap operator takes
  `operator(extensions.&&)`.
- **Frontend E2E: inject auth with `addInitScript`, never after `goto`.** Writing
  tokens to localStorage after a navigation races the AuthProvider, which has
  already settled on "anonymous"; tests then fail at random. The suite went from
  flaky-and-3-minutes to stable-and-20-seconds by injecting up front. Tests that
  need a *different* identity open their own browser context rather than
  clearing storage, because the init script would just re-inject.
- **Vitest alias must use `fileURLToPath`.** This repo's path contains
  non-ASCII characters and `new URL(...).pathname` leaves them percent-encoded,
  so an alias built that way silently fails to resolve.
- A malformed annotation aborts Lombok's annotation processing, and every
  generated getter/setter then reports **"cannot find symbol"** across unrelated
  files. When a compile suddenly produces dozens of missing-accessor errors,
  look for one broken annotation, not dozens of broken classes.

---

## 5. Refactor — target state

Decided 2026-08-16 by the repo owner. **All six feature domains are in scope in one pass.**

### Stack

```
supabase/    PostgreSQL 17 + Storage, run locally via Supabase CLI in Docker
backend/     Spring Boot 4.1.0 · Java 25 · Spring Framework 7 · Hibernate 7 (Jakarta EE 11)
             Stateless REST API, JSON only. No JSP, no server-rendered views.
frontend/    Next.js (App Router) · TypeScript · Tailwind CSS
             Talks to the backend over HTTP; no direct DB access.
```

### Fixed decisions

1. **Front/back separated.** The backend exposes a versioned REST API under `/api/v1/**`.
   The frontend is a separate Next.js app. The 64 JSPs are deleted, not ported.
2. **Auth: Spring Security 7 + a `users` table in Postgres.** BCrypt password hashes carry over
   from the legacy schema. Because the frontend is a separate origin, the API issues **JWTs**
   (short-lived access token + refresh token) rather than using server sessions.
   *Consequence: Supabase Auth/GoTrue is not used, and Postgres RLS is not the authorization
   mechanism — the backend connects as a privileged role and enforces authorization in the service
   layer.* RLS is still enabled on tables as defence-in-depth.
3. **Database: Supabase (PostgreSQL).** MS SQL Server is removed entirely.
3a. **The 2021 visual identity is kept.** Design tokens were lifted from
   `legacy/src/main/webapp/css/{style,main}.css` and are declared in
   `frontend/src/app/globals.css`: `--color-pepper #ff3d1c` (the original
   `.btn`), `--color-mint #31ff7a` (the `.hero__caption` script tagline),
   `--color-gold #c6a16e`, Sacramento for script text and Sulphur Point for
   display. Hero photography and the circular logo were copied to
   `frontend/public/brand/`. The header lies transparently over the hero on
   pages that have one. Do not substitute a generic palette.
4. **Schema is owned by SQL migrations** in `supabase/migrations/`. Hibernate runs with
   `ddl-auto: validate` and never writes DDL.
5. **Images move out of the database** into Supabase Storage buckets. Every `Blob` column becomes a
   `text` path column; the backend uploads via the Storage API and returns public/signed URLs.
6. **No secrets in the repo.** Config comes from environment variables, loaded from a gitignored
   `.env`, with a committed `.env.example`. All currently-committed secrets are rotated by the owner.
7. **`bin/` is deleted** and gitignored.

### Backend package layout

Root package `com.peppernoodles`. One package per bounded context, each internally layered:

```
com.peppernoodles
├── PepperNoodlesApplication.java
├── common/          config, security (JWT filter/provider), error handling,
│                    storage (Supabase Storage client), mail, pagination
├── auth/            login, refresh, registration, email verification, password reset
├── user/            profile, food tags, friends, favourites, forum, notifications
├── company/         business accounts
├── restaurant/      restaurants, menus, business hours, events, reviews & replies
├── map/             geo/name/tag search
├── shop/            products, cart, orders, ECPay payment, sales reports
├── admin/           back office, member suspension, contact-us inbox
├── chat/            STOMP WebSocket chat
└── linebot/         LINE messaging webhook
```

Each context: `api/` (`@RestController` + request/response records) · `domain/` (entities, enums) ·
`repository/` · `service/` (interface + impl). Nothing crosses a context boundary except through
another context's `service` interface.

### Known gaps

- **Newsletter subscribe** — the 2021 首頁 had a Subscribe form; there is no
  mailing-list backend, so it is not rebuilt rather than shipped as a dead input.
- **LINE bot replies** — the webhook verifies signatures and
  `LineBotService#replyTo` holds the lookup logic, but nothing is pushed back to
  LINE; that needs a channel token, and the project's channel was deleted.
- **Product images** — seed products have no photos. The legacy images lived in
  MSSQL BLOBs and that database is gone. Upload via
  `POST /api/v1/shop/products/{id}/image`.

### Non-goals

- Re-creating the 2021 markup. The *visual design* is deliberately preserved
  (see below); the JSP/jQuery implementation of it is not.
- Keeping the ~130 MB of vendored jQuery plugins under `src/main/webapp/plugins` and `vendor`.
- Re-implementing ECPay from scratch — the vendored SDK is replaced with the official Maven artifact
  or an isolated adapter, but the payment flow itself is out of scope for restructuring.
- Migrating production data. The old MSSQL database is not available; the new schema ships with
  `supabase/seed.sql` demo data instead.

---

## 6. Conventions for new code

Until §5 is settled, apply these to anything written or touched:

- **Never commit a secret.** Read config from the environment; add a key to `.env.example` instead.
- **Logging:** SLF4J (`private static final Logger log = LoggerFactory.getLogger(X.class)` or Lombok
  `@Slf4j`). Never `System.out.println`.
- **Layering:** controller → service (interface + single impl) → repository. Controllers do no JPA and
  hold no `EntityManager`. Services own `@Transactional` boundaries.
- **DTOs at the boundary.** Entities never leave the service layer and are never serialised to JSON.
- **Naming:** `PascalCase` classes, `camelCase` fields, `repository/` for Spring Data interfaces,
  `service/impl/` for implementations. One consistent spelling across all features.
- **JPA:** no `CascadeType.ALL` on `@ManyToOne`/`@ManyToMany`; `FetchType.LAZY` on all associations;
  no `@Component` on entities; no vendor-specific SQL in JPQL.
- **Schema changes go through a migration file**, never through an entity edit alone.
- **Comments and identifiers in English**; user-facing strings stay in 繁體中文.
- Delete dead code rather than commenting it out — git history is the archive.

### Domain glossary (DB/code term → meaning)

| Term in code | Meaning |
|---|---|
| `accountIndex` / `acoount_index` | login e-mail address |
| `UserAccount` | credentials + roles; `UserDetail` holds the profile |
| `CompanyDetail` | business-owner profile, 1:1 with a `UserAccount` |
| `LevelDetail` | membership tier |
| `FoodTag` / `FoodTagUser` / `FoodTagProduct` | cuisine tags and their join tables |
| `RestaurantBusinHour` | restaurant opening hours (`BusinHour` = business hour) |
| `MessageBox` / `ForumMessageBox` / `RestaurantMessageBox` / `RearMessageBox` | comment threads for user walls, forum, restaurant reviews, and the admin contact-us inbox |
| `rear` / `rearsystem` | 後台 = admin back office |
| `EventList` | restaurant promotional campaigns |
| `status` values | `'上架中'` = listed, `'下架中'` = delisted, `'已付款'` = paid |
