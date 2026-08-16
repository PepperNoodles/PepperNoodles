-- =============================================================================
-- Row Level Security
--
-- Architecture note: authorization for this application is enforced in the
-- Spring Boot service layer, not by RLS. The backend connects with a
-- privileged role and therefore bypasses these policies.
--
-- RLS is enabled here as defence in depth: it means that if the anon or
-- authenticated Supabase keys ever leak, PostgREST/GraphQL still expose
-- nothing, because no permissive policy exists for those roles. Turning RLS on
-- with zero policies is a deny-all.
--
-- If Supabase Auth is ever adopted (see CLAUDE.md §5, decision 2), real
-- per-row policies replace this file.
-- =============================================================================

do $$
declare
  t record;
begin
  for t in
    select tablename
    from pg_tables
    where schemaname = 'public'
  loop
    execute format('alter table public.%I enable row level security', t.tablename);
    execute format('alter table public.%I force row level security', t.tablename);
  end loop;
end;
$$;

-- The reporting views are owned by the migration role; make sure they do not
-- silently bypass the deny-all on their base tables.
alter view public.restaurant_ratings set (security_invoker = true);
alter view public.daily_sales        set (security_invoker = true);
alter view public.product_sales      set (security_invoker = true);

revoke all on all tables    in schema public from anon, authenticated;
revoke all on all sequences in schema public from anon, authenticated;
revoke all on all functions in schema public from anon, authenticated;
