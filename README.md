

docker-compose up --build

# EWM Microservices Project

This project consists of a set of microservices for managing an event-driven application. The architecture uses Spring Boot, Spring Cloud, and Eureka for service discovery, configuration management, and API gateway functionality. It includes database integrations and health checks for all critical components.

## Services Overview

- **Discovery Server**: Service discovery using Eureka.
- **Config Server**: Centralized configuration management for all microservices.
- **Gateway Server**: API Gateway for routing requests to other services.
- **EWM Main Service**: Core business logic and main application functionality.
- **Stats Service**: Service for handling statistical data and analytics.
- **Postgres Databases**:
    - `ewm-main-db`: Database for the main service.
    - `ewm-stats-db`: Database for the stats service.

## Prerequisites

Ensure you have the following installed on your system:
- Docker
- Docker Compose

## How to Run the Project

### Step 1: Clone the Repository
```bash
git clone https://github.com/Stepan5024/java-plus-graduation.git
cd java-plus-graduation
```

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

## Step 3: Access the Services

- **Eureka Dashboard (Discovery Server)**: [http://localhost:8761](http://localhost:8761)
- **Gateway Server**: [http://localhost:8080](http://localhost:8080)
- **Main Service**: [http://localhost:8081](http://localhost:8081)
- **Stats Service**: [http://localhost:9090](http://localhost:9090)

---

## Step 4: Verify Services

You can verify the health of each service using their `/actuator/health` endpoint:

- **Discovery Server**: [http://localhost:8761/actuator/health](http://localhost:8761/actuator/health)
- **Config Server**: [http://localhost:9091/actuator/health](http://localhost:9091/actuator/health)
- **Gateway Server**: [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health)
- **Main Service**: [http://localhost:8081/actuator/health](http://localhost:8081/actuator/health)
- **Stats Service**: [http://localhost:9090/actuator/health](http://localhost:9090/actuator/health)

---

## Configuration Details

### Networking
All services are connected through the `ewm-net` Docker network for seamless communication.

### Environment Variables
- **Databases**:
    - **Username**: `root`
    - **Password**: `root`
    - **Database Names**:
        - `ewm-main` (main)
        - `ewm-stats` (stats)
- **Eureka Service Discovery**:  
  Services register with the discovery server at [http://discovery-server:8761/eureka/](http://discovery-server:8761/eureka/).

---

## Health Checks

Each service has a defined health check mechanism to ensure they are running correctly:
- Monitors key endpoints.
- Retries with specified intervals.

---

## Stopping the Project
```bash
docker-compose down
```
Troubleshooting

Port Conflicts:

Ensure ports 8761, 8080, 8081, 9090, and 9091 are not in use.

# Выделенные сервисы

## 1. Инфраструктурные сервисы

Эти сервисы обеспечивают базовые функции, необходимые для работы и взаимодействия остальных микросервисов:

### 1.1. discovery-server (Сервер регистрации сервисов)
- **Описание**:
  Осуществляет мониторинг и динамическую регистрацию/дискавери микросервисов.
- **Технология**:
  Используется **Netflix Eureka** для реализации реестра.
- **Функции**:
    - Все сервисы взаимодействуют с ним для регистрации и поиска друг друга (параметр `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE` у других сервисов).

### 1.2. config-server (Сервер конфигурации)
- **Описание**:
  Центральное хранилище конфигурации для всех микросервисов.
- **Функции**:
    - Позволяет динамически доставлять конфигурации по запросу.
    - Зависит от `discovery-server`, чтобы зарегистрироваться в реестре.

---

## 2. Приложенческие сервисы

Эти сервисы реализуют основные компоненты бизнес-логики приложения:

### 2.1. gateway-server (API Gateway)
- **Описание**:
  Выполняет функции маршрутизации, пробрасывая запросы клиентских приложений к нужным микросервисам.
- **Функции**:
    - Конфигурируется для взаимодействия с сервисами через `discovery-server`.
    - Публикует входные API на порту `8080`.

### 2.2. stats-server (Сервис статистики)
- **Описание**:
  Обрабатывает и управляет статистическими данными.
- **Функции**:
    - Подключается к базе данных `stats-db` для работы со статистикой.
    - Использует `config-server` для настройки и `discovery-server` для регистрации.

### 2.3. ewm-service (Основной бизнес-сервис)
- **Описание**:
  Реализует ключевую бизнес-логику приложения.
- **Зависимости**:
    - **ewm-db**: Для хранения данных.
    - **stats-server**: Для работы со статистикой.
    - **config-server**: Для настройки.

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
