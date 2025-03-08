-- The script to initialize the prod and dev databases
-- It only runs in the following conditions:
-- - When the database is run for the first time
-- - Or, when this script is updated

-- Gets the environment variables from .env file
\getenv postgres_db POSTGRES_DB
\getenv userservice_role POSTGRES_USERSERVICE_ROLE
\getenv userservice_user POSTGRES_USERSERVICE_USER
\getenv userservice_password POSTGRES_USERSERVICE_PASSWORD

-- Creates the project database if it does not exist
-- Default schema, `public`, will be used, so we do not need a new schema
SELECT format('CREATE DATABASE %I', :'postgres_db')
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = :'postgres_db') \gexec

-- Switches to the project database through the command invoked in psql
\c :postgres_db

-- Creates the roles for the database users per microservices
-- Creates the user service role if it doesn’t exist:
SELECT 
    CASE 
      WHEN NOT EXISTS (SELECT 1 FROM pg_roles WHERE rolname = :'userservice_role')
      THEN format('CREATE ROLE %I', :'userservice_role')
    END
\gexec

-- Creates the database user for the user microservice if it doesn’t exist:
-- Note: Users and roles reside at the same namespace, pg_roles, in PostgreSQL
    -- User is a role with the LOGIN privilege
SELECT 
    CASE 
      WHEN NOT EXISTS (SELECT 1 FROM pg_roles WHERE rolname = :'userservice_user')
      THEN format('CREATE USER %I WITH ENCRYPTED PASSWORD %L', 
                  :'userservice_user', 
                  :'userservice_password')
    END
\gexec

-- Grants privileges to the user microservice role
-- Allows the user microservice role to connect to the database
SELECT format('GRANT CONNECT ON DATABASE %I TO %I', :'postgres_db', :'userservice_role') \gexec
-- The user microservice role is able to access the database objects (tables, procedures, etc.) in the public schema of the project database
    -- Note: The individual privilege settings for the database objects will override this setting; this is just a global setting
    -- The user microservice is also able to create new database objects in the public schema of the project database
SELECT format('GRANT USAGE, CREATE ON SCHEMA public TO %I', :'userservice_role') \gexec

-- Grants the user microservice role to the user microservice user
SELECT format('GRANT %I TO %I', :'userservice_role', :'userservice_user') \gexec

-- Creates the pgcrypto extension if it does not exist
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Other privileges will be given on the basis of individual database objects to the roles in the related migrations