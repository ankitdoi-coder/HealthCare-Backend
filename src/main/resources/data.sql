-- FileName: src/main/resources/data.sql
-- Insert roles
INSERT INTO roles (name) VALUES ('ADMIN');
INSERT INTO roles (name) VALUES ('PATIENT');
INSERT INTO roles (name) VALUES ('DOCTOR');

-- Insert default admin (password: Admin9352@healthcare)
INSERT INTO admins (email, password, name, is_active) VALUES 
('admin@healthcare.com', '$2a$12$OxDAs7w9iv6Vw81RDR/WvuR/ZzD.3J32BZs5HvVhT5UCzqSLN4RGi', 'System Admin', true);