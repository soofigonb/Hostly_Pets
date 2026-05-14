INSERT INTO bd_propiedades.tipo_mascota (nombre_tipo_mascota) VALUES ('Perro');
INSERT INTO bd_propiedades.tipo_mascota (nombre_tipo_mascota) VALUES ('Gato');
INSERT INTO bd_propiedades.tipo_mascota (nombre_tipo_mascota) VALUES ('Conejo');
INSERT INTO bd_propiedades.tipo_mascota (nombre_tipo_mascota) VALUES ('Perro y gato');

INSERT INTO bd_propiedades.tamano_mascota (nombre_tamano_mascota) VALUES ('Pequeño');
INSERT INTO bd_propiedades.tamano_mascota (nombre_tamano_mascota) VALUES ('Mediano');
INSERT INTO bd_propiedades.tamano_mascota (nombre_tamano_mascota) VALUES ('Grande');

INSERT INTO bd_propiedades.propiedad (
    id_anfitrion, titulo, descripcion, direccion, ciudad,
    precio_noche, tiene_patio, costo_extra_mascota, disponible,
    id_tipo_propiedad, id_tipo_mascota, id_tamano_mascota
) VALUES (
    1, 'Casa familiar pet friendly',
    'Casa amplia con patio ideal para viajar con mascotas.',
    'Av. Los Pinos 123', 'Santiago',
    45000, true, 5000, true,
    1, 4, 3
);

INSERT INTO bd_propiedades.propiedad (
    id_anfitrion, titulo, descripcion, direccion, ciudad,
    precio_noche, tiene_patio, costo_extra_mascota, disponible,
    id_tipo_propiedad, id_tipo_mascota, id_tamano_mascota
) VALUES (
    2, 'Departamento céntrico para mascotas pequeñas',
    'Departamento cómodo cercano al centro, acepta mascotas pequeñas.',
    'Calle Central 456', 'Valparaíso',
    35000, false, 3000, true,
    2, 1, 1
);

INSERT INTO bd_propiedades.propiedad (
    id_anfitrion, titulo, descripcion, direccion, ciudad,
    precio_noche, tiene_patio, costo_extra_mascota, disponible,
    id_tipo_propiedad, id_tipo_mascota, id_tamano_mascota
) VALUES (
    3, 'Cabaña acogedora para conejos',
    'Cabaña tranquila con patio cerrado ideal para conejos.',
    'Camino Verde 789', 'Puerto Varas',
    40000, true, 2000, true,
    3, 3, 1
);