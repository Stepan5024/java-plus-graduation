-- Удаляем таблицы, если они существуют
drop table if exists categories, location, events, compilations, compilations_events;

-- Таблица категорий событий
create table if not exists categories
(
    id bigint generated always as identity primary key,
    name varchar(100) not null
);

-- Таблица для хранения географической информации о событиях
create table if not exists location
(
    id bigint generated always as identity primary key,
    lat float not null,
    lon float not null
);

-- Таблица для хранения информации о событиях
create table if not exists events
(
    id bigint generated always as identity primary key,
    annotation varchar,
    category_id bigint REFERENCES categories(id) ON DELETE CASCADE ON UPDATE CASCADE,
    created_on TIMESTAMP NOT NULL,
    description varchar NOT NULL,
    event_date TIMESTAMP NOT NULL,
    initiator_id bigint not null, -- ID инициатора, который будет передаваться через API
    location_id bigint REFERENCES location(id) ON DELETE CASCADE ON UPDATE CASCADE,
    paid BOOLEAN NOT NULL,
    participant_limit INTEGER NOT NULL,
    published_on TIMESTAMP,
    request_moderation BOOLEAN NOT NULL,
    state varchar NOT NULL,
    title varchar NOT NULL,
    confirmed_requests INTEGER NOT NULL,
    rating INTEGER NOT NULL
);

-- Таблица подборок событий
create table if not exists compilations
(
    id bigint generated always as identity primary key,
    pinned boolean not null,
    title varchar(255) not null
);

-- Связь между подборками и событиями
create table if not exists compilations_events
(
    compilation_id bigint references compilations (id),
    event_id bigint references events(id)
);
