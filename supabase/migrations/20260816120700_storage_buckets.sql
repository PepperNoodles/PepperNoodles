-- =============================================================================
-- Storage buckets
--
-- Every image the legacy schema held as a java.sql.Blob column now lives here.
-- Tables keep only the object path.
-- =============================================================================

insert into storage.buckets (id, name, public, file_size_limit, allowed_mime_types)
values
  ('restaurant-photos', 'restaurant-photos', true, 15728640,
   array['image/jpeg', 'image/png', 'image/webp', 'image/avif']),
  ('menu-photos',       'menu-photos',       true, 15728640,
   array['image/jpeg', 'image/png', 'image/webp', 'image/avif']),
  ('event-photos',      'event-photos',      true, 15728640,
   array['image/jpeg', 'image/png', 'image/webp', 'image/avif']),
  ('product-photos',    'product-photos',    true, 15728640,
   array['image/jpeg', 'image/png', 'image/webp', 'image/avif']),
  ('user-avatars',      'user-avatars',      true,  5242880,
   array['image/jpeg', 'image/png', 'image/webp', 'image/avif']),
  ('forum-images',      'forum-images',      true, 15728640,
   array['image/jpeg', 'image/png', 'image/webp', 'image/avif'])
on conflict (id) do nothing;

-- Buckets are readable by anyone (these are public product and venue photos),
-- but only the backend's service_role key may write. Uploads are always
-- brokered by the API so that ownership and file type are checked first.
create policy "public read of image buckets"
  on storage.objects
  for select
  to public
  using (
    bucket_id in ('restaurant-photos', 'menu-photos', 'event-photos',
                  'product-photos', 'user-avatars', 'forum-images')
  );
