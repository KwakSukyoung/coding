DROP TABLE IF EXISTS User;
CREATE TABLE User(
        username  VARCHAR(255) NOT NULL,
        password  VARCHAR(255) NOT NULL,
        email     VARCHAR(255) NOT NULL,
        firstName VARCHAR(255) NOT NULL,
        lastName  VARCHAR(255) NOT NULL,
        gender    VARCHAR(1)   NOT NULL,
        personID  VARCHAR(255) NOT NULL,
        PRIMARY KEY(username)
);

DROP TABLE IF EXISTS Person;
CREATE TABLE Person(
        personID            VARCHAR(255) NOT NULL,
        associatedUsername  VARCHAR(255) NOT NULL,
        firstName           VARCHAR(255) NOT NULL,
        lastName            VARCHAR(255) NOT NULL,
        gender              VARCHAR(1)   NOT NULL,
        fatherID            VARCHAR(255),
        motherID            VARCHAR(255),
        spouseID            VARCHAR(255),
        PRIMARY KEY(personID)
);

DROP TABLE IF EXISTS Event;
CREATE TABLE Event(
        eventID            VARCHAR(255) NOT NULL,
        associatedUsername VARCHAR(255) NOT NULL,
        personID           VARCHAR(255) NOT NULL,
        latitude           FLOAT        NOT NULL,
        longitude          FLOAT        NOT NULL,
        country            VARCHAR(255) NOT NULL,
        city               VARCHAR(255) NOT NULL,
        eventType          VARCHAR(255) NOT NULL,
        year               INT NOT NULL,
        PRIMARY KEY(eventID)
);

DROP TABLE IF EXISTS Authtoken;
CREATE TABLE Authtoken(
        authtoken VARCHAR(255) NOT NULL,
        username  VARCHAR(255) NOT NULL,
        PRIMARY KEY(authtoken)
);
