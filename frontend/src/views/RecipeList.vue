

<template>
    <div class="recipe-list-page">
        <header class="page-header">
            <h1>ALL Recipes</h1>
            <router-link to="/recipes/add" class="btn">+ Add Recipe</router-link>
        </header>

        <div v-if="loading" class="loading-message">loading recipess chilll.....</div>
        <div v-else-if="error" class="error-message">{{ error }}</div>
        <div v-else-if="recipes.length === 0" class="no-recipes">No recipes found</div>
        <div v-else class="recipe-grid">
            <RecipeCard v-for="recipe in recipes" :key="recipe.id" :recipe="recipe" /> 
        </div>
    </div>

</template>


<script setup  lang="ts">
   import {ref,onMounted} from 'vue'; //ref used to create reactive variables while onmounted used to call functions when component is mounted
   import RecipeService, {type Recipe} from '@/services/recipe.service'; //fetch recipe data from api 
   import RecipeCard from '@/components/recipes/RecipeCard.vue'; //display recipe data in cards 

   const recipes = ref<Recipe[]>([]);//array of recipe data
   const loading = ref(true);//true while loading data
   const error = ref<string | null>(null);//error message

   onMounted(async() => {
    try{
        recipes.value = await RecipeService.getAllRecipes();//fetch all recipes

    }catch(err:any){
        error.value = err.message || 'Failed to fetch recipes';
    }finally{
        loading.value = false;
    }
   })
    
    </script>



    <style scoped>
.recipe-list-page {
  max-width: 1200px;
  margin: 40px auto;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 2px 15px rgba(0, 0, 0, 0.08);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  border-bottom: 1px solid #eee;
  padding-bottom: 20px;
}

.page-header h1 {
  font-size: 2.5em;
  color: #333;
  margin: 0;
}

.add-recipe-btn {
  background-color: #28a745;
  color: white;
  padding: 12px 20px;
  border-radius: 5px;
  text-decoration: none;
  font-weight: 600;
  transition: background-color 0.3s ease;
}

.add-recipe-btn:hover {
  background-color: #218838;
}

.recipe-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 25px;
}

.loading-message, .error-message, .no-recipes-message {
  text-align: center;
  font-size: 1.2em;
  color: #666;
  padding: 50px 0;
}

.error-message {
  color: #dc3545;
}
</style>

