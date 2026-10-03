# Clinic posts: Spring Boot reader

Scope: Spring Boot only. No Node.js, Android, credentials, or deployed database was modified.

## Shared schema

The original Node.js service writes clinic_posts(id, clinic_id, title, content, created_at).
Before deploying the reader, apply database/migrations/001_create_clinic_posts.sql if the table
does not exist, then apply 002_extend_clinic_posts.sql ONCE. Back up the database first.
The second migration adds image_url (nullable), status (PUBLISHED by default), updated_at
(nullable), and a status/id index. It adds no summary or published_at columns.
Existing Node.js inserts still work because the new fields have defaults.

Example commands from the backend directory (password is prompted):
```sh
mysql -h 127.0.0.1 -u comemyway -p pet_heartbeat_db < database/migrations/001_create_clinic_posts.sql
mysql -h 127.0.0.1 -u comemyway -p pet_heartbeat_db < database/migrations/002_extend_clinic_posts.sql
```

These are manual migrations, not executed by Spring Boot. Do not rerun 002.
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
Detail includes content instead of excerpt.

Only PUBLISHED rows are returned; DRAFT/ARCHIVED, deleted or absent details return 404.
Order is id DESC, with id < lastPostId for the next page; limit accepts 1..50.
Excerpt is derived from plain text content (up to 180 Unicode code points).
No image is fabricated: imageUrl may be null until the posting service is extended separately.
Clinic avatar comes from clinics.thumbnail_url. Treat content as plain text on clients.

## Verification

Run the focused tests:
```sh
./mvnw -Dtest=ClinicPostReadServiceTest,ClinicPostReadRepositoryTest test
```

On a disposable shared database: create a clinic post using Node.js, check that Spring Boot
returns it, set status to DRAFT or ARCHIVED and verify both list exclusion and detail 404,
then delete it through Node.js and verify detail 404. Check a feed with more than one page.
Image URLs and statuses require a separate authorized change to the Node.js writer;
this implementation does not modify that backend.
