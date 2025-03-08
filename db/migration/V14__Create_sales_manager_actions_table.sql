CREATE TABLE sales_manager_actions (
    action_id SERIAL PRIMARY KEY,
    sales_manager_id INT REFERENCES users(user_id),
    action_type VARCHAR(255) NOT NULL,
    action_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    details TEXT
);

ALTER TABLE sales_manager_actions OWNER TO user_service_role;
GRANT ALL PRIVILEGES ON TABLE sales_manager_actions TO user_service_role;
GRANT USAGE, SELECT ON SEQUENCE sales_manager_actions_action_id_seq TO user_service_role;