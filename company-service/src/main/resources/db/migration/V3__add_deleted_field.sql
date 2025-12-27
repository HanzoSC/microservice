ALTER TABLE companies.company ADD COLUMN IF NOT EXISTS deleted BOOLEAN DEFAULT FALSE NOT NULL;
CREATE INDEX IF NOT EXISTS idx_company_deleted ON companies.company (deleted);

