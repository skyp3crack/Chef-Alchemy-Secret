import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router';
import HomePage from '@/views/HomePage.vue'
import RegisterForm from '../components/auth/RegisterForm.vue';

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'Home',
    component: HomePage,
  },
  {
    path: '/register',
    name: 'Register',
    component: RegisterForm,
  },
  // Catch-all route for 404
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/NotFoundPage.vue')
  },
  // You can add a login route here later
  // {
  //   path: '/login',
  //   name: 'Login',
  //   component: LoginPage
  // }
]



const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
});

export default router
