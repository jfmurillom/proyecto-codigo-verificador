-- ============================================
-- Script de Base de Datos - productos.sql
-- ============================================

-- Crear la base de datos (opcional, dependiendo de tu servidor SQL)
CREATE DATABASE IF NOT EXISTS verificador_codigos;
USE verificador_codigos;

-- Crear la tabla productos
CREATE TABLE IF NOT EXISTS productos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(50) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insertar datos de ejemplo
INSERT INTO productos (codigo, nombre) VALUES 
('PROD001', 'Laptop Dell XPS 15'),
('PROD002', 'Mouse Logitech MX Master'),
('PROD003', 'Teclado Mecánico Corsair'),
('PROD004', 'Monitor Samsung 27 pulgadas'),
('PROD005', 'Auriculares Sony WH-1000XM4');

-- Verificar los datos insertados
SELECT * FROM productos;

-- Consulta de ejemplo para verificar un código específico
-- SELECT * FROM productos WHERE codigo = 'PROD001';