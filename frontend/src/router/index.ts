import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router';
import HomeView from '../views/HomeView.vue';
import RegisterForm from '../components/auth/RegisterForm.vue';

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'Home',
    component: HomeView,
  },
  {
    path: '/register',
    name: 'Register',
    component: RegisterForm,
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
