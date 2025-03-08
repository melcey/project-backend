CREATE TABLE product_manager_actions (
    action_id SERIAL PRIMARY KEY,
    product_manager_id INT REFERENCES users(user_id),
    action_type VARCHAR(255) NOT NULL,
    action_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    details TEXT
);

ALTER TABLE product_manager_actions OWNER TO user_service_role;
GRANT ALL PRIVILEGES ON TABLE product_manager_actions TO user_service_role;
GRANT USAGE, SELECT ON SEQUENCE product_manager_actions_action_id_seq TO user_service_role;