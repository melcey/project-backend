-- V12__Create_refunds_table.sql
CREATE TABLE refunds (
    refund_id SERIAL PRIMARY KEY,
    order_id INT REFERENCES orders(order_id),
    product_id INT REFERENCES products(product_id),
    refund_amount DECIMAL(10, 2) NOT NULL,
    refund_status VARCHAR(50) CHECK (refund_status IN ('pending', 'approved', 'rejected')),
    request_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    approval_date TIMESTAMP
);