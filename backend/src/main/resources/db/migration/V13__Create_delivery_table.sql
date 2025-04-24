-- V14__Create_delivery_table.sql
CREATE TABLE delivery (
    delivery_id SERIAL PRIMARY KEY,
    order_id INT REFERENCES orders(order_id),
    product_id INT REFERENCES products(product_id),
    quantity INT NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    delivery_address TEXT,
    delivery_status VARCHAR(50) CHECK (delivery_status IN ('processing', 'in-transit', 'delivered'))
);