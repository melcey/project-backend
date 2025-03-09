-- V15__Privileges_users_table.sql
ALTER TABLE users OWNER TO user_service_role;
GRANT ALL PRIVILEGES ON TABLE users TO user_service_role;
GRANT USAGE, SELECT ON SEQUENCE users_user_id_seq TO user_service_role;