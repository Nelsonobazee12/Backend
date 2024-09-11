

CREATE TABLE transaction (
                             id BIGSERIAL PRIMARY KEY,
                             transaction_id VARCHAR(255) NOT NULL UNIQUE,
                             amount DOUBLE PRECISION NOT NULL,
                             type VARCHAR(50) NOT NULL,
                             timestamp VARCHAR(255) NOT NULL,
                             description VARCHAR(255),
                             card_number VARCHAR(255),
                             card_holder_name VARCHAR(255),
                             balance_after_transaction DOUBLE PRECISION NOT NULL,
                             bank_card_id BIGINT,
                             CONSTRAINT fk_transaction_bank_card
                                 FOREIGN KEY (bank_card_id) REFERENCES bank_card(id)
                                     ON DELETE CASCADE
);
