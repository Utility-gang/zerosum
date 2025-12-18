CREATE TABLE users (
    id bigserial PRIMARY KEY,
    username TEXT NOT NULL UNIQUE,
    cash Decimal(19,2)
);

CREATE TABLE companies (
    symbol TEXT PRIMARY KEY,
    logo TEXT
);

CREATE TABLE stocks (
    id bigserial PRIMARY KEY,
    amount DOUBLE PRECISION,
    owner_id BIGINT NOT NULL,
    company_id TEXT NOT NULL,
    CONSTRAINT fk_user_stocks FOREIGN KEY (owner_id)
         REFERENCES users(id),
    CONSTRAINT fk_company_stocks FOREIGN KEY (company_id)
         REFERENCES companies(symbol)
);