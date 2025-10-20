-- Seed companies (director_id references existing active user ids 1 or 2)
INSERT INTO companies.company (id, name, ogrn, description, director_id) VALUES
  (1, 'ООО Ромашка', '1234567890123', 'Оптовая торговля цветами', 1),
  (2, 'АО Техно', '9876543210987', 'Разработка ПО', 2)
ON CONFLICT (id) DO NOTHING;


