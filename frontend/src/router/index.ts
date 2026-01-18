import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router';
import HomePage from '@/views/HomePage.vue'
import RegisterPage from '@/views/RegisterPage.vue';
import LoginPage from '@/views/LoginPage.vue';
import RecipeList from '@/views/RecipeList.vue';
import AuthService from '@/services/auth.service';
import AddEditRecipePage from '@/views/AddEditRecipePage.vue';

import RecipeDetail from '@/views/RecipeDetail.vue';

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    name: 'Home',
    component: HomePage,
    meta: { requiresAuth: true },
  },
  {
    path: '/register',
    name: 'Register',
    component: RegisterPage,
    meta: { guest: true },
  },
  {
    path: '/login',
    name: 'Login',
    component: LoginPage,
    meta: { guest: true },
  },
  {
    path: '/recipes',
    name: 'RecipeList',
    component: RecipeList,
    meta: { requiresAuth: true },
  },
  {
    path: '/recipes/add',
    name: 'AddRecipe',
    component: AddEditRecipePage,
    meta: { requiresAuth: true }
  },
  {
    path: '/recipes/:id',
    name: 'RecipeDetail',
    component: RecipeDetail,
    meta: { requiresAuth: true },
  },
  // Catch-all route for 404
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/NotFoundPage.vue')
  },
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
});


//navigation guard
router.beforeEach((to, from, next) => {
  const isLoggedIn = AuthService.isLoggedIn()

  // If route requires auth and user is not logged in
  if (to.meta.requiresAuth && !isLoggedIn) {
    next('/login')
  }
  // If route is for guests only and user is logged in
  else if (to.meta.guest && isLoggedIn) {
    next('/')
  }
  else {
    next()
  }

});

export default router
