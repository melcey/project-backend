-- V5__Create_order_items_table.sql
CREATE TABLE order_items (
    order_item_id SERIAL PRIMARY KEY,
    order_id INT REFERENCES orders(order_id),
    product_id INT REFERENCES products(product_id),
    quantity INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL
);

ALTER TABLE order_items OWNER TO user_service_role;
GRANT ALL PRIVILEGES ON TABLE order_items TO user_service_role;
GRANT USAGE, SELECT ON SEQUENCE order_items_order_item_id_seq TO user_service_role;