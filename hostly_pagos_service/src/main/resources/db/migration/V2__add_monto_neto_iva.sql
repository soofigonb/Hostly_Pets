-- Modificar la tabla pagos para añadir campos de desglose de impuestos
ALTER TABLE pagos 
ADD COLUMN IF NOT EXISTS monto_neto DECIMAL(10,2),
ADD COLUMN IF NOT EXISTS iva DECIMAL(10,2);

-- Actualizar los registros existentes aplicando el CAST a numeric para que ROUND funcione
UPDATE pagos 
SET monto_neto = ROUND((monto / 1.19)::numeric, 2),
    iva = ROUND((monto - (monto / 1.19))::numeric, 2)
WHERE monto_neto IS NULL;