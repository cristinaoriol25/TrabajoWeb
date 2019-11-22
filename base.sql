CREATE TABLE Corso(
    titulo  VARCHAR(30) PRIMARY KEY,
    attiva  BOOLEAN NOT NULL
);
CREATE TABLE Docente(
    nome    VARCHAR(20), 
    cognome VARCHAR(20),
    attiva BOOLEAN NOT NULL,
    CONSTRAINT doc_k PRIMARY KEY (nome, cognome)
);
CREATE TABLE Utente(
    account VARCHAR(20) PRIMARY KEY,
    pass    VARCHAR(20),
    admin   BOOLEAN NOT NULL
);
CREATE TABLE Imparte(
    nome    VARCHAR(20),
    cognome VARCHAR(20),
    corso   VARCHAR(30),
    attiva BOOLEAN NOT NULL,
    CONSTRAINT f_doc FOREIGN KEY (nome, cognome) REFERENCES Docente(nome, cognome),
    CONSTRAINT f_cor FOREIGN KEY (corso) REFERENCES Corso(nome, titulo),
    CONSTRAINT p_imp PRIMARY KEY (nome, cognome, corso)
);
CREATE TABLE Prenotazione(
    usuario VARCHAR(20),
    nome    VARCHAR(20),
    cognome VARCHAR(20),
    corso   VARCHAR(30),
    giorno  VARCHAR(15),
    ora     INT,
    stato VARCHAR(10) NOT NULL CHECK (stato='ATTIVA' OR stato='EFFETTUATA' OR stato='CANCELLATA'),
    CONSTRAINT f_preimp FOREIGN KEY (nome, cognome, corso) REFERENCES Imparte(nome, cognome, corso),
    CONSTRAINT f_impus FOREIGN KEY (usuario) REFERENCES Utente(account),
    CONSTRAINT p_pre PRIMARY KEY (usuario, giorno, ora)
);