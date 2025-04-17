CREATE TABLE cart_audit_log (
    log_id SERIAL PRIMARY KEY,
    cart_id INT NOT NULL,
    user_id INT REFERENCES users(user_id),
    action VARCHAR(10) NOT NULL CHECK (action IN ('CREATE','UPDATE','DELETE')),
    changed_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    old_values JSONB,
    new_values JSONB
);

-- Add trigger function (will be created separately)
COMMENT ON TABLE cart_audit_log IS 'Tracks all changes to shopping carts';