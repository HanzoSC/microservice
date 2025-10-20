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

microlabs.postman_collection.json - коллекция для импорта в Postman  

pom.xml - опции под Maven в каждом модуле проекта  

В каждом модуле проекта присутствует start.bat для последовательного запуска всего проекта  

Основные инструкции  
1. Запустить через start.bat последовательно каждый модуль  
config-server -> user-service -> company-service  
2. Импортировать json-коллекцию в Postman и пройтись по всем запросам.
