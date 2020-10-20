# taskapp
Task management sample app

## Run the app

### Option 1 (Maven + Java)
- Step 1: Open a terminal and in the project root directory (where the _pom.xml_ file is located) execute `mvn clean package` 
- Step 2: go to _target_ directory `cd target` and execute `java -jar springboot-taskapp.jar` the app will start on _localhost_ port _8080_
- Step 3: once you see in the logs the message _Started TaskappApplication_ the app should be available

To stop the app just type `Control + C` on the terminal

### Option 2 (Maven + Docker)
- Step 1: Open a terminal and in the project root directory (where the _pom.xml_ file is located) execute `mvn clean package` 
- Step 2: in the project root directory (also where the _Dockerfile_ is located) execute `docker build -t [image-name]:[image-version] .`, providing an _[image-name]_ and an _[image-version]_ for instance `docker build -t taskapp_img:0.0.1 .`
- Step 3: execute `docker run -d --name [container-name] -p [port]:[port] [image-name]:[image-version]`, providing the needed values, for instance:
    `docker run -d --name taskapp -p 8080:8080 taskapp_img:0.0.1`
- Step 4: verify with `docker ps -a` that the container is running, and check the logs with `docker logs [CONTAINER ID]`. Once you see in the logs the message _Started TaskappApplication_ the app should be available.

To stop the app use the command `docker stop [CONTAINER ID]`

## Using the app
An API client is needed in order to use the Taskapp, the recomended one is [Postman](https://www.postman.com/) (a postman collection file is included in the project _TASKAPP.postman_collection.json_ and is ready to be imported).

### Services
- **AUTH** (POST|http://localhost:8080/taskapp/api/authenticate): First step needed is to get an authentication token. The available users are

    - username: `john`, password `1234`
    - username: `susan`. password `1234`
    
    The service is configured to use Basic Auth and in case of success will return a **JWT token** in the `Authorization` header of the response.
> If you are using the postman collection provided you may need to create a postman variable `{{taskAppToken}}` in the postman environment that will allow to automate the token assignment.
    

- **LIST** (GET|http://localhost:8080/taskapp/api/v1/tasks): By default the user `john` has a few tasks created, you can list them using this service:

    - headers:
    
    Name | Value
    --- | ---
    Authorization | the JWT token
    
    - body (all the parameters are optional):
    ```
  {    
    "title": "to filter by title",
    "description": "to filter by description",
    "dueDate": "to filter by due date, format yyyy-MM-ddTHH:mm:ss",
    "done": true / false to filter by status,
    "page": the number of the page to retrieve (by default 0),
    "pageSize": the size of the page to paginate (by default 50)
  }
    ```


- **BY ID** (GET|http://localhost:8080/taskapp/api/v1/tasks/:id): obtains the details of a task based on the ID
 
    - headers:
    
    Name | Value
    --- | ---
    Authorization | the JWT token
     
 
- **CREATE** (POST|http://localhost:8080/taskapp/api/v1/tasks): creates a new task

    - headers:
    
    Name | Value
    --- | ---
    Authorization | the JWT token
    
    - body:
    ```
  {    
    "title": "the title (mandatory)",
    "description": "the description",
    "dueDate": "the due date, format yyyy-MM-ddTHH:mm:ss"
  }
    ```
 
 - **UPDATE** (PUT|http://localhost:8080/taskapp/api/v1/tasks/:id): updates an existing task (parameters are optional, only the provided ones will be updated)
 
     - headers:
     
     Name | Value
     --- | ---
     Authorization | the JWT token
     
     - body accepts:
     ```
   {    
     "title": "the title",
     "description": "the description",
     "dueDate": "the due date, format yyyy-MM-ddTHH:mm:ss",
     "done": true/false (allows to mark a task as 'done')
   }
     ```

- **DELETE** (DELETE|http://localhost:8080/taskapp/api/v1/tasks/:id): deletes an existing task

    - headers:
    
    Name | Value
    --- | ---
    Authorization | the JWT token

## Future Features
- Implement an external DB
- Replace the showcase authentication with a more complex implementation.