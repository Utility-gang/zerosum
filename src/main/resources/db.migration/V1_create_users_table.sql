CREATE TABLE users (
    id bigserial PRIMARY KEY,
    username varchar(50) NOT NULL UNIQUE,
    cash Decimal(19,4)
);

CREATE TABLE stocks (
    id bigserial PRIMARY KEY,
    symbol TEXT NOT NULL UNIQUE,
    logo TEXT,
    last_change TIMESTAMP,
    amount INTEGER,
    owner_id BIGINT NOT NULL,
    CONSTRAINT fk_user_stocks FOREIGN KEY (owner_id)
         REFERENCES users(id)
);