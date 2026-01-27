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
        <label for="image">Recipe Image:</label>
        <input type="file" id="image" @change="handleImageUpload" accept="image/*" />
        <div v-if="imagePreviewUrl" class="image-preview">
          <img :src="imagePreviewUrl" alt="Image Preview" />
          <button type="button" @click="removeImage" class="remove-image-btn">Remove Image</button>
        </div>
        <p v-else-if="recipe.imageUrl" class="current-image-text">
          Current Image: <a :href="recipe.imageUrl" target="_blank">View</a>
          <button type="button" @click="removeImage" class="remove-image-btn">Remove Current Image</button>
        </p>
      </div>

      <div class="form-group">
        <label for="categories">Categories:</label>
        <select id="categories" v-model="selectedCategoryIds" multiple>
          <option v-for="category in availableCategories" :key="category.id" :value="category.id">
            {{ category.name }}
          </option>
        </select>
        <p v-if="categoryError" class="error-message">{{ categoryError }}</p>
      </div>

      <div class="form-group">
        <label for="tags">Tags:</label>
        <select id="tags" v-model="selectedTagIds" multiple>
          <option v-for="tag in availableTags" :key="tag.id" :value="tag.id">
            {{ tag.name }}
          </option>
        </select>
        <p v-if="tagError" class="error-message">{{ tagError }}</p>
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
    import RecipeService ,{ type Recipe, type Category, type Tag} from '@/services/recipe.service';
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
            imageUrl: '',
            imageFile: null, //file object for new image upload
        })

        const availableCategories = ref<Category[]>([]) 
        const availableTags = ref<Tag[]>([])           
        const selectedCategoryIds = ref<number[]>([])   
        const selectedTagIds = ref<number[]>([])       
        const categoryError = ref<string | null>(null) 
        const tagError = ref<string | null>(null)       
    
        const loading = ref(false);
        const message = ref('');
        const isSuccess = ref(false);

        const imagePreviewUrl = ref<string | null>(null) //for displaying selected file

onMounted(async () => {
  // Fetch categories and tags
  try {
    availableCategories.value = await RecipeService.getAllCategories()
  } catch (err: any) {
    categoryError.value = err.message || 'Failed to load categories.'
  }
  try {
    availableTags.value = await RecipeService.getAllTags()
  } catch (err: any) {
    tagError.value = err.message || 'Failed to load tags.'
  }

  if (route.params.id) {
    isEditMode.value = true
    recipeId.value = Number(route.params.id)
    try {
      const fetchedRecipe = await RecipeService.getRecipeById(recipeId.value)
      recipe.value = fetchedRecipe
      //image preview
      if(fetchedRecipe.imageUrl){
        imagePreviewUrl.value = fetchedRecipe.imageUrl
      }

      // Pre-select categories and tags for editing
      selectedCategoryIds.value = availableCategories.value
        .filter(cat => fetchedRecipe.categories?.some(c => c.name === cat.name))
        .map(cat => cat.id)
      selectedTagIds.value = availableTags.value
        .filter(tag => fetchedRecipe.tags?.some(t => t.name === tag.name))
        .map(tag => tag.id)
    } catch (err: any) {
      message.value = err.message || 'Failed to load recipe for editing.'
      isSuccess.value = false
    }
  }
})

    const handleImageUpload = (event:Event)=>{
      const target = event.target as HTMLInputElement;
      if(target.files && target.files[0]){
        const file = target.files[0]; //get the file
        recipe.value.imageFile = file;
        imagePreviewUrl.value = URL.createObjectURL(file); // Create a URL for preview
        recipe.value.imageUrl = undefined; // Clear existing URL if new file is selected
      } else {
        recipe.value.imageFile = null;
        imagePreviewUrl.value = recipe.value.imageUrl || null; // Revert to existing or null
      }
    };


    const removeImage = () => {
    recipe.value.imageFile = null;
    recipe.value.imageUrl = null; // Explicitly set to null to signal backend to remove
    imagePreviewUrl.value = null;
    // Clear the file input visually if possible (depends on browser/framework)
    const fileInput = document.getElementById('image') as HTMLInputElement;
    if (fileInput) {
      fileInput.value = '';
    }
  };

const handleSubmit = async () => {
  loading.value = true
  message.value = ''
  isSuccess.value = false

  try {
    // Assign selected IDs to the recipe object before sending
    recipe.value.categoryIds = selectedCategoryIds.value
    recipe.value.tagIds = selectedTagIds.value

    if (isEditMode.value && recipeId.value) {
      await RecipeService.updateRecipe(recipe.value)
      message.value = 'Recipe updated successfully!'
      isSuccess.value = true
    } else {
      await RecipeService.createRecipe(recipe.value)
      message.value = 'Recipe added successfully!'
      isSuccess.value = true
      // Clear form after successful creation
      recipe.value = { title: '', description: '', ingredients: '', instructions: '', imageUrl: '', imageFile: null }
      selectedCategoryIds.value = []
      selectedTagIds.value = []
      imagePreviewUrl.value = null; //clear image preview
      const fileInput = document.getElementById('image') as HTMLInputElement;
      if (fileInput) {
        fileInput.value = '';
      }
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
textarea, select { 
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 5px;
  font-size: 1em;
  box-sizing: border-box;
  transition: border-color 0.3s ease;
}

input:focus,
textarea:focus, select:focus {
  outline: none;
  border-color: #007bff;
}

textarea {
  resize: vertical;
}

select[multiple] { /* NEW: Style for multiple select */
  min-height: 100px; /* Ensure multiple select is tall enough */
}

.image-preview {
  margin-top: 15px;
  text-align: center;
}

.image-preview img {
  max-width: 100%;
  height: 200px;
  object-fit: contain; /* Use contain to show full image without cropping */
  border: 1px solid #eee;
  border-radius: 5px;
  margin-bottom: 10px;
}
.current-image-text {
  margin-top: 15px;
  font-size: 0.9em;
  color: #666;
  display: flex;
  align-items: center;
  gap: 10px;
}

.current-image-text a {
  color: #007bff;
  text-decoration: none;
}

.remove-image-btn {
  background-color: #dc3545;
  color: white;
  border: none;
  padding: 8px 12px;
  border-radius: 5px;
  cursor: pointer;
  font-size: 0.9em;
  transition: background-color 0.3s ease;
}

.remove-image-btn:hover {
  background-color: #c82333;
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