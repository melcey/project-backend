ALTER TABLE products ADD COLUMN image_url TEXT;

ALTER TABLE products OWNER TO user_service_role;
GRANT ALL PRIVILEGES ON TABLE products TO user_service_role;