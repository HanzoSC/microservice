-- Seed users
INSERT INTO users.user_account (id, name, login, password, email, active, company_id) VALUES
  (1, 'Иван Иванов', 'ivan', '{noop}password1', 'ivan@example.com', true, 1),
  (2, 'Петр Петров', 'petr', '{noop}password2', 'petr@example.com', true, 1),
  (3, 'Сидор Сидоров', 'sidor', '{noop}password3', 'sidor@example.com', false, 2)
ON CONFLICT (id) DO NOTHING;


