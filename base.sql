create table Corso(
    titulo  varchar(30) PRIMARY key,
    attiva  boolean
);
create TABLE Docente(
    nome    varchar(20), 
    cognome varchar(20),
    CONSTRAINT doc_k PRIMARY KEY (nome, cognome)
);
create table Utente(
    account VARCHAR(20) PRIMARY key,
    pass    varchar(20),
    admin   boolean
);
create table Imparte(
    nome    VARCHAR(20),
    cognome varchar(20),
    corso   varchar(30),
    CONSTRAINT f_doc FOREIGN KEY (nome, cognome) REFERENCES Docente(nome, cognome),
    CONSTRAINT f_cor FOREIGN KEY (corso) REFERENCES Corso(nome, titulo),
    CONSTRAINT p_imp PRIMARY key (nome, cognome, corso)
);
create table Prenotazione(
    usuario varchar(20),
    nome    VARCHAR(20),
    cognome varchar(20),
    corso   varchar(30),
    giorno  varchar(15),
    ora     int,
    CONSTRAINT f_preimp FOREIGN KEY (nome, cognome, corso) REFERENCES Imparte(nome, cognome, corso),
    CONSTRAINT f_impus FOREIGN KEY (usuario) REFERENCES Utente(account),
    CONSTRAINT p_pre PRIMARY key (usuario, giorno, ora)
);