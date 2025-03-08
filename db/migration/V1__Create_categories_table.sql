-- V1__Create_categories_table.sql
CREATE TABLE categories (
    category_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT
);

