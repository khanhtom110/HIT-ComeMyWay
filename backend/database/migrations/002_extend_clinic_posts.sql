-- Apply ONCE after 001, or against the existing original Node.js table.
-- Existing Node.js INSERT statements remain valid: omitted fields have defaults.
ALTER TABLE clinic_posts
  ADD COLUMN image_url VARCHAR(2048) NULL,
  ADD COLUMN status VARCHAR(20) NOT NULL DEFAULT 'PUBLISHED',
  ADD COLUMN updated_at DATETIME(3) NULL,
  ADD CONSTRAINT chk_clinic_posts_status CHECK (status IN ('DRAFT', 'PUBLISHED', 'ARCHIVED')),
  ADD INDEX idx_clinic_posts_status_id (status, id);

-- Original timestamps were written in UTC by Node.js.
UPDATE clinic_posts SET updated_at = created_at WHERE updated_at IS NULL;
