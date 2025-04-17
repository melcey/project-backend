-- Make delivery statuses match order statuses
ALTER TABLE delivery 
ALTER COLUMN delivery_status TYPE VARCHAR(50),
DROP CONSTRAINT IF EXISTS delivery_delivery_status_check;

ALTER TABLE delivery
ADD CONSTRAINT delivery_status_check 
CHECK (delivery_status IN ('pending', 'processing', 'shipped', 'delivered', 'cancelled'));
