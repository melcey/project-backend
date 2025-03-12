-- V8__Create_invoices_table.sql
CREATE TABLE invoices (
    invoice_id SERIAL PRIMARY KEY,
    order_id INT REFERENCES orders(order_id),
    invoice_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_price DECIMAL(10, 2) NOT NULL,
    pdf_path TEXT
);

ALTER TABLE invoices OWNER TO user_service_role;
GRANT ALL PRIVILEGES ON TABLE invoices TO user_service_role;
GRANT USAGE, SELECT ON SEQUENCE invoices_invoice_id_seq TO user_service_role;