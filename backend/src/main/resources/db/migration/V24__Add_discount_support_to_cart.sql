-- Add discount reference to cart items
ALTER TABLE cart_items 
ADD COLUMN applied_discount_id INT REFERENCES discounts(discount_id) ON DELETE SET NULL;

-- Add computed price after discount
ALTER TABLE cart_items 
ADD COLUMN final_price DECIMAL(10, 2) GENERATED ALWAYS AS (
    price_at_addition * (1 - COALESCE(
        (SELECT discount_rate FROM discounts WHERE discount_id = applied_discount_id),
        0
    ))
) STORED;

