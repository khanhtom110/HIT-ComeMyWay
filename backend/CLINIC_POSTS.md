# Clinic posts: Spring Boot reader

Scope: Spring Boot only. No Node.js, Android, credentials, or deployed database was modified.

## Shared schema

Both services must connect to the same database. The current Node.js service writes
clinic_posts(id, clinic_id, title, content, image_urls JSON NULL, created_at).
The reader uses that schema directly; no approval/status field is required.
Use Node.js migrations 001_create_clinic_posts.sql and 002_add_clinic_post_images.sql
when setting up a new database. Inspect the schema and back up the database first;
only apply the image migration if image_urls does not already exist.
Do not apply the legacy Spring Boot 002_extend_clinic_posts.sql for new deployments:
its image_url/status fields are no longer used. Existing legacy columns may remain.
This change does not drop database columns or migrate legacy image_url data.
If old posts only have image_url, migrate those images separately into image_urls.

Example schema inspection (password is prompted):
```sh
mysql -h 127.0.0.1 -u comemyway -p pet_heartbeat_db -e "SHOW CREATE TABLE clinic_posts;"
```

Schema migrations are manual, not executed by Spring Boot.
No JPA entity is registered for this table, so Hibernate does not manage its schema.
created_at remains a UTC DATETIME written by Node.js; it is not returned in post responses.
updated_at is metadata only; the reader does not invent update timestamps.

## APIs on Spring Boot

Public endpoints under the existing public security rules:
- GET /api/v1/public/clinic-posts?limit=10
- GET /api/v1/public/clinic-posts?limit=10&lastPostId=123
- GET /api/v1/public/clinic-posts/123

Response envelope: statusCode, message, data, timestamp.
List data: content, hasNext, lastPostId.
Each item: id, clinicId, clinicName, clinicAvatarUrl, title, excerpt, imageUrl.
Detail includes content instead of excerpt, and also returns imageUrls (all images).

All existing posts are immediately visible without admin approval or a status filter.
This includes legacy DRAFT/ARCHIVED rows: review those rows before deploying if they
must not be public. Deleted or absent details return 404.
Order is id DESC, with id < lastPostId for the next page; limit accepts 1..50.
Excerpt is derived from plain text content (up to 180 Unicode code points).
imageUrls preserves the order of the Node.js JSON array; imageUrl is the first image
(thumbnail), or null when no images exist. SQL NULL and an empty array produce imageUrls: [].
Clinic avatar comes from clinics.thumbnail_url. Treat content as plain text on clients.

## Verification

Run the focused tests:
```sh
./mvnw -Dtest=ClinicPostReadServiceTest,ClinicPostReadRepositoryTest test
```

On a disposable shared database: create a clinic post using Node.js and check that Spring Boot
immediately returns it, with the first image in the feed and all images in detail.
Test posts without images, multiple pages, and any legacy status values (no filtering).
After an authorized edit, verify the updated data; after deletion through Node.js,
verify list exclusion and detail 404. This implementation does not modify Node.js
or add Spring Boot mutation endpoints.
