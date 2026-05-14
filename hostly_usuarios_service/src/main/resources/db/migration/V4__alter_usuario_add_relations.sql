ALTER TABLE db_usuario.usuario
ADD COLUMN id_rol BIGINT NOT NULL,
ADD COLUMN id_estado_usuario BIGINT NOT NULL;

ALTER TABLE db_usuario.usuario
ADD CONSTRAINT fk_usuario_rol
FOREIGN KEY (id_rol)
REFERENCES db_usuario.rol(id_rol);

ALTER TABLE db_usuario.usuario
ADD CONSTRAINT fk_usuario_estado
FOREIGN KEY (id_estado_usuario)
REFERENCES db_usuario.estado_usuario(id_estado_usuario);