CREATE DATABASE banco_digital;
USE banco_digital;

CREATE TABLE contas(
    numero 		INT 				PRIMARY KEY AUTO_INCREMENT,
    titular 	VARCHAR(100)		NOT NULL,
    saldo 		DECIMAL(10, 2)		NOT NULL
);

INSERT INTO
    contas (titular, saldo)
    VALUES
        ("Luís Perossi", 9000.00),
        ("Mateus Fonseca", 3863.00),
        ("Felipe Leandro", 12892.00)
;