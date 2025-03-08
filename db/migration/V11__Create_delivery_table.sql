CREATE TABLE delivery (
    delivery_id SERIAL PRIMARY KEY,
    order_id INT REFERENCES orders(order_id),
    product_id INT REFERENCES products(product_id),
    quantity INT NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    delivery_address TEXT,
    delivery_status VARCHAR(50) CHECK (delivery_status IN ('pending', 'in-transit', 'delivered'))
);

ALTER TABLE delivery OWNER TO user_service_role;
GRANT ALL PRIVILEGES ON TABLE delivery TO user_service_role;
GRANT USAGE, SELECT ON SEQUENCE delivery_delivery_id_seq TO user_service_role;