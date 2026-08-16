-- =============================================================================
-- Development seed data
--
-- Applied automatically by `supabase db reset` / `supabase start`.
-- The original MSSQL database is not available, so this stands in for it.
--
-- Every seeded account uses the password:  Password123!
-- =============================================================================

-- -----------------------------------------------------------------------------
-- Accounts
-- -----------------------------------------------------------------------------
insert into public.users (email, password_hash, enabled, email_verified_at) values
  ('admin@peppernoodles.local',   '$2a$10$aXzXjZAk1q9rLu7G4T340eUOfTlOEL3H54bvCAVAhIwrw1o0ySkJ2', true, now()),
  ('owner.din@peppernoodles.local',    '$2a$10$aXzXjZAk1q9rLu7G4T340eUOfTlOEL3H54bvCAVAhIwrw1o0ySkJ2', true, now()),
  ('owner.chan@peppernoodles.local',   '$2a$10$aXzXjZAk1q9rLu7G4T340eUOfTlOEL3H54bvCAVAhIwrw1o0ySkJ2', true, now()),
  ('owner.chun@peppernoodles.local',   '$2a$10$aXzXjZAk1q9rLu7G4T340eUOfTlOEL3H54bvCAVAhIwrw1o0ySkJ2', true, now()),
  ('mei@example.com',            '$2a$10$aXzXjZAk1q9rLu7G4T340eUOfTlOEL3H54bvCAVAhIwrw1o0ySkJ2', true, now()),
  ('wei@example.com',            '$2a$10$aXzXjZAk1q9rLu7G4T340eUOfTlOEL3H54bvCAVAhIwrw1o0ySkJ2', true, now()),
  ('ling@example.com',           '$2a$10$aXzXjZAk1q9rLu7G4T340eUOfTlOEL3H54bvCAVAhIwrw1o0ySkJ2', true, now()),
  ('pending@example.com',        '$2a$10$aXzXjZAk1q9rLu7G4T340eUOfTlOEL3H54bvCAVAhIwrw1o0ySkJ2', false, null);

insert into public.user_roles (user_id, role_id)
select u.id, r.id
from public.users u
join public.roles r on r.name = case
  when u.email = 'admin@peppernoodles.local' then 'ROLE_ADMIN'
  when u.email like 'owner.%'                then 'ROLE_COMPANY'
  else 'ROLE_USER'
end;

insert into public.user_profiles (user_id, real_name, nickname, phone, birth_date, gender, location)
select u.id, p.real_name, p.nickname, p.phone, p.birth_date, p.gender, p.location
from (values
  ('mei@example.com',     '林小美', '小美',   '0912345678', date '1995-04-12', 'FEMALE', '台北市大安區'),
  ('wei@example.com',     '陳大偉', '偉哥',   '0922333444', date '1990-11-03', 'MALE',   '台北市中山區'),
  ('ling@example.com',    '黃玲玲', '玲玲',   '0933555777', date '1998-07-21', 'FEMALE', '新北市板橋區'),
  ('pending@example.com', '待驗證', '新用戶', '0900000000', date '2000-01-01', 'OTHER',  '台北市信義區')
) as p (email, real_name, nickname, phone, birth_date, gender, location)
join public.users u on u.email = p.email;

insert into public.company_profiles (user_id, real_name, phone, location, tier)
select u.id, c.real_name, c.phone, c.location, c.tier
from (values
  ('owner.din@peppernoodles.local',  '鼎豐餐飲股份有限公司', '0227208889', '台北市信義區', 'GOLD'),
  ('owner.chan@peppernoodles.local', '詹記餐飲有限公司',     '0225082222', '台北市中山區', 'SILVER'),
  ('owner.chun@peppernoodles.local', '春水堂人文茶館',       '0225118899', '台北市中山區', 'GOLD')
) as c (email, real_name, phone, location, tier)
join public.users u on u.email = c.email;

insert into public.user_stats (user_id)
select id from public.users;

-- -----------------------------------------------------------------------------
-- Food tags
-- -----------------------------------------------------------------------------
insert into public.food_tags (name) values
  ('台式'), ('日式'), ('韓式'), ('義式'), ('美式'), ('中式'), ('泰式'),
  ('早午餐'), ('火鍋'), ('燒烤'), ('甜點'), ('咖啡'), ('素食'),
  ('小吃'), ('麵食'), ('牛肉麵'), ('小籠包'), ('手搖飲'), ('宵夜'), ('平價');

