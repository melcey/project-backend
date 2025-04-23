-- V12__Create_wishlist_table.sql
CREATE TABLE wishlist (
    wishlist_id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(user_id),
    product_id INT REFERENCES products(product_id)
);