


<template>
    <div class="recipe-card">
        <img :src="recipe.imageUrl || '/placeholder-image.jpg'" alt="Recipe Image" class="recipe-image">
        <div class="card-content">
            <h3 class="recipe-title">{{ recipe.title }}</h3>
            <p class="recipe-author">{{ recipe.authorUsername }}</p>
            <p class="recipe-description">{{ truncateDescription(recipe.description) }}</p>
            <div class="card-rating" v-if="recipe.averageRating !== undefined">
            <StarRating :modelValue="recipe.averageRating" :editable="false" />
            <span class="rating-score">{{ recipe.averageRating?.toFixed(1) }}</span>
            <span class="rating-count">({{ recipe.ratingCount || 0 }})</span>
            </div>
            <div class="card-meta" v-if="recipe.tags && recipe.tags.length > 0">
              <span v-for="(tag, index) in recipe.tags.slice(0, 3)" :key="tag" class="meta-tag">
                {{ tag }}<span v-if="index < recipe.tags.slice(0,3).length - 1">, </span>
              </span>
              <span v-if="recipe.tags.length > 3">...</span>
            </div>
            <router-link :to="{ name: 'RecipeDetail', params: { id: recipe.id } }" class="view-details-btn">
        View Details
      </router-link>
        </div>
    </div>
</template>



<script setup lang="ts">

import type { Recipe } from '@/services/recipe.service' //import type Recipe from recipe.ts
import StarRating from '@/components/common/StarRating.vue'
const props = defineProps<{recipe:Recipe}>(); //this prop used to pass recipe data to the component

const truncateDescription = (description:string | undefined) =>{
    if(!description){
        return '';
    }
    return description.substring(0,100) + '...';
}
</script>


<style scoped>
.recipe-card {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  display: flex;
  flex-direction: column;
  transition: transform 0.2s ease-in-out;
}

.recipe-card:hover {
  transform: translateY(-5px);
}

.recipe-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
  display: block;
}

.card-content {
  padding: 15px;
  flex-grow: 1;
  display: flex;
  flex-direction: column;
}

.recipe-title {
  font-size: 1.4em;
  color: #333;
  margin-bottom: 8px;
  line-height: 1.3;
}

.recipe-author {
  font-size: 0.9em;
  color: #666;
  margin-bottom: 10px;
}

.recipe-description {
  font-size: 0.95em;
  color: #555;
  line-height: 1.5;
  margin-bottom: 15px;
  flex-grow: 1;
}
.card-rating {
  display: flex;
  align-items: center;
  margin-bottom: 10px; /* Added margin-bottom */
  font-size: 0.9em;
  color: #777;
}

.card-rating .star-rating {
  font-size: 1.2em; /* Adjust star size in card */
  margin-right: 5px;
}

.card-rating .rating-score {
  font-weight: 600;
  color: #333;
  margin-right: 3px;
}

.card-rating .rating-count {
  font-size: 0.85em;
  color: #999;
}

.card-meta {
  margin-top: 5px;
  margin-bottom: 10px;
  font-size: 0.8em;
  color: #777;
}

.meta-tag {
  background-color: #e9ecef;
  padding: 3px 8px;
  border-radius: 10px;
  margin-right: 5px;
  white-space: nowrap;
}

.view-details-btn {
  display: inline-block;
  background-color: #007bff;
  color: white;
  padding: 10px 15px;
  border-radius: 5px;
  text-align: center;
  text-decoration: none;
  font-weight: 600;
  transition: background-color 0.3s ease;
  margin-top: auto; /* Pushes button to the bottom */
}

.view-details-btn:hover {
  background-color: #0056b3;
}
</style>