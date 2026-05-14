-- 1. Crear tabla de estados de reserva
CREATE TABLE IF NOT EXISTS estados_reserva (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

-- 2. Crear tabla de reservas
CREATE TABLE IF NOT EXISTS reservas (
    id SERIAL PRIMARY KEY,
    id_usuario BIGINT NOT NULL,
    id_propiedad BIGINT NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    cantidad_mascotas INT DEFAULT 1,
    tipo_mascota VARCHAR(50),
    tamano_mascota VARCHAR(50),
    total_reserva DECIMAL(10,2),
    id_estado_reserva INT REFERENCES estados_reserva(id)
);

-- 3. Insertar estados base
INSERT INTO estados_reserva (id, nombre) VALUES (1, 'PENDIENTE') ON CONFLICT (id) DO NOTHING;
INSERT INTO estados_reserva (id, nombre) VALUES (2, 'CONFIRMADA') ON CONFLICT (id) DO NOTHING;
INSERT INTO estados_reserva (id, nombre) VALUES (3, 'CANCELADA') ON CONFLICT (id) DO NOTHING;