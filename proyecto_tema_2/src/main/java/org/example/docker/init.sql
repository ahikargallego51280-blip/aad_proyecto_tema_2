
CREATE TABLE IF NOT EXISTS asociacion (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    cif VARCHAR(20) NOT NULL UNIQUE,
    direccion VARCHAR(200) NOT NULL,
    telefono VARCHAR(20),
    email VARCHAR(100)
) ENGINE=InnoDB;


CREATE TABLE IF NOT EXISTS socio (
    documento VARCHAR(20) PRIMARY KEY,
    nombre VARCHAR(60) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    edad INT NOT NULL,
    email VARCHAR(100),
    activa BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT chk_socio_edad
        CHECK (edad >= 0)
) ENGINE=InnoDB;


CREATE TABLE IF NOT EXISTS juego (
    id_juego INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    autores VARCHAR(200) NOT NULL,
    descripcion TEXT,
    jugadores_min INT NOT NULL,
    jugadores_max INT NOT NULL,
    duracion_min INT NOT NULL,
    tematica VARCHAR(100),
    tipo ENUM(
        'COMPETITIVO',
        'COOPERATIVO',
        'AMBOS'
    ) NOT NULL,
    ubicacion VARCHAR(100),

    estado ENUM(
        'DISPONIBLE',
        'PRESTADO',
        'REPARACION'
    ) NOT NULL DEFAULT 'DISPONIBLE',

    propietario_documento VARCHAR(20),
    fecha_alta DATE NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_juego_propietario
        FOREIGN KEY (propietario_documento)
        REFERENCES socio(documento)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,

    CONSTRAINT chk_juego_jugadores_min
        CHECK (jugadores_min >= 1),

    CONSTRAINT chk_juego_jugadores_max
        CHECK (jugadores_max >= jugadores_min),

    CONSTRAINT chk_juego_duracion
        CHECK (duracion_min > 0)
) ENGINE=InnoDB;


CREATE TABLE IF NOT EXISTS partida (
    id_partida INT PRIMARY KEY AUTO_INCREMENT,
    id_juego INT NOT NULL,
    fecha DATE NOT NULL,
    hora TIME NOT NULL,
    jugadores_deseados INT NOT NULL,
    activa BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_partida_juego
        FOREIGN KEY (id_juego)
        REFERENCES juego(id_juego)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,

    CONSTRAINT chk_partida_jugadores
        CHECK (jugadores_deseados >= 1)
) ENGINE=InnoDB;


CREATE TABLE IF NOT EXISTS participacion (
    id_participacion INT PRIMARY KEY AUTO_INCREMENT,
    id_partida INT NOT NULL,
    documento_socio VARCHAR(20),
    nombre_invitada VARCHAR(120),
    es_ganadora BOOLEAN NOT NULL DEFAULT FALSE,

    CONSTRAINT fk_participacion_partida
        FOREIGN KEY (id_partida)
        REFERENCES partida(id_partida)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,

    CONSTRAINT fk_participacion_socio
        FOREIGN KEY (documento_socio)
        REFERENCES socio(documento)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,

    CONSTRAINT chk_participacion_persona
        CHECK (
            (
                documento_socio IS NOT NULL
                AND nombre_invitada IS NULL
            )
            OR
            (
                documento_socio IS NULL
                AND nombre_invitada IS NOT NULL
            )
        ),

    CONSTRAINT uq_partida_socio
        UNIQUE (id_partida, documento_socio)
) ENGINE=InnoDB;
