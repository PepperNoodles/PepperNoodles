-- =============================================================================
-- Restaurants and everything owned by a restaurant
--
-- Legacy origin: restaurant, restaurantBusinHour, menuDetail, eventList,
--                restaurantMessageBox, restaurantReplyMessage,
--                foodtag_restaurant, userAccount_Restaurant
-- =============================================================================

create table public.restaurants (
  id            bigint generated always as identity primary key,
  owner_user_id bigint not null references public.users (id) on delete restrict,
  name          text   not null,
  address       text   not null unique,
  contact       text,
  website       text,
  photo_path    text,
  latitude      numeric(10, 7) not null,
  longitude     numeric(10, 7) not null,

  -- Derived point used for radius search. Kept in the database so "restaurants
  -- near me" is an indexed GiST lookup instead of the legacy lat/long BETWEEN
  -- bounding-box scan.
  geo extensions.geography(Point, 4326)
    generated always as (
      extensions.st_setsrid(extensions.st_makepoint(longitude, latitude), 4326)::extensions.geography
    ) stored,

  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),

  constraint restaurants_latitude_range  check (latitude  between  -90 and  90),
  constraint restaurants_longitude_range check (longitude between -180 and 180)
);

create index restaurants_owner_user_id_idx on public.restaurants (owner_user_id);
create index restaurants_geo_idx           on public.restaurants using gist (geo);
create index restaurants_name_trgm_idx
  on public.restaurants using gin (name extensions.gin_trgm_ops);
create index restaurants_address_trgm_idx
  on public.restaurants using gin (address extensions.gin_trgm_ops);

create trigger restaurants_set_updated_at
  before update on public.restaurants
  for each row execute function public.set_updated_at();

comment on table public.restaurants is 'Legacy table: restaurant.';
comment on column public.restaurants.owner_user_id is
  'Owning business account. ON DELETE RESTRICT — the legacy mapping had
   CascadeType.ALL on this @ManyToOne, so deleting a restaurant deleted its owner.';
comment on column public.restaurants.photo_path is
  'Object path in the restaurant-photos Storage bucket. Legacy column: restaurantPhoto (BLOB).';

-- --- tags --------------------------------------------------------------------
create table public.restaurant_food_tags (
  restaurant_id bigint not null references public.restaurants (id) on delete cascade,
  tag_id        bigint not null references public.food_tags (id)   on delete cascade,
  primary key (restaurant_id, tag_id)
);

create index restaurant_food_tags_tag_id_idx on public.restaurant_food_tags (tag_id);

-- --- business hours ----------------------------------------------------------
-- The legacy row held up to three open/close pairs in wide columns
-- (open_time, open_time_2nd, open_time_3rd, …) as free-text strings. Here each
-- opening interval is its own row with real `time` values.
create table public.restaurant_business_hours (
  id            bigint generated always as identity primary key,
  restaurant_id bigint   not null references public.restaurants (id) on delete cascade,
  day_of_week   smallint not null,
  opens_at      time     not null,
  closes_at     time     not null,

  constraint restaurant_business_hours_day_range check (day_of_week between 0 and 6),
  constraint restaurant_business_hours_order     check (closes_at > opens_at),
  constraint restaurant_business_hours_unique    unique (restaurant_id, day_of_week, opens_at)
);

create index restaurant_business_hours_restaurant_id_idx
  on public.restaurant_business_hours (restaurant_id, day_of_week);

comment on table public.restaurant_business_hours is
  'One row per opening interval. day_of_week: 0 = Sunday … 6 = Saturday.
   Legacy table: restaurantBusinHour (one wide row per day).';

-- --- menu --------------------------------------------------------------------
-- Legacy `menuDetail` held nothing but a BLOB, so a menu could not be labelled
-- or ordered.
create table public.restaurant_menu_items (
  id            bigint generated always as identity primary key,
  restaurant_id bigint  not null references public.restaurants (id) on delete cascade,
  caption       text,
  image_path    text    not null,
  position      integer not null default 0,
  created_at    timestamptz not null default now()
);

