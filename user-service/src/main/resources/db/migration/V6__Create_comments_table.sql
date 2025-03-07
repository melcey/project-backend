-- V6__Create_comments_table.sql
CREATE TABLE comments (
    comment_id SERIAL PRIMARY KEY,
    product_id INT REFERENCES products(product_id),
    user_id INT REFERENCES users(user_id),
    comment TEXT,
    rating INT CHECK (rating BETWEEN 1 AND 10),
    approved BOOLEAN DEFAULT FALSE,
    comment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);