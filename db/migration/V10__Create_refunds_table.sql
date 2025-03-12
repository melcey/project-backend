CREATE TABLE refunds (
    refund_id SERIAL PRIMARY KEY,
    order_id INT REFERENCES orders(order_id),
    product_id INT REFERENCES products(product_id),
    refund_amount DECIMAL(10, 2) NOT NULL,
    refund_status VARCHAR(50) CHECK (refund_status IN ('pending', 'approved', 'rejected')),
    request_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    approval_date TIMESTAMP
);

ALTER TABLE refunds OWNER TO user_service_role;
GRANT ALL PRIVILEGES ON TABLE refunds TO user_service_role;
GRANT USAGE, SELECT ON SEQUENCE refunds_refund_id_seq TO user_service_role;