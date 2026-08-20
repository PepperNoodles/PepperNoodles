#!/usr/bin/env bash
#
# Bulk-uploads product photos.
#
# The 2021 product images lived in MS SQL BLOBs and that database is gone, so
# the seeded catalogue ships without photos. This uploads a folder of them
# through the normal API — the same path the company dashboard uses — so the
# images land in Supabase Storage rather than the database.
#
# Name each file after the product id:  12.jpg, 13.png, …
# Find ids with:  curl -s "$API/shop/products?size=100" | jq '.content[] | {id, name}'
#
#   ./scripts/upload-product-images.sh ./photos admin@peppernoodles.local
#
set -euo pipefail

DIR=${1:-}
EMAIL=${2:-admin@peppernoodles.local}
API=${API_BASE_URL:-http://localhost:8080/api/v1}

if [ -z "$DIR" ] || [ ! -d "$DIR" ]; then
  echo "usage: $0 <directory-of-images> [account-email]" >&2
  echo "       images must be named <productId>.<ext>, e.g. 12.jpg" >&2
  exit 64
fi

read -rsp "Password for $EMAIL: " PASSWORD
echo

TOKEN=$(curl -fsS -X POST "$API/auth/login" \
  -H 'Content-Type: application/json' \
  -d "{\"email\":\"$EMAIL\",\"password\":\"$PASSWORD\"}" \
  | python3 -c 'import json,sys; print(json.load(sys.stdin)["accessToken"])')

if [ -z "$TOKEN" ]; then
  echo "Login failed." >&2
  exit 1
fi

uploaded=0
failed=0

for file in "$DIR"/*; do
  [ -f "$file" ] || continue

  base=$(basename "$file")
  product_id=${base%%.*}

  if ! [[ "$product_id" =~ ^[0-9]+$ ]]; then
    echo "skip  $base (filename is not a product id)"
    continue
  fi

  # The upload endpoint enforces ownership, so a 403 here means this account
  # does not own that product — use an admin, or the right company account.
  status=$(curl -s -o /tmp/upload-response -w '%{http_code}' \
    -X POST "$API/shop/products/$product_id/image" \
    -H "Authorization: Bearer $TOKEN" \
    -F "file=@$file")

  if [ "$status" = "200" ]; then
    echo "ok    $base"
    uploaded=$((uploaded + 1))
  else
    echo "FAIL  $base -> HTTP $status: $(head -c 200 /tmp/upload-response)"
    failed=$((failed + 1))
  fi
done

rm -f /tmp/upload-response
echo
echo "$uploaded uploaded, $failed failed."
[ "$failed" -eq 0 ]
