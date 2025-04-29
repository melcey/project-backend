-- V20__Create_payment_table.sql

CREATE TABLE payments (
    payment_id SERIAL PRIMARY KEY,
    order_id INT UNIQUE NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    payment_date TIMESTAMP NOT NULL,
    payment_status VARCHAR(20) NOT NULL CHECK (payment_status IN ('PENDING', 'COMPLETED', 'FAILED')),
    credit_card_id INT,
    
    CONSTRAINT fk_payment_order
        FOREIGN KEY (order_id)
        REFERENCES orders (order_id)
        ON DELETE CASCADE,
        
    CONSTRAINT fk_payment_credit_card
        FOREIGN KEY (credit_card_id)
        REFERENCES credit_cards (card_id)
        ON DELETE SET NULL
);

CREATE INDEX idx_payment_order ON payments (order_id);
CREATE INDEX idx_payment_credit_card ON payments (credit_card_id);