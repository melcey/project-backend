-- V5__Create_orders_table.sql
CREATE TABLE orders (
    order_id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(user_id),
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50) CHECK (status IN ( 'processing', 'in-transit', 'delivered', )),
    total_price DECIMAL(10, 2) NOT NULL,
    delivery_address TEXT
);