
INSERT INTO caracteristicas (`icono`, `titulo`) VALUES
(NULL, '4 personas'),
(NULL, 'manual'),
(NULL, 'automatico'),
(NULL, 'kilometraje ilimitado'),
(NULL, '2 valijas'),
(NULL, 'A/C'),
(NULL, '6 personas'),
(NULL, 'GPS integrado'),
(NULL, 'Pet friendly'),
(NULL, 'Motor eléctrico'),
(NULL, 'Con butaca para bebés'),
(NULL, '8 personas'),
(NULL, 'Airbag');

INSERT INTO categorias (`borrado`, `descripcion`, `titulo`, `url_imagen`) VALUES
(NULL, 'Espacioso para volumen', 'Familiar', 'https://sx-content-labs.sixt.io/Media/2fleet-350x200/kia-rio-4d-weiss-2018.png'),
(NULL, 'Ideal para viajes cortos en la ciudad', 'Deportivo', 'https://dealerimages.dealereprocess.com/image/upload/c_limit,f_auto,fl_lossy,w_600/v1/svp/dep/20bmwm8/bmw_20m8_angularfront_white'),
(NULL, 'Ideal para viajes en grandes ciudades', 'De lujo', 'https://crdms.images.consumerreports.org/c_lfill,w_470,q_auto,f_auto/prod/cars/cr/model-years/12857-2021-mercedes-benz-s-class'),
(NULL, 'Ideal para viajes largos y a un precio accesible', 'Utilitario', 'https://i.pinimg.com/originals/17/d7/e7/17d7e7a9275103b8d32497ba53270b80.png'),
(NULL, 'Ideal para viajes de dos a 4 personas', 'Mediano', 'https://www.lanacion.com.ar/resizer/v2/la-version-2017-del-toyota-QU33BVQ62FG3ZBACKM62DJ7UNQ.jpg?auth=eadccf4916114370f597302a3b7ce7fc56aa016c561fb56a2099bbcfd88d68ac&width=880&height=586&quality=70&smart=true'),
(NULL, 'Ideal para viaje de una o dos personas', 'Compacto', 'https://i0.wp.com/minutomotor.com.ar/wp-content/uploads/2019/07/ToyotaRush.jpg?resize=1200%2C794&ssl=1'),
('2025-05-26', 'Ideal para viajes de dos a 4 personas', 'Mediano', 'https://www.lanacion.com.ar/resizer/v2/la-version-2017-del-toyota-QU33BVQ62FG3ZBACKM62DJ7UNQ.jpg?auth=eadccf4916114370f597302a3b7ce7fc56aa016c561fb56a2099bbcfd88d68ac&width=880&height=586&quality=70&smart=true'),
(NULL, 'Ideal para rentar por mas de dos semanas', 'Economico', 'https://www.lanacion.com.ar/resizer/v2/la-version-2017-del-toyota-QU33BVQ62FG3ZBACKM62DJ7UNQ.jpg?auth=eadccf4916114370f597302a3b7ce7fc56aa016c561fb56a2099bbcfd88d68ac&width=880&height=586&quality=70&smart=true');

INSERT INTO ciudades (`pais`, `titulo`) VALUES
('Argentina', 'Buenos Aires'),
('Argentina', 'Mendoza'),
('Argentina', 'Rosario'),
('Argentina', 'Cordoba');

INSERT INTO politicas (`descripcion`, `titulo`) VALUES
('La cancelación será sin cargo antes de las 24 horas', 'Política de cancelación'),
('En el momento de la recogida, el conductor principal dejará un depósito de seguridad reembolsable', 'Normas reglamentarias'),
('La documentación necesaria para la reserva son documento de identidad o pasaporte y licencia de conducir', 'Normas reglamentarias'),
('Al recoger el coche, el depósito de combustible estará lleno o parcialmente lleno.Tienes que reponer el combustible que hayas gastado justo antes de devolver el coche', 'Normas reglamentarias'),
('Si se daña la carrocería del coche, lo máximo que pagarás por las reparaciones cubiertas por la cobertura parcial por colisión es la franquicia por daños (ARS 1.800.000,00).', 'Normas reglamentarias'),
('La compañía de alquiler le cobrará un importe adicional si el vehículo requiere una limpieza a fondo una vez finalizada su reserva.', 'Salud y seguridad'),
('En caso de retraso, la reserva se mantendrá durante 59 minutos a partir de la hora de reserva especificada, después de lo cual se clasificará como no presentado', 'Normas reglamentarias');

