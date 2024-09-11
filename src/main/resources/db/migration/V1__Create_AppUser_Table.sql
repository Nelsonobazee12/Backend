-- Filename: V1__Create_AppUser_Table.sql

CREATE TABLE app_user (
                          id SERIAL PRIMARY KEY,
                          name VARCHAR(50) NOT NULL,
                          email VARCHAR(255) NOT NULL UNIQUE,
                          password VARCHAR(255) NOT NULL,
                          roles VARCHAR(50) NOT NULL,
                          enabled BOOLEAN NOT NULL
);
