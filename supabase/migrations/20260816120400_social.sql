-- =============================================================================
-- Social graph, walls, forum, and chat
--
-- Legacy origin: friendlist, UserFollowerForm, messageBox, likefeeds, forum,
--                forumMessageBox, forumReplyMessage, forumCollections,
--                Forum_tag, socketMessage
-- =============================================================================

-- --- friendships -------------------------------------------------------------
-- Legacy `friendlist` stored a free-text `friendship` column and allowed both
-- (a,b) and (b,a) to exist independently. A canonical ordering plus a check
-- constraint makes a duplicate pair impossible.
create table public.friendships (
  id                bigint generated always as identity primary key,
  requester_user_id bigint not null references public.users (id) on delete cascade,
  addressee_user_id bigint not null references public.users (id) on delete cascade,
  status            text   not null default 'PENDING',
  created_at        timestamptz not null default now(),
  responded_at      timestamptz,

  constraint friendships_status_check   check (status in ('PENDING', 'ACCEPTED', 'DECLINED', 'BLOCKED')),
  constraint friendships_not_self       check (requester_user_id <> addressee_user_id),
  constraint friendships_unique_pair    unique (requester_user_id, addressee_user_id)
);

create index friendships_addressee_idx on public.friendships (addressee_user_id, status);
create index friendships_requester_idx on public.friendships (requester_user_id, status);

comment on table public.friendships is '好友系統. Legacy table: friendlist.';

-- --- follows -----------------------------------------------------------------
create table public.user_follows (
  follower_user_id bigint not null references public.users (id) on delete cascade,
  followee_user_id bigint not null references public.users (id) on delete cascade,
  created_at       timestamptz not null default now(),

  primary key (follower_user_id, followee_user_id),
  constraint user_follows_not_self check (follower_user_id <> followee_user_id)
);

create index user_follows_followee_idx on public.user_follows (followee_user_id);

comment on table public.user_follows is 'Legacy table: UserFollowerForm.';

-- --- user wall ---------------------------------------------------------------
-- Legacy `messageBox`: comments left on a member's profile, with self-
-- referencing replies one level deep.
create table public.wall_messages (
  id                bigint not null generated always as identity primary key,
  wall_owner_user_id bigint not null references public.users (id) on delete cascade,
  author_user_id    bigint not null references public.users (id) on delete cascade,
  parent_id         bigint references public.wall_messages (id) on delete cascade,
  body              text   not null,
  created_at        timestamptz not null default now(),
  updated_at        timestamptz not null default now()
);

create index wall_messages_owner_idx  on public.wall_messages (wall_owner_user_id, created_at desc);
create index wall_messages_parent_idx on public.wall_messages (parent_id);

create trigger wall_messages_set_updated_at
  before update on public.wall_messages
  for each row execute function public.set_updated_at();

comment on table public.wall_messages is '會員留言板. Legacy table: messageBox.';

-- Legacy kept both a `likefeeds` table and a denormalised `likeAmount` column
-- that drifted apart. The table is now the single source of truth and the
-- composite key stops a member liking the same message twice.
create table public.wall_message_likes (
  message_id bigint not null references public.wall_messages (id) on delete cascade,
  user_id    bigint not null references public.users (id)         on delete cascade,
  created_at timestamptz not null default now(),
  primary key (message_id, user_id)
);

create index wall_message_likes_user_idx on public.wall_message_likes (user_id);

comment on table public.wall_message_likes is 'Legacy table: likefeeds.';

-- --- forum -------------------------------------------------------------------
create table public.forum_posts (
  id             bigint generated always as identity primary key,
  author_user_id bigint not null references public.users (id) on delete cascade,
  body           text   not null,
  image_path     text,
  created_at     timestamptz not null default now(),
  updated_at     timestamptz not null default now()
);

create index forum_posts_author_idx  on public.forum_posts (author_user_id);
create index forum_posts_created_idx on public.forum_posts (created_at desc);

create trigger forum_posts_set_updated_at
  before update on public.forum_posts
  for each row execute function public.set_updated_at();

comment on table public.forum_posts is 'Legacy table: forum.';

create table public.forum_post_tags (
  post_id bigint not null references public.forum_posts (id) on delete cascade,
  tag_id  bigint not null references public.food_tags (id)   on delete cascade,
  primary key (post_id, tag_id)
);

create index forum_post_tags_tag_idx on public.forum_post_tags (tag_id);

comment on table public.forum_post_tags is 'Legacy table: Forum_tag.';

create table public.forum_bookmarks (
  post_id    bigint not null references public.forum_posts (id) on delete cascade,
  user_id    bigint not null references public.users (id)       on delete cascade,
  created_at timestamptz not null default now(),
  primary key (post_id, user_id)
);

create index forum_bookmarks_user_idx on public.forum_bookmarks (user_id);

comment on table public.forum_bookmarks is 'Legacy table: forumCollections.';

create table public.forum_comments (
  id             bigint generated always as identity primary key,
  post_id        bigint not null references public.forum_posts (id) on delete cascade,
  author_user_id bigint not null references public.users (id)       on delete cascade,
  body           text   not null,
  score          smallint,
  created_at     timestamptz not null default now(),
  updated_at     timestamptz not null default now(),

  constraint forum_comments_score_range check (score is null or score between 1 and 5)
);

create index forum_comments_post_idx on public.forum_comments (post_id, created_at);

create trigger forum_comments_set_updated_at
  before update on public.forum_comments
  for each row execute function public.set_updated_at();

comment on table public.forum_comments is 'Legacy table: forumMessageBox.';

-- Legacy `forumReplyMessage` carried a OneToOne to the comment *and* two
-- separate user FKs (the replier and the member being replied to).
create table public.forum_comment_replies (
  id               bigint generated always as identity primary key,
  comment_id       bigint not null references public.forum_comments (id) on delete cascade,
  author_user_id   bigint not null references public.users (id)          on delete cascade,
  reply_to_user_id bigint references public.users (id)                   on delete set null,
  body             text   not null,
  created_at       timestamptz not null default now(),
  updated_at       timestamptz not null default now()
);

create index forum_comment_replies_comment_idx on public.forum_comment_replies (comment_id, created_at);

create trigger forum_comment_replies_set_updated_at
  before update on public.forum_comment_replies
  for each row execute function public.set_updated_at();

comment on table public.forum_comment_replies is 'Legacy table: forumReplyMessage.';

-- --- direct chat -------------------------------------------------------------
create table public.chat_messages (
  id                bigint generated always as identity primary key,
  sender_user_id    bigint not null references public.users (id) on delete cascade,
  recipient_user_id bigint not null references public.users (id) on delete cascade,
  body              text   not null,
  read_at           timestamptz,
  created_at        timestamptz not null default now(),

  constraint chat_messages_not_self check (sender_user_id <> recipient_user_id)
);

-- Serves "the conversation between A and B, newest first" in both directions.
create index chat_messages_pair_idx
  on public.chat_messages (least(sender_user_id, recipient_user_id),
                           greatest(sender_user_id, recipient_user_id),
                           created_at desc);
create index chat_messages_unread_idx
  on public.chat_messages (recipient_user_id) where read_at is null;

comment on table public.chat_messages is 'WebSocket 好友聊天. Legacy table: socketMessage.';
