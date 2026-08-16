-- =============================================================================
-- Cuisine tags (食物標籤) and their join tables
--
-- Legacy origin: foodTag, foodtag_user, foodtag_restaurant, foodTag_Product,
--                Forum_tag
--
-- The legacy join tables each carried a surrogate identity key and a pair of
-- FKs with no uniqueness constraint, so the same tag could be attached twice.
-- Here every join table is a plain composite-key table.
-- =============================================================================

create table public.food_tags (
  id         bigint generated always as identity primary key,
  name       text not null unique,
  created_at timestamptz not null default now()
);

create index food_tags_name_trgm_idx
  on public.food_tags using gin (name extensions.gin_trgm_ops);

comment on table public.food_tags is 'Cuisine / interest tags. Legacy table: foodTag.';

-- Interests picked at registration. Legacy table: foodtag_user.
create table public.user_food_tags (
  user_id bigint not null references public.users (id)      on delete cascade,
  tag_id  bigint not null references public.food_tags (id)  on delete cascade,
  primary key (user_id, tag_id)
);

create index user_food_tags_tag_id_idx on public.user_food_tags (tag_id);
