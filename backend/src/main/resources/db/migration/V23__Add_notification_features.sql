-- V23__Add_notification_features.sql
-- Beware of that migration prior to this one may have wrong titles. 
--Always check the migration names twice for the correct order.

-- 1. Add new columns to existing wishlist table
ALTER TABLE wishlist 
  ADD COLUMN created_at TIMESTAMPTZ DEFAULT NOW(),
  ADD COLUMN notification_sent BOOLEAN DEFAULT FALSE,
  ADD COLUMN last_notified_at TIMESTAMPTZ;

-- 2. Prevent duplicate wishlist items
ALTER TABLE wishlist ADD CONSTRAINT unique_user_product UNIQUE (user_id, product_id);

-- 3. Create new notifications table
CREATE TABLE wishlist_notifications (
    notification_id SERIAL PRIMARY KEY,
    wishlist_id INT NOT NULL REFERENCES wishlist(wishlist_id) ON DELETE CASCADE,
    product_id INT NOT NULL REFERENCES products(product_id),
    discount_percentage DECIMAL(5,2) NOT NULL,
    old_price DECIMAL(10,2) NOT NULL,
    new_price DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    is_read BOOLEAN DEFAULT FALSE
);

-- 4. Add indexes for performance
CREATE INDEX idx_wishlist_user_product ON wishlist(user_id, product_id);
CREATE INDEX idx_unread_notifications ON wishlist_notifications(is_read) WHERE is_read = FALSE;