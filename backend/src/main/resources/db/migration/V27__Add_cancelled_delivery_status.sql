-- V28__Add_cancelled_delivery_status.sql

ALTER TABLE delivery
DROP CONSTRAINT delivery_delivery_status_check;

ALTER TABLE delivery
ADD CONSTRAINT delivery_delivery_status_check
    CHECK (delivery_status IN ('processing', 'in-transit', 'delivered', 'cancelled'));