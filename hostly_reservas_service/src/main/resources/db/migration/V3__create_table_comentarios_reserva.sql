-- Crear tabla para las reseñas y comentarios de las reservas finalizadas
CREATE TABLE IF NOT EXISTS comentarios_reserva (
    id SERIAL PRIMARY KEY,
    id_reserva INT NOT NULL REFERENCES reservas(id),
    calificacion INT CHECK (calificacion >= 1 AND calificacion <= 5),
    comentario TEXT,
    fecha_comentario TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);