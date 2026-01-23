<template>
     <div class="recipe-detail-page">
    <div v-if="loading" class="loading-message">Loading recipe...</div>
    <div v-else-if="error" class="error-message">{{ error }}</div>
    <div v-else-if="recipe" class="recipe-content">
      <img :src="recipe.imageUrl || '/placeholder-recipe.jpg'" alt="Recipe Image" class="recipe-detail-image" />
      
      <h1 class="recipe-detail-title">{{ recipe.title }}</h1>
      <p class="recipe-detail-author">By: {{ recipe.authorUsername }}</p>
      <p class="recipe-detail-date">Created: {{ formatDate(recipe.createdAt) }}</p>


      <div class="average-rating-section">
        <StarRating :modelValue="recipe.averageRating || 0" :editable="false" />
        <span class="rating-text">
          {{ recipe.averageRating?.toFixed(1) || '0.0' }} ({{ recipe.ratingCount || 0 }} ratings)
        </span>
      </div>

      <div class="recipe-meta" v-if="recipe.categories && recipe.categories.length>0">
        <strong>Categories:</strong>
        <span v-for="category in recipe.categories" :key="category" class="category-tag">
            {{ category }}
        </span>
      </div>

       <div class="recipe-meta" v-if="recipe.tags && recipe.tags.length > 0">
        <strong>Tags:</strong>
        <span v-for="tag in recipe.tags" :key="tag" class="meta-item category-tag">
          {{ tag }}
        </span>
      </div>

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

      <div class="user-rating-section" v-if="isLoggedIn && !isAuthor">
        <h3>Your rating:</h3>
        <StarRating v-model="myRating" :editable="true" @update:modelValue="handleRatingChange"/>
        <p v-if="ratingMessage" :class="{'success-message': isRatingSuccess, 'error-message': !isRatingSuccess}">
          {{ ratingMessage }}
        </p>
      </div>

      <div class="reviews-section">
        <h2>Reviews ({{ recipe.reviews?.length || 0 }})</h2>
        <div v-if="isLoggedIn && !isAuthor" class="add-review-form">
          <h3>Add Your Review:</h3>
          <textarea v-model="newReviewComment" rows="4" placeholder="Write your review here..."></textarea>
          <button @click="submitReview" :disabled="reviewLoading">
            <span v-if="reviewLoading">Submitting...</span>
            <span v-else>Submit Review</span>
          </button>
          <p v-if="reviewMessage" :class="{'success-message': isReviewSuccess, 'error-message': !isReviewSuccess}">
            {{ reviewMessage }}
          </p>
        </div>

         <div v-if="recipe.reviews && recipe.reviews.length > 0" class="review-list">
          <div v-for="review in recipe.reviews" :key="review.id" class="review-item">
            <p class="review-author">{{ review.username }}</p>
            <p class="review-date">{{ formatDate(review.createdAt) }}</p>
            <p class="review-comment">{{ review.comment }}</p>
            <div class="review-actions" v-if="review.userId === currentUserId">
              <button @click="startEditReview(review)" class="edit-review-btn">Edit</button>
              <button @click="confirmDeleteReview(review.id!)" class="delete-review-btn">Delete</button>
            </div>
            <div v-if="editingReviewId === review.id" class="edit-review-form">
              <textarea v-model="editingReviewComment" rows="3"></textarea>
              <button @click="saveEditReview(review.id!)">Save</button>
              <button @click="cancelEditReview">Cancel</button>
            </div>
          </div>
        </div>
        <p v-else class="no-reviews-message">No reviews yet. Be the first to leave one!</p>
      </div>
    </div>
  </div>

</template>



