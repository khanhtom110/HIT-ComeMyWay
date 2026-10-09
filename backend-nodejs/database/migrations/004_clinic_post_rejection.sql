-- Run once after 003_clinic_post_moderation.sql. Preserve existing post statuses.
ALTER TABLE clinic_posts
  MODIFY COLUMN status ENUM('PENDING', 'APPROVED', 'REJECTED') NOT NULL DEFAULT 'PENDING';
