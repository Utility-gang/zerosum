CREATE TABLE users (
    id bigserial PRIMARY KEY,
    username TEXT NOT NULL UNIQUE,
    cash Decimal(19,2)
);

CREATE TABLE stocks (
    id bigserial PRIMARY KEY,
    amount INTEGER,
    owner_id BIGINT NOT NULL,
    company_id BIGINT NOT NULL,
    CONSTRAINT fk_user_stocks FOREIGN KEY (owner_id)
         REFERENCES users(id)
    CONSTRAINT fk_company_stocks FOREIGN KEY (company_id)
         REFERENCES companies(id)
);

CREATE TABLE companies (
    id bigserial PRIMARY KEY,
    symbol TEXT NOT NULL UNIQUE,
    logo TEXT
);