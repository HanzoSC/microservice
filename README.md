Главная ветка  
6232-020402D  
Лазарев М.Ю.  
Дьяконов А.В.  
  
**Реализация Spring-приложения на микросервисной архитектуре.**  
**Основные сервисы:**  
  
- PostgreSQL: localhost:5432  
- Config Server: http://localhost:8080  
- Discovery Server: http://localhost:8761  
- User Service: http://localhost:8081  
- Company Service: http://localhost:8083  
- API Gateway: http://localhost:8085  
  
**Лабораторная работа 1**  
**Структура проекта:**  
  
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
  
microlabs.postman_collection.json - коллекция для импорта в Postman  
pom.xml - опции под Maven в каждом модуле проекта  
  
В каждом модуле проекта присутствует start.bat для последовательного запуска всего проекта  
  
**Основные инструкции**  
1. Запустить через start.bat последовательно каждый модуль  
config-server -> user-service -> company-service  
2. Импортировать json-коллекцию в Postman и пройтись по всем запросам.  
  
**Лабораторная работа 2**  
  
**Расширение структуры:**  
  
docker-compose.yml - оркестрация postgres, config-server, user-service, company-service   
initdb\01_create_schemas.sql - создание схем БД (users, companies)  
Flyway миграции: user-service\src\main\resources\db\migration, company-service\src\main\resources\db\migration  
  
Lab2 Microservices.postman_collection.json - коллекция для импорта в Postman  
  
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
  
**Основные инструкции**  
  
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
6. Проверка функциональности:  
   - Users: список, создание, обновление (имя/емейл/компания), активация/деактивация, проверка существования по id  
   - Companies: список (с ФИО директора), создание, проверка существования по id  
7. Подключение к БД:  
   - Host: localhost, Port: 5432, DB: postgres, User: postgres, Password: postgres  
   - currentSchema=users (для user-service), currentSchema=companies (для company-service)  
  
**Лабораторная работа 3**  
  
**Расширение структуры:**  
  
discovery-server - микросервис Eureka для регистрации существующих микросервисов  
src\main\java\com\example\discoveryserver\DiscoveryServerApplication.java - реализация микросервиса Eureka Discovery Server  
  
api-gateway - API-шлюз для маршрутизации запросов  
src\main\java\com\example\apigateway\ApiGatewayApplication.java - реализация микросервиса API-шлюза  
  
Lab3-Eureka-Gateway.postman_collection.json - коллекция для импорта в Postman  
  
**Основные инструкции**  
  
1. Запустить Docker:  
   - docker compose up -d --build  
   - Порядок запуска сервисов заранее настроен:  
    postgres-database -> config-server -> discovery-server -> user-service -> company-service -> api-gateway  
   - Проверить регистрацию в Eureka http://localhost:8761  
2. Импортировать коллекцию в Postman и провести тестирование:  
   - Eureka Discovery Server — проверка регистрации сервисов (discovery-server)  
   - API Gateway - User Service Routes — проверка маршрутов /user/** (user-service)  
   - API Gateway - Company Service Routes — проверка маршрутов /company/** (company-service)  
   - Feign Clients — проверка межсервисных вызовов через API-шлюз  
  
**Лабораторная работа 4**  
  
**Расширение структуры:**  
  
docker-compose.yml - добавлены зависимости Zookeeper + Kafka  
  
Для основных микросервисов реализованы producer-конфиги Kafka:  
user-service\src\main\java\com\example\userservice\config\KafkaConfig.java  
company-service\src\main\java\com\example\companyservice\config\KafkaConfig.java  
  
Реализованы ивенты Kafka по удалению и подтверждения удаления через Delete-топик:  
company-service\src\main\java\com\example\companyservice\dto\CompanyDeletedEvent.java  
company-service\src\main\java\com\example\companyservice\dto\CompanyHardDeleteEvent.java  
user-service\src\main\java\com\example\userservice\dto\CompanyDeletedEvent.java  
user-service\src\main\java\com\example\userservice\dto\CompanyHardDeleteEvent.java  
  
Отдельно прописан consumer для каждого микросервиса:  
company-service\src\main\java\com\example\companyservice\service\CompanyDeleteConsumer.java  
user-service\src\main\java\com\example\userservice\service\CompanyDeletedConsumer.java  
  
Основные сервисы UserService.java и CompanyService.java дополнены логикой удаления и замены компании  
  
Lab4-Kafka-Delete.postman_collection.json - коллекция Postman для поэтапной проверки результатов ТЗ  
  
  1. Запустить Docker:  
   - docker compose up -d --build  
   - Порядок запуска сервисов заранее настроен:  
    postgres-database -> zookeeper -> config-server -> kafka -> discovery-server -> user-service -> company-service -> api-gateway  
  2. Импортировать коллекцию в Postman и выполнить запросы начиная с первого этапа.  
