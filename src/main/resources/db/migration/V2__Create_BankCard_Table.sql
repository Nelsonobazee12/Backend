-- Drop the table and all dependent objects (use with caution!)

-- Recreate the table
CREATE TABLE bank_card (
                           id SERIAL PRIMARY KEY,
                           card_number VARCHAR(255) NOT NULL,
                           expiry_date VARCHAR(10) NOT NULL,
                           cvv VARCHAR(10) NOT NULL,
                           app_user_id BIGINT NOT NULL,
                           balance DOUBLE PRECISION DEFAULT 0.0,
                           CONSTRAINT fk_app_user
                               FOREIGN KEY (app_user_id)
                                   REFERENCES app_user (id)
                                   ON DELETE CASCADE
);
