CREATE TABLE IF NOT EXISTS companies.company (
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    ogrn            VARCHAR(15)  NOT NULL UNIQUE,
    description     TEXT,
    director_id     BIGINT       NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_company_director_id ON companies.company (director_id);

