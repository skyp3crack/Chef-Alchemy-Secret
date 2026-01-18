# Chef's Alchemy Secret - Backend

The REST API backend for the Chef's Alchemy Secret application.

## Technologies
*   **Java 21**
*   **Spring Boot 3.3.1**
*   **PostgreSQL**
*   **Maven**
*   **Spring Security & JWT**

## Configuration

### Database
Configure your PostgreSQL connection in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5433/chefs_alchemy_db
spring.datasource.username=postgres
spring.datasource.password=your_password
```

### JWT
Review `src/main/resources/application.properties` for JWT secret configurations.

## API Endpoints

### Authentication
*   `POST /api/auth/register` - Register a new user
*   `POST /api/auth/login` - Login and receive JWT

### Recipes
*   `GET /api/recipes` - Get all recipes
*   `GET /api/recipes/{id}` - Get recipe details
*   `POST /api/recipes` - Create a new recipe (Auth required)
*   `PUT /api/recipes/{id}` - Update a recipe (Auth required, Owner only)
*   `DELETE /api/recipes/{id}` - Delete a recipe (Auth required, Owner only)

## Running the Application

Using Maven Wrapper:
```bash
./mvnw spring-boot:run
```

The server will start on port `8080`.
