-- The script to initialize the prod and dev databases
-- It only runs in the following conditions:
-- - When the database is going to be run for the first time
-- - Or, when this script is updated

-- Creates the project database if it does not exist
-- Default schema, `public`, will be used, so we do not need a new schema
SELECT 'CREATE DATABASE project_db'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'project_db')\gexec

-- Switches to the project database through the command invoked in psql
\c project_db;

-- Creates the roles for the database users per microservices
DO $$
BEGIN
    -- Creates the user service role for that database user
    IF NOT EXISTS (SELECT 1 FROM pg_roles WHERE rolname = 'user_service_role') THEN
        CREATE ROLE user_service_role;
    END IF;

    -- Creates the database user for the user microservice
    -- Note: Users and roles reside at the same namespace, pg_roles, in PostgreSQL
        -- User is a role with the LOGIN privlege
    IF NOT EXISTS (SELECT 1 FROM pg_roles WHERE rolname = 'user_service') THEN
        -- The password is retrieved from the .env file and forcefully encrypted
        CREATE USER user_service WITH ENCRYPTED PASSWORD 'user_db_pwd';
    END IF;
END $$;

-- Grants the following privileges to `user_service_role`:
    -- user_service_role is able to connect to the database project_db
GRANT CONNECT ON DATABASE project_db TO user_service_role;
    -- user_service_role is able to access the database objects (tables, procedures, etc.) in the public schema of project_db
        -- Note: The individual privilege settings for the database objects will override this setting; this is just a global setting
    -- user_service_role is also able to create new database objects in the public schema of project_db
GRANT USAGE, CREATE ON SCHEMA public TO user_service_role;

-- Grants the role to the user
GRANT user_service_role TO user_service;

CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Other privileges will be given on the basis of individual database objects to the roles in the related migrations