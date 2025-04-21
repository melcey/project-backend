-- V7__Create_comments_table.sql
CREATE TABLE comments (
    comment_id SERIAL PRIMARY KEY,
    product_id INT REFERENCES products(product_id),
    user_id INT REFERENCES users(user_id),
    comment TEXT,
    approved BOOLEAN DEFAULT FALSE,
    comment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);