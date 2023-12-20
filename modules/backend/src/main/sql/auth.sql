
-- Tables for user authentication

CREATE TABLE user_account_table(
    id SERIAL NOT NULL UNIQUE,
    first_name VARCHAR(255) NOT NULL,
    last_Name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    is_verified BOOLEAN NOT NULL DEFAULT FALSE
);