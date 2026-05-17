-- Crea tabla para almacenar los comprobantes de pago
CREATE TABLE IF NOT EXISTS comprobantes_pago (
    id SERIAL PRIMARY KEY,
    id_pago INT NOT NULL REFERENCES pagos(id),
    numero_comprobante VARCHAR(100) NOT NULL UNIQUE,
    url_pdf VARCHAR(255),
    fecha_emision TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);