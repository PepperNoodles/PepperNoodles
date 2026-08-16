-- =============================================================================
-- Race-free order numbers
--
-- The first implementation derived the daily counter from
-- `select count(*) from orders where order_no like 'PN20260817-%'`, which is a
-- read-then-write race: two concurrent checkouts both counted N and both built
-- PN20260817-<N+1>, so the second insert died on orders_order_no_key. A
-- concurrency test caught exactly that.
--
-- A sequence hands out values outside transaction isolation, so two callers can
-- never receive the same one. Numbers are globally monotonic rather than
-- restarting each day, which is fine — the date in the prefix still tells you
-- when the order was placed.
-- =============================================================================

create sequence if not exists public.order_no_seq as bigint start with 1 increment by 1;

comment on sequence public.order_no_seq is
  'Feeds the numeric part of orders.order_no (PN<yyyyMMdd>-<seq>).';
