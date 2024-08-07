-- V1__Create_app_user_and_token_tables.sql

CREATE TABLE app_user (
                          id SERIAL PRIMARY KEY,
                          name VARCHAR(50) NOT NULL,
                          email VARCHAR(255) NOT NULL,
                          password VARCHAR(255) NOT NULL,
                          roles VARCHAR(50) NOT NULL,
                          enabled BOOLEAN NOT NULL
);

CREATE TABLE token (
                       id SERIAL PRIMARY KEY,
                       token_value VARCHAR(255) UNIQUE NOT NULL,
                       token_type VARCHAR(50) NOT NULL,
                       is_revoked BOOLEAN NOT NULL DEFAULT FALSE,
                       is_expired BOOLEAN NOT NULL DEFAULT FALSE,
                       app_user_id BIGINT NOT NULL,
                       CONSTRAINT fk_app_user FOREIGN KEY (app_user_id) REFERENCES app_user(id) ON DELETE CASCADE
);

