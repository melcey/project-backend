ALTER TABLE products
-- Adds the product_manager_id column to the products table
ADD COLUMN product_manager_id INT,
-- Makes the product_manager_id column a foreign key referencing the users table
-- The logic on the roles will be handled in the API part.
-- Normally, the user ID will not be updated; therefore, reaction to such behavior (if ever happens) is defined as RESTRICT.
ADD CONSTRAINT fk_product_manager FOREIGN KEY (product_manager_id) REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE RESTRICT;