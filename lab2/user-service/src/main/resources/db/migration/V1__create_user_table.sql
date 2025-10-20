CREATE TABLE IF NOT EXISTS users.user_account (
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    login           VARCHAR(100) NOT NULL UNIQUE,
    password        VARCHAR(255) NOT NULL,
    email           VARCHAR(255) NOT NULL UNIQUE,
    active          BOOLEAN      NOT NULL DEFAULT TRUE,
    company_id      BIGINT
);

CREATE INDEX IF NOT EXISTS idx_user_account_company_id ON users.user_account (company_id);

