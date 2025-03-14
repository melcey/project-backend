-- V15__Create_sales_manager_actions_table.sql
CREATE TABLE sales_manager_actions (
    action_id SERIAL PRIMARY KEY,
    sales_manager_id INT REFERENCES users(user_id),
    action_type VARCHAR(255) NOT NULL,
    action_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    details TEXT
);