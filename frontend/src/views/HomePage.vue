<template>
  <div class="home-container">
    <header class="header">
    <h1>Welcome to Chef's Alchemy Secret</h1>
    <nav class="main-nav">
    <router-link to="/recipes" class="nav-link">Recipes</router-link>
    <router-link to="recipes/add" class="nav-link">Add Recipe</router-link>
    </nav>
    <div class="user-info" v-if="user">
      <span>{{ user.username }}!</span>
      <button @click="handleLogout" class="logout-btn">Logout</button>
    </div>
    <div class="user-info" v-else>
      <p>Please login or register to access this page</p>
      <router-link to="/login" class="btn">Login</router-link>
      <router-link to="/register" class="btn">Get Started - Register</router-link>
    </div>
    </header>
    <p>Your personal recipe sharing platform</p>

    <main class="main-content">
      <h2>Making your cooking journey easier</h2>
      <p>With Chef's Alchemy Secret, you can easily save and share your favorite recipes with others.</p>
    <div class="quick-actions">
        
      <router-link to="/recipes" class="action-card">
          <h3>📝 My Recipes</h3>
          <p>View and manage your recipes</p>
        </router-link>
        <router-link to="/recipes/add" class="action-card">
          <h3>➕ Add Recipe</h3>
          <p>Create a new recipe</p>
        </router-link>
        <router-link to="/recipes" class="action-card">
          <h3>🔍 Explore</h3>
          <p>Discover new recipes</p>
        </router-link>
      </div>
      </main>
  </div>
</template>

<script setup lang="ts">
import { ref , onMounted } from 'vue';
import { useRouter } from 'vue-router';
import AuthService, {type UserData} from '@/services/auth.service';
const router = useRouter();
const user = ref<UserData | null>(null); //ref<UserData meaning user data type and null>(null) null value no user data

onMounted(()=>{
  user.value = AuthService.getCurrentUser();//get current user data
})

  const handleLogout = () =>{
    AuthService.logout();
    user.value = null;
    router.push('/login');
  }
</script>

<style scoped>
.home-container {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.header {
  background-color: #007bff;
  color: white;
  padding: 20px 40px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 5px rgba(0,0,0,0.1);
}

.header h1 {
  margin: 0;
  font-size: 24px;
}

.main-nav {
  display: flex;
  gap: 20px;
}

.nav-link {
  color: white;
  text-decoration: none;
  font-weight: 600;
  padding: 5px 10px;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.nav-link:hover {
  background-color: rgba(255, 255, 255, 0.2);
}

.user-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

.logout-btn, .btn-link {
  padding: 8px 16px;
  background-color: white;
  color: #007bff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-weight: 600;
  transition: background-color 0.3s, color 0.3s;
  text-decoration: none; /* For router-link */
  display: inline-block; /* For router-link */
}

.logout-btn:hover, .btn-link:hover {
  background-color: #f0f0f0;
  color: #0056b3;
}

.main-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px;
}

.main-content h2 {
  color: #333;
  margin-bottom: 10px;
  text-align: center;
}

.main-content p {
  color: #666;
  margin-bottom: 30px;
  text-align: center;
}

.quick-actions {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  margin-top: 30px;
}

.action-card {
  background-color: white;
  padding: 25px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  text-align: center;
  text-decoration: none; /* For router-link */
  color: inherit; /* For router-link */
}

.action-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.15);
}

.action-card h3 {
  color: #007bff;
  margin-bottom: 10px;
}

.action-card p {
  color: #555;
  font-size: 0.9em;
  margin-bottom: 0;
}
</style>