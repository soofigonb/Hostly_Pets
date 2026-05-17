-- 1. Crear tabla metodos_pago
CREATE TABLE IF NOT EXISTS metodos_pago (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

-- 2. Insertar valores base
INSERT INTO metodos_pago (id, nombre) VALUES (1, 'TARJETA') ON CONFLICT (id) DO NOTHING;
INSERT INTO metodos_pago (id, nombre) VALUES (2, 'TRANSFERENCIA') ON CONFLICT (id) DO NOTHING;
INSERT INTO metodos_pago (id, nombre) VALUES (3, 'EFECTIVO') ON CONFLICT (id) DO NOTHING;

-- 3. Añadir la FK de forma segura
ALTER TABLE pagos ADD COLUMN IF NOT EXISTS id_metodo_pago INT REFERENCES metodos_pago(id);

-- 4. Migrar registros de texto viejos a la nueva FK
UPDATE pagos SET id_metodo_pago = 1 WHERE metodo_pago ILIKE 'TARJETA' OR metodo_pago ILIKE 'CREDITO' OR metodo_pago ILIKE 'DEBITO';
UPDATE pagos SET id_metodo_pago = 2 WHERE metodo_pago ILIKE 'TRANSFERENCIA';
UPDATE pagos SET id_metodo_pago = 3 WHERE metodo_pago ILIKE 'EFECTIVO';

-- Default por seguridad si no calza o estaba vacío
UPDATE pagos SET id_metodo_pago = 1 WHERE id_metodo_pago IS NULL;

-- 5. Eliminar la columna de texto antigua
ALTER TABLE pagos DROP COLUMN metodo_pago;
