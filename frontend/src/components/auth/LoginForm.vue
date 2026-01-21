<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import AuthService from '@/services/auth.service';

const router = useRouter()
const credentials = ref({
            username: '',
            password:''
})
const rememberMe = ref(false)

const loading = ref(false)
const message = ref('')
const isSuccess = ref(false)




const handleLogin = async ()=> {
loading.value = true //
message.value = ''
isSuccess.value = false
try{
  const user = await AuthService.login(credentials.value.username, credentials.value.password, rememberMe.value)
  message.value = "Login successful"
  isSuccess.value = true
  setTimeout(()=>{
    router.push('/') //redirect to home page
  },1500)
}catch(e:any){
  message.value = e.message || "Login failed"
  isSuccess.value=false
}finally{
  loading.value = false
}
}
</script>

<template>
  <div class="auth-container">
    <h2>Login</h2>
    <form @submit.prevent="handleLogin">
      <div class="form-group">
        <label for="username">Username:</label>
        <input
          type="text"
          id="username"
          v-model="credentials.username"
          required
        />
      </div>
      <div class="form-group">
        <label for="password">Password:</label>
        <input
          type="password"
          id="password"
          v-model="credentials.password"
          required
        />
      </div>
      <div class="form-group checkbox-group">
        <input
          type="checkbox"
          id="rememberMe"
          v-model="rememberMe"
        />
        <label for="rememberMe">Remember Me</label>
      </div>
      <button type="submit" :disabled="loading">
        <span v-if="loading">Logging in...</span>
        <span v-else>Login</span>
      </button>
      <div v-if="message" :class="{'success-message': isSuccess, 'error-message': !isSuccess}">
        {{ message }}
      </div>
      <p class="switch-auth">
        Don't have an account? 
        <router-link to="/register">Register here</router-link>
      </p>
    </form>
  </div>
</template>


<style scoped>
.auth-container {
  max-width: 400px;
  margin: 50px auto;
  padding: 30px;
  border: 1px solid #ddd;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  background-color: #fff;
}

h2 {
  text-align: center;
  color: #333;
  margin-bottom: 25px;
}

.form-group {
  margin-bottom: 20px;
}

label {
  display: block;
  margin-bottom: 5px;
  color: #555;
  font-weight: 600;
}

input {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 16px;
  box-sizing: border-box;
  transition: border-color 0.3s;
}

input:focus {
  outline: none;
  border-color: #007bff;
}

button {
  width: 100%;
  padding: 12px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s;
}

button:hover:not(:disabled) {
  background-color: #0056b3;
}

button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.success-message {
  margin-top: 15px;
  padding: 10px;
  background-color: #d4edda;
  color: #155724;
  border-radius: 4px;
  text-align: center;
}

.error-message {
  margin-top: 15px;
  padding: 10px;
  background-color: #f8d7da;
  color: #721c24;
  border-radius: 4px;
  text-align: center;
}

.switch-auth {
  text-align: center;
  margin-top: 20px;
  color: #666;
}

.switch-auth a {
  color: #007bff;
  text-decoration: none;
}

.switch-auth a:hover {
  text-decoration: underline;
}

.checkbox-group {
  display: flex;
  align-items: center;
}

.checkbox-group input {
  width: auto;
  margin-right: 10px;
}

.checkbox-group label {
  margin-bottom: 0;
  display: inline;
  font-weight: normal;
}
</style>