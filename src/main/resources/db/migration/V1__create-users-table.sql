CREATE TABLE users
(
    id        BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    full_name VARCHAR(100),
    document  VARCHAR(14),
    email     VARCHAR(100),
    CONSTRAINT pk_users PRIMARY KEY (id)
);