-- Manual migration: back up the database and verify SHOW CREATE TABLE users first.
-- Run against the database actually used by Spring Boot (not necessarily the import database).
-- Matches CommonConstant.ADDRESS_LENGTH = 255. Existing address values are retained.
-- If the existing column has a custom charset/collation/comment, retain those in this statement.
ALTER TABLE users MODIFY COLUMN home_address VARCHAR(255) NULL;