insert into public.user_food_tags (user_id, tag_id)
select u.id, t.id
from (values
  ('mei@example.com',  '日式'), ('mei@example.com',  '甜點'), ('mei@example.com',  '咖啡'),
  ('wei@example.com',  '火鍋'), ('wei@example.com',  '燒烤'), ('wei@example.com',  '台式'),
  ('ling@example.com', '素食'), ('ling@example.com', '早午餐')
) as s (email, tag)
join public.users u     on u.email = s.email
join public.food_tags t on t.name  = s.tag;

-- -----------------------------------------------------------------------------
-- Restaurants
-- -----------------------------------------------------------------------------
insert into public.restaurants (owner_user_id, name, address, contact, website, latitude, longitude)
select u.id, r.name, r.address, r.contact, r.website, r.lat, r.lng
from (values
  ('owner.din@peppernoodles.local',  '鼎豐小籠包 信義店',   '台北市信義區松高路11號',      '02-2720-8889', 'https://example.com/dinfeng',  25.0396, 121.5670),
  ('owner.din@peppernoodles.local',  '鼎豐小籠包 中山店',   '台北市中山區南京東路二段99號', '02-2508-1188', 'https://example.com/dinfeng',  25.0521, 121.5330),
  ('owner.chan@peppernoodles.local', '詹記麻辣火鍋 南京店', '台北市中山區南京東路三段5號',  '02-2508-2222', 'https://example.com/chanji',   25.0519, 121.5382),
  ('owner.chan@peppernoodles.local', '詹記燒烤 敦北店',     '台北市松山區敦化北路145號',    '02-2718-3333', null,                            25.0532, 121.5490),
  ('owner.chun@peppernoodles.local', '春水堂 中山店',       '台北市中山區中山北路一段85號', '02-2511-8899', 'https://example.com/chunshui', 25.0524, 121.5228),
  ('owner.chun@peppernoodles.local', '春水堂 永康店',       '台北市大安區永康街12號',       '02-2395-6677', 'https://example.com/chunshui', 25.0312, 121.5296),
  ('owner.din@peppernoodles.local',  '阜杭豆漿 華山店',     '台北市中正區忠孝東路一段108號','02-2392-2175', null,                            25.0443, 121.5251),
  ('owner.chan@peppernoodles.local', '樂麵屋 忠孝店',       '台北市大安區忠孝東路四段181號','02-2711-4567', null,                            25.0414, 121.5533)
) as r (email, name, address, contact, website, lat, lng)
join public.users u on u.email = r.email;

insert into public.restaurant_food_tags (restaurant_id, tag_id)
select r.id, t.id
from (values
  ('鼎豐小籠包 信義店',   '小籠包'), ('鼎豐小籠包 信義店',   '中式'), ('鼎豐小籠包 信義店',   '台式'),
  ('鼎豐小籠包 中山店',   '小籠包'), ('鼎豐小籠包 中山店',   '中式'),
  ('詹記麻辣火鍋 南京店', '火鍋'),   ('詹記麻辣火鍋 南京店', '台式'), ('詹記麻辣火鍋 南京店', '宵夜'),
  ('詹記燒烤 敦北店',     '燒烤'),   ('詹記燒烤 敦北店',     '宵夜'),
  ('春水堂 中山店',       '手搖飲'), ('春水堂 中山店',       '甜點'), ('春水堂 中山店',       '台式'),
  ('春水堂 永康店',       '手搖飲'), ('春水堂 永康店',       '咖啡'),
  ('阜杭豆漿 華山店',     '早午餐'), ('阜杭豆漿 華山店',     '小吃'), ('阜杭豆漿 華山店',     '平價'),
  ('樂麵屋 忠孝店',       '日式'),   ('樂麵屋 忠孝店',       '麵食')
) as s (restaurant, tag)
join public.restaurants r on r.name = s.restaurant
join public.food_tags   t on t.name = s.tag;

-- Business hours: Tue–Sun lunch and dinner service, closed Mondays.
insert into public.restaurant_business_hours (restaurant_id, day_of_week, opens_at, closes_at)
select r.id, d.dow, s.opens, s.closes
from public.restaurants r
cross join (values (0), (2), (3), (4), (5), (6)) as d (dow)
cross join (values (time '11:00', time '14:30'), (time '17:00', time '21:30')) as s (opens, closes)
where r.name not in ('阜杭豆漿 華山店', '春水堂 中山店', '春水堂 永康店');

