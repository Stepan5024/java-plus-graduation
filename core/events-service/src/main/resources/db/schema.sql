-- core/events-service/resources/db/schema.sql
drop table if exists events, categories, compilations, compilations_events;

create table if not exists categories
(
    id bigint generated always as identity primary key,
    name varchar(100) not null
);

CREATE TABLE IF NOT EXISTS events (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    annotation VARCHAR,
    category_id BIGINT NOT NULL,
    created_on TIMESTAMP NOT NULL,
    description VARCHAR NOT NULL,
    event_date TIMESTAMP NOT NULL,
    initiator_id BIGINT NOT NULL,
    location_id bigint REFERENCES location(id) ON DELETE CASCADE ON UPDATE CASCADE,
    paid BOOLEAN NOT NULL,
    participant_limit BIGINT NOT NULL,
    published_on TIMESTAMP,
    request_moderation BOOLEAN NOT NULL,
    state VARCHAR NOT NULL,
    title VARCHAR NOT NULL,
    confirmed_requests INTEGER NOT NULL,
    rating INTEGER NOT NULL
);

create table if not exists location
(
    id bigint generated always as identity primary key,
    lat float not null,
    lon float not null
);

create table if not exists compilations
(
    id bigint generated always as identity primary key,
    pinned boolean not null,
    title varchar(255) not null
);

CREATE TABLE IF NOT EXISTS compilations_events (
    compilation_id BIGINT NOT NULL,
    event_id BIGINT NOT NULL,
    PRIMARY KEY (compilation_id, event_id)
);
