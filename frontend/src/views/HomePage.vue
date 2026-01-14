<template>
  <div class="home-container">
    <h1>Welcome to Chef's Alchemy Secret</h1>
    <p>Your personal recipe sharing platform</p>
    <router-link to="/register" class="btn">Get Started - Register</router-link>
  </div>
</template>

<script setup lang="ts">
// Home page logic will go here
</script>

<style scoped>
.home-container {
  text-align: center;
  padding: 50px 20px;
}

h1 {
  color: #2c3e50;
  margin-bottom: 20px;
}

p {
  color: #666;
  margin-bottom: 30px;
}

.btn {
  display: inline-block;
  padding: 12px 24px;
  background-color: #007bff;
  color: white;
  text-decoration: none;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.btn:hover {
  background-color: #0056b3;
}
</style>