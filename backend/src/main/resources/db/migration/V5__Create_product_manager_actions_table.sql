-- V5__Create_product_manager_actions_table.sql
CREATE TABLE product_manager_actions (
    action_id SERIAL PRIMARY KEY,
    product_manager_id INT REFERENCES users(user_id),
    action_type VARCHAR(255) NOT NULL,
    action_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    details TEXT
);