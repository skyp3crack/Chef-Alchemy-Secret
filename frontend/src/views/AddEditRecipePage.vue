<template>
    <div class="add-edit-recipe-page">
        <RecipeForm />
    </div>
</template>


<script setup lang="ts">
import RecipeForm from '@/components/recipes/RecipeForm.vue';

</script>


<style scoped>
    .add-edit-recipe-page {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding: 20px 0;
}
</style>