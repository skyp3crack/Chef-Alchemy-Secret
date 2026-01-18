# Chef's Alchemy Secret - Frontend

The frontend user interface for Chef's Alchemy Secret, built with Vue 3.

## Technologies
*   **Vue 3** (Composition API, `<script setup>`)
*   **TypeScript**
*   **Vite** - Fast build tool
*   **Vue Router** - SPA Routing
*   **Axios** - API requests

## Features
*   User Authentication (Login/Register)
*   Recipe Dashboard (View All)
*   Create, Edit, and Delete Recipes
*   Responsive Design

## Project Setup

### Install Dependencies
```bash
npm install
```

### Compile and Hot-Reload for Development
```bash
npm run dev
```
The application will be available at `http://localhost:5173`.

### Type-Check, Compile and Minify for Production
```bash
npm run build
```

## Structure
*   `src/views/` - Page components (HomePage, RecipeList, etc.)
*   `src/components/` - Reusable components (RecipeCard, RecipeForm)
*   `src/services/` - API integration (auth.service, recipe.service)
*   `src/router/` - Navigation logic
