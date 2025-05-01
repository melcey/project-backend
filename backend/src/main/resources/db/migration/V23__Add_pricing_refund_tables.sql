-- Add pricing columns to products
ALTER TABLE products 
ADD COLUMN is_priced BOOLEAN DEFAULT FALSE,
ADD COLUMN original_price DECIMAL(10,2),
ADD COLUMN discount_rate DECIMAL(5,2) DEFAULT 0,
ADD COLUMN cost_price DECIMAL(10,2);

-- Create refund tables
CREATE TABLE return_requests (
    request_id SERIAL PRIMARY KEY,
    order_id INT REFERENCES orders(order_id),
    user_id INT REFERENCES users(user_id),
    product_id INT REFERENCES products(product_id),
    quantity INT NOT NULL,
    original_price DECIMAL(10,2) NOT NULL,
    reason TEXT,
    status VARCHAR(20) DEFAULT 'PENDING',
    request_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    resolution_date TIMESTAMP,
    resolved_by INT REFERENCES users(user_id)
);

CREATE TABLE refunds (
    refund_id SERIAL PRIMARY KEY,
    request_id INT REFERENCES return_requests(request_id),
    amount DECIMAL(10,2) NOT NULL,
    refund_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'PROCESSING'
);