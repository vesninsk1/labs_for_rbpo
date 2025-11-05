-- Добавление тестовых пользователей
INSERT INTO users (name, email, credit_card) VALUES 
('Ivan Ivanov', 'ivan.ivanov@example.com', '1234-5678-9012-3456'),
('Petr Petrov', 'petr.petrov@example.com', '9876-5432-1098-7654'),
('Maria Sidorova', 'maria.sidorova@example.com', '5555-6666-7777-8888');

-- Добавление тестовых автомобилей
INSERT INTO cars (model, number, available) VALUES 
('Toyota Camry', 'A123BC', true),
('Honda Civic', 'B456DE', true),
('BMW X5', 'C789FG', true);

-- Добавление тестовых поездок
INSERT INTO rides (user_id, car_id, total_time, tarif, kilometrs, status, start_time, end_time) VALUES 
(1, 1, 45, 5.0, 22.5, 'COMPLETED', '2024-01-15 10:30:00', '2024-01-15 11:15:00'),
(2, 2, 30, 4.5, 15.0, 'COMPLETED', '2024-01-15 14:00:00', '2024-01-15 14:30:00');

-- Добавление тестовых платежей
INSERT INTO payments (ride_id, user_id, amount, status) VALUES 
(1, 1, 247.5, 'PAID'),
(2, 2, 195.0, 'PAID');