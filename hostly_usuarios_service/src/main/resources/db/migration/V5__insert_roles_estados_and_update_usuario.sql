INSERT INTO db_usuario.rol (nombre_rol) VALUES
('ADMIN'),
('CLIENTE'),
('ANFITRION');

INSERT INTO db_usuario.estado_usuario (nombre_estado) VALUES
('ACTIVO'),
('INACTIVO'),
('BLOQUEADO');

-- Actualizamos usuarios para asignar rol y estado
UPDATE db_usuario.usuario SET id_rol = 1, id_estado_usuario = 1 WHERE id_usuario = 1;
UPDATE db_usuario.usuario SET id_rol = 2, id_estado_usuario = 1 WHERE id_usuario = 2;