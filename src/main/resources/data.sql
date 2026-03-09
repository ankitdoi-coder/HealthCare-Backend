-- FileName: src/main/resources/data.sql
-- Insert roles
INSERT INTO roles (name) VALUES ('ADMIN');
INSERT INTO roles (name) VALUES ('PATIENT');
INSERT INTO roles (name) VALUES ('DOCTOR');

-- Insert default admin (password: admin123)
-- Using the correct BCrypt hash from database
-- INSERT INTO admins (email, password, name, is_active) VALUES 
-- ('admin@healthcare.com', '$2a$12$0ifh8Ve3XgzfQxuhnzr4reNjBWTrt9BiZIYO2cPCP2LqWxUkaqteW', 'System Admin', true);
-- Insert a default admin user with a pre-hashed password.
-- This replaces the hardcoded admin logic in the Java code.
-- Email: admin@healthcare.com, Password: Admin9352@healthcare
INSERT INTO users (email, password, role_id, is_approved) VALUES
('admin@healthcare.com', '$2a$12$OxDAs7w9iv6Vw81RDR/WvuR/ZzD.3J32BZs5HvVhT5UCzqSLN4RGi', 1, true);