-- Breakfast shop: early mornings, closed Mondays.
insert into public.restaurant_business_hours (restaurant_id, day_of_week, opens_at, closes_at)
select r.id, d.dow, time '05:30', time '12:30'
from public.restaurants r
cross join (values (0), (2), (3), (4), (5), (6)) as d (dow)
where r.name = '阜杭豆漿 華山店';

-- Tea houses: open all week, one continuous service.
insert into public.restaurant_business_hours (restaurant_id, day_of_week, opens_at, closes_at)
select r.id, d.dow, time '10:00', time '22:00'
from public.restaurants r
cross join (values (0), (1), (2), (3), (4), (5), (6)) as d (dow)
where r.name in ('春水堂 中山店', '春水堂 永康店');

insert into public.restaurant_events (restaurant_id, name, content, starts_on, ends_on)
select r.id, e.name, e.content, e.starts_on, e.ends_on
from (values
  ('鼎豐小籠包 信義店',   '開幕週年慶',     '小籠包買十送三，內用限定。',            current_date - 3,  current_date + 25),
  ('詹記麻辣火鍋 南京店', '深夜鍋物時段',   '晚間十點後鍋底半價。',                  current_date - 10, current_date + 50),
  ('春水堂 中山店',       '珍奶第二杯半價', '每日下午兩點至五點，珍珠奶茶第二杯半價。', current_date,      current_date + 14),
  ('樂麵屋 忠孝店',       '限定味噌拉麵',   '北海道白味噌湯頭，數量有限。',           current_date + 7,  current_date + 37)
) as e (restaurant, name, content, starts_on, ends_on)
join public.restaurants r on r.name = e.restaurant;

-- -----------------------------------------------------------------------------
-- Reviews
-- -----------------------------------------------------------------------------
insert into public.restaurant_reviews (restaurant_id, author_user_id, body, score, created_at)
select r.id, u.id, v.body, v.score, now() - (v.days_ago || ' days')::interval
from (values
  ('鼎豐小籠包 信義店',   'mei@example.com',  '皮薄餡多，湯汁很鮮，可惜要排隊快一小時。', 5, 2),
  ('鼎豐小籠包 信義店',   'wei@example.com',  '穩定好吃，但價格逐年上升。',               4, 9),
  ('鼎豐小籠包 信義店',   'ling@example.com', '素食選擇偏少，只能點青菜。',               3, 15),
  ('詹記麻辣火鍋 南京店', 'wei@example.com',  '鴨血豆腐無限續，湯頭夠麻夠辣！',           5, 1),
  ('詹記麻辣火鍋 南京店', 'mei@example.com',  '很好吃但很油，隔天會後悔。',               4, 6),
  ('春水堂 中山店',       'mei@example.com',  '珍奶還是這家最對味，糖度可以調。',         5, 4),
  ('春水堂 中山店',       'ling@example.com', '環境舒服，適合坐著工作一下午。',           4, 11),
  ('阜杭豆漿 華山店',     'wei@example.com',  '厚燒餅加蛋是本體，但六點就要去排。',       5, 3),
  ('阜杭豆漿 華山店',     'ling@example.com', '排隊太久，東西不錯但不會特地再來。',       3, 20),
  ('樂麵屋 忠孝店',       'mei@example.com',  '叉燒偏乾，湯頭還可以。',                   3, 5),
  ('詹記燒烤 敦北店',     'wei@example.com',  '宵夜首選，啤酒很冰。',                     4, 8)
) as v (restaurant, email, body, score, days_ago)
join public.restaurants r on r.name  = v.restaurant
join public.users       u on u.email = v.email;

insert into public.restaurant_review_replies (review_id, author_user_id, body)
select rv.id, u.id, '謝謝您的回饋！我們會持續改進出餐速度。'
from public.restaurant_reviews rv
join public.restaurants r on r.id = rv.restaurant_id
join public.users       u on u.id = r.owner_user_id
where rv.score <= 3;

