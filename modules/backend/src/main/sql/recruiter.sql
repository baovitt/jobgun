-- Tables for recruiters

CREATE TABLE recruiter_account_table(
    id SERIAL NOT NULL UNIQUE,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    company TEXT NOT NULL,
    company_url TEXT NOT NULL,
    recruitee_integrated BOOLEAN NOT NULL DEFAULT FALSE
    is_verified BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE recruitee_integration_table(
    id SERIAL NOT NULL UNIQUE,
    recruiter_id INTEGER NOT NULL REFERENCES recruiter_account_table(id)
    company_id VARCHAR(255) NOT NULL,
    recruitee_token VARCHAR(255) NOT NULL,
    last_verified TIMESTAMP NOT NULL
);