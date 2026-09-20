CREATE DATABASE banco_digital;
USE banco_digital;

CREATE TABLE contas(
    numero 		INT 				PRIMARY KEY AUTO_INCREMENT,
    titular 	VARCHAR(100)		NOT NULL,
    saldo 		DECIMAL(10, 2)		NOT NULL
);