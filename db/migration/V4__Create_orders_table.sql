-- V4__Create_orders_table.sql
CREATE TABLE orders (
    order_id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(user_id),
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50) CHECK (status IN ('processing', 'in-transit', 'delivered')),
    total_price DECIMAL(10, 2) NOT NULL,
    delivery_address TEXT
);

ALTER TABLE orders OWNER TO user_service_role;
GRANT ALL PRIVILEGES ON TABLE orders TO user_service_role;
GRANT USAGE, SELECT ON SEQUENCE orders_order_id_seq TO user_service_role;