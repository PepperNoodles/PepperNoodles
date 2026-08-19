-- =============================================================================
-- 電子報訂閱
--
-- The 2021 首頁 carried a "Subscribe For Newsletter" form that posted nowhere.
-- This is the backend it never had.
--
-- Double opt-in on purpose: an address is only mailable once its owner has
-- clicked the confirmation link, so the form cannot be used to sign somebody
-- else up. Every row also carries a permanent unsubscribe token.
-- =============================================================================

create table public.newsletter_subscriptions (
  id                bigint generated always as identity primary key,

  -- Stored lower-cased for the same reason as users.email: a plain unique
  -- constraint then gives case-insensitive uniqueness.
  email             text not null unique,

  -- Set once the confirmation link is followed. Null means "not mailable".
  confirmed_at      timestamptz,

  -- Set when the reader opts out. Kept rather than deleted so a later
  -- re-subscribe does not silently resurrect someone who asked to leave
  -- without confirming again.
  unsubscribed_at   timestamptz,

  confirm_token_hash     text unique,
  confirm_expires_at     timestamptz,
  -- Permanent: it has to keep working in mail sent months ago.
  unsubscribe_token_hash text not null unique,

  source            text,
  created_at        timestamptz not null default now(),
  updated_at        timestamptz not null default now(),

  constraint newsletter_email_format
    check (email ~ '^[^@[:space:]]+@[^@[:space:]]+\.[^@[:space:]]+$'),
  constraint newsletter_email_lowercase check (email = lower(email))
);

create index newsletter_subscriptions_mailable_idx
  on public.newsletter_subscriptions (confirmed_at)
  where confirmed_at is not null and unsubscribed_at is null;

create trigger newsletter_subscriptions_set_updated_at
  before update on public.newsletter_subscriptions
  for each row execute function public.set_updated_at();

comment on table public.newsletter_subscriptions is
  '電子報訂閱. Double opt-in; only rows with confirmed_at set and
   unsubscribed_at null may be mailed.';

alter table public.newsletter_subscriptions enable row level security;
