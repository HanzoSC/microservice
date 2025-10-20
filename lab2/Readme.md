Реализация Spring-приложения на микросервисной архитектуре.  

Структура проекта:  

config-server - основной сервер для реализации микросервисной архитектуры  
src\main\java\com\example\configserver\ConfigServerApplication.java - основная реализация Spring-сервера  
src\main\resources\application.yml - настройки запуска сервера  

config-repo - реализация репозитория для взаимосвязи микросервисов 
yml-файлы содержат настройки портов и описание микросервисов  

user-service - микросервис пользователей  
src\main\java\com\example\userservice\UserServiceApplication.java - основная реализация микросервиса пользователей   
src\main\java\com\example\userservice\controller\InfoController.java - основной контроллер информации о микросервисе пользователей  
src\main\resources\application.yml - настройки запуска микросервиса пользователей  

company-service - микросервис компаний  
src\main\java\com\example\companyservice\CompanyServiceApplication.java - основная реализация микросервиса компаний   
src\main\java\com\example\companyservice\controller\InfoController.java - основной контроллер информации о микросервисе компаний  
src\main\resources\application.yml - настройки запуска микросервиса компаний  

docker-compose.yml - оркестрация postgres, config-server, user-service, company-service   
initdb\01_create_schemas.sql - создание схем БД (users, companies)  
Flyway миграции: user-service\src\main\resources\db\migration, company-service\src\main\resources\db\migration  


Lab2 Microservices.postman_collection.json - коллекция для импорта в Postman

pom.xml - опции под Maven в каждом модуле проекта  

Для company-service (Company) и user-service (User):  

Реализация сервисов через OpenFeign   
service\(...)FeignClient.java - Feign-клиент  
service\(...)FeignClientFallback.java - обработка ошибок Feign-клиента  

Дополненные REST-контроллеры  
controller\(...)Controller.java  

Реализация Data Transfer, репозиториев и JPA-моделей  
dto\(...)Dto.java  
repository\(...)AccountRepository.java  
entity\(...)Account.java  


Основные инструкции  
1. Запустить Docker:  
   - docker compose up -d --build  
   - Проверка здоровья:  
     - Config Server: http://localhost:8080/actuator/health  
     - User Service: http://localhost:8081/actuator/health  
     - Company Service: http://localhost:8083/actuator/health  
2. Импортировать коллекцию в Postman и провести тестирование  
3. База данных:  
   - PostgreSQL, отдельные схемы: users (user-service), companies (company-service)  
   - Миграции Flyway накатываются автоматически при старте сервисов  
4. Взаимодействие сервисов:  
   - Синхронно через OpenFeign  
   - user-service проверяет существование компании по id в company-service  
   - company-service проверяет существование директора (пользователя) по id в user-service  
5. Обработка ошибок:  
   - Если компания/директор не найдены или неактивны → 404 (ResponseStatusException)  
   - Исключения Feign обрабатываются: 404 не приводит к 500; для справочной информации в списках имена подставляются как "Unknown"  
6. Проверка функционала:  
   - Users: список, создание, обновление (имя/емейл/компания), активация/деактивация, проверка существования по id  
   - Companies: список (с ФИО директора), создание, проверка существования по id  
7. Подключение к БД:  
   - Host: localhost, Port: 5432, DB: postgres, User: postgres, Password: postgres  
   - currentSchema=users (для user-service), currentSchema=companies (для company-service)
