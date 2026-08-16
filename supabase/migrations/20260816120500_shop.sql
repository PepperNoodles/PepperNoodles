-- =============================================================================
-- Shopping mall: catalogue, cart, orders, payments
--
-- Legacy origin: ProductMainClass, ProductDetailClass, Product, foodTag_Product,
--                OrderList, OrderDetail
-- =============================================================================

-- --- catalogue ---------------------------------------------------------------
create table public.product_categories (
  id   bigint generated always as identity primary key,
  name text not null unique
);

comment on table public.product_categories is '商品大分類. Legacy table: ProductMainClass.';

create table public.product_subcategories (
  id          bigint generated always as identity primary key,
  category_id bigint not null references public.product_categories (id) on delete restrict,
  name        text   not null,

  constraint product_subcategories_unique unique (category_id, name)
);

create index product_subcategories_category_idx on public.product_subcategories (category_id);

comment on table public.product_subcategories is '商品子分類. Legacy table: ProductDetailClass.';

-- --- products ----------------------------------------------------------------
create table public.products (
  id             bigint generated always as identity primary key,
  restaurant_id  bigint not null references public.restaurants (id)          on delete cascade,
  subcategory_id bigint references public.product_subcategories (id)         on delete set null,
  name           text   not null,
  description    text,
  price          numeric(10, 2) not null,
  quantity       integer not null default 0,
  image_path     text,
  status         text   not null default 'DELISTED',
  released_at    timestamptz,
  created_at     timestamptz not null default now(),
  updated_at     timestamptz not null default now(),

  constraint products_price_positive  check (price >= 0),
  constraint products_quantity_signed check (quantity >= 0),
  constraint products_status_check    check (status in ('LISTED', 'DELISTED'))
);

create index products_restaurant_idx  on public.products (restaurant_id);
create index products_subcategory_idx on public.products (subcategory_id);
create index products_listed_idx      on public.products (status, released_at desc) where status = 'LISTED';
create index products_price_idx       on public.products (price) where status = 'LISTED';
create index products_name_trgm_idx
  on public.products using gin (name extensions.gin_trgm_ops);

create trigger products_set_updated_at
  before update on public.products
  for each row execute function public.set_updated_at();

comment on table public.products is 'Legacy table: Product.';
comment on column public.products.price is
  'numeric(10,2). Legacy stored this as an integer, so no product could be priced in cents.';
comment on column public.products.status is
  'LISTED / DELISTED. Legacy stored the Chinese strings 上架中 / 下架中 directly.';

create table public.product_food_tags (
  product_id bigint not null references public.products (id)  on delete cascade,
  tag_id     bigint not null references public.food_tags (id) on delete cascade,
  primary key (product_id, tag_id)
);

create index product_food_tags_tag_idx on public.product_food_tags (tag_id);

comment on table public.product_food_tags is 'Legacy table: foodTag_Product.';

-- --- cart --------------------------------------------------------------------
-- The legacy cart lived only in the HTTP session, so it was lost on logout and
-- could not be read by the order-expiry job.
create table public.cart_items (
  user_id    bigint  not null references public.users (id)    on delete cascade,
  product_id bigint  not null references public.products (id) on delete cascade,
  quantity   integer not null,
  updated_at timestamptz not null default now(),

  primary key (user_id, product_id),
  constraint cart_items_quantity_positive check (quantity > 0)
);

create trigger cart_items_set_updated_at
  before update on public.cart_items
  for each row execute function public.set_updated_at();

