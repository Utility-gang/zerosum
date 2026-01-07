
ALTER TABLE stocks
    DROP CONSTRAINT IF EXISTS fk_company_stocks;

ALTER TABLE stocks
    ADD CONSTRAINT fk_company_stocks
        FOREIGN KEY (company_id)
            REFERENCES companies(symbol)
            ON DELETE CASCADE;