insert into public.user_favourite_restaurants (user_id, restaurant_id)
select u.id, r.id
from (values
  ('mei@example.com',  '鼎豐小籠包 信義店'),
  ('mei@example.com',  '春水堂 中山店'),
  ('wei@example.com',  '詹記麻辣火鍋 南京店'),
  ('wei@example.com',  '阜杭豆漿 華山店'),
  ('ling@example.com', '春水堂 中山店')
) as f (email, restaurant)
join public.users       u on u.email = f.email
join public.restaurants r on r.name  = f.restaurant;

-- -----------------------------------------------------------------------------
-- Social graph
-- -----------------------------------------------------------------------------
insert into public.friendships (requester_user_id, addressee_user_id, status, responded_at)
select a.id, b.id, f.status, case when f.status = 'ACCEPTED' then now() end
from (values
  ('mei@example.com',  'wei@example.com',  'ACCEPTED'),
  ('mei@example.com',  'ling@example.com', 'ACCEPTED'),
  ('wei@example.com',  'ling@example.com', 'PENDING')
) as f (requester, addressee, status)
join public.users a on a.email = f.requester
join public.users b on b.email = f.addressee;

insert into public.user_follows (follower_user_id, followee_user_id)
select a.id, b.id
from (values
  ('ling@example.com', 'mei@example.com'),
  ('wei@example.com',  'mei@example.com')
) as f (follower, followee)
join public.users a on a.email = f.follower
join public.users b on b.email = f.followee;

insert into public.chat_messages (sender_user_id, recipient_user_id, body, created_at, read_at)
select s.id, r.id, m.body, now() - (m.mins || ' minutes')::interval,
       case when m.seen then now() end
from (values
  ('mei@example.com', 'wei@example.com', '欸今晚要不要去詹記？', 90, true),
  ('wei@example.com', 'mei@example.com', '好啊，幾點？',          85, true),
  ('mei@example.com', 'wei@example.com', '八點南京店見',          80, true),
  ('wei@example.com', 'mei@example.com', '收到，我先去排隊',      12, false)
) as m (sender, recipient, body, mins, seen)
join public.users s on s.email = m.sender
join public.users r on r.email = m.recipient;

-- -----------------------------------------------------------------------------
-- Forum
-- -----------------------------------------------------------------------------
insert into public.forum_posts (author_user_id, body, created_at)
select u.id, p.body, now() - (p.days || ' days')::interval
from (values
  ('mei@example.com',  '台北信義區有推薦的平價午餐嗎？每天吃便當快吃膩了。', 1),
  ('wei@example.com',  '整理了一份台北宵夜地圖，火鍋燒烤都有，需要的自取。', 4),
  ('ling@example.com', '有沒有素食友善的早午餐？最好可以久坐。',             7)
) as p (email, body, days)
join public.users u on u.email = p.email;

insert into public.forum_post_tags (post_id, tag_id)
select p.id, t.id
from public.forum_posts p
join public.users u on u.id = p.author_user_id
join public.food_tags t on t.name = case
  when u.email = 'mei@example.com'  then '平價'
  when u.email = 'wei@example.com'  then '宵夜'
  else '素食'
end;

insert into public.forum_comments (post_id, author_user_id, body)
select p.id, u.id, c.body
from (values
  ('mei@example.com',  'wei@example.com',  '阜杭豆漿早上去，一百塊有找。'),
  ('mei@example.com',  'ling@example.com', '樂麵屋午餐有套餐，還算划算。'),
  ('wei@example.com',  'mei@example.com',  '收藏了，感謝分享！')
) as c (post_author, email, body)
join public.users pa on pa.email = c.post_author
join public.forum_posts p on p.author_user_id = pa.id
join public.users u on u.email = c.email;

insert into public.forum_bookmarks (post_id, user_id)
select p.id, u.id
from public.forum_posts p
join public.users pa on pa.id = p.author_user_id
join public.users u  on u.email = 'mei@example.com'
where pa.email = 'wei@example.com';

insert into public.wall_messages (wall_owner_user_id, author_user_id, body)
select o.id, a.id, w.body
from (values
  ('mei@example.com', 'wei@example.com',  '生日快樂！'),
  ('mei@example.com', 'ling@example.com', '上次那家小籠包真的讚')
) as w (owner, author, body)
join public.users o on o.email = w.owner
join public.users a on a.email = w.author;

insert into public.wall_message_likes (message_id, user_id)
select m.id, u.id
from public.wall_messages m
join public.users u on u.email = 'mei@example.com';