INSERT INTO roles (`nombre`) VALUES
('ROLE_SUPER_ADMIN'),
('ROLE_ADMIN'),
('ROLE_USER');

INSERT INTO usuarios (`apellido`, `ciudad`, `contrasenia`, `email`, `nombre`, `id_rol`) VALUES
('apellido_test', '', '$2a$10$wBsc9Ob9P67Yied1MEWLVu2WHglrO0fAUXiAzEMM5XMuDeCW19onW', 'email_test@hotmail.com', 'nombre_test', 3);

INSERT INTO productos (`coordenadas`, `descripcion`, `disponibilidad`, `rating`, `titulo`, `ubicacion`, `id_categoria`, `id_ciudad`, `precio`) VALUES
('-34.614778, -58.442889', 'update test', 1, 4, 'Citroen C1', 'C. Dr. Juan Felipe Aranguren 736', 1, 1, 20000),
('-32.883611, -68.854722', 'para lantinha', 1, 5, 'Subaru 300 miles', 'Av. Juan B Justo 123', 1, 2, 15000),
('-32.8840367,-68.8523993', 'test', 1, 2, 'Dacia Sandero', 'Av. Juan B Justo 279', 6, 2, 18000),
('-31.4078836,-64.2033403', 'test', 1, 5, 'Ferrari', 'Chubut 198', 3, 4, 430000),
('-32.8875947,-68.8542895', 'test', 1, 4, 'Fiat', 'Av. Emilio Civit 337', 2, 2, 13000);

INSERT INTO imagenes (`titulo`, `url`, `id_producto`) VALUES
('Citroen C1 image', 'https://fotos.quecochemecompro.com/citroen-c1/14597.jpg?size=1200x800', 1),
('Citroen C1 image 2', 'https://fotos.quecochemecompro.com/citroen-c1/14594.jpg?size=1200x800', 1),
('Citroen C1 image 3', 'https://fotos.quecochemecompro.com/citroen-c1/14599.jpg?size=1200x800', 1),
('Citroen C1 image 4', 'https://fotos.quecochemecompro.com/citroen-c1/14598.jpg?size=1200x800', 1),
('Citroen C1 image 5', 'https://fotos.quecochemecompro.com/citroen-c1/14596.jpg?size=1200x800', 1),
('Subaru', 'https://assets.adac.de/image/upload/v1/Autodatenbank/Fahrzeugbilder/im01704-1-subaru-legacy.jpg', 2),
('foto', 'https://fotos.quecochemecompro.com/dacia-sandero/vista-delantera-lateral-dacia-sandero.jpg?size=1200x800', 3),
('foto', 'https://cdn.topgear.es/sites/navi.axelspringer.es/public/media/image/2017/11/dos-logos-que-tienen-coches-ferrari-calle.jpg?tf=1200x', 4),
('foto', 'https://www.km77.com/revista/wp-content/files/2009/11/Eng091109_500barbie2-300x200.jpg', 5),
('foto', 'https://www.km77.com/revista/wp-content/files/2009/11/Eng091109_500barbie3-300x200.jpg', 5);

INSERT INTO reservas
(`borrado`, `fecha_final`, `fecha_inicial`, `hora_comienzo`, `id_producto`, `id_usuario`, `precio`, `estado_pago`, `estado`, `pago_id`) VALUES
(NULL, '2025-10-20', '2025-10-15', '11:00:00', 1, 1, 45000, 'approved', 'Confirmada', 4235357563423),
(NULL, '2025-10-20', '2025-10-10', '10:00:00', 2, 1, 45000, 'approved', 'Confirmada', 4235357563423),
(NULL, '2025-10-20', '2025-10-15', '11:00:00', 2, 1, 45000, 'approved', 'Confirmada', NULL);

INSERT INTO productos_has_caracteristicas
(id_producto,id_caracteristica) VALUES
(4,6),
(4,4),
(1,1),
(1,4),
(1,5),
(1,6),
(2,2),
(2,4),
(2,3),
(3,10),
(3,6),
(3,11),
(5,13),
(5,9),
(5,8);

INSERT INTO productos_has_politicas (id_producto,id_politica) VALUES
(4,1),
(4,6),
(4,5),
(1,1),
(1,2),
(1,3),
(1,4),
(3,1),
(3,7),
(3,6),
(2,2),
(2,4),
(2,6),
(5,1),
(5,2),
(5,5);