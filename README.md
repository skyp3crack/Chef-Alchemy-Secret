# Chef-Alchemy-Secret

A full-stack recipe sharing application built with Spring Boot (Backend) and Vue 3 (Frontend).

## Project Overview

**Chef's Alchemy Secret** is a platform where users can:
*   **Register and Login**: Secure authentication using JWT.
*   **Manage Recipes**: Create, read, update, and delete (CRUD) their own recipes.
*   **Discover**: Browse recipes shared by other users.
*   **Responsive UI**: A clean, responsive interface for all devices.

## Technologies Used

### Backend
*   **Framework**: Spring Boot 3.3.1
*   **Language**: Java 21
*   **Database**: PostgreSQL
*   **Security**: Spring Security + JWT
*   **Build Tool**: Maven

### Frontend
*   **Framework**: Vue 3 (Composition API)
*   **Language**: TypeScript
*   **Build Tool**: Vite
*   **Routing**: Vue Router
*   **HTTP Client**: Axios

## Getting Started

### Prerequisites
*   **Java JDK 21+**
*   **Node.js** (v18 or higher) & **npm**
*   **PostgreSQL** installed and running
*   **Git**

### Installation & Run

1.  **Clone the repository**:
    ```bash
    git clone https://github.com/skyp3crack/Chef-Alchemy-Secret.git
    cd Chef-Alchemy-Secret
    ```

2.  **Setup Backend**:
    *   Go to `backend/src/main/resources/application.properties`.
    *   Update the database `url`, `username`, and `password` to match your local PostgreSQL setup.
    *   Run the backend:
        ```bash
        cd backend
        ./mvnw spring-boot:run
        ```
    *   Server runs at: `http://localhost:8080`

3.  **Setup Frontend**:
    *   Open a new terminal.
    *   Run the frontend:
        ```bash
        cd frontend
        npm install
        npm run dev
        ```
    *   Client runs at: `http://localhost:5173`

## Files to Ignore (Do Not Commit)
*   `node_modules/`
*   `dist/` or `build/`
*   `.env` (local environment variables)
*   `target/` (Maven build output)
*   `.idea/`, `.vscode/` (IDE settings)
*   `.DS_Store` (Mac system files)