-- -----------------------------------------------------------------------------
-- Shop
-- -----------------------------------------------------------------------------
insert into public.product_categories (name) values
  ('冷凍食品'), ('飲品'), ('醬料'), ('伴手禮');

insert into public.product_subcategories (category_id, name)
select c.id, s.name
from (values
  ('冷凍食品', '冷凍小籠包'), ('冷凍食品', '冷凍鍋物'), ('冷凍食品', '冷凍麵條'),
  ('飲品',     '茶葉'),       ('飲品',     '沖泡飲品'),
  ('醬料',     '辣椒醬'),     ('醬料',     '火鍋湯底'),   ('醬料',     '烤肉醬'),
  ('伴手禮',   '禮盒')
) as s (category, name)
join public.product_categories c on c.name = s.category;

insert into public.products (restaurant_id, subcategory_id, name, description, price, quantity, status, released_at)
select r.id, sc.id, p.name, p.description, p.price, p.quantity, p.status,
       case when p.status = 'LISTED' then now() - (p.days || ' days')::interval end
from (values
  ('鼎豐小籠包 信義店',   '冷凍小籠包', '冷凍小籠包 20入',   '在家蒸八分鐘，還原店內風味。',        320.00, 120, 'LISTED',   30),
  ('鼎豐小籠包 信義店',   '冷凍小籠包', '冷凍蝦仁燒賣 15入', '整顆蝦仁，適合早餐或便當。',          280.00,  80, 'LISTED',   25),
  ('鼎豐小籠包 中山店',   '禮盒',       '經典組合禮盒',      '小籠包加燒賣，附提袋。',              680.00,  40, 'LISTED',   14),
  ('詹記麻辣火鍋 南京店', '火鍋湯底',   '招牌麻辣湯底 1kg',  '需冷凍保存，一包可煮四人份。',        450.00,  60, 'LISTED',   40),
  ('詹記麻辣火鍋 南京店', '冷凍鍋物',   '鴨血豆腐組合包',    '店內同款鴨血與凍豆腐。',              260.00, 100, 'LISTED',   40),
  ('詹記麻辣火鍋 南京店', '辣椒醬',     '手工辣椒醬 200g',   '中辣，拌麵拌飯都適合。',              180.00, 200, 'LISTED',   60),
  ('詹記燒烤 敦北店',     '烤肉醬',     '燒烤醬 300ml',      '店內同款刷醬。',                      160.00,   0, 'DELISTED', 90),
  ('春水堂 中山店',       '茶葉',       '四季春茶葉 150g',   '手搖店同款茶葉，冷泡熱泡皆宜。',      380.00,  90, 'LISTED',   20),
  ('春水堂 中山店',       '沖泡飲品',   '珍珠奶茶沖泡包 8入','附黑糖珍珠，三分鐘完成。',            420.00,  75, 'LISTED',   10),
  ('春水堂 永康店',       '禮盒',       '茶香禮盒',          '茶葉兩罐加茶具，送禮體面。',          980.00,  25, 'LISTED',    5),
  ('阜杭豆漿 華山店',     '沖泡飲品',   '濃豆漿粉 500g',     '無添加糖，可自行調整甜度。',          220.00, 150, 'LISTED',   35),
  ('樂麵屋 忠孝店',       '冷凍麵條', '冷凍拉麵組 2人份',  '含麵條、湯頭與叉燒。',                360.00,  50, 'LISTED',   12)
) as p (restaurant, subcategory, name, description, price, quantity, status, days)
join public.restaurants          r  on r.name  = p.restaurant
join public.product_subcategories sc on sc.name = p.subcategory;

insert into public.product_food_tags (product_id, tag_id)
select p.id, t.id
from public.products p
join public.restaurant_food_tags rft on rft.restaurant_id = p.restaurant_id
join public.food_tags t on t.id = rft.tag_id;

insert into public.cart_items (user_id, product_id, quantity)
select u.id, p.id, c.qty
from (values
  ('mei@example.com', '冷凍小籠包 20入',    2),
  ('mei@example.com', '四季春茶葉 150g',    1),
  ('wei@example.com', '招牌麻辣湯底 1kg',   3)
) as c (email, product, qty)
join public.users    u on u.email = c.email
join public.products p on p.name  = c.product;

