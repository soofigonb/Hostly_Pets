-- 1. Crear la tabla de estados de pago
CREATE TABLE IF NOT EXISTS estados_pago (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

-- 2. Crear la tabla principal de pagos
CREATE TABLE IF NOT EXISTS pagos (
    id SERIAL PRIMARY KEY,
    id_reserva BIGINT NOT NULL,
    monto DECIMAL(10,2) NOT NULL,
    metodo_pago VARCHAR(50),
    fecha_pago TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_estado_pago INT REFERENCES estados_pago(id)
);

-- 3. Insertar los estados iniciales necesarios para que el Service no falle
INSERT INTO estados_pago (id, nombre) VALUES (1, 'PENDIENTE') ON CONFLICT (id) DO NOTHING;
INSERT INTO estados_pago (id, nombre) VALUES (2, 'COMPLETADO') ON CONFLICT (id) DO NOTHING;
INSERT INTO estados_pago (id, nombre) VALUES (3, 'RECHAZADO') ON CONFLICT (id) DO NOTHING;