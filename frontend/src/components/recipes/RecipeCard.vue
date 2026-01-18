


<template>
    <div class="recipe-card">
        <img :src="recipe.imageUrl || '/placeholder-image.jpg'" alt="Recipe Image" class="recipe-image">
        <div class="card-content">
            <h3 class="recipe-title">{{ recipe.title }}</h3>
            <p class="recipe-author">{{ recipe.authorUsername }}</p>
            <p class="recipe-description">{{ truncateDescription(recipe.description) }}</p>
            <router-link :to="{ name: 'RecipeDetail', params: { id: recipe.id } }" class="view-details-btn">
        View Details
      </router-link>
        </div>
    </div>
</template>



<script setup lang="ts">
import type { Recipe } from '@/services/recipe.service' //import type Recipe from recipe.ts

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