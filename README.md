# Task Management API

A simple Task Management REST API built with Spring Boot, JPA, and H2 in-memory database. This project allows you to create, read, update, delete, and filter tasks.

## Features

- Create, update, delete, and retrieve tasks
- Filter tasks by status, due date, or both
- Retrieve all tasks or tasks due till a specific date
- Input validation and global exception handling
- In-memory H2 database with sample data
- Swagger/OpenAPI documentation

## Tech Stack

- Java
- Spring Boot
- Spring Data JPA
- H2 Database
- Maven
- Swagger/OpenAPI

## API Endpoints

| Method | Endpoint                  | Description                                 |
|--------|--------------------------|---------------------------------------------|
| POST   | `/v1/tasks`              | Create a new task                           |
| GET    | `/v1/tasks/{id}`         | Get a task by ID                            |
| PUT    | `/v1/tasks/{id}`         | Update a task by ID                         |
| DELETE | `/v1/tasks/{id}`         | Delete a task by ID                         |
| GET    | `/v1/tasks`              | Get all tasks                               |
| GET    | `/v1/tasks/filter`       | Filter tasks by status and/or due date       |
| GET    | `/v1/tasks/till-date`    | Get tasks with due date till a specific date|

## Data Model

- **Task**
  - `id`: Long (auto-generated)
  - `title`: String (required)
  - `description`: String
  - `status`: Enum (`TODO`, `IN_PROGRESS`, `DONE`)
  - `dueDate`: LocalDate

## Database

- Uses H2 in-memory database
- Sample data is loaded on startup (see `src/main/resources/schema.sql`)
- H2 Console available at: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

## API Documentation

- Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
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