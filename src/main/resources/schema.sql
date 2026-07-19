CREATE TABLE IF NOT EXISTS capacidades (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    descripcion VARCHAR(90)
);

CREATE TABLE IF NOT EXISTS capacidad_bootcamp (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_capacidad BIGINT NOT NULL,
    id_bootcamp BIGINT NOT NULL
);
