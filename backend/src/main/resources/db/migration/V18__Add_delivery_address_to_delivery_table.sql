ALTER TABLE delivery ADD COLUMN delivery_adress TEXT;

ALTER TABLE delivery OWNER TO user_service_role;
GRANT ALL PRIVILEGES ON TABLE delivery TO user_service_role;
