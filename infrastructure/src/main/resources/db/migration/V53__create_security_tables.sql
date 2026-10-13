-- V53: Tablas de autenticación y autorización JWT (SecurityApp)

CREATE TABLE users (
    id CHAR(36) PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    status VARCHAR(20) NOT NULL
);

CREATE TABLE roles (
    id CHAR(36) PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    authority VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE user_roles (
    user_id CHAR(36) NOT NULL,
    role_id CHAR(36) NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

CREATE TABLE refresh_tokens (
    id CHAR(36) PRIMARY KEY,
    user_id CHAR(36) NOT NULL,
    token VARCHAR(255) UNIQUE NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    revoked BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_refresh_tokens_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_refresh_tokens_token ON refresh_tokens(token);
CREATE INDEX idx_refresh_tokens_user_id ON refresh_tokens(user_id);

-- Insertar roles por defecto
INSERT INTO roles (id, name, authority) VALUES
('b1a0d8e2-45e3-4c91-b98a-111111111111', 'USER', 'ROLE_USER'),
('b1a0d8e2-45e3-4c91-b98a-222222222222', 'ADMIN', 'ROLE_ADMIN'),
('b1a0d8e2-45e3-4c91-b98a-333333333333', 'MODERATOR', 'ROLE_MODERATOR');

-- Insertar usuario admin inicial (email: admin@mindconnect.com, password: password123)
-- Hash BCrypt: $2a$10$9q6k55WgzcEKMDYRAJ96dewFc.ocw0q.4YO2/vSb4Vof5QagQ0QGO
INSERT INTO users (id, email, password, status) VALUES
('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'admin@mindconnect.com', '$2a$10$9q6k55WgzcEKMDYRAJ96dewFc.ocw0q.4YO2/vSb4Vof5QagQ0QGO', 'ACTIVE'),
('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a22', 'user@mindconnect.com', '$2a$10$9q6k55WgzcEKMDYRAJ96dewFc.ocw0q.4YO2/vSb4Vof5QagQ0QGO', 'ACTIVE');

-- Asignar roles
INSERT INTO user_roles (user_id, role_id) VALUES
('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'b1a0d8e2-45e3-4c91-b98a-222222222222'),
('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'b1a0d8e2-45e3-4c91-b98a-111111111111'),
('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a22', 'b1a0d8e2-45e3-4c91-b98a-111111111111');
