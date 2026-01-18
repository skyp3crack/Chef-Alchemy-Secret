<template>
     <div class="recipe-detail-page">
    <div v-if="loading" class="loading-message">Loading recipe...</div>
    <div v-else-if="error" class="error-message">{{ error }}</div>
    <div v-else-if="recipe" class="recipe-content">
      <img :src="recipe.imageUrl || '/placeholder-recipe.jpg'" alt="Recipe Image" class="recipe-detail-image" />
      
      <h1 class="recipe-detail-title">{{ recipe.title }}</h1>
      <p class="recipe-detail-author">By: {{ recipe.authorUsername }}</p>
      <p class="recipe-detail-date">Created: {{ formatDate(recipe.createdAt) }}</p>

      <div class="recipe-section">
        <h2>Description</h2>
        <p>{{ recipe.description }}</p>
      </div>

      <div class="recipe-section">
        <h2>Ingredients</h2>
        <pre class="recipe-pre">{{ recipe.ingredients }}</pre>
      </div>

      <div class="recipe-section">
        <h2>Instructions</h2>
        <pre class="recipe-pre">{{ recipe.instructions }}</pre>
      </div>

      <div class="action-buttons" v-if="isAuthor">
        <router-link :to="{ name: 'EditRecipe', params: { id: recipe.id } }" class="edit-btn">Edit Recipe</router-link>
        <button @click="confirmDelete" class="delete-btn">Delete Recipe</button>
      </div>
    </div>
  </div>

</template>



<script setup lang="ts">
    import {ref,onMounted,computed} from 'vue';
    import RecipeService, {type Recipe} from '@/services/recipe.service';
    import RecipeCard from '@/components/recipes/RecipeCard.vue';
    import {useRoute,useRouter} from 'vue-router';
import authService from '@/services/auth.service';

    const route = useRoute();
    const router = useRouter();

    const recipe = ref<Recipe | null>(null);
    const loading = ref(true);
    const error = ref<string | null>(null);

    const currentUserId = computed(() => authService.getCurrentUser()?.id) //get current user id , computed used to get value from ref
    const isAuthor = computed(() => recipe.value?.authorId === currentUserId.value) //check if current user is author


    onMounted(async ()=>{
        const recipeId = Number(route.params.id);
        if(isNaN(recipeId)){
            error.value = 'Invalid recipe ID';
            loading.value = false;
            return;
        }
        try{
        const response = await RecipeService.getRecipeById(recipeId);
        recipe.value = response;
     }catch(err:any){
        error.value = err.message;
     }finally{
        loading.value = false;
     }
    })


    const formatDate = (dateString: string | undefined) => {
        if (!dateString) return 'N/A'
        const options: Intl.DateTimeFormatOptions = { year: 'numeric', month: 'long', day: 'numeric' }
        return new Date(dateString).toLocaleDateString(undefined, options)
    }
    

        const confirmDelete = async () => {
        if (confirm('Are you sure you want to delete this recipe? This action cannot be undone.')) {
        try {
        if (recipe.value?.id) {
            await RecipeService.deleteRecipe(recipe.value.id)
            alert('Recipe deleted successfully!')
            router.push('/recipes') // Redirect to recipe list
        }
        } catch (err: any) {
        alert(`Error deleting recipe: ${err.message}`)
        }
    }
    }

</script>

<style scoped>
.recipe-detail-page {
  max-width: 900px;
  margin: 40px auto;
  padding: 30px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 15px rgba(0, 0, 0, 0.08);
}

.recipe-detail-image {
  width: 100%;
  height: 400px;
  object-fit: cover;
  border-radius: 8px;
  margin-bottom: 30px;
}

.recipe-detail-title {
  font-size: 3em;
  color: #333;
  margin-bottom: 10px;
  text-align: center;
}

.recipe-detail-author, .recipe-detail-date {
  font-size: 1em;
  color: #777;
  text-align: center;
  margin-bottom: 5px;
}

.recipe-section {
  margin-top: 30px;
  padding-top: 20px;
  border-top: 1px solid #eee;
}

.recipe-section h2 {
  font-size: 1.8em;
  color: #007bff;
  margin-bottom: 15px;
}

.recipe-section p, .recipe-pre {
  font-size: 1.1em;
  line-height: 1.7;
  color: #444;
  white-space: pre-wrap; /* Preserve whitespace and wrap text */
  word-wrap: break-word;
}

.recipe-pre {
  background-color: #f8f8f8;
  border: 1px solid #ddd;
  padding: 15px;
  border-radius: 5px;
}

.action-buttons {
  margin-top: 40px;
  text-align: center;
}

.edit-btn, .delete-btn {
  display: inline-block;
  padding: 12px 25px;
  border-radius: 5px;
  font-weight: 600;
  text-decoration: none;
  margin: 0 10px;
  transition: background-color 0.3s ease;
}

.edit-btn {
  background-color: #007bff;
  color: white;
}

.edit-btn:hover {
  background-color: #0056b3;
}

.delete-btn {
  background-color: #dc3545;
  color: white;
  border: none;
  cursor: pointer;
}

.delete-btn:hover {
  background-color: #c82333;
}

.loading-message, .error-message {
  text-align: center;
  font-size: 1.2em;
  color: #666;
  padding: 50px 0;
}

.error-message {
  color: #dc3545;
}
</style>