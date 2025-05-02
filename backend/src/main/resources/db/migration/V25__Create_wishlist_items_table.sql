-- Drop the index wrongly constructed
DROP INDEX idx_wishlist_user_product;

-- Drop the previous constraint unique_user_product on wishlist table
ALTER TABLE wishlist
DROP CONSTRAINT unique_user_product;

-- Remove the product ID column from wishlist table
ALTER TABLE wishlist
DROP COLUMN product_id;

-- Remove the product ID column from wishlist_notifications
ALTER TABLE wishlist_notifications
DROP COLUMN product_id;

-- Ensure the uniqueness of user_id in the wishlist table
ALTER TABLE wishlist
ADD CONSTRAINT unique_user_id UNIQUE (user_id);

CREATE TABLE wishlist_items (
    wishlist_item_id SERIAL PRIMARY KEY,
    wishlist_id INT REFERENCES wishlist(wishlist_id),
    product_id INT REFERENCES products(product_id)
);

-- Add the wishlist_item_id column to wishlist_notifications table
ALTER TABLE wishlist_notifications
ADD COLUMN wishlist_item_id INT NOT NULL REFERENCES wishlist_items(wishlist_item_id);