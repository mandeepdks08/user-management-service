CREATE TABLE IF NOT EXISTS users (
    id bigserial PRIMARY KEY,
    userid text UNIQUE NOT NULL,
    name varchar(256) NOT NULL,
    email varchar(256) UNIQUE NOT NULL,
    phone varchar(20) UNIQUE NOT NULL,
    password varchar(256) NOT NULL,
    enabled boolean,
    createdon timestamp NOT NULL,
    processedon timestamp NOT NULL
);