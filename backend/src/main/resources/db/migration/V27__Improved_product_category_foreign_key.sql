-- V27__Alter_products_category_fk.sql

ALTER TABLE products
    DROP CONSTRAINT IF EXISTS products_category_id_fkey;

ALTER TABLE products
    ADD CONSTRAINT fk_products_category
        FOREIGN KEY (category_id)
        REFERENCES categories(category_id)
        ON DELETE SET NULL
        ON UPDATE RESTRICT;