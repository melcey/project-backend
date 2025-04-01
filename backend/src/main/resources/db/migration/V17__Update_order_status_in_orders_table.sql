ALTER TABLE orders
DROP COLUMN status;

ALTER TABLE orders
ADD COLUMN status VARCHAR(50) CHECK (status IN ('pending', 'processing', 'shipped', 'delivered', 'cancelled'));