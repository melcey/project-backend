ALTER TABLE comments
DROP COLUMN rating;

CREATE TABLE ratings (
    rating_id SERIAL PRIMARY KEY,
    product_id INT REFERENCES products(product_id),
    user_id INT REFERENCES users(user_id),
    rating INT CHECK (rating BETWEEN 1 AND 10),
    rating_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);