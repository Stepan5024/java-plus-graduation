CREATE TABLE IF NOT EXISTS statistics
(
    id        INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    app       varchar     NOT NULL,
    uri       varchar     NOT NULL,
    ip        varchar(16) NOT NULL,
    timestamp TIMESTAMP   NOT NULL
);