-- =============================================================================
-- Accounts, roles, profiles, and auth tokens
--
-- Legacy origin: userAccount, roles, users_roles, userDetail, companyDetail,
--                levelDetail
-- =============================================================================

-- --- roles -------------------------------------------------------------------
create table public.roles (
  id          bigint generated always as identity primary key,
  name        text not null unique,
  description text
);

comment on table public.roles is 'Authorities granted to an account. Legacy table: roles.';

insert into public.roles (name, description) values
  ('ROLE_USER',    '一般會員 — normal consumer account'),
  ('ROLE_COMPANY', '企業會員 — restaurant owner / business account'),
  ('ROLE_ADMIN',   '系統管理員 — back-office administrator');

-- --- users -------------------------------------------------------------------
-- Legacy `userAccount`. The misspelled `acoount_index` column was in fact the
-- login e-mail address; it is named honestly here.
create table public.users (
  id                bigint generated always as identity primary key,
  -- Stored lower-cased so a plain unique constraint gives case-insensitive
  -- uniqueness and a plain b-tree index serves login lookups.
  email             text        not null unique,
  password_hash     text        not null,
  enabled           boolean     not null default false,
  suspended_at      timestamptz,
  suspended_reason  text,
  email_verified_at timestamptz,
  last_login_at     timestamptz,
  created_at        timestamptz not null default now(),
  updated_at        timestamptz not null default now(),

  constraint users_email_format check (email ~ '^[^@[:space:]]+@[^@[:space:]]+\.[^@[:space:]]+$'),
  constraint users_email_lowercase check (email = lower(email))
);

create trigger users_set_updated_at
  before update on public.users
  for each row execute function public.set_updated_at();

comment on table public.users is 'Login credentials and account state. Legacy table: userAccount.';
comment on column public.users.email is 'Login identifier. Legacy column: acoount_index (sic).';
comment on column public.users.password_hash is 'BCrypt hash. Legacy hashes carry over unchanged.';

-- --- user_roles --------------------------------------------------------------
create table public.user_roles (
  user_id bigint not null references public.users (id) on delete cascade,
  role_id bigint not null references public.roles (id) on delete restrict,
  primary key (user_id, role_id)
);

create index user_roles_role_id_idx on public.user_roles (role_id);

comment on table public.user_roles is 'Legacy table: users_roles.';

-- --- user_profiles -----------------------------------------------------------
-- Legacy `userDetail`. The `userphoto` BLOB becomes a Storage object path.
create table public.user_profiles (
  id          bigint generated always as identity primary key,
  user_id     bigint not null unique references public.users (id) on delete cascade,
  real_name   text,
  nickname    text,
  phone       text,
  birth_date  date,
  gender      text,
  location    text,
  avatar_path text,
  created_at  timestamptz not null default now(),
  updated_at  timestamptz not null default now(),

  constraint user_profiles_gender_check check (gender is null or gender in ('MALE', 'FEMALE', 'OTHER'))
);

create index user_profiles_nickname_trgm_idx
  on public.user_profiles using gin (nickname extensions.gin_trgm_ops);

create trigger user_profiles_set_updated_at
  before update on public.user_profiles
  for each row execute function public.set_updated_at();

comment on table public.user_profiles is 'Consumer profile, 1:1 with users. Legacy table: userDetail.';
comment on column public.user_profiles.avatar_path is
  'Object path in the user-avatars Storage bucket. Legacy column: userphoto (BLOB).';

-- --- company_profiles --------------------------------------------------------
create table public.company_profiles (
  id          bigint generated always as identity primary key,
  user_id     bigint not null unique references public.users (id) on delete cascade,
  real_name   text not null,
  phone       text,
  location    text,
  tier        text,
  avatar_path text,
  created_at  timestamptz not null default now(),
  updated_at  timestamptz not null default now()
);

create trigger company_profiles_set_updated_at
  before update on public.company_profiles
  for each row execute function public.set_updated_at();

comment on table public.company_profiles is 'Business-owner profile, 1:1 with users. Legacy table: companyDetail.';

-- --- user_stats --------------------------------------------------------------
-- Legacy `levelDetail`: gamification counters, 1:1 with an account.
create table public.user_stats (
  user_id         bigint primary key references public.users (id) on delete cascade,
  tier            text    not null default 'BRONZE',
  post_count      integer not null default 0 check (post_count      >= 0),
  like_count      integer not null default 0 check (like_count      >= 0),
  follower_count  integer not null default 0 check (follower_count  >= 0),
  reply_count     integer not null default 0 check (reply_count     >= 0),
  login_count     integer not null default 0 check (login_count     >= 0),
  purchase_count  integer not null default 0 check (purchase_count  >= 0),
  updated_at      timestamptz not null default now()
);

create trigger user_stats_set_updated_at
  before update on public.user_stats
  for each row execute function public.set_updated_at();

comment on table public.user_stats is 'Per-account activity counters. Legacy table: levelDetail.';

-- --- auth tokens -------------------------------------------------------------
-- The legacy app carried a @Transient `code` field on the entity and mailed
-- verification codes with no expiry or single-use guarantee. These tables make
-- both explicit.

create table public.email_verification_tokens (
  id         bigint generated always as identity primary key,
  user_id    bigint      not null references public.users (id) on delete cascade,
  token_hash text        not null unique,
  expires_at timestamptz not null,
  consumed_at timestamptz,
  created_at timestamptz not null default now()
);

create index email_verification_tokens_user_id_idx on public.email_verification_tokens (user_id);

create table public.password_reset_tokens (
  id          bigint generated always as identity primary key,
  user_id     bigint      not null references public.users (id) on delete cascade,
  token_hash  text        not null unique,
  expires_at  timestamptz not null,
  consumed_at timestamptz,
  created_at  timestamptz not null default now()
);

create index password_reset_tokens_user_id_idx on public.password_reset_tokens (user_id);

create table public.refresh_tokens (
  id         bigint generated always as identity primary key,
  user_id    bigint      not null references public.users (id) on delete cascade,
  token_hash text        not null unique,
  expires_at timestamptz not null,
  revoked_at timestamptz,
  user_agent text,
  created_at timestamptz not null default now()
);

create index refresh_tokens_user_id_idx on public.refresh_tokens (user_id);

comment on table public.refresh_tokens is
  'Rotating refresh tokens for the stateless JWT API. Only the SHA-256 hash is stored.';
