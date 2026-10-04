-- Run once after 002_add_clinic_post_images.sql. Existing posts require approval.
ALTER TABLE clinic_posts
  ADD COLUMN status ENUM('PENDING', 'APPROVED') NOT NULL DEFAULT 'PENDING',
  ADD COLUMN approved_by BIGINT NULL,
  ADD COLUMN approved_at DATETIME(3) NULL,
  ADD INDEX idx_clinic_posts_status_id (status, id);
