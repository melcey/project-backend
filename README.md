# **project-backend**
Backend code for the course project of the CS 308 course in Sabancı University.

## IMPORTANT: Major updates in the repo:

- ### The backend is going to be modified to have a monolithic architecture:

  - With a unanimous decision, the project will have a monolithic backend structure going forward.

  - #### What are the changes going to be?

    - All the code that has been previously written for the backend will be moved into a single Spring Boot project, eliminating the need for Eureka Server, gateway, and related stuff at all.

    - Correspondingly, we will have a single Docker container to run the backend application, i.e., the Spring Boot project, and another Docker container to run the PostgreSQL database.

- ### Parameterized configuration being removed:

  - Since we are in the process of simplifying the configurations and the backend application, I also think that I need to step back from my ambitions on having a parameterized configuration as well. As a result, I am going to remove the .env file and instead hardcode the stuff across the configuration files of application and the Docker containers for your convenience. [@OnurOrman](https://github.com/OnurOrman)

- ### Changes on the database:

  - **Database user**: Since we are going to change the microservices architecture into a monolithic one, it is no longer necessary to have multiple users on the database; therefore, we are going to have a single user, the admin `postgres` for the database instead.

  - **Initialization changes**: The script `db_init.sql` and the dedicated container for database initialization are no longer necessary since we are going to have only a single database user, and thus, the aforementioned configuration stuff is entirely removed from the project; the creation of the database is going to be automatically performed by the container itself through the use of environment variable `POSTGRES_DB`.

  - **Privileges on migrations**: As a result of the changes on the use of database, it is no longer necessary to define privileges on the database objects; each are created and owned by the `postgres` account with all the privileges.

  - **Location of the migrations**: The new location for the migrations is `/backend/src/main/resources/db/migration`.

## How to run the frontend and backend together?

- Please, run the following command on your terminal while running Docker:
  - ```docker network create --driver bridge website-net```

- This command will create an externally shared network between the frontend and backend application containers.

- Afterwards, you will be able to send requests from the frontend container to the backend container.

## Important Notes on Database Connections

- The dev database, which we are going to use as a playground, is mapped to the port `5433` on the machine, and can be used with the dev environment, i.e., the Docker Compose files `docker-compose.dev.*.yml`.

- The prod database, on which we are going to store the actual data, is mapped to the port `5432` on the machine, and can be used with the prod environment, i.e., the Docker Compose files `docker-compose.prod.*.yml`.

## How to do remote debugging on IntelliJ?

- Click the current configuration name.

- Click "Edit Configurations."

- Click the "+" button, hovering "Add New Configuration."

- In the dropdown menu, select "Remote JVM Debug" as the configuration type.

- Keep the debugger mode as "Attach to remote JVM."

- Enter the port specified in docker-compose.*.debug.yml files for the machine, which is `5005`, as the port.

- Select the name of the relevant module as the module classpath.

- Save the configuration.

## How to do remote debugging on VS Code?

- Following is the configuration you can use in VS Code by inserting into the related `launch.json` file:

```json
{
  "type": "java",
  "name": "Attach to Backend on Docker",
  "request": "attach",
  "hostName": "localhost",
  "port": 5005,
  "projectName": "backend"
}
```

- After setting the configuration, go to the Run & Debug menu on the left sidebar, select your configuration from the dropdown menu, and run the configuration.