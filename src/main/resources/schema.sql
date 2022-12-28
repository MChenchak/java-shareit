CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(512) UNIQUE NOT NULL
    );

CREATE TABLE IF NOT EXISTS items (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id BIGINT references users (id),
    name VARCHAR(255),
    description VARCHAR(1000),
    available boolean,
    request_id BIGINT
    );

CREATE TABLE IF NOT EXISTS item_request (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    description VARCHAR(1000),
    user_id BIGINT references users (id),
    created timestamp
);

CREATE TABLE IF NOT EXISTS bookings (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    start_time timestamp,
    end_time timestamp,
    item_id BIGINT references items (id),
    booker_id BIGINT references users (id),
    status VARCHAR(255)
    );

CREATE TABLE IF NOT EXISTS comments (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    created timestamp,
    item_id BIGINT references items (id),
    user_id BIGINT references users (id),
    text VARCHAR(1000)
    );