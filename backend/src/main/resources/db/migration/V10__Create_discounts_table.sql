-- V10__Create_discounts_table.sql
CREATE TABLE discounts (
    discount_id SERIAL PRIMARY KEY,
    product_id INT REFERENCES products(product_id),
    discount_rate DECIMAL(5, 2) NOT NULL,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL
);