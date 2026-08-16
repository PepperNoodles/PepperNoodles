-- =============================================================================
-- Back office (後台)
--
-- Legacy origin: rearMessageBox
-- =============================================================================

-- 聯絡我們 — enquiries submitted from the public contact form and worked
-- through by an administrator.
create table public.admin_inquiries (
  id                bigint generated always as identity primary key,
  -- Nullable: the legacy form was reachable while logged out, in which case
  -- there was no account to attach.
  user_id           bigint references public.users (id) on delete set null,
  contact_email     text,
  body              text not null,
  status            text not null default 'OPEN',
  resolution_note   text,
  resolved_by_user_id bigint references public.users (id) on delete set null,
  resolved_at       timestamptz,
  created_at        timestamptz not null default now(),
  updated_at        timestamptz not null default now(),

  constraint admin_inquiries_status_check      check (status in ('OPEN', 'RESOLVED')),
  constraint admin_inquiries_resolved_check    check ((status = 'RESOLVED') = (resolved_at is not null)),
  constraint admin_inquiries_contactable       check (user_id is not null or contact_email is not null),
  constraint admin_inquiries_email_lowercase   check (contact_email is null or contact_email = lower(contact_email))
);

create index admin_inquiries_status_idx on public.admin_inquiries (status, created_at desc);
create index admin_inquiries_user_idx   on public.admin_inquiries (user_id);

create trigger admin_inquiries_set_updated_at
  before update on public.admin_inquiries
  for each row execute function public.set_updated_at();

comment on table public.admin_inquiries is '聯絡我們 inbox. Legacy table: rearMessageBox.';
comment on column public.admin_inquiries.status is
  'OPEN / RESOLVED. Legacy used a boolean column literally named "condition".';

-- Audit trail for administrative actions (suspension, re-activation, deletion).
-- The legacy app performed these silently.
create table public.admin_audit_log (
  id             bigint generated always as identity primary key,
  actor_user_id  bigint references public.users (id) on delete set null,
  action         text   not null,
  target_type    text   not null,
  target_id      text   not null,
  detail         jsonb,
  created_at     timestamptz not null default now()
);

create index admin_audit_log_actor_idx  on public.admin_audit_log (actor_user_id, created_at desc);
create index admin_audit_log_target_idx on public.admin_audit_log (target_type, target_id);

comment on table public.admin_audit_log is 'Who did what in the back office, and when.';
