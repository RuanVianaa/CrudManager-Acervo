DROP DATABASE IF EXISTS crud_manager;

CREATE DATABASE IF NOT EXISTS crud_manager;

USE crud_manager;

CREATE TABLE IF NOT EXISTS users(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    sexo ENUM('M', 'F'),
    email VARCHAR(150) NOT NULL
);

CREATE TABLE IF NOT EXISTS posts(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    content TEXT NOT NULL,
    post_date DATE NOT NULL,
    user_id INT NOT NULL,
    FOREIGN KEY(user_id)
    REFERENCES users(id) ON DELETE cascade
);

CREATE TABLE IF NOT EXISTS companies (
id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
`name` VARCHAR(128) NOT NULL,
`role` VARCHAR(128) NOT NULL,
`start` DATE NOT NULL,
`end` DATE,
user_id INT NOT NULL,
FOREIGN KEY(user_id)
REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS artistas(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    nasc_date DATE NOT NULL,
    pais VARCHAR(150) NOT NULL,
    ativo BOOLEAN NOT NULL,
    biografia TEXT NOT NULL,
    user_id INT NOT NULL,
    FOREIGN KEY(user_id)
    REFERENCES users(id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS albuns(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    data_lancamento DATE NOT NULL,
    numero_faixas INT NOT NULL,
    formato ENUM('CD','Vinil','Digital') NOT NULL,
    explicito BOOLEAN NOT NULL,
    descricao TEXT NOT NULL,
    artista_id INT NOT NULL,
    FOREIGN KEY(artista_id)
    REFERENCES artistas(id) ON DELETE CASCADE
);

INSERT INTO users VALUES
(DEFAULT, "Emerson Carvalho", "M", "emerson@mail.com"),
(DEFAULT, "Luiza Carvalho", "F", "lu@mail.com"),
(DEFAULT, "Elenice Carvalho", "F", "le@mail.com"),
(DEFAULT, "Noé Carvalho", "M", "noe@mail.com"),
(DEFAULT, "Rosânia Carvalho", "F", "ro@mail.com");

INSERT INTO posts VALUES
(DEFAULT, "Olá do Emerson", CURDATE(), 1),
(DEFAULT, "Olá da Luiza", CURDATE(), 2),
(DEFAULT, "Olá da Denise", CURDATE(), 3),
(DEFAULT, "Olá do Noé", CURDATE(), 4),
(DEFAULT, "Olá da Rosânia", CURDATE(), 5),
(DEFAULT, "Olá da Rosânia 1", CURDATE(), 5),
(DEFAULT, "Olá da Rosânia 2", CURDATE(), 5),
(DEFAULT, "Olá da Rosânia 3", CURDATE(), 5);

describe artistas;

USE crud_manager;
ALTER TABLE users ADD COLUMN senha VARCHAR(255) NOT NULL DEFAULT '123456';

CREATE TABLE IF NOT EXISTS logins(
    user_id INT NOT NULL PRIMARY KEY,
    senha CHAR(64) NOT NULL,
    FOREIGN KEY(user_id)
        REFERENCES users(id) ON DELETE CASCADE
);

INSERT INTO logins (user_id, senha)
SELECT u.id, SHA2('123456', 256)
FROM users u
WHERE u.id NOT IN (SELECT user_id FROM logins);

ALTER TABLE users
MODIFY senha VARCHAR(255) NOT NULL;
