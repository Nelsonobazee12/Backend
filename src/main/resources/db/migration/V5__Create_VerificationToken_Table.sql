

CREATE TABLE verification_tokens (
                                     id BIGSERIAL PRIMARY KEY,
                                     token VARCHAR(255) NOT NULL UNIQUE,
                                     user_id BIGINT NOT NULL,
                                     expiration_time TIMESTAMP NOT NULL,
                                     CONSTRAINT fk_verification_token_user
                                         FOREIGN KEY (user_id) REFERENCES app_user(id)
                                             ON DELETE CASCADE
);
