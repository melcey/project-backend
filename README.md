# **project-backend**
Backend code for the course project of the CS 308 course in Sabancı University.

# IMPORTANT: Major updates in the repo:

- ## The backend is going to be modified to have a monolithic architecture:

  - With a unanimous decision, the project will have a monolithic backend structure going forward.

  - ### What are the changes going to be?

    - All the code that has been previously written for the backend will be moved into a single Spring Boot project, eliminating the need for Eureka Server, gateway, and related stuff at all.

    - Correspondingly, we will have a single Docker container to run the backend application, i.e., the Spring Boot project.

- ## Parameterized configuration being removed:

  - Since we are in the process of simplifying the configurations and the backend application, I also think that I need to step back from my (kind of) ambitions on having a parameterized configuration as well. As a result, I am going to remove the .env file and instead hardcode the stuff across the configuration files of application and the Docker containers. [@OnurOrman](https://github.com/OnurOrman)

- ## Changes on the database:

  - **Database user**: Since we are going to eliminate the microservices altogether, it is no longer necessary to have multiple users on the database; therefore, we are going to have a single user, the admin `postgres` for the database instead.

  - **Initialization changes**: The script `db_init.sql` and the dedicated container for database initialization are no longer necessary since we are going to have only a single database user, and thus, the aforementioned configuration stuff is entirely removed from the project; the creation of the database is going to be automatically performed by the container itself through the use of environment variable `POSTGRES_DB`.



## **How to do remote debugging on IntelliJ?**

- Click the current configuration name.

- Click "Edit Configurations."

- Click the "+" button, hovering "Add New Configuration."

- In the dropdown menu, select "Remote JVM Debug" as the configuration type.

- Keep the debugger mode as "Attach to remote JVM."

- Enter the port specified in docker-compose.*.debug.yml files for the machine as the port.

  - e.g., for `5006:5005`, enter **`5006`** as the port number, keeping the host as `localhost`.

- Select the name of the relevant module as the module classpath.

- Save the configuration.

## **How to do remote debugging on VS Code?**

- Following is an example configuration for VS Code to insert into the related `launch.json` file:

```json
{
  "type": "java",
  "name": "Attach to Remote Spring Boot App",
  "request": "attach",
  "hostName": "localhost",
  "port": 5005,
  "projectName": "myapp"
}
```

- Here, you need to insert this snippet into your `launch.json` file for each Spring Boot project, and modify the fields according to the project you want to use.

- Then, go to the Run & Debug menu on the left sidebar, select your configuration from the dropdown menu, and run the configuration.