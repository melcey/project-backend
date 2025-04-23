-- V2__Create_users_table.sql
CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email BYTEA, -- Encrypted email
    home_address TEXT,
    password_hash BYTEA, -- Encrypted password
    role VARCHAR(50) NOT NULL CHECK (role IN ('customer', 'sales_manager', 'product_manager'))
);