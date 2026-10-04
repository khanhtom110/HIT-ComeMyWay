-- Apply once to an existing MySQL database when User.homeAddress becomes optional.
ALTER TABLE users MODIFY COLUMN home_address VARCHAR(255) NULL;
