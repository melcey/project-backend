CREATE TABLE payments (
    payment_id SERIAL PRIMARY KEY,
    order_id INT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    payment_date TIMESTAMP NOT NULL,
    payment_status VARCHAR(20) NOT NULL CHECK (payment_status IN ('PENDING', 'COMPLETED', 'FAILED')),
    card_details BYTEA,
    
    CONSTRAINT fk_payment_order
        FOREIGN KEY (order_id)
        REFERENCES orders (order_id)
        ON DELETE CASCADE
);

CREATE INDEX idx_payment_order ON payments (order_id);