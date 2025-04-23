-- Add discount reference to cart items
ALTER TABLE cart_items 
ADD COLUMN applied_discount_id INT REFERENCES discounts(discount_id) ON DELETE SET NULL;

-- Add a regular column for the computed price
ALTER TABLE cart_items 
ADD COLUMN final_price DECIMAL(10, 2);

-- Create a trigger function to calculate the final price after discount
CREATE OR REPLACE FUNCTION calculate_final_price()
RETURNS TRIGGER AS $$
BEGIN
    NEW.final_price := NEW.price_at_addition * (1 - COALESCE(
        (SELECT discount_rate FROM discounts WHERE discount_id = NEW.applied_discount_id),
        0
    ));
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create a trigger to call the function before insert or update
CREATE TRIGGER trigger_calculate_final_price
BEFORE INSERT OR UPDATE ON cart_items
FOR EACH ROW
EXECUTE FUNCTION calculate_final_price();

