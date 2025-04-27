-- V21__Create_invoices_table.sql
CREATE TABLE invoices (
    invoice_id SERIAL PRIMARY KEY,
    payment_id INT REFERENCES payments(payment_id) UNIQUE ON DELETE CASCADE ON UPDATE RESTRICT,
    invoice_number TEXT,
    order_id INT REFERENCES orders(order_id) UNIQUE,
    invoice_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10, 2) NOT NULL,
    pdf_content BYTEA
);