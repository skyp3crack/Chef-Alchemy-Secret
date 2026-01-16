
<script setup lang="ts">
import LoginForm from '@/components/auth/LoginForm.vue';
</script>



<template>
    <div class="login-page">
        <LoginForm/>
    </div>
</template>


<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f5f5f5;
}
</style>