-- =============================================================================
-- Extensions and shared helpers
-- =============================================================================

-- Note: citext is deliberately NOT used. Hibernate's schema validator reports it
-- as Types#OTHER and rejects it against a String field, and it does not exist in
-- a plain Postgres test container. E-mail addresses are stored lower-cased
-- instead, with a CHECK constraint holding the invariant.
create extension if not exists postgis with schema extensions;
create extension if not exists pg_trgm with schema extensions;

-- Keeps updated_at honest without the application having to remember.
create or replace function public.set_updated_at()
returns trigger
language plpgsql
as $$
begin
  new.updated_at = now();
  return new;
end;
$$;

comment on function public.set_updated_at() is
  'BEFORE UPDATE trigger: stamps updated_at with the current transaction time.';
