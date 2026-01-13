<script setup>
import {ref} from 'vue';
import AuthService from '@/services/auth.service';

const user = ref({
    username: '',
    email: '',
    password: '',
})

const loading = ref(false);
const message = ref('');
const isSuccess= ref(false);

const handleRegister = async () => {
    try{
       const response = await AuthService.register(user.value.username, user.value.email,
        user.value.password);
        message.value = response; //user registered successfully
        isSuccess.value = true;
        //optionally reset form or redirect to login
        user.value = {
            username: '',
            email: '',
            password: '',
        }
    }catch(err){
        message.value = err.message;
        isSuccess.value = false; //why false ? becouse we have error 
    }finally{
        loading.value= false;
    }
}
</script>

<template>
    <div class="auth-container">
        <h2>Register</h2>
        <form @submit.prevent="handleRegister">
            <div class="form-group">
                <label for="username">Username</label>
                <input type="text" id="username" v-model="user.username" required/>
            </div>
            <div class="form-group">
                <label for="email">Email</label>
                <input type="text" id="email" v-model="user.email" required/>
            </div> 
            <div class="form-group">
                <label for="password">Password:</label>
                <input type="password" id="password" v-model="user.password" required />
            </div>
            <button type="submit" :disabled="loading">
                <span v-if="loading">Registering...</span>
                <span v-else>Register</span>
            </button>
            <div v-if="message" :class="{'success-message': isSuccess, 'error-message': !isSuccess}">
                {{ message }}
            </div>
        </form>
    </div>
</template>

<style scoped>
    .auth-container{
        max-width: 400px;
        margin: 50px auto;
        padding: 20px;
        border: 1px solid #ccc;
        border-radius: 8px;
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        background-color: #fff;
    }   

    h2{
        text-align: center;
        color: #333;
        margin-bottom: 20px;
    }

    .form-group {
        margin-bottom: 15px;
    }

    label {
        display: block;
        margin-bottom: 5px;
        color: #555;
        font-weight: bold;
    }

    input[type="text"],
    input[type="email"],
    input[type="password"] {
        width: calc(100% - 20px);
        padding: 10px;
        border: 1px solid #ddd;
        border-radius: 4px;
        font-size: 16px;
    }

    button {
    width: 100%;
    padding: 10px;
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 4px;
    font-size: 16px;
    cursor: pointer;
    transition: background-color 0.3s ease;
    }

    button:hover:not(:disabled) {
    background-color: #0056b3;
    }

    button:disabled {
    background-color: #cccccc;
    cursor: not-allowed;
    }

    .success-message {
    margin-top: 15px;
    padding: 10px;
    background-color: #d4edda;
    color: #155724;
    border: 1px solid #c3e6cb;
    border-radius: 4px;
    text-align: center;
    }

    .error-message {
    margin-top: 15px;
    padding: 10px;
    background-color: #f8d7da;
    color: #721c24;
    border: 1px solid #f5c6cb;
    border-radius: 4px;
    text-align: center;
    }
    </style>