create index restaurant_menu_items_restaurant_id_idx
  on public.restaurant_menu_items (restaurant_id, position);

comment on table public.restaurant_menu_items is 'Legacy table: menuDetail.';

-- --- events / campaigns ------------------------------------------------------
create table public.restaurant_events (
  id            bigint generated always as identity primary key,
  restaurant_id bigint not null references public.restaurants (id) on delete cascade,
  name          text   not null,
  content       text,
  image_path    text,
  starts_on     date   not null,
  ends_on       date   not null,
  created_at    timestamptz not null default now(),
  updated_at    timestamptz not null default now(),

  constraint restaurant_events_date_order check (ends_on >= starts_on)
);

create index restaurant_events_restaurant_id_idx on public.restaurant_events (restaurant_id);
create index restaurant_events_active_idx        on public.restaurant_events (starts_on, ends_on);

create trigger restaurant_events_set_updated_at
  before update on public.restaurant_events
  for each row execute function public.set_updated_at();

comment on table public.restaurant_events is '餐廳活動. Legacy table: eventList.';

-- --- reviews -----------------------------------------------------------------
create table public.restaurant_reviews (
  id             bigint generated always as identity primary key,
  restaurant_id  bigint   not null references public.restaurants (id) on delete cascade,
  author_user_id bigint   not null references public.users (id)       on delete cascade,
  body           text     not null,
  score          smallint,
  created_at     timestamptz not null default now(),
  updated_at     timestamptz not null default now(),

  constraint restaurant_reviews_score_range check (score is null or score between 1 and 5)
);

create index restaurant_reviews_restaurant_id_idx on public.restaurant_reviews (restaurant_id, created_at desc);
create index restaurant_reviews_author_idx        on public.restaurant_reviews (author_user_id);

create trigger restaurant_reviews_set_updated_at
  before update on public.restaurant_reviews
  for each row execute function public.set_updated_at();

comment on table public.restaurant_reviews is 'Legacy table: restaurantMessageBox.';

create table public.restaurant_review_replies (
  id             bigint generated always as identity primary key,
  review_id      bigint not null references public.restaurant_reviews (id) on delete cascade,
  author_user_id bigint not null references public.users (id)              on delete cascade,
  body           text   not null,
  created_at     timestamptz not null default now(),
  updated_at     timestamptz not null default now()
);

create index restaurant_review_replies_review_id_idx
  on public.restaurant_review_replies (review_id, created_at);

create trigger restaurant_review_replies_set_updated_at
  before update on public.restaurant_review_replies
  for each row execute function public.set_updated_at();

comment on table public.restaurant_review_replies is 'Legacy table: restaurantReplyMessage.';

-- --- favourites --------------------------------------------------------------
create table public.user_favourite_restaurants (
  user_id       bigint not null references public.users (id)       on delete cascade,
  restaurant_id bigint not null references public.restaurants (id) on delete cascade,
  created_at    timestamptz not null default now(),
  primary key (user_id, restaurant_id)
);

create index user_favourite_restaurants_restaurant_id_idx
  on public.user_favourite_restaurants (restaurant_id);

comment on table public.user_favourite_restaurants is
  '餐廳收藏. Legacy table: userAccount_Restaurant.';

-- --- rating rollup -----------------------------------------------------------
-- The legacy schema denormalised this onto the restaurant row as
-- totalScore (integer) and restaurantAmount (a *string* count), updated by
-- hand from several controllers and routinely out of sync. A view keeps the
-- numbers derived instead.
create view public.restaurant_ratings as
select
  r.id                                                as restaurant_id,
  count(rv.score)                                     as rating_count,
  coalesce(sum(rv.score), 0)                          as rating_sum,
  round(avg(rv.score)::numeric, 2)                    as rating_average,
  count(rv.id)                                        as review_count
from public.restaurants r
left join public.restaurant_reviews rv on rv.restaurant_id = r.id
group by r.id;

comment on view public.restaurant_ratings is
  'Derived rating rollup. Replaces the hand-maintained restaurant.totalScore /
   restaurant.restaurantAmount columns.';
