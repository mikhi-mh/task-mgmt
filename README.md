# Task Management API

A simple Task Management REST API built with Spring Boot, JPA, and H2 in-memory database. This project allows you to create, read, update, delete, filter, and paginate tasks.

## Features

- Create, update, delete, and retrieve tasks by ID
- Retrieve all tasks or paginated tasks with sorting
- Filter tasks by status, due date, or both
- Retrieve tasks with due date up to a specific date
- Input validation and global exception handling
- In-memory H2 database with sample data
- Swagger/OpenAPI documentation
- Dockerized application for easy deployment

## Tech Stack

- Java 11
- Spring Boot 2.7.5
- Spring Data JPA
- H2 Database
- Maven
- Swagger/OpenAPI (springdoc-openapi-ui)
- Docker

## API Endpoints

| Method | Endpoint                        | Description                                         |
|--------|---------------------------------|-----------------------------------------------------|
| POST   | `/v1/tasks`                     | Create a new task                                   |
| GET    | `/v1/tasks/{id}`                | Get a task by ID                                    |
| PUT    | `/v1/tasks/{id}`                | Update a task by ID                                 |
| DELETE | `/v1/tasks/{id}`                | Delete a task by ID                                 |
| GET    | `/v1/tasks`                     | Get all tasks                                       |
| GET    | `/v1/tasks/paginated`           | Get paginated and sorted tasks                      |
| GET    | `/v1/tasks/filter`              | Filter tasks by status and/or due date              |
| GET    | `/v1/tasks/till-date`           | Get tasks with due date up to a specific date       |

### Filtering and Pagination

- `/v1/tasks/filter?status=TODO&dueDate=2024-06-01` — filter by status and due date (both optional)
- `/v1/tasks/paginated?page=0&size=10&sortBy=dueDate&direction=ASC` — pagination and sorting

## Data Model

- **Task**
    - `id`: Long (auto-generated)
    - `title`: String (required)
    - `description`: String
    - `status`: Enum (`TODO`, `IN_PROGRESS`, `DONE`)
    - `dueDate`: LocalDate

- **ApiResponseDto\<T\>**
    - `message`: String
    - `data`: T
    - `success`: boolean

## Database

- Uses H2 in-memory database
- Sample data is loaded on startup (see `src/main/resources/schema.sql`)
- H2 Console available at: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

## API Documentation

- Swagger UI: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- OpenAPI docs: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

## How to Run Locally

1. **Clone the repository**
   ```sh
   git clone <your-repo-url>
   cd <project-directory>
   ```

2. **Build the project**
   ```sh
   mvn clean install
   ```

3. **Run the application**
   ```sh
   mvn spring-boot:run
   ```
   or run the `TaskManagementApplication` main class from your IDE.

4. **Access the API**
    - API base URL: `http://localhost:8080/v1/tasks`
    - Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
    - H2 Console: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

**Note:**
- Default H2 credentials:
    - Username: `sa`
    - Password: *(leave blank)*
- JDBC URL: `jdbc:h2:mem:testdb`

---
## How to Generate Javadoc / Check docs

1. **Generate Javadoc using Maven**
   ```sh
   mvn javadoc:javadoc
   ```
2. **Generate Javadoc for a Specific File** :  <br> Run the following command to generate Javadoc for TestController.java :
   ```sh
   javadoc -d docs/ src/main/java/org/mikhi/taskM/controller/TestController.java
   ```
3. **Output The generated Javadoc will be available in:**
   `target/site/apidocs/`

4. **View the Javadoc Open the following file in a browser:** `target/site/apidocs/index.html`
5. **Navigate to your TestController documentation.**
---

## Docker Support

The application is now Dockerized (also for easy deployment). Follow the steps below to build and run the Docker container:
<br>
(This project comes with a **Dockerfile** to easily build and run the Spring Boot application in a containerized environment.)
1.  **Create a Dockerfile in root directory**
    <br> You can refer dockerfile in root dir for details <br> <br>
2. **Build the Docker image**
   ```sh
   docker build -t task-management-api .
   ```
   instead of 'task-management-api' , we can use any name

3. **Run the Docker container**
   ```sh
   docker run -p 8080:8080 task-management-api
   ```

4. **Access the API**
    - API base URL: `http://localhost:8080/v1/tasks`
    - Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## Hosted Application

The application is hosted and accessible at: [mikhi-task-mgmt-java-sp-app](https://mikhi-task-mgmt-java-sp-app.onrender.com/swagger-ui/index.html)
<br> plain-text-link : https://mikhi-task-mgmt-java-sp-app.onrender.com/swagger-ui/index.html
<br>
(a big thanks to [Render](https://render.com/) for providing free hosting )