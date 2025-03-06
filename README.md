# **project-backend**
Backend code for the course project of the CS 308 course in Sabancı University.

## **How to do remote debugging on IntelliJ?**
- Click the current configuration name.
- Click "Edit Configurations."
- Click the "+" button, hovering "Add New Configuration."
- In the dropdown menu, select "Remote JVM Debug" as the configuration type.
- Keep the debugger mode as "Attach to remote JVM."
- Enter the port specified in docker-compose.*.debug.yml files for the machine as the port.
  - e.g., for `5006:5005`, enter **5006** as the port number, keeping the host as `localhost`.
- Select name of the relevant module as the module classpath.
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
- Here, you need to insert this snippet per Spring Boot project, and modify the fields according to the project you want to use.
- Then, go to the Run & Debug menu on the left sidebar, select your configuration from the dropdown menu, and run the configuration.