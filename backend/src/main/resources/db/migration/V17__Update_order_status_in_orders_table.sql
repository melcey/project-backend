ALTER TABLE orders
DROP COLUMN status;

ALTER TABLE orders
ADD COLUMN status VARCHAR(50) CHECK (status IN ('pending', 'processing', 'shipped', 'delivered', 'cancelled'));

ALTER TABLE orders OWNER TO user_service_role;
GRANT ALL PRIVILEGES ON TABLE orders TO user_service_role;