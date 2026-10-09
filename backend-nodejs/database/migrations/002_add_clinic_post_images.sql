-- Run once after 001_create_clinic_posts.sql.
ALTER TABLE clinic_posts ADD COLUMN image_urls JSON NULL;
