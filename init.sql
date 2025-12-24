-- Script de inicialización para la base de datos
USE prueba_gestion_productos;

-- Crear tabla de roles
CREATE TABLE IF NOT EXISTS roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

-- Crear tabla de usuarios
CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- Crear tabla intermedia usuarios_roles
CREATE TABLE IF NOT EXISTS usuarios_roles (
    usuario_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (usuario_id, role_id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- Crear tabla de productos
CREATE TABLE IF NOT EXISTS productos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    cantidad INT NOT NULL DEFAULT 0,
    precio DOUBLE NOT NULL DEFAULT 0.0
);

-- Insertar roles básicos
INSERT INTO roles (nombre) VALUES ('ROLE_ADMIN'), ('ROLE_USER')
ON DUPLICATE KEY UPDATE nombre=nombre;

-- Insertar usuario administrador
-- Password: 1234 (BCrypt hash)
INSERT INTO usuarios (nombre, apellido, email, username, password)
VALUES ('Admin', 'Sistema', 'admin@sistema.com', 'admin', '$2a$12$ctaRykbsUuPuUdmcMrQBL.Iauyw.F8.KiSXvUgcwBhoeCIsU3qEV6')
ON DUPLICATE KEY UPDATE username=username;

-- Insertar usuario regular
-- Password: 1234 (BCrypt hash)
INSERT INTO usuarios (nombre, apellido, email, username, password)
VALUES ('Usuario', 'Prueba', 'user@sistema.com', 'user', '$2a$12$ctaRykbsUuPuUdmcMrQBL.Iauyw.F8.KiSXvUgcwBhoeCIsU3qEV6')
ON DUPLICATE KEY UPDATE username=username;

-- Asignar rol ADMIN al usuario admin
INSERT INTO usuarios_roles (usuario_id, role_id)
SELECT u.id, r.id
FROM usuarios u, roles r
WHERE u.username = 'admin' AND r.nombre = 'ROLE_ADMIN'
ON DUPLICATE KEY UPDATE usuario_id=usuario_id;

-- Asignar rol USER al usuario user
INSERT INTO usuarios_roles (usuario_id, role_id)
SELECT u.id, r.id
FROM usuarios u, roles r
WHERE u.username = 'user' AND r.nombre = 'ROLE_USER'
ON DUPLICATE KEY UPDATE usuario_id=usuario_id;

-- Insertar algunos productos de ejemplo
INSERT INTO productos (nombre, descripcion, cantidad, precio) VALUES
('Laptop Dell', 'Laptop Dell Inspiron 15, 8GB RAM, 256GB SSD', 10, 799.99),
('Mouse Logitech', 'Mouse inalámbrico Logitech MX Master 3', 50, 99.99),
('Teclado Mecánico', 'Teclado mecánico RGB', 30, 149.99)
ON DUPLICATE KEY UPDATE nombre=nombre;