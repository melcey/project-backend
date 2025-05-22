-- V26__Add_cancelled_status_to_orders_table.sql

ALTER TABLE orders
DROP CONSTRAINT orders_status_check;

ALTER TABLE orders
ADD CONSTRAINT orders_status_check
    CHECK (status IN ('processing', 'in-transit', 'delivered', 'cancelled'));