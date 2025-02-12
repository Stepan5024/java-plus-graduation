
## EXPLORE WITH ME

Backend - приложение позволяет пользователям делиться информацией об интересных событиях и находить компанию для участия в них.

### Cтек:
- Java 21
- Spring Boot (starters: web, validator, actuator, jpa)
- Spring Cloud (Eureka, Config, Gateway, OpenFeign)
- Maven
- DB: PostgreSQL (main), H2 (testing)
- Docker (including docker-compose)
- Lombok

This project consists of a set of microservices for managing an event-driven application. The architecture uses Spring Boot, Spring Cloud, and Eureka for service discovery, configuration management, and API gateway functionality. It includes database integrations and health checks for all critical components.
### Микросервисы:
- сore (основной модуль)
	- main-service.
	- user-service
	- request-service
	- location-service
	- like-service
	- interaction-api (общие dto, внутренние клиенты, exceptions)
- infra (модуль инфраструктуры)
	- config-server (реализация внешней конфигурации с помощью Spring Cloud Config)
	- discovery-server (реализация Service Discovery с помощью Spring Cloud Netflix Eureka)
	- gateway-server (реализация паттерна Api Gateway с помощью Spring Cloud Gateway)
- stats (модуль сбора и передачи статистики)
	- stats-client
	- stats-dto
	- stats-server

## Services Overview
## main-service
Управление событиями (`Event`), категориями (`Category`), подбороками событий (`Compilation`).
	
`Public API`

- **Discovery Server**: Service discovery using Eureka.
- **Config Server**: Centralized configuration management for all microservices.
- **Gateway Server**: API Gateway for routing requests to other services.
- **EWM Main Service**: Core business logic and main application functionality.
- **Stats Service**: Service for handling statistical data and analytics.
- **Postgres Databases**:
    - `ewm-main-db`: Database for the main service.
    - `ewm-stats-db`: Database for the stats service.
- Получение одиночного события по id 
- Получение списка событий по заданным параметрам (содержание текста, категории, доступность, даты начала и окончания, размер)
- Получение топ-просматриваемых событий
- Получение топ-понравившихся событий

## Prerequisites

Ensure you have the following installed on your system:
- Docker
- Docker Compose
- Получение категории по id
- Получение списка категорий по параметрам

## How to Run the Project

### Step 1: Clone the Repository
```bash
git clone https://github.com/Stepan5024/java-plus-graduation.git
cd java-plus-graduation
```
- Получение компиляции событий по id
- Получение списка компиляций по параметрам

Step 2: Build and Start the Services
Run the following command to build and start the services:

запустить проект 
```bash
mvn clean package
```
Сборка в Docker
```bash
docker-compose up --build
```
`Private API`
	

## Step 3: Access the Services
- Создание события
- Получение собственного события по id.
- Получение списка собственных событий по параметрам
- Обновление собственного события

