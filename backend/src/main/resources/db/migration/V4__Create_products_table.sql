-- V4__Create_products_table.sql
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
    is_active BOOLEAN DEFAULT TRUE,
    image_url TEXT,
    product_manager_id INT,
    -- Makes the product_manager_id column a foreign key referencing the users table
    -- The logic on the roles will be handled in the API part.
    -- Normally, the user ID will not be updated; therefore, reaction to such behavior (if ever happens) is defined as RESTRICT.
    CONSTRAINT fk_product_manager FOREIGN KEY (product_manager_id) REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE RESTRICT
);