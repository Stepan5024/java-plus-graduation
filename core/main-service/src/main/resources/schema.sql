drop table if exists compilations, compilations_events, likes;

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


CREATE TABLE IF NOT EXISTS likes (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    event_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    status VARCHAR(8) NOT NULL,
    created TIMESTAMP NOT NULL
);
