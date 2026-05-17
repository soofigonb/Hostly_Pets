-- Añadir código de autorización bancaria a la tabla de pagos
ALTER TABLE pagos 
ADD COLUMN IF NOT EXISTS codigo_autorizacion VARCHAR(50);