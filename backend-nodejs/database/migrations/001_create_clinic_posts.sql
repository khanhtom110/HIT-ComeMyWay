CREATE TABLE IF NOT EXISTS clinic_posts (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  clinic_id BIGINT NOT NULL,
  title VARCHAR(200) NOT NULL,
  content TEXT NOT NULL,
  created_at DATETIME(3) NOT NULL,
  INDEX idx_clinic_posts_clinic_id_id (clinic_id, id),
  CONSTRAINT fk_clinic_posts_clinic FOREIGN KEY (clinic_id) REFERENCES clinics (id)
);
