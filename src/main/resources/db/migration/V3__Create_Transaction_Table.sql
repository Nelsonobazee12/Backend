
CREATE TABLE transaction (
                             id SERIAL PRIMARY KEY,
                             transaction_id VARCHAR(255) NOT NULL UNIQUE,
                             amount DOUBLE PRECISION NOT NULL,
                             type VARCHAR(50) NOT NULL, -- Could be "DEPOSIT", "WITHDRAWAL", "TRANSFER"
                             timestamp VARCHAR(50) NOT NULL, -- Adjust format or type if using TIMESTAMP
                             description TEXT,
                             card_number VARCHAR(255), -- Snapshot fields
                             card_holder_name VARCHAR(255) NOT NULL,
                             balance_after_transaction DOUBLE PRECISION NOT NULL,
                             bank_card_id BIGINT,
                             CONSTRAINT fk_bank_card
                                 FOREIGN KEY (bank_card_id)
                                     REFERENCES bank_card (id)
                                     ON DELETE SET NULL
);
