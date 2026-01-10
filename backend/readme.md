
# Chef's Alchemy Backend

This is the Spring Boot backend for the Chef's Alechemy application.

## Technologies
*   Spring Boot [version]
*   Java [version]
*   Database: [PostgreSQL]
*   Maven

## How to Run
1.  Ensure you have Java JDK [version] installed.
2.  Ensure your [PostgreSQL] instance is running.
3.  Update `src/main/resources/application.properties` (or `application.yml`) with your database connection details.
4.  Navigate to this directory (`chefs-alchemy-secret/backend`).
5.  Run the application:
    ```bash
    ./mvnw spring-boot:run  # For Maven
    ```
The backend will typically run on `http://localhost:8080`.