-- --- orders ------------------------------------------------------------------
create table public.orders (
  id               bigint generated always as identity primary key,
  order_no         text   not null unique,
  user_id          bigint not null references public.users (id) on delete restrict,
  receiver_name    text   not null,
  receiver_phone   text   not null,
  receiver_address text   not null,
  status           text   not null default 'PENDING',
  total_cost       numeric(12, 2) not null,
  created_at       timestamptz not null default now(),
  updated_at       timestamptz not null default now(),
  paid_at          timestamptz,
  cancelled_at     timestamptz,
  expires_at       timestamptz not null,

  constraint orders_status_check      check (status in ('PENDING', 'PAID', 'CANCELLED', 'EXPIRED')),
  constraint orders_total_positive    check (total_cost >= 0),
  constraint orders_paid_consistency  check ((status = 'PAID') = (paid_at is not null))
);

create index orders_user_idx    on public.orders (user_id, created_at desc);
create index orders_status_idx  on public.orders (status, created_at desc);
-- Drives the "auto-cancel unpaid orders" scheduled job.
create index orders_expiry_idx  on public.orders (expires_at) where status = 'PENDING';

create trigger orders_set_updated_at
  before update on public.orders
  for each row execute function public.set_updated_at();

comment on table public.orders is 'Legacy table: OrderList.';
comment on column public.orders.order_no is
  'Human-readable unique order number. Legacy used a bigint column literally named UUID.';
comment on column public.orders.expires_at is
  '訂單保留期限 — after this the scheduled job moves a PENDING order to EXPIRED.';

create table public.order_items (
  id         bigint  generated always as identity primary key,
  order_id   bigint  not null references public.orders (id)   on delete cascade,
  product_id bigint  references public.products (id)          on delete set null,
  -- Snapshot of the product at purchase time: the legacy schema joined back to
  -- Product for name and price, so an order's history changed whenever the
  -- product was edited.
  product_name text    not null,
  unit_price   numeric(10, 2) not null,
  quantity     integer not null,

  constraint order_items_quantity_positive check (quantity > 0),
  constraint order_items_price_positive    check (unit_price >= 0)
);

create index order_items_order_idx   on public.order_items (order_id);
create index order_items_product_idx on public.order_items (product_id);

comment on table public.order_items is 'Legacy table: OrderDetail.';

-- --- payments ----------------------------------------------------------------
-- Legacy code called ECPay and kept no record of the exchange beyond flipping
-- OrderList.Status, which made failed or duplicated callbacks untraceable.
create table public.payments (
  id                bigint generated always as identity primary key,
  order_id          bigint not null references public.orders (id) on delete cascade,
  provider          text   not null default 'ECPAY',
  merchant_trade_no text   not null unique,
  amount            numeric(12, 2) not null,
  status            text   not null default 'PENDING',
  raw_callback      jsonb,
  created_at        timestamptz not null default now(),
  updated_at        timestamptz not null default now(),

  constraint payments_status_check check (status in ('PENDING', 'SUCCEEDED', 'FAILED'))
);

create index payments_order_idx on public.payments (order_id);

create trigger payments_set_updated_at
  before update on public.payments
  for each row execute function public.set_updated_at();

comment on table public.payments is '綠界金流 transaction log.';

-- --- sales reporting ---------------------------------------------------------
-- Replaces the legacy monthly-revenue queries, which used the SQL Server
-- specific convert(varchar, date, 111) to bucket by day and therefore cannot
-- run on PostgreSQL at all.
create view public.daily_sales as
select
  (o.paid_at at time zone 'Asia/Taipei')::date as sales_date,
  count(*)                                     as order_count,
  sum(o.total_cost)                            as revenue
from public.orders o
where o.status = 'PAID'
  and o.paid_at is not null
group by 1;

comment on view public.daily_sales is
  'Daily paid revenue in Asia/Taipei. Replaces convert(varchar, OrderCreatedDate, 111) grouping.';

create view public.product_sales as
select
  oi.product_id,
  min(oi.product_name)             as product_name,
  sum(oi.quantity)                 as units_sold,
  sum(oi.quantity * oi.unit_price) as revenue
from public.order_items oi
join public.orders o on o.id = oi.order_id
where o.status = 'PAID'
group by oi.product_id;

comment on view public.product_sales is 'Units and revenue per product across paid orders.';
