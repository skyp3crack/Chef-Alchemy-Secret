

<template>
    <div class="recipe-list-page">
        <header class="page-header">
            <h1>ALL Recipes</h1>
            <router-link to="/recipes/add" class="btn">+ Add Recipe</router-link>
        </header>
        
        <div class="filter-section">
          <input type="text" v-model="searchKeyword" placeholder="Search by title, ingredients, or instructions..."
          @input="debouncedFetchRecipes" class="search-input" />

          <select v-model="selectedCategory" class="filter-select" @change="fetchRecipes">
            <option :value="null">All Categories</option>
            <option v-for="category in availableCategories" :key="category.id" :value="category.id">{{ category.name }}</option>
          </select>

          <select v-model="selectedTag" class="filter-select" @change="fetchRecipes">
            <option :value="null">All Tags</option>
            <option v-for="tag in availableTags" :key="tag.id" :value="tag.id">{{ tag.name }}</option>
          </select>
        </div>

        <div v-if="loading" class="loading-message">loading recipess chilll.....</div>
        <div v-else-if="error" class="error-message">{{ error }}</div>
        <div v-else-if="recipes.length === 0" class="no-recipes-message">No recipes found</div>
        <div v-else class="recipe-grid">
            <RecipeCard v-for="recipe in recipes" :key="recipe.id" :recipe="recipe" /> 
        </div>
    </div>

</template>


<script setup  lang="ts">
   import {ref,onMounted} from 'vue'; //ref used to create reactive variables while onmounted used to call functions when component is mounted
   import RecipeService, {type Recipe, type Tag, type Category} from '@/services/recipe.service'; //fetch recipe data from api 
   import RecipeCard from '@/components/recipes/RecipeCard.vue'; //display recipe data in cards 

   const recipes = ref<Recipe[]>([]);//array of recipe data
   const loading = ref(true);//true while loading data
   const error = ref<string | null>(null);//error message

   const searchKeyword = ref<string>('');
   const selectedCategory = ref<number | null>(null)
   const availableCategories = ref<Category[]>([]);
   const availableTags = ref<Tag[]>([]);
   const selectedTag = ref<number | null>(null);

    let debounceTimer: number | null = null; //debounce timer to prevent multiple requests
    
    const fetchRecipes = async() =>{
      loading.value = true;
      error.value=null;
      try{
        const categoryIds = selectedCategory.value ? [selectedCategory.value] : undefined; //if selectedCategory is not null then create an array with selectedCategory value
        const tagIds = selectedTag.value ? [selectedTag.value] : undefined; //if selectedTag is not null then create an array with selectedTag value
        recipes.value = await RecipeService.getAllRecipes(searchKeyword.value, categoryIds,tagIds); //fetch recipes based on search keyword, categoryIds and tagIds
      }catch(err:any){
        error.value = err.message || 'An unknown error occurred while fetching recipes.';
      }finally{
        loading.value = false;
      }
    }

    const debouncedFetchRecipes = () =>{ //after create debounceTimer then we clear it using clearTimeout in this function
      if(debounceTimer){
        clearTimeout(debounceTimer);
      }
      debounceTimer = setTimeout(() =>{
        fetchRecipes();
      }, 300);
    };

   onMounted(async() => {
    try{
      availableCategories.value = await RecipeService.getAllCategories();
      availableTags.value = await RecipeService.getAllTags();
    }catch(err:any){
        error.value = err.message || 'Failed to load filter options', console.error(err) ;
    }await fetchRecipes();
    }
   )
    
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

.filter-section{
  display:flex;
  gap:15px;
  margin-bottom:30px;
  padding: 15px;
  background-color: #e9ecef;
  border-radius: 8px;
  flex-wrap: wrap;
  align-items: center; /*align items in center*/

}

.search-input{
  flex-grow: 1; /* Allows search input to take available space */
  padding: 10px 15px;
  border: 1px solid #ced4da;
  border-radius: 5px;
  font-size: 1em;
  min-width: 200px; /* Ensure search input is not too small */

}

.filter-select {
  padding: 10px 15px;
  border: 1px solid #ced4da;
  border-radius: 5px;
  background-color: white;
  font-size: 0.9em;
  cursor: pointer;
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

