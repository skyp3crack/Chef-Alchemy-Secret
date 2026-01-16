
<template>
  <div class="register-page">
    <RegisterForm />
  </div>
</template>

<script setup lang="ts">
import RegisterForm from '@/components/auth/RegisterForm.vue'
</script>

<style scoped>
.register-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f5f5f5;
}
</style>
