-- Filename: V1__Create_AppUser_Table.sql

CREATE TABLE app_user (
                          id SERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          email VARCHAR(255) NOT NULL UNIQUE,
                          password VARCHAR(255) NOT NULL,
                          profile_image VARCHAR(255) NOT NULL DEFAULT 'https://res.cloudinary.com/dbjwj3ugv/image/upload/v1725105327/fjmlxipfxhaltwfnlxsc.png',
                          roles VARCHAR(50) NOT NULL,
                          enabled BOOLEAN NOT NULL,
                          is_first_login BOOLEAN DEFAULT TRUE,
                          is_two_factor_auth_enabled BOOLEAN NOT NULL,
                          secret VARCHAR(255)
);
