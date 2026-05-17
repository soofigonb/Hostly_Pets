-- 1. Crear tabla detalle_reserva
CREATE TABLE IF NOT EXISTS detalle_reserva (
    id SERIAL PRIMARY KEY,
    id_reserva BIGINT NOT NULL UNIQUE REFERENCES reservas(id) ON DELETE CASCADE,
    monto_base DECIMAL(10,2) NOT NULL,
    monto_mascota DECIMAL(10,2) NOT NULL,
    total DECIMAL(10,2) NOT NULL
);
