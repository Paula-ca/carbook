CREATE TABLE IF NOT EXISTS logs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    log_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    level VARCHAR(10),
    logger VARCHAR(255),
    message TEXT,
    thread VARCHAR(255)
);
CREATE TABLE IF NOT EXISTS roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    contrasenia VARCHAR(255) NOT NULL,
    ciudad VARCHAR(100),
    id_rol BIGINT,
    FOREIGN KEY (id_rol) REFERENCES roles(id)
);

CREATE TABLE IF NOT EXISTS caracteristicas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    icono VARCHAR(255),
    titulo VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS categorias (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    borrado DATE,
    descripcion TEXT,
    titulo VARCHAR(100),
    url_imagen TEXT
);

CREATE TABLE IF NOT EXISTS ciudades (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pais VARCHAR(100),
    titulo VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS productos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100),
    descripcion TEXT,
    ubicacion VARCHAR(255),
    coordenadas VARCHAR(100),
    rating INT,
    disponibilidad BOOLEAN,
    precio DECIMAL(10,2),
    id_categoria BIGINT,
    id_ciudad BIGINT,
    FOREIGN KEY (id_categoria) REFERENCES categorias(id),
    FOREIGN KEY (id_ciudad) REFERENCES ciudades(id)
);

CREATE TABLE IF NOT EXISTS imagenes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100),
    url TEXT,
    id_producto BIGINT,
    FOREIGN KEY (id_producto) REFERENCES productos(id)
);

CREATE TABLE IF NOT EXISTS politicas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100),
    descripcion TEXT
);

CREATE TABLE IF NOT EXISTS reservas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha_inicial DATE NOT NULL,
    fecha_final DATE NOT NULL,
    hora_comienzo TIME NOT NULL,
    precio DECIMAL(10,2),
    estado_pago VARCHAR(50),
    estado VARCHAR(50),
    pago_id BIGINT,
    borrado DATE,
    id_usuario BIGINT,
    id_producto BIGINT,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id),
    FOREIGN KEY (id_producto) REFERENCES productos(id)
);

CREATE TABLE IF NOT EXISTS productos_has_caracteristicas (
    id_producto BIGINT NOT NULL,
    id_caracteristica BIGINT NOT NULL,
    PRIMARY KEY (id_producto, id_caracteristica),
    FOREIGN KEY (id_producto) REFERENCES productos(id),
    FOREIGN KEY (id_caracteristica) REFERENCES caracteristicas(id)
);
CREATE TABLE IF NOT EXISTS productos_has_politicas (
    id_producto BIGINT NOT NULL,
    id_politica BIGINT NOT NULL,
    PRIMARY KEY (id_producto, id_politica),
    FOREIGN KEY (id_producto) REFERENCES productos(id),
    FOREIGN KEY (id_politica) REFERENCES politicas(id)
);

