DROP TABLE IF EXISTS stocks;

CREATE TABLE stocks (
    id bigserial PRIMARY KEY,
    symbol varchar(50) NOT NULL UNIQUE,
    logo TEXT,
    last_change TIMESTAMP,
    amount INTEGER,
    CONSTRAINT fk_user_stocks FOREIGN KEY (user_id)
         REFERENCES users(id)
);