<script setup lang="ts">
    import {ref,onMounted,computed} from 'vue';
    import RecipeService, { type Recipe, type Review } from '@/services/recipe.service';
    import {useRoute,useRouter} from 'vue-router';
    import authService from '@/services/auth.service';
    import StarRating from '@/components/common/StarRating.vue';

    const route = useRoute();
    const router = useRouter();

    const recipe = ref<Recipe | null>(null);
    const loading = ref(true);
    const error = ref<string | null>(null);

    const currentUserId = computed(() => authService.getCurrentUser()?.id) //get current user id , computed used to get value from ref
    const isLoggedIn = computed(() => !!authService.getCurrentUser())
    const isAuthor = computed(() => recipe.value?.authorId === currentUserId.value) //check if current user is author

    const myRating = ref(0)
    const ratingMessage = ref('')
    const isRatingSuccess = ref(false)
    const newReviewComment = ref('')

    const reviewLoading = ref(false)
    const reviewMessage = ref('')
    const isReviewSuccess = ref(false)
    const editingReviewId = ref<number | null>(null)
    const editingReviewComment = ref('')

    const fetchRecipeDetails = async () => {
        const recipeId = Number(route.params.id);
        if(isNaN(recipeId)){
            error.value = 'Invalid recipe ID';
            loading.value = false;
            return;
        }
        // Only show full page loading on first load
        if (!recipe.value) {
            loading.value = true;
        }
        
        try{
            const response = await RecipeService.getRecipeById(recipeId);
            recipe.value = response;

            // Fetch user's rating if logged in
            if (isLoggedIn.value) {
                try {
                    const rating = await RecipeService.getUserRatingForRecipe(recipeId);
                    myRating.value = rating;
                } catch (e) {
                    // Ignore error if no rating exists
                    myRating.value = 0;
                }
            }
        }catch(err:any){
            error.value = err.message;
        }finally{
            loading.value = false;
        }
    }

    onMounted(async ()=>{
        await fetchRecipeDetails();
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

  const handleRatingChange = async (newScore: number) => {
      if (!recipe.value?.id) return;
      ratingMessage.value = '';
      isRatingSuccess.value = false;
      try {
        await RecipeService.submitRating(recipe.value.id, newScore);
        ratingMessage.value = 'Rating submitted successfully!';
        isRatingSuccess.value = true;
        await fetchRecipeDetails(); // Refresh recipe details to show updated average rating
      } catch (err: any) {
        ratingMessage.value = err.message || 'Failed to submit rating.';
    isRatingSuccess.value = false;
  }
};

const submitReview = async () =>{
  if(!recipe.value?.id || !newReviewComment.value.trim()){
    reviewMessage.value = 'Review cannot be empty.';
    isReviewSuccess.value = false;
    return;
  }
  reviewLoading.value = true;
  reviewMessage.value = '';
  isReviewSuccess.value = false;

  try{
    await RecipeService.addReview(recipe.value.id, newReviewComment.value);
    newReviewComment.value = '';
    reviewMessage.value = 'Review submitted successfully!';
    isReviewSuccess.value = true;
    await fetchRecipeDetails();
    
    }catch(error:any){
      reviewMessage.value = error.message || 'Failed to add review.';
      isReviewSuccess.value = false;
    }finally{
      reviewLoading.value = false;
    }
  }
  const startEditReview = (review: Review) => {
  editingReviewId.value = review.id!;
  editingReviewComment.value = review.comment;
};

const cancelEditReview = () => {
  editingReviewId.value = null;
  editingReviewComment.value = '';
};

const saveEditReview = async (reviewId: number) => {
  if (!recipe.value?.id || !editingReviewComment.value.trim()) {
    reviewMessage.value = 'Review cannot be empty.';
    isReviewSuccess.value = false;
    return;
  }
  reviewLoading.value = true;
  reviewMessage.value = '';
  isReviewSuccess.value = false;

  try {
    await RecipeService.updateReview(recipe.value.id, reviewId, editingReviewComment.value);
    reviewMessage.value = 'Review updated successfully!';
    isReviewSuccess.value = true;
    cancelEditReview();
    await fetchRecipeDetails(); // Refresh recipe details
  } catch (err: any) {
    reviewMessage.value = err.message || 'Failed to update review.';
    isReviewSuccess.value = false;
  } finally {
    reviewLoading.value = false;
  }
};

const confirmDeleteReview = async (reviewId: number) => {
  if (confirm('Are you sure you want to delete this review?')) {
    if (!recipe.value?.id) return;
    try {
      await RecipeService.deleteReview(recipe.value.id, reviewId);
      reviewMessage.value = 'Review deleted successfully!';
      isReviewSuccess.value = true;
      await fetchRecipeDetails(); // Refresh recipe details
    } catch (err: any) {
      reviewMessage.value = err.message || 'Failed to delete review.';
      isReviewSuccess.value = false;
    }
  }
};


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

.average-rating-section {
  text-align: center;
  margin-top: 15px;
  margin-bottom: 20px;
  font-size: 1.2em;
  color: #555;
}

.rating-text {
  margin-left: 10px;
  font-weight: 600;
}

.recipe-meta {
  text-align: center;
  margin-top: 15px;
  margin-bottom: 15px;
  font-size: 0.95em;
  color: #555;
}

.recipe-meta strong {
  margin-right: 8px;
  color: #333;
}

.meta-item {
  display: inline-block;
  background-color: #e9ecef;
  color: #495057;
  padding: 5px 10px;
  border-radius: 15px;
  margin: 0 5px 5px 0;
  font-size: 0.85em;
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

.user-rating-section {
  margin-top: 40px;
  padding-top: 20px;
  border-top: 1px solid #eee;
  text-align: center;
}
.user-rating-section h3 {
  font-size: 1.5em;
  color: #333;
  margin-bottom: 15px;
}

/* NEW: Reviews Section */
.reviews-section {
  margin-top: 40px;
  padding-top: 20px;
  border-top: 1px solid #eee;
}

.reviews-section h2 {
  font-size: 1.8em;
  color: #007bff;
  margin-bottom: 20px;
}

.add-review-form {
  background-color: #f8f8f8;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 30px;
}

.add-review-form h3 {
  font-size: 1.4em;
  color: #333;
  margin-bottom: 15px;
}

.add-review-form textarea {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
  margin-bottom: 15px;
  box-sizing: border-box;
  resize: vertical;
}

.add-review-form button {
  background-color: #28a745;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.add-review-form button:hover {
  background-color: #218838;
}

.review-list {
  margin-top: 20px;
}

.review-item {
  background-color: #fdfdfd;
  border: 1px solid #eee;
  border-radius: 8px;
  padding: 15px;
  margin-bottom: 15px;
  box-shadow: 0 1px 5px rgba(0, 0, 0, 0.05);
}

.review-author {
  font-weight: 600;
  color: #333;
  margin-bottom: 5px;
}

.review-date {
  font-size: 0.85em;
  color: #777;
  margin-bottom: 10px;
}

.review-comment {
  color: #555;
  line-height: 1.6;
}

.review-actions {
  margin-top: 10px;
  text-align: right;
}

.edit-review-btn, .delete-review-btn {
  background: none;
  border: none;
  color: #007bff;
  cursor: pointer;
  font-size: 0.9em;
  margin-left: 10px;
  transition: color 0.3s ease;
}

.delete-review-btn {
  color: #dc3545;
}

.edit-review-btn:hover {
  text-decoration: underline;
  color: #0056b3;
}

.delete-review-btn:hover {
  text-decoration: underline;
  color: #c82333;
}

.edit-review-form {
  margin-top: 10px;
  padding-top: 10px;
  border-top: 1px dashed #eee;
}

.edit-review-form textarea {
  width: 100%;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
  margin-bottom: 10px;
  box-sizing: border-box;
}

.edit-review-form button {
  background-color: #007bff;
  color: white;
  padding: 8px 15px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-right: 10px;
}

.edit-review-form button:last-child {
  background-color: #6c757d;
}

.edit-review-form button:hover {
  opacity: 0.9;
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