CREATE TABLE IF NOT EXISTS provincias (
	id INT NOT NULL auto_increment,
	nombre VARCHAR(200) NOT NULL,
	PRIMARY KEY(id)
);

INSERT INTO provincias (id, nombre) VALUES (1, 'Santa Fe');
INSERT INTO provincias (id, nombre) VALUES (2, 'Cordoba');
INSERT INTO provincias (id, nombre) VALUES (3, 'Entre Rios');
INSERT INTO provincias (id, nombre) VALUES (4, 'Corrientes');
INSERT INTO provincias (id, nombre) VALUES (5, 'Formosa');
