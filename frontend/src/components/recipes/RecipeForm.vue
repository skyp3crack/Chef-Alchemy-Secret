<template>
    <div class="recipe-form-container">
    <h2>{{ isEditMode ? 'Edit Recipe' : 'Add New Recipe' }}</h2>
    <form @submit.prevent="handleSubmit">
      <div class="form-group">
        <label for="title">Title:</label>
        <input type="text" id="title" v-model="recipe.title" required />
      </div>

      <div class="form-group">
        <label for="description">Description:</label>
        <textarea id="description" v-model="recipe.description" rows="4" required></textarea>
      </div>

      <div class="form-group">
        <label for="ingredients">Ingredients (one per line or comma-separated):</label>
        <textarea id="ingredients" v-model="recipe.ingredients" rows="6" required></textarea>
      </div>

      <div class="form-group">
        <label for="instructions">Instructions (step-by-step):</label>
        <textarea id="instructions" v-model="recipe.instructions" rows="8" required></textarea>
      </div>

      <div class="form-group">
        <label for="imageUrl">Image URL (optional):</label>
        <input type="url" id="imageUrl" v-model="recipe.imageUrl" />
      </div>

      <button type="submit" :disabled="loading">
        <span v-if="loading">{{ isEditMode ? 'Saving...' : 'Adding...' }}</span>
        <span v-else>{{ isEditMode ? 'Update Recipe' : 'Add Recipe' }}</span>
      </button>

      <div v-if="message" :class="{'success-message': isSuccess, 'error-message': !isSuccess}">
        {{ message }}
      </div>
    </form>
  </div>
</template>




<script setup lang="ts">
    import {ref, onMounted} from 'vue';
    import RecipeService ,{ type Recipe} from '@/services/recipe.service';
    import {useRoute, useRouter} from 'vue-router';

    const route = useRoute();
    const router = useRouter();

    const isEditMode = ref(false);
    const recipeId = ref<number | null>(null);
    
        const recipe = ref<Recipe>({
            title: '',
            description: '',
            ingredients: '',
            instructions: '',
            imageUrl: ''
        })
    
        const loading = ref(false);
        const message = ref('');
        const isSuccess = ref(false);

    onMounted(async()=>{
        if(route.params.id){ //check if there is an id in the route
            isEditMode.value = true; //set isEditMode to true if there is an id
            recipeId.value = Number(route.params.id); //convert id to number for type safety  
            
            try{
                const fetchedRecipe = await RecipeService.getRecipeById(recipeId.value);
                recipe.value = fetchedRecipe;
            }catch(err:any){
                message.value = err.message || 'Failed to load recipe';
                isSuccess.value = false;
            }finally{
                loading.value = false;
            }
        }
    })

    const handleSubmit = async () => {
    loading.value = true;
    message.value = '';
    isSuccess.value = false;
    try {
    if (isEditMode.value && recipeId.value) {
      await RecipeService.updateRecipe(recipe.value)
      message.value = 'Recipe updated successfully!'
      isSuccess.value = true
    } else {
      await RecipeService.createRecipe(recipe.value)
      message.value = 'Recipe added successfully!'
      isSuccess.value = true
      // Clear form after successful creation
      recipe.value = { title: '', description: '', ingredients: '', instructions: '', imageUrl: '' }
    }
    // Redirect after a short delay
    setTimeout(() => {
        router.push('/recipes')
        }, 1500)
    } catch (err: any) {
        message.value = err.message || 'An error occurred.'
        isSuccess.value = false
    } finally {
        loading.value = false
    }
}
    
</script>



<style scoped>
.recipe-form-container {
  max-width: 800px;
  margin: 40px auto;
  padding: 30px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 15px rgba(0, 0, 0, 0.08);
}

h2 {
  text-align: center;
  font-size: 2.2em;
  color: #333;
  margin-bottom: 30px;
}

.form-group {
  margin-bottom: 20px;
}

label {
  display: block;
  margin-bottom: 8px;
  font-weight: 600;
  color: #555;
  font-size: 1.1em;
}

input[type="text"],
input[type="url"],
textarea {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 5px;
  font-size: 1em;
  box-sizing: border-box;
  transition: border-color 0.3s ease;
}

input:focus,
textarea:focus {
  outline: none;
  border-color: #007bff;
}

textarea {
  resize: vertical;
}

button[type="submit"] {
  width: 100%;
  padding: 15px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  font-size: 1.1em;
  font-weight: 600;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

button[type="submit"]:hover:not(:disabled) {
  background-color: #0056b3;
}

button[type="submit"]:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}

.success-message {
  margin-top: 20px;
  padding: 12px;
  background-color: #d4edda;
  color: #155724;
  border-radius: 4px;
  text-align: center;
  font-weight: 500;
}

.error-message {
  margin-top: 20px;
  padding: 12px;
  background-color: #f8d7da;
  color: #721c24;
  border-radius: 4px;
  text-align: center;
  font-weight: 500;
}
</style>