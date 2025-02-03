-- core/events-service/resources/db/schema.sql
drop table if exists events, location;

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
    participant_limit INTEGER NOT NULL,
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