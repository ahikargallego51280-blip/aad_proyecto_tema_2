
CREATE DATABASE IF NOT EXISTS asociacion_juegos
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE asociacion_juegos;

CREATE TABLE asociacion (
                            id INT PRIMARY KEY,
                            razon_social VARCHAR(150) NOT NULL,
                            cif VARCHAR(20) NOT NULL UNIQUE,
                            direccion VARCHAR(200) NOT NULL,
                            telefono VARCHAR(25),
                            email VARCHAR(120),
                            fecha_apertura DATE
);

CREATE TABLE socia (
                       documento VARCHAR(20) PRIMARY KEY,
                       nombre VARCHAR(80) NOT NULL,
                       apellidos VARCHAR(120) NOT NULL,
                       telefono VARCHAR(25),
                       edad INT NOT NULL,
                       email VARCHAR(120) NOT NULL UNIQUE,
                       eliminada BOOLEAN NOT NULL DEFAULT FALSE,
                       CHECK (edad >= 0)
);

CREATE TABLE juego (
                       id INT PRIMARY KEY AUTO_INCREMENT,
                       nombre VARCHAR(150) NOT NULL,
                       autores VARCHAR(250) NOT NULL,
                       descripcion TEXT,
                       min_jugadores INT NOT NULL,
                       max_jugadores INT NOT NULL,
                       duracion_minutos INT NOT NULL,
                       tematica VARCHAR(100),
                       tipo ENUM('COMPETITIVO', 'COOPERATIVO') NOT NULL,
                       ubicacion VARCHAR(150),
                       estado ENUM('DISPONIBLE', 'PRESTADO', 'REPARACION')
        NOT NULL DEFAULT 'DISPONIBLE',
                       propietario_socia VARCHAR(20) NULL,
                       fecha_alta DATE NOT NULL,
                       eliminado BOOLEAN NOT NULL DEFAULT FALSE,

                       CONSTRAINT fk_juego_propietaria
                           FOREIGN KEY (propietario_socia)
                               REFERENCES socia(documento)
                               ON DELETE RESTRICT,

                       CHECK (min_jugadores >= 1),
                       CHECK (max_jugadores >= min_jugadores),
                       CHECK (duracion_minutos > 0)
);

CREATE TABLE partida (
                         id INT PRIMARY KEY AUTO_INCREMENT,
                         juego_id INT NOT NULL,
                         fecha DATE NOT NULL,
                         hora TIME NOT NULL,
                         jugadores_deseados INT NOT NULL,
                         eliminada BOOLEAN NOT NULL DEFAULT FALSE,

                         CONSTRAINT fk_partida_juego
                             FOREIGN KEY (juego_id)
                                 REFERENCES juego(id)
                                 ON DELETE RESTRICT,

                         CHECK (jugadores_deseados >= 1)
);

CREATE TABLE participante_partida (
                                      id INT PRIMARY KEY AUTO_INCREMENT,
                                      partida_id INT NOT NULL,
                                      socia_documento VARCHAR(20) NULL,
                                      nombre_invitada VARCHAR(100) NULL,
                                      ganadora BOOLEAN NOT NULL DEFAULT FALSE,

                                      CONSTRAINT fk_participante_partida
                                          FOREIGN KEY (partida_id)
                                              REFERENCES partida(id)
                                              ON DELETE CASCADE,

                                      CONSTRAINT fk_participante_socia
                                          FOREIGN KEY (socia_documento)
                                              REFERENCES socia(documento)
                                              ON DELETE RESTRICT,

                                      CONSTRAINT uq_socia_partida
                                          UNIQUE (partida_id, socia_documento),

                                      CHECK (
                                          (socia_documento IS NOT NULL AND nombre_invitada IS NULL)
                                              OR
                                          (socia_documento IS NULL AND nombre_invitada IS NOT NULL)
                                          )
);

CREATE INDEX idx_juego_nombre ON juego(nombre);
CREATE INDEX idx_socia_apellidos ON socia(apellidos);
CREATE INDEX idx_partida_fecha ON partida(fecha);
CREATE INDEX idx_partida_juego ON partida(juego_id);

INSERT INTO asociacion (
    id, razon_social, cif, direccion, telefono, email, fecha_apertura
) VALUES (
             1,
             'Asociación de Juegos de Mesa',
             'PENDIENTE',
             'Vitoria-Gasteiz',
             NULL,
             NULL,
             NULL
         );
