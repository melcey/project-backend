# **project-backend**
Backend code for the course project of the CS 308 course in Sabancı University.

## On the various details regarding configuration

### **How to use .env to have your own configuration for passwords:**

- Create a file named `.env`, and copy the content of .env.template into that file.

- Fill the password fields to the .env file on your own; the rest will be handled within the project.

- As the project progresses, new fields will be added to the .env file, so make sure you keep track of the developments in your configuration.

- **Note: I am keeping the actual .env file on the repo for Sprint 1 alongside .env.template. From Sprint 2 onwards, I will update .gitignore to include the actual .env file, prompting you to generate your .env files on your own in the above specified way instead, so that you can secure your own personal configuration without affecting the rest of the repo. @OnurOrman**

### **How to do remote debugging on IntelliJ?**

- Click the current configuration name.

- Click "Edit Configurations."

- Click the "+" button, hovering "Add New Configuration."

- In the dropdown menu, select "Remote JVM Debug" as the configuration type.

- Keep the debugger mode as "Attach to remote JVM."

- Enter the port specified in docker-compose.*.debug.yml files for the machine as the port.

  - e.g., for `5006:5005`, enter **`5006`** as the port number, keeping the host as `localhost`.

- Select the name of the relevant module as the module classpath.

- Save the configuration.

### **How to do remote debugging on VS Code?**

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