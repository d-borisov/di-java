CREATE TABLE accounts
(
    name  VARCHAR PRIMARY KEY,
    money INTEGER NOT NULL
);

CREATE TABLE money_transactions
(
    from_account VARCHAR                     NOT NULL,
    to_account   VARCHAR                     NOT NULL,
    amount       INTEGER                     NOT NULL,
    datetime     TIMESTAMP WITHOUT TIME ZONE NOT NULL,

    CONSTRAINT fk_from_account
        FOREIGN KEY (from_account)
            REFERENCES accounts (name)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,

    CONSTRAINT fk_to_account
        FOREIGN KEY (to_account)
            REFERENCES accounts (name)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
);

INSERT INTO accounts(name, money)
VALUES ('salesman', 2000),
       ('buyer', 2000);


-- CREATE FUNCTION not_later_than() RETURNS trigger AS
-- '
--     BEGIN
--         IF new.datetime::TIME > ''18:00:00'' THEN
--             RAISE EXCEPTION ''Cashdesk is already closed'';
--         END IF;
--         RETURN new;
--     END;
-- ' LANGUAGE plpgsql;
--
-- CREATE TRIGGER money_transactions_not_later_than
--     BEFORE INSERT OR UPDATE
--     ON money_transactions
--     FOR EACH ROW
-- EXECUTE PROCEDURE not_later_than();
