CREATE TABLE IF NOT EXISTS provincias (
	id INT NOT NULL,
	nombre VARCHAR(200) NOT NULL,
	PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS localidades (
	id INT NOT NULL,
	nombre VARCHAR(200) NOT NULL,
	provinciaId INT NOT NULL,
	PRIMARY KEY(id),
	FOREIGN KEY (provinciaId) REFERENCES provincias(id)
);

INSERT INTO provincias (id, nombre) VALUES (1, 'Santa Fe');
INSERT INTO provincias (id, nombre) VALUES (2, 'Cordoba');

INSERT INTO localidades (id, nombre, provinciaId) VALUES (1, 'Rosario', 1);
INSERT INTO localidades (id, nombre, provinciaId) VALUES (2, 'Capitan Bermudez', 1);
INSERT INTO localidades (id, nombre, provinciaId) VALUES (3, 'Carlos Paz', 2);