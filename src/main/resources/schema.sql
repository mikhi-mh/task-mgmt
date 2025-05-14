-- Create Task table
CREATE TABLE IF NOT EXISTS task (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255),
    description VARCHAR(1000),
    status VARCHAR(20),
    due_date DATE
);

-- Insert sample data
INSERT INTO task (title, description, status, due_date) VALUES
    ('Project Setup', 'Initialize Spring Boot project structure', 'IN_PROGRESS', '2024-04-10'),
    ('API Documentation', 'Create Swagger documentation for REST endpoints', 'TODO', '2024-04-15'),
    ('Database Design', 'Design and implement database schema', 'DONE', '2024-04-05'),
    ('Unit Testing', 'Write unit tests for service layer', 'TODO', '2024-04-20'),
    ('Security Implementation', 'Add Spring Security configuration', 'TODO', '2024-04-25');