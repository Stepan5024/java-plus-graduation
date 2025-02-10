CREATE TABLE IF NOT EXISTS REQUESTS
(
    REQUEST_ID BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    CREATED TIMESTAMP NOT NULL,
    EVENT_ID BIGINT NOT NULL,
    REQUESTER_ID BIGINT NOT NULL,
    STATUS VARCHAR(32) NOT NULL,

    CONSTRAINT REQUESTS_PK
        PRIMARY KEY (REQUEST_ID),
    CONSTRAINT REQUESTS_UNIQUE_EVENT_ID_REQUESTER_ID
        UNIQUE (EVENT_ID, REQUESTER_ID)
);
