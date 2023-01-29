CREATE TABLE IF NOT EXISTS person
(
    id    serial NOT NULL PRIMARY KEY,
    name  VARCHAR(255)          NOT NULL,
    email VARCHAR(512) UNIQUE   NOT NULL
);

CREATE TABLE IF NOT EXISTS items
(
    id          serial NOT NULL PRIMARY KEY,
    user_id     BIGINT references person,
    name        VARCHAR(255),
    description VARCHAR(1000),
    available   boolean,
    request_id  BIGINT
);

CREATE TABLE IF NOT EXISTS item_request
(
    id          serial NOT NULL PRIMARY KEY,
    description VARCHAR(1000),
    user_id     BIGINT references person,
    created     timestamp
);

CREATE TABLE IF NOT EXISTS bookings
(
    id         serial NOT NULL PRIMARY KEY,
    start_time timestamp,
    end_time   timestamp,
    item_id    BIGINT references items,
    booker_id  BIGINT references person,
    status     VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS comments
(
    id      serial NOT NULL PRIMARY KEY,
    created timestamp,
    item_id BIGINT references items,
    user_id BIGINT references person,
    text    VARCHAR(1000)
);