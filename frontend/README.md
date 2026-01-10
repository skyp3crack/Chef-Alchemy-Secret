# API Endpoints (Planned)

- POST /api/auth/register - Register a new user
- POST /api/auth/login - Authenticate user and get token
- GET /api/recipes - Get all recipes
- POST /api/recipes - Create a new recipe (Auth required)
- GET /api/recipes/{id} - Get a single recipe
- PUT /api/recipes/{id} - Update a recipe (Auth required, owner only)
- DELETE /api/recipes/{id} - Delete a recipe (Auth required, owner only)
(More to be added for users, categories, comments, etc.)


# Features

## Basic Requirements (Must-Have)

- User Login & Register
- CRUD operations for Recipes
- RESTful API
- Database integration
- Frontend-Backend separation

## Bonus Features (Planned)

- JWT Authentication
- Search/Filter/Sorting for recipes
- File/Image Upload for recipe photos
- Responsive UI
(Future: Recipe rating, commenting, meal planning)

## Future AI Integration Ideas

- Recipe Recommendation Engine
- Dietary Analysis
- Ingredient Substitution Suggestions



# Chef's Notebook Frontend

This is the [Vue] frontend for the Chef's Notebook application.

## Technologies
*   [Vue]
*   JavaScript/TypeScript
*   npm/yarn

## How to Run
1.  Ensure you have Node.js and npm/yarn installed.
2.  Navigate to this directory (`chefs-notebook/frontend`).
3.  Install dependencies:
    ```bash
    npm install # or yarn install
    ```
4.  Start the development server:
    ```bash
    npm run dev # For Vite/Vue
    ```
The frontend will typically run on `http://localhost:5173` (Vite/Vue).
