/*SUPPRESION DATABASE  Heptathlon + CREATION */
DROP DATABASE IF EXISTS HeptathlonCentral;
CREATE DATABASE HeptathlonCentral;
USE HeptathlonCentral;

/* CREATION TABLE Famille */
CREATE TABLE Famille (
id_famille  	    int(3)          PRIMARY KEY AUTO_INCREMENT,
nom           	    varchar(20)     NOT NULL
);

/* INSERTION DE VALEUR DANS LA TABLE Famille */
INSERT INTO Famille (id_famille, nom) VALUES (1,'Vetement');
INSERT INTO Famille (id_famille, nom) VALUES (2,'Chaussure');
INSERT INTO Famille (id_famille, nom) VALUES (3,'Tennis');
INSERT INTO Famille (id_famille, nom) VALUES (4,'Football');
INSERT INTO Famille (id_famille, nom) VALUES (5,'Boxe');
INSERT INTO Famille (id_famille, nom) VALUES (6,'Pêche');

/* CREATION TABLE Article */
CREATE TABLE Article (
id_article          int(3)         PRIMARY KEY AUTO_INCREMENT,
reference           varchar(6)     NOT NULL,
nom_article		    varchar(20)    NOT NULL,
famille             int(3)         NOT NULL,
prix_article        int(4)         NOT NULL,
stock	            int(4),
FOREIGN KEY (famille) REFERENCES Famille(id_famille)
);

/* INSERTION DE VALEUR DANS LA TABLE Article */
INSERT INTO Article (reference, nom_article, famille, prix_article, stock) VALUES ('XYH251', 'Pantacourt', 1, 20, 56);
INSERT INTO Article (reference, nom_article, famille, prix_article, stock) VALUES ('XYH252', 'Chaussettes blanches', 1, 4, 0);
INSERT INTO Article (reference, nom_article, famille, prix_article, stock) VALUES ('XYH253', 'Casquettes', 1, 30, 25);
INSERT INTO Article (reference, nom_article, famille, prix_article, stock) VALUES ('IOS398', 'Gant', 5, 15, 22);