-- Orders: one paid, one awaiting payment, one already expired.
insert into public.orders (order_no, user_id, receiver_name, receiver_phone, receiver_address,
                           status, total_cost, created_at, paid_at, cancelled_at, expires_at)
select o.order_no, u.id, o.name, o.phone, o.address, o.status, o.total,
       o.created, o.paid, o.cancelled, o.expires
from (values
  ('PN20260810-000001', 'mei@example.com', '林小美', '0912345678', '台北市大安區信義路四段1號',
   'PAID',    920.00, now() - interval '6 days', now() - interval '6 days' + interval '20 minutes', null, now() - interval '5 days'),
  ('PN20260814-000002', 'wei@example.com', '陳大偉', '0922333444', '台北市中山區南京東路三段2號',
   'PENDING', 1350.00, now() - interval '2 hours', null, null, now() + interval '22 hours'),
  ('PN20260801-000003', 'ling@example.com', '黃玲玲', '0933555777', '新北市板橋區文化路一段3號',
   'EXPIRED', 380.00, now() - interval '15 days', null, now() - interval '14 days', now() - interval '14 days')
) as o (order_no, email, name, phone, address, status, total, created, paid, cancelled, expires)
join public.users u on u.email = o.email;

insert into public.order_items (order_id, product_id, product_name, unit_price, quantity)
select o.id, p.id, p.name, p.price, i.qty
from (values
  ('PN20260810-000001', '冷凍小籠包 20入',  2),
  ('PN20260810-000001', '濃豆漿粉 500g',    1),
  ('PN20260810-000001', '手工辣椒醬 200g',  1),
  ('PN20260814-000002', '招牌麻辣湯底 1kg', 3),
  ('PN20260801-000003', '四季春茶葉 150g',  1)
) as i (order_no, product, qty)
join public.orders   o on o.order_no = i.order_no
join public.products p on p.name     = i.product;

insert into public.payments (order_id, merchant_trade_no, amount, status)
select o.id, replace(o.order_no, '-', ''), o.total_cost, 'SUCCEEDED'
from public.orders o
where o.status = 'PAID';

-- -----------------------------------------------------------------------------
-- Back office
-- -----------------------------------------------------------------------------
insert into public.admin_inquiries (user_id, contact_email, body, status, resolved_at, resolved_by_user_id)
select u.id, null, i.body, i.status,
       case when i.status = 'RESOLVED' then now() - interval '1 day' end,
       case when i.status = 'RESOLVED' then (select id from public.users where email = 'admin@peppernoodles.local') end
from (values
  ('mei@example.com',  '想詢問訂單什麼時候會出貨？',      'OPEN'),
  ('ling@example.com', '網站上的餐廳地址標錯位置了。',    'RESOLVED')
) as i (email, body, status)
join public.users u on u.email = i.email;

insert into public.admin_inquiries (user_id, contact_email, body, status)
values (null, 'visitor@example.com', '請問要如何申請成為企業會員？', 'OPEN');

-- -----------------------------------------------------------------------------
-- Sanity checks
--
-- Most inserts above are `insert ... select ... join`, which silently drops a
-- row whenever a natural-key lookup misses. These assertions turn that into a
-- loud failure at seed time.
-- -----------------------------------------------------------------------------
do $$
declare
  expected constant jsonb := '{
    "users": 8, "user_roles": 8, "user_profiles": 4, "company_profiles": 3,
    "food_tags": 20, "user_food_tags": 8,
    "restaurants": 8, "restaurant_food_tags": 20, "restaurant_business_hours": 80,
    "restaurant_events": 4, "restaurant_reviews": 11, "user_favourite_restaurants": 5,
    "friendships": 3, "user_follows": 2, "chat_messages": 4,
    "forum_posts": 3, "forum_comments": 3, "wall_messages": 2,
    "product_categories": 4, "product_subcategories": 9, "products": 12,
    "cart_items": 3, "orders": 3, "order_items": 5, "payments": 1,
    "admin_inquiries": 3
  }'::jsonb;
  tbl text;
  want bigint;
  got  bigint;
begin
  for tbl, want in select key, value::bigint from jsonb_each_text(expected) loop
    execute format('select count(*) from public.%I', tbl) into got;
    if got <> want then
      raise exception 'seed: table % has % rows, expected %', tbl, got, want;
    end if;
  end loop;
  raise notice 'seed: all row counts match';
end;
$$;
