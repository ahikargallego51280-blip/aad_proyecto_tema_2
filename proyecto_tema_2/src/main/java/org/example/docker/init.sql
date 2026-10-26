
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







INSERT INTO asociacion (nombre, cif, direccion, telefono, email)
SELECT
    'Asociacion Juegos de Charles',
    'G01020304',
    'Calle Inventada 67',
    '676677667',
    'charles@gmail.com'
WHERE NOT EXISTS (
    SELECT 1
    FROM asociacion
);

INSERT INTO socio (documento, nombre, apellidos, telefono, edad, email, activa)
VALUES
('12345678A', 'Carlos', 'Garcia Lopez', '600111111', 25, 'carlos@gmail.com', TRUE),
('12345678B', 'Maria', 'Fernandez Ruiz', '600222222', 30, 'maria@gmail.com', TRUE),
('12345678C', 'Lucia', 'Martinez Perez', '600333333', 22, 'lucia@gmail.com', TRUE),
('12345678D', 'Pablo', 'Sanchez Torres', '600444444', 28, 'pablo@gmail.com', TRUE),
('12345678E', 'Ana', 'Lopez Martin', '600555555', 35, 'ana@gmail.com', FALSE)
ON DUPLICATE KEY UPDATE documento = documento;


INSERT INTO juego (
    nombre, autores, descripcion,
    jugadores_min, jugadores_max, duracion_min,
    tematica, tipo, ubicacion, estado,
    propietario_documento, fecha_alta, activo
)
SELECT
    'Catan', 'Klaus Teuber', 'Juego de estrategia y comercio',
    3, 4, 75, 'Estrategia', 'COMPETITIVO',
    'Estanteria A', 'DISPONIBLE',
    '12345678A', '2026-01-10', TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM juego WHERE nombre = 'Catan'
);

INSERT INTO juego (
    nombre, autores, descripcion,
    jugadores_min, jugadores_max, duracion_min,
    tematica, tipo, ubicacion, estado,
    propietario_documento, fecha_alta, activo
)
SELECT
    'Pandemic', 'Matt Leacock', 'Los jugadores colaboran para salvar el mundo',
    2, 4, 45, 'Cooperacion', 'COOPERATIVO',
    'Estanteria B', 'DISPONIBLE',
    '12345678B', '2026-01-12', TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM juego WHERE nombre = 'Pandemic'
);

INSERT INTO juego (
    nombre, autores, descripcion,
    jugadores_min, jugadores_max, duracion_min,
    tematica, tipo, ubicacion, estado,
    propietario_documento, fecha_alta, activo
)
SELECT
    'Dixit', 'Jean-Louis Roubira', 'Juego de imaginacion e interpretacion de imagenes',
    3, 6, 30, 'Creatividad', 'AMBOS',
    'Estanteria C', 'DISPONIBLE',
    '12345678C', '2026-01-15', TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM juego WHERE nombre = 'Dixit'
);


INSERT INTO partida (id_juego, fecha, hora, jugadores_deseados, activa)
SELECT j.id_juego, '2026-11-01', '17:00:00', 4, TRUE
FROM juego j
WHERE j.nombre = 'Catan'
AND NOT EXISTS (
    SELECT 1 FROM partida p
    WHERE p.id_juego = j.id_juego
      AND p.fecha = '2026-11-01'
      AND p.hora = '17:00:00'
);

INSERT INTO partida (id_juego, fecha, hora, jugadores_deseados, activa)
SELECT j.id_juego, '2026-11-02', '18:00:00', 4, TRUE
FROM juego j
WHERE j.nombre = 'Pandemic'
AND NOT EXISTS (
    SELECT 1 FROM partida p
    WHERE p.id_juego = j.id_juego
      AND p.fecha = '2026-11-02'
      AND p.hora = '18:00:00'
);

INSERT INTO partida (id_juego, fecha, hora, jugadores_deseados, activa)
SELECT j.id_juego, '2026-11-03', '17:30:00', 5, FALSE
FROM juego j
WHERE j.nombre = 'Dixit'
AND NOT EXISTS (
    SELECT 1 FROM partida p
    WHERE p.id_juego = j.id_juego
      AND p.fecha = '2026-11-03'
      AND p.hora = '17:30:00'
);


INSERT INTO participacion (
    id_partida, documento_socio, nombre_invitada, es_ganadora
)
SELECT p.id_partida, '12345678A', NULL, TRUE
FROM partida p
JOIN juego j ON j.id_juego = p.id_juego
WHERE j.nombre = 'Catan'
  AND p.fecha = '2026-11-01'
  AND p.hora = '17:00:00'
  AND NOT EXISTS (
      SELECT 1 FROM participacion pa
      WHERE pa.id_partida = p.id_partida
        AND pa.documento_socio = '12345678A'
  );

INSERT INTO participacion (
    id_partida, documento_socio, nombre_invitada, es_ganadora
)
SELECT p.id_partida, '12345678B', NULL, FALSE
FROM partida p
JOIN juego j ON j.id_juego = p.id_juego
WHERE j.nombre = 'Catan'
  AND p.fecha = '2026-11-01'
  AND p.hora = '17:00:00'
  AND NOT EXISTS (
      SELECT 1 FROM participacion pa
      WHERE pa.id_partida = p.id_partida
        AND pa.documento_socio = '12345678B'
  );

INSERT INTO participacion (
    id_partida, documento_socio, nombre_invitada, es_ganadora
)
SELECT p.id_partida, '12345678C', NULL, FALSE
FROM partida p
JOIN juego j ON j.id_juego = p.id_juego
WHERE j.nombre = 'Catan'
  AND p.fecha = '2026-11-01'
  AND p.hora = '17:00:00'
  AND NOT EXISTS (
      SELECT 1 FROM participacion pa
      WHERE pa.id_partida = p.id_partida
        AND pa.documento_socio = '12345678C'
  );

INSERT INTO participacion (
    id_partida, documento_socio, nombre_invitada, es_ganadora
)
SELECT p.id_partida, NULL, 'Elena Invitada', TRUE
FROM partida p
JOIN juego j ON j.id_juego = p.id_juego
WHERE j.nombre = 'Pandemic'
  AND p.fecha = '2026-11-02'
  AND p.hora = '18:00:00'
  AND NOT EXISTS (
      SELECT 1 FROM participacion pa
      WHERE pa.id_par
      tida = p.id_partida
        AND pa.nombre_invitada = 'Elena Invitada'
  );


