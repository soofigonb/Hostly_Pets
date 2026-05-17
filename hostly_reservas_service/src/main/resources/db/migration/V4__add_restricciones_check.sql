-- Añadir restricciones de validación para proteger la integridad de los datos
ALTER TABLE reservas
ADD CONSTRAINT check_cantidad_mascotas CHECK (cantidad_mascotas > 0),
ADD CONSTRAINT check_fechas_coherentes CHECK (fecha_fin >= fecha_inicio);