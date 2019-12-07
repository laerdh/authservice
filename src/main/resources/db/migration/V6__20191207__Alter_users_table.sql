ALTER TABLE users
    DROP COLUMN password,
    DROP COLUMN auth_method;

ALTER TABLE users
    ADD COLUMN firebase_id VARCHAR;

ALTER TABLE users
    ADD CONSTRAINT unique_phone_number UNIQUE (phone_number);

DROP TYPE IF EXISTS authentication_method;