- **Eureka Dashboard (Discovery Server)**: [http://localhost:8761](http://localhost:8761)
- **Gateway Server**: [http://localhost:8080](http://localhost:8080)
- **Main Service**: [http://localhost:8081](http://localhost:8081)
- **Stats Service**: [http://localhost:9090](http://localhost:9090)
`Admin API`
    

---
- Обновление события (в том числе подтверждение)
- Получение списка событий по параметрам

## Step 4: Verify Services

You can verify the health of each service using their `/actuator/health` endpoint:
- Создание категории событий
- Удаление категории событий
- Обновление категории событий

- **Discovery Server**: [http://localhost:8761/actuator/health](http://localhost:8761/actuator/health)
- **Config Server**: [http://localhost:9091/actuator/health](http://localhost:9091/actuator/health)
- **Gateway Server**: [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health)
- **Main Service**: [http://localhost:8081/actuator/health](http://localhost:8081/actuator/health)
- **Stats Service**: [http://localhost:9090/actuator/health](http://localhost:9090/actuator/health)

---
 - Создание подборки событий
 - Удаление подборки событий
 - Обновление подборки событий

## Configuration Details

### Networking
All services are connected through the `ewm-net` Docker network for seamless communication.
`Internal API`
- Получение события по id.

### Environment Variables
- **Databases**:
    - **Username**: `root`
    - **Password**: `root`
    - **Database Names**:
        - `ewm-main` (main)
        - `ewm-stats` (stats)
- **Eureka Service Discovery**:  
  Services register with the discovery server at [http://discovery-server:8761/eureka/](http://discovery-server:8761/eureka/).
## user-service
Управление пользователями (`User`)

---
`Admin API`

## Health Checks
- Добавление пользователя
- Удаление пользователя
- Получение списка пользователей по id

Each service has a defined health check mechanism to ensure they are running correctly:
- Monitors key endpoints.
- Retries with specified intervals.
`Internal API`
	
- Получение пользователя по id
- Проверка наличия пользователя
- Получение списка пользователей

---
## request-service
Управление заявками на участие в событии

## Stopping the Project
```bash
docker-compose down
```
Troubleshooting
`Private API`

Port Conflicts:
- Создание заявки
- Отмена заявки
- Обновление статуса заявки на определенное событие
- Получение списка собственных заявок
- Получение списка заявок для собственного события

Ensure ports 8761, 8080, 8081, 9090, and 9091 are not in use.

# Выделенные сервисы
`Internal API`

## 1. Инфраструктурные сервисы
- Получение количества заявок с определенным статусом события
- Получение количества заявок с определенным статусом событий

Эти сервисы обеспечивают базовые функции, необходимые для работы и взаимодействия остальных микросервисов:
### location-service
Управление локациями

### 1.1. discovery-server (Сервер регистрации сервисов)
- **Описание**:
  Осуществляет мониторинг и динамическую регистрацию/дискавери микросервисов.
- **Технология**:
  Используется **Netflix Eureka** для реализации реестра.
- **Функции**:
    - Все сервисы взаимодействуют с ним для регистрации и поиска друг друга (параметр `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE` у других сервисов).
`Private API`

### 1.2. config-server (Сервер конфигурации)
- **Описание**:
  Центральное хранилище конфигурации для всех микросервисов.
- **Функции**:
    - Позволяет динамически доставлять конфигурации по запросу.
    - Зависит от `discovery-server`, чтобы зарегистрироваться в реестре.
- Создание локации
- Получение том-понравившихся локаций

---
`Internal API`

## 2. Приложенческие сервисы
- Получение локации по id
- Получение списка локаций по id

Эти сервисы реализуют основные компоненты бизнес-логики приложения:
## like-service
Управление лайками событий и локаций

### 2.1. gateway-server (API Gateway)
- **Описание**:
  Выполняет функции маршрутизации, пробрасывая запросы клиентских приложений к нужным микросервисам.
- **Функции**:
    - Конфигурируется для взаимодействия с сервисами через `discovery-server`.
    - Публикует входные API на порту `8080`.
`Private API`

### 2.2. stats-server (Сервис статистики)
- **Описание**:
  Обрабатывает и управляет статистическими данными.
- **Функции**:
    - Подключается к базе данных `stats-db` для работы со статистикой.
    - Использует `config-server` для настройки и `discovery-server` для регистрации.
- Добавить лайк событию
- Удалить лайк события
- Добавить лайк локации
- Удалить лайк локации
	
`Internal API`	

### 2.3. ewm-service (Основной бизнес-сервис)
- **Описание**:
  Реализует ключевую бизнес-логику приложения.
- **Зависимости**:
    - **ewm-db**: Для хранения данных.
    - **stats-server**: Для работы со статистикой.
    - **config-server**: Для настройки.
- Получить количество лайков события
- Получить количество лайков списка событий
- Получить количество лайков локации
- Получить количество лайков самых понравившихся событий
- Получить количество лайков самых понравившихся локаций

---

## 3. Сервисы хранения данных

Эти компоненты предоставляют хранилища для данных приложения и статистики:

### 3.1. ewm-db (База данных основного сервиса)
- **Описание**:
  PostgreSQL-база для хранения основной бизнес-информации.

### 3.2. stats-db (База данных статистики)
- **Описание**:
  PostgreSQL-база для хранения статистических данных.


---
## Архитектура и взаимодействие сервисов

### Регистрация и поиск сервисов
- Каждый микросервис (например, `gateway-server`, `stats-server`, `ewm-service`) регистрируется в сервере `discovery-server`.
- **Сервер регистрации (`discovery-server`)**:
    - Управляет реестром сервисов.
    - Позволяет микросервисам находить друг друга через URL реестра: [http://discovery-server:8761/eureka/](http://discovery-server:8761/eureka/).

---

### Централизованное управление конфигурацией
- Все сервисы (`stats-server`, `ewm-service`, `gateway-server`) получают свои настройки из `config-server`.
- **Пример настроек**:
    - Подключение к базам данных (`SPRING_DATASOURCE_*`).
    - Параметры портов (`SERVER_PORT`).

---

### Маршрутизация запросов
- Пользователи взаимодействуют с API через `gateway-server`.
- **Функции Gateway**:
    - Перенаправляет запросы в нужный микросервис на основании маршрутов.
    - Примеры маршрутов:
        - `/api/service-1` -> `ewm-service`.
        - `/api/stats` -> `stats-server`.

---

### Работа с базами данных
- **`ewm-service`**:
    - Подключается к базе данных `ewm-db` для хранения основной бизнес-логики.
- **`stats-server`**:
    - Подключается к базе данных `stats-db` для работы со статистическими данными.
- **Протокол взаимодействия**:
    - Подключение к базам данных выполняется через **JDBC**.

---

### Зависимости
- **depends_on**:
    - Задаёт порядок запуска сервисов и определяет их зависимости.
    - Примеры:
        - `ewm-service` зависит от:
            - `ewm-db`
            - `config-server`
            - `stats-server`.
        - `config-server` ожидает, пока `discovery-server` станет доступным.

---

### Итоговая схема взаимодействия

1. **Пользователь -> gateway-server -> маршрутизация -> соответствующий сервис**:
    - `/api/service-1` -> `ewm-service`.
    - `/api/stats` -> `stats-server`.

2. **`discovery-server`**:
    - Позволяет `gateway-server` находить адреса и порты остальных сервисов.

3. **`config-server`**:
    - Раздаёт общие настройки (`SPRING_DATASOURCE_*`, `SERVER_PORT`) всем сервисам.

4. **Сервисы**:
    - Работают с PostgreSQL-базами данных напрямую:
        - `ewm-service` -> `ewm-db`.
        - `stats-server` -> `stats-db`.

---
