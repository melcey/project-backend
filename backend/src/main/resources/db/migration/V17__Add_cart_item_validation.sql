-- V17__Add_cart_item_validation.sql

-- Create validation function
CREATE OR REPLACE FUNCTION validate_cart_item()
RETURNS TRIGGER AS $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM products 
        WHERE product_id = NEW.product_id 
        AND is_active = TRUE
        AND quantity_in_stock >= NEW.quantity
    ) THEN
        RAISE EXCEPTION 'Product not available or insufficient stock';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Add trigger
CREATE TRIGGER trg_validate_cart_item
BEFORE INSERT OR UPDATE ON cart_items
FOR EACH ROW EXECUTE FUNCTION validate_cart_item();