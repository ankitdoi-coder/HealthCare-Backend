-- FileName: src/main/resources/data.sql
-- Insert roles
INSERT INTO roles (name) VALUES ('ADMIN');
INSERT INTO roles (name) VALUES ('PATIENT');
INSERT INTO roles (name) VALUES ('DOCTOR');

-- Insert default admin (password: admin123)
-- Using the correct BCrypt hash from database
-- INSERT INTO admins (email, password, name, is_active) VALUES 
-- ('admin@healthcare.com', '$2a$12$0ifh8Ve3XgzfQxuhnzr4reNjBWTrt9BiZIYO2cPCP2LqWxUkaqteW', 'System Admin', true);