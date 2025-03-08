-- V2__Create_products_table.sql
CREATE TABLE products (
    product_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    model VARCHAR(255),
    serial_number VARCHAR(255),
    description TEXT,
    quantity_in_stock INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    warranty_status VARCHAR(255),
    distributor_info TEXT,
    category_id INT REFERENCES categories(category_id),
    is_active BOOLEAN DEFAULT TRUE
);

ALTER TABLE products OWNER TO user_service_role;
GRANT ALL PRIVILEGES ON TABLE products TO user_service_role;
GRANT USAGE, SELECT ON SEQUENCE products_product_id_seq TO user_service_role;