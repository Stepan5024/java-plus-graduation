drop table if exists compilations, compilations_events, likes;



CREATE TABLE IF NOT EXISTS likes (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    event_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    status VARCHAR(8) NOT NULL,
    created TIMESTAMP NOT NULL
);
