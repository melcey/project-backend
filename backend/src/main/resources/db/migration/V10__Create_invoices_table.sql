-- V10__Create_invoices_table.sql
CREATE TABLE invoices (
    invoice_id SERIAL PRIMARY KEY,
    invoice_number TEXT,
    order_id INT REFERENCES orders(order_id),
    invoice_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_price DECIMAL(10, 2) NOT NULL,
    pdf_content BYTEA
);