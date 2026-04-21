<template>
  <div class="home-page">

    <!-- ═══════════════════════════════════ STICKY NAV ═══════════════════════════════════ -->
    <nav class="site-nav" :class="{ 'nav-scrolled': isScrolled }" id="site-nav">
      <div class="nav-inner">
        <router-link to="/" class="nav-brand" id="nav-brand-link">
          <span class="brand-icon">🧪</span>
          <span class="brand-text">Chef's <em>Alchemy</em></span>
        </router-link>

        <div class="nav-links" :class="{ 'nav-open': mobileMenuOpen }">
          <router-link to="/recipes" class="nav-item" id="nav-recipes-link" @click="mobileMenuOpen = false">Recipes</router-link>
          <router-link to="/recipes/add" class="nav-item" id="nav-add-link" @click="mobileMenuOpen = false">Add Recipe</router-link>
          <a href="#meal-planner" class="nav-item" id="nav-planner-link" @click.prevent="scrollTo('#meal-planner'); mobileMenuOpen = false">Meal Planner</a>

          <template v-if="user">
            <span class="nav-user">👨‍🍳 {{ user.username }}</span>
            <button class="btn-ghost" id="nav-logout-btn" @click="handleLogout">Logout</button>
          </template>
          <template v-else>
            <router-link to="/login" class="btn-ghost" id="nav-login-link" @click="mobileMenuOpen = false">Login</router-link>
            <router-link to="/register" class="btn-primary" id="nav-register-link" @click="mobileMenuOpen = false">Get Started</router-link>
          </template>
        </div>

        <button class="hamburger" id="hamburger-btn" @click="mobileMenuOpen = !mobileMenuOpen" :aria-expanded="mobileMenuOpen" aria-label="Toggle menu">
          <span></span><span></span><span></span>
        </button>
      </div>
    </nav>

    <!-- ═══════════════════════════════════ HERO SECTION ═══════════════════════════════════ -->
    <section class="hero" id="hero-section">
      <div class="hero-bg">
        <div class="hero-gradient-overlay"></div>
        <div class="hero-pattern"></div>
      </div>

      <div class="hero-content">
        <div class="hero-text-block">
          <span class="hero-eyebrow">✦ The Art of Culinary Secrets</span>
          <h1 class="hero-headline">
            Master Your Kitchen.<br>
            <em>Plan. Cook. Share.</em>
          </h1>
          <p class="hero-subtext">
            Discover thousands of hand-crafted recipes, organize your weekly meals like a pro, and share your culinary creations with a passionate community of home chefs.
          </p>
          <div class="hero-ctas">
            <router-link to="/recipes" class="cta-primary" id="hero-explore-btn">
              <span class="cta-icon">🍽️</span> Explore Recipes
            </router-link>
            <a href="#meal-planner" class="cta-secondary" id="hero-planner-btn" @click.prevent="scrollTo('#meal-planner')">
              <span class="cta-icon">📅</span> Start Meal Planning
            </a>
          </div>
          <div class="hero-stats">
            <div class="stat-item">
              <span class="stat-number">{{ animatedRecipeCount }}+</span>
              <span class="stat-label">Recipes</span>
            </div>
            <div class="stat-divider"></div>
            <div class="stat-item">
              <span class="stat-number">{{ animatedChefCount }}+</span>
              <span class="stat-label">Home Chefs</span>
            </div>
            <div class="stat-divider"></div>
            <div class="stat-item">
              <span class="stat-number">{{ animatedCuisineCount }}</span>
              <span class="stat-label">Cuisines</span>
            </div>
          </div>
        </div>

        <div class="hero-visual">
          <div class="hero-card-stack">
            <div class="floating-recipe-card frc-back">
              <div class="frc-image" style="background: linear-gradient(135deg,#e8a027,#c4841a);">🥩</div>
              <div class="frc-info"><span>Braised Short Ribs</span><span class="frc-rating">⭐ 4.9</span></div>
            </div>
            <div class="floating-recipe-card frc-main">
              <div class="frc-image" style="background: linear-gradient(135deg,#7c9e74,#5a7a52);">🍝</div>
              <div class="frc-info">
                <span class="frc-title">Truffle Carbonara</span>
                <span class="frc-rating">⭐ 4.8</span>
              </div>
              <div class="frc-meta">
                <span>⏱ 35 min</span><span>👤 Chef Marco</span>
              </div>
            </div>
            <div class="floating-recipe-card frc-side">
              <div class="frc-image" style="background: linear-gradient(135deg,#f5c561,#e8a027);">🥗</div>
              <div class="frc-info"><span>Niçoise Salad</span><span class="frc-rating">⭐ 4.7</span></div>
            </div>
          </div>

          <div class="hero-badge hero-badge-1">
            <span class="badge-icon">🔥</span>
            <div>
              <div class="badge-title">Trending Today</div>
              <div class="badge-sub">Saffron Risotto</div>
            </div>
          </div>
          <div class="hero-badge hero-badge-2">
            <span class="badge-icon">✅</span>
            <div>
              <div class="badge-title">Just Planned</div>
              <div class="badge-sub">Week of meals ready!</div>
            </div>
          </div>
        </div>
      </div>

      <div class="hero-scroll-hint" @click="scrollTo('#featured')">
        <span>Scroll to explore</span>
        <div class="scroll-arrow"></div>
      </div>
    </section>

    <!-- ═══════════════════════════════════ CATEGORY STRIP ═══════════════════════════════════ -->
    <section class="category-strip" id="category-strip">
      <div class="container">
        <div class="category-track">
          <button
            v-for="cat in cuisineCategories"
            :key="cat.id"
            class="category-pill"
            :class="{ 'active': activeCategoryId === cat.id }"
            :id="`cat-btn-${cat.id}`"
            @click="filterByCategory(cat.id)"
          >
            <span class="cat-emoji">{{ cat.emoji }}</span>
            <span>{{ cat.name }}</span>
          </button>
        </div>
      </div>
    </section>

    <!-- ═══════════════════════════════════ FEATURED RECIPES ═══════════════════════════════════ -->
    <section class="featured-recipes" id="featured">
      <div class="container">
        <div class="section-header">
          <div class="section-label">🌟 Featured Picks</div>
          <h2 class="section-title">Trending Recipes <em>This Week</em></h2>
          <p class="section-subtitle">Handpicked by our culinary community — fresh, delicious, and ready to try.</p>
        </div>

        <div class="recipe-grid" v-if="!loadingRecipes">
          <article
            v-for="recipe in displayedRecipes"
            :key="recipe.id"
            class="recipe-card-premium"
            :id="`recipe-card-${recipe.id}`"
            @mouseenter="hoverCard = recipe.id ?? null"
            @mouseleave="hoverCard = null"
          >
            <div class="rcp-image-wrap">
              <img
                :src="recipe.imageUrl || ''"
                :alt="recipe.title"
                class="rcp-image"
                @error="onImgError"
              />
              <div class="rcp-image-fallback" v-if="!recipe.imageUrl">
                <span>{{ recipe.emoji || '🍴' }}</span>
              </div>
              <div class="rcp-overlay">
                <router-link
                  :to="{ name: 'RecipeDetail', params: { id: recipe.id } }"
                  class="rcp-quick-view"
                  :id="`recipe-view-${recipe.id}`"
                >
                  View Recipe →
                </router-link>
              </div>
              <div class="rcp-badge" v-if="recipe.badge">{{ recipe.badge }}</div>
            </div>

            <div class="rcp-body">
              <div class="rcp-top">
                <div class="rcp-cats">
                  <span
                    v-for="cat in (recipe.categories || []).slice(0,2)"
                    :key="cat.id"
                    class="rcp-cat-tag"
                  >{{ cat.name }}</span>
                </div>
                <div class="rcp-rating" v-if="recipe.averageRating">
                  <span class="star-filled">★</span>
                  <span>{{ recipe.averageRating.toFixed(1) }}</span>
                  <span class="rcp-count">({{ recipe.ratingCount || 0 }})</span>
                </div>
              </div>

              <h3 class="rcp-title">{{ recipe.title }}</h3>
              <p class="rcp-desc">{{ truncate(recipe.description, 90) }}</p>

              <div class="rcp-footer">
                <div class="rcp-meta-row">
                  <span class="rcp-meta-pill">⏱ {{ recipe.cookTime || '~30 min' }}</span>
                  <span class="rcp-meta-pill">👤 {{ recipe.authorUsername || 'Chef Anonymous' }}</span>
                </div>
                <router-link
                  :to="{ name: 'RecipeDetail', params: { id: recipe.id } }"
                  class="rcp-btn"
                  :id="`recipe-btn-${recipe.id}`"
                >
                  Explore ↗
                </router-link>
              </div>
            </div>
          </article>

          <!-- Skeleton cards while no data -->
          <div v-if="displayedRecipes.length === 0" class="recipe-card-premium skeleton" v-for="n in 6" :key="`s${n}`">
            <div class="rcp-image-wrap skeleton-img"></div>
            <div class="rcp-body">
              <div class="skeleton-line w-60"></div>
              <div class="skeleton-line w-90 mt-8"></div>
              <div class="skeleton-line w-40 mt-8"></div>
            </div>
          </div>
        </div>

        <div class="loading-state" v-if="loadingRecipes">
          <div class="spinner"></div>
          <p>Fetching delicious recipes…</p>
        </div>

        <div class="section-cta">
          <router-link to="/recipes" class="cta-outlined" id="view-all-recipes-btn">
            View All Recipes
            <span class="arrow-right">→</span>
          </router-link>
        </div>
      </div>
    </section>

    <!-- ═══════════════════════════════════ MEAL PLANNER TEASER ═══════════════════════════════════ -->
    <section class="meal-planner-section" id="meal-planner">
      <div class="container planner-container">
        <div class="planner-text">
          <div class="section-label">📅 Smart Planning</div>
          <h2 class="section-title">Your Week, <em>Beautifully Planned</em></h2>
          <p class="section-subtitle">
            Stop the daily "What's for dinner?" scramble. Our intuitive meal planner lets you drag, drop, and organize a full week of delicious meals in minutes — then auto-generate your grocery list.
          </p>

          <ul class="planner-features">
            <li v-for="feat in plannerFeatures" :key="feat.title" class="planner-feature-item">
              <div class="pf-icon">{{ feat.icon }}</div>
              <div>
                <div class="pf-title">{{ feat.title }}</div>
                <div class="pf-desc">{{ feat.desc }}</div>
              </div>
            </li>
          </ul>

          <router-link to="/recipes" class="cta-primary" id="planner-cta-btn" style="display: inline-flex; margin-top: 2rem;">
            <span class="cta-icon">🚀</span> Start Planning Now
          </router-link>
        </div>

        <div class="planner-visual" aria-hidden="true">
          <div class="planner-ui-mock">
            <div class="pui-header">
              <span class="pui-title">📅 Weekly Meal Plan</span>
              <span class="pui-week">Apr 21 – 27</span>
            </div>
            <div class="pui-grid">
              <div class="pui-day-col" v-for="day in weekDays" :key="day.short">
                <div class="pui-day-label">{{ day.short }}</div>
                <div
                  class="pui-meal-slot"
                  :class="{ 'slot-filled': !!day.meal, 'slot-empty': !day.meal }"
                >
                  <template v-if="day.meal">
                    <div class="pui-meal-emoji">{{ day.meal.emoji }}</div>
                    <div class="pui-meal-name">{{ day.meal.name }}</div>
                  </template>
                  <template v-else>
                    <span class="pui-add-btn">+</span>
                  </template>
                </div>
              </div>
            </div>
            <div class="pui-footer">
              <span class="pui-tag">✅ 5 of 7 planned</span>
              <button class="pui-grocery-btn">🛒 Grocery List</button>
            </div>
          </div>

          <div class="planner-floating-card pf-card-1">
            <span>🥦</span>
            <div>
              <div class="pfc-title">Grocery Ready</div>
              <div class="pfc-sub">23 items • 4 stores</div>
            </div>
          </div>
          <div class="planner-floating-card pf-card-2">
            <span>⏰</span>
            <div>
              <div class="pfc-title">Prep Reminder</div>
              <div class="pfc-sub">Sunday 5 PM</div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- ═══════════════════════════════════ WHY CHEF ALCHEMY ═══════════════════════════════════ -->
    <section class="why-section" id="why-section">
      <div class="container">
        <div class="section-header">
          <div class="section-label">🧪 Why Chef's Alchemy?</div>
          <h2 class="section-title">Everything a Home Chef <em>Needs</em></h2>
        </div>
        <div class="why-grid">
          <div class="why-card" v-for="item in whyItems" :key="item.title" :id="`why-${item.id}`">
            <div class="why-icon-wrap">{{ item.icon }}</div>
            <h3 class="why-title">{{ item.title }}</h3>
            <p class="why-desc">{{ item.desc }}</p>
          </div>
        </div>
      </div>
    </section>

    <!-- ═══════════════════════════════════ CTA BANNER ═══════════════════════════════════ -->
    <section class="cta-banner" id="cta-banner" v-if="!user">
      <div class="container cta-banner-inner">
        <div class="cta-banner-text">
          <h2>Ready to Transform <em>Your Kitchen?</em></h2>
          <p>Join thousands of home chefs discovering the alchemy of great cooking.</p>
        </div>
        <div class="cta-banner-actions">
          <router-link to="/register" class="cta-primary" id="banner-register-btn">Create Free Account</router-link>
          <router-link to="/login" class="cta-ghost" id="banner-login-btn">Sign In</router-link>
        </div>
      </div>
    </section>

    <!-- ═══════════════════════════════════ FOOTER ═══════════════════════════════════ -->
    <footer class="site-footer" id="site-footer">
      <div class="container footer-inner">
        <div class="footer-brand">
          <span class="brand-icon">🧪</span>
          <span class="brand-text">Chef's <em>Alchemy</em> Secret</span>
        </div>
        <p class="footer-tagline">Where culinary secrets become art.</p>
        <div class="footer-links">
          <router-link to="/recipes">Recipes</router-link>
          <router-link to="/recipes/add">Add Recipe</router-link>
          <a href="#meal-planner" @click.prevent="scrollTo('#meal-planner')">Meal Planner</a>
        </div>
        <p class="footer-copy">© {{ new Date().getFullYear() }} Chef's Alchemy Secret. All rights reserved.</p>
      </div>
    </footer>

  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import AuthService, { type UserData } from '@/services/auth.service'
import RecipeService, { type Recipe } from '@/services/recipe.service'

// ── Auth ──────────────────────────────────────────────────────────────────────
const router = useRouter()
const user = ref<UserData | null>(null)

onMounted(() => {
  user.value = AuthService.getCurrentUser()
})

const handleLogout = () => {
  AuthService.logout()
  user.value = null
  router.push('/login')
}

// ── Sticky Nav ────────────────────────────────────────────────────────────────
const isScrolled = ref(false)
const mobileMenuOpen = ref(false)

const handleScroll = () => {
  isScrolled.value = window.scrollY > 60
}

onMounted(() => window.addEventListener('scroll', handleScroll))
onUnmounted(() => window.removeEventListener('scroll', handleScroll))

const scrollTo = (selector: string) => {
  const el = document.querySelector(selector)
  if (el) el.scrollIntoView({ behavior: 'smooth', block: 'start' })
}

// ── Animated counters ─────────────────────────────────────────────────────────
const animatedRecipeCount = ref(0)
const animatedChefCount = ref(0)
const animatedCuisineCount = ref(0)

const animateNumber = (ref: { value: number }, target: number, duration = 1200) => {
  const step = target / (duration / 16)
  const tick = () => {
    ref.value = Math.min(ref.value + step, target)
    if (ref.value < target) requestAnimationFrame(tick)
    else ref.value = target
  }
  requestAnimationFrame(tick)
}

// ── Category filter ───────────────────────────────────────────────────────────
const activeCategoryId = ref<number | null>(null)

const cuisineCategories = ref([
  { id: 1,  name: 'Italian',    emoji: '🍝' },
  { id: 2,  name: 'Asian',      emoji: '🍜' },
  { id: 3,  name: 'BBQ',        emoji: '🥩' },
  { id: 4,  name: 'Vegetarian', emoji: '🥗' },
  { id: 5,  name: 'Desserts',   emoji: '🍰' },
  { id: 6,  name: 'Seafood',    emoji: '🦞' },
  { id: 7,  name: 'Breakfast',  emoji: '🍳' },
  { id: 8,  name: 'Mexican',    emoji: '🌮' },
])

const filterByCategory = (id: number) => {
  activeCategoryId.value = activeCategoryId.value === id ? null : id
  fetchFeaturedRecipes()
}

// ── Recipes ───────────────────────────────────────────────────────────────────
interface RecipeWithExtras extends Recipe {
  emoji?: string
  cookTime?: string
  badge?: string
}

const loadingRecipes = ref(false)
const hoverCard = ref<number | null>(null)
const liveRecipes = ref<RecipeWithExtras[]>([])

// Mock data – shown immediately; live data merges in after API responds
const mockRecipes: RecipeWithExtras[] = [
  {
    id: 101, title: 'Truffle Mushroom Risotto', emoji: '🍄',
    description: 'A creamy, luxurious Italian classic elevated with black truffle shavings and aged Parmigiano-Reggiano.',
    ingredients: '', instructions: '',
    authorUsername: 'Chef Marco', cookTime: '45 min',
    imageUrl: '', averageRating: 4.9, ratingCount: 128,
    badge: '🔥 Trending',
    categories: [{ id: 1, name: 'Italian' }],
    tags: [{ id: 1, name: 'Vegetarian' }],
  },
  {
    id: 102, title: 'Miso-Glazed Salmon', emoji: '🐟',
    description: 'Silky salmon fillet glazed with a sweet-savory miso marinade, broiled to perfection with sesame.',
    ingredients: '', instructions: '',
    authorUsername: 'Chef Yuki', cookTime: '20 min',
    imageUrl: '', averageRating: 4.7, ratingCount: 95,
    badge: '⭐ Top Rated',
    categories: [{ id: 2, name: 'Asian' }],
    tags: [{ id: 2, name: 'Seafood' }],
  },
  {
    id: 103, title: 'Saffron Lamb Tagine', emoji: '🥘',
    description: 'Slow-braised Moroccan tagine with lamb shoulder, dried apricots, almonds, and a fragrant saffron broth.',
    ingredients: '', instructions: '',
    authorUsername: 'Chef Amira', cookTime: '2h 30 min',
    imageUrl: '', averageRating: 4.8, ratingCount: 74,
    badge: '✨ New',
    categories: [{ id: 3, name: 'Mediterranean' }],
    tags: [],
  },
  {
    id: 104, title: 'Smoked BBQ Brisket', emoji: '🥩',
    description: 'Texas-style brisket slow smoked over oak for 12 hours with a robust dry rub and tangy house sauce.',
    ingredients: '', instructions: '',
    authorUsername: 'PitMaster Jake', cookTime: '12 hrs',
    imageUrl: '', averageRating: 5.0, ratingCount: 211,
    badge: '🏆 Best Ever',
    categories: [{ id: 4, name: 'BBQ' }],
    tags: [],
  },
  {
    id: 105, title: 'Lemon Ricotta Pancakes', emoji: '🥞',
    description: 'Cloud-light ricotta pancakes with fresh lemon zest, drizzled with wildflower honey and blueberries.',
    ingredients: '', instructions: '',
    authorUsername: 'Chef Elena', cookTime: '25 min',
    imageUrl: '', averageRating: 4.6, ratingCount: 183,
    categories: [{ id: 5, name: 'Breakfast' }],
    tags: [{ id: 3, name: 'Vegetarian' }],
  },
  {
    id: 106, title: 'Avocado Tuna Poke Bowl', emoji: '🍱',
    description: 'Fresh sashimi-grade tuna over seasoned sushi rice, topped with creamy avocado, edamame, and ponzu.',
    ingredients: '', instructions: '',
    authorUsername: 'Chef Kai', cookTime: '15 min',
    imageUrl: '', averageRating: 4.8, ratingCount: 147,
    categories: [{ id: 6, name: 'Asian' }],
    tags: [{ id: 2, name: 'Seafood' }],
  },
]

const displayedRecipes = computed<RecipeWithExtras[]>(() => {
  if (liveRecipes.value.length > 0) return liveRecipes.value.slice(0, 6)
  return mockRecipes
})

const fetchFeaturedRecipes = async () => {
  loadingRecipes.value = true
  try {
    const catIds = activeCategoryId.value ? [activeCategoryId.value] : undefined
    const data = await RecipeService.getAllRecipes(undefined, catIds)
    if (data.length > 0) {
      liveRecipes.value = data.slice(0, 6) as RecipeWithExtras[]
    }
  } catch {
    // Silently fall back to mock data
  } finally {
    loadingRecipes.value = false
  }
}

// ── Meal Planner ──────────────────────────────────────────────────────────────
const weekDays = ref([
  { short: 'Mon', meal: { name: 'Risotto',  emoji: '🍄' } },
  { short: 'Tue', meal: { name: 'Salmon',   emoji: '🐟' } },
  { short: 'Wed', meal: null },
  { short: 'Thu', meal: { name: 'Tagine',   emoji: '🥘' } },
  { short: 'Fri', meal: { name: 'Brisket',  emoji: '🥩' } },
  { short: 'Sat', meal: null },
  { short: 'Sun', meal: { name: 'Pancakes', emoji: '🥞' } },
])

const plannerFeatures = [
  { icon: '🗓️', title: 'Drag & Drop Scheduling',       desc: 'Effortlessly plan each day of the week in seconds.' },
  { icon: '🛒', title: 'Auto Grocery List',             desc: 'Instantly generate a smart shopping list from your plan.' },
  { icon: '🔔', title: 'Prep Reminders',                desc: 'Get timely alerts so you\'re never caught off guard.' },
  { icon: '🔄', title: 'Save & Reuse Weekly Templates', desc: 'Create templates for your favourite weekly routines.' },
]

// ── Why Cards ─────────────────────────────────────────────────────────────────
const whyItems = [
  { id: 'share',   icon: '🤝', title: 'Share & Discover',    desc: 'Upload your own recipes and discover creations from chefs around the globe.' },
  { id: 'plan',    icon: '📅', title: 'Smart Meal Planning', desc: 'Plan every meal for the week and generate a grocery list automatically.' },
  { id: 'rate',    icon: '⭐', title: 'Rate & Review',       desc: 'Star ratings and community reviews to highlight the best recipes.' },
  { id: 'filter',  icon: '🔍', title: 'Powerful Filters',    desc: 'Search by ingredient, cuisine, tag, diet, or cook time.' },
  { id: 'create',  icon: '✍️', title: 'Rich Recipe Editor',  desc: 'Beautiful editor with ingredient lists, step-by-step instructions, and image upload.' },
  { id: 'mobile',  icon: '📱', title: 'Mobile Friendly',     desc: 'Designed to shine on every screen — cook with your phone propped on the counter.' },
]

// ── Helpers ───────────────────────────────────────────────────────────────────
const truncate = (str: string | undefined, len: number) => {
  if (!str) return ''
  return str.length > len ? str.slice(0, len) + '…' : str
}

const onImgError = (e: Event) => {
  const img = e.target as HTMLImageElement
  img.style.display = 'none'
}

// ── Lifecycle ─────────────────────────────────────────────────────────────────
onMounted(() => {
  animateNumber(animatedRecipeCount, 1200)
  animateNumber(animatedChefCount,   850)
  animateNumber(animatedCuisineCount, 42)
  fetchFeaturedRecipes()
})
</script>

<style scoped>
/* ════════════════════════════════════════════════════════════
   NAV
════════════════════════════════════════════════════════════ */
.site-nav {
  position: fixed;
  top: 0; left: 0; right: 0;
  z-index: 1000;
  transition: background 0.4s ease, box-shadow 0.4s ease, padding 0.3s ease;
  padding: 1.25rem 0;
}
.site-nav.nav-scrolled {
  background: rgba(30, 31, 34, 0.97);
  backdrop-filter: blur(20px);
  box-shadow: 0 2px 32px rgba(0,0,0,0.3);
  padding: 0.75rem 0;
}

.nav-inner {
  max-width: 1280px;
  margin: 0 auto;
  padding: 0 2rem;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 1rem;
}

.nav-brand {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  text-decoration: none;
  color: #fff;
  flex-shrink: 0;
}
.brand-icon { font-size: 1.5rem; }
.brand-text {
  font-family: var(--font-display);
  font-size: 1.2rem;
  font-weight: 600;
  color: #fff;
}
.brand-text em { color: var(--ca-saffron); font-style: italic; }

.nav-links {
  display: flex;
  align-items: center;
  gap: 0.25rem;
}

.nav-item {
  color: rgba(255,255,255,0.8);
  font-size: 0.9rem;
  font-weight: 500;
  padding: 0.5rem 0.9rem;
  border-radius: 8px;
  transition: color 0.2s, background 0.2s;
  text-decoration: none;
  cursor: pointer;
  border: none;
  background: none;
}
.nav-item:hover, .nav-item.router-link-active {
  color: #fff;
  background: rgba(255,255,255,0.1);
}

.nav-user {
  color: var(--ca-saffron-light);
  font-weight: 600;
  font-size: 0.9rem;
  padding: 0.5rem 0.75rem;
}

.btn-ghost {
  color: rgba(255,255,255,0.85);
  border: 1px solid rgba(255,255,255,0.25);
  background: transparent;
  padding: 0.5rem 1.25rem;
  border-radius: 100px;
  font-size: 0.9rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
  text-decoration: none;
  font-family: var(--font-body);
}
.btn-ghost:hover {
  background: rgba(255,255,255,0.1);
  border-color: rgba(255,255,255,0.5);
  color: #fff;
}

.btn-primary {
  background: linear-gradient(135deg, var(--ca-saffron), var(--ca-saffron-dark));
  color: #fff;
  border: none;
  padding: 0.55rem 1.35rem;
  border-radius: 100px;
  font-size: 0.9rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.25s;
  text-decoration: none;
  font-family: var(--font-body);
  box-shadow: 0 4px 16px rgba(232,160,39,0.4);
}
.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 24px rgba(232,160,39,0.5);
}

.hamburger {
  display: none;
  flex-direction: column;
  gap: 5px;
  background: none;
  border: none;
  cursor: pointer;
  padding: 4px;
}
.hamburger span {
  display: block;
  width: 22px;
  height: 2px;
  background: #fff;
  border-radius: 2px;
  transition: all 0.3s;
}

/* ════════════════════════════════════════════════════════════
   HERO
════════════════════════════════════════════════════════════ */
.hero {
  min-height: 100vh;
  position: relative;
  display: flex;
  flex-direction: column;
  justify-content: center;
  overflow: hidden;
  padding: 8rem 2rem 5rem;
  background: var(--ca-charcoal);
}

.hero-bg {
  position: absolute; inset: 0;
  z-index: 0;
}
.hero-gradient-overlay {
  position: absolute; inset: 0;
  background:
    radial-gradient(ellipse 70% 80% at 75% 50%, rgba(232,160,39,0.18) 0%, transparent 60%),
    radial-gradient(ellipse 50% 60% at 10% 80%, rgba(124,158,116,0.15) 0%, transparent 50%),
    linear-gradient(160deg, #1e1f22 0%, #2a2b2f 100%);
}
.hero-pattern {
  position: absolute; inset: 0;
  background-image: radial-gradient(circle, rgba(255,255,255,0.035) 1px, transparent 1px);
  background-size: 32px 32px;
  opacity: 0.6;
}

.hero-content {
  position: relative; z-index: 1;
  max-width: 1280px;
  margin: 0 auto;
  width: 100%;
  display: grid;
  grid-template-columns: 1fr 1fr;
  align-items: center;
  gap: 4rem;
}

/* Hero text */
.hero-eyebrow {
  display: inline-block;
  color: var(--ca-saffron);
  font-size: 0.85rem;
  font-weight: 600;
  letter-spacing: 0.12em;
  text-transform: uppercase;
  margin-bottom: 1.25rem;
}

.hero-headline {
  font-family: var(--font-display);
  font-size: clamp(2.5rem, 5vw, 4rem);
  font-weight: 700;
  color: #fff;
  line-height: 1.15;
  margin-bottom: 1.5rem;
}
.hero-headline em {
  color: var(--ca-saffron);
  font-style: italic;
}

.hero-subtext {
  font-size: 1.05rem;
  color: rgba(255,255,255,0.65);
  line-height: 1.75;
  max-width: 500px;
  margin-bottom: 2.5rem;
}

.hero-ctas {
  display: flex;
  gap: 1rem;
  flex-wrap: wrap;
  margin-bottom: 3rem;
}

.cta-primary {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  background: linear-gradient(135deg, var(--ca-saffron), var(--ca-saffron-dark));
  color: #fff;
  font-weight: 700;
  font-size: 1rem;
  padding: 0.85rem 2rem;
  border-radius: 100px;
  text-decoration: none;
  transition: all 0.3s;
  box-shadow: 0 8px 32px rgba(232,160,39,0.45);
  border: none;
  cursor: pointer;
  font-family: var(--font-body);
}
.cta-primary:hover {
  transform: translateY(-3px);
  box-shadow: 0 14px 40px rgba(232,160,39,0.55);
}

.cta-secondary {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  background: rgba(255,255,255,0.08);
  color: rgba(255,255,255,0.9);
  font-weight: 600;
  font-size: 1rem;
  padding: 0.85rem 2rem;
  border-radius: 100px;
  text-decoration: none;
  border: 1px solid rgba(255,255,255,0.2);
  transition: all 0.3s;
  backdrop-filter: blur(8px);
}
.cta-secondary:hover {
  background: rgba(255,255,255,0.14);
  transform: translateY(-3px);
}

.cta-icon { font-size: 1.1rem; }

.hero-stats {
  display: flex;
  align-items: center;
  gap: 2rem;
}
.stat-item { text-align: center; }
.stat-number {
  display: block;
  font-family: var(--font-display);
  font-size: 1.8rem;
  font-weight: 700;
  color: var(--ca-saffron);
  line-height: 1;
}
.stat-label {
  font-size: 0.8rem;
  color: rgba(255,255,255,0.5);
  text-transform: uppercase;
  letter-spacing: 0.08em;
  margin-top: 4px;
}
.stat-divider {
  width: 1px;
  height: 36px;
  background: rgba(255,255,255,0.15);
}

/* Hero Visual */
.hero-visual {
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 480px;
}

.hero-card-stack {
  position: relative;
  width: 300px;
  height: 380px;
}

.floating-recipe-card {
  position: absolute;
  background: rgba(255,255,255,0.06);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255,255,255,0.12);
  border-radius: 20px;
  overflow: hidden;
  box-shadow: var(--ca-shadow-lg);
  transition: transform 0.4s ease;
}

.frc-back {
  width: 220px; left: -20px; top: 30px;
  transform: rotate(-10deg) scale(0.88);
  opacity: 0.6;
  z-index: 1;
}
.frc-main {
  width: 260px; left: 20px; top: 60px;
  z-index: 3;
  border-color: rgba(232,160,39,0.3);
}
.frc-side {
  width: 200px; right: -30px; bottom: 20px;
  transform: rotate(8deg) scale(0.85);
  opacity: 0.55;
  z-index: 2;
}

.hero-card-stack:hover .frc-back  { transform: rotate(-12deg) scale(0.9) translateX(-10px); }
.hero-card-stack:hover .frc-main  { transform: translateY(-8px) scale(1.02); }
.hero-card-stack:hover .frc-side  { transform: rotate(10deg) scale(0.87) translateX(8px); }

.frc-image {
  height: 120px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 3rem;
}
.frc-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.75rem 1rem;
  color: #fff;
  font-size: 0.85rem;
}
.frc-title { font-weight: 600; font-size: 0.95rem; }
.frc-rating { color: var(--ca-saffron-light); font-size: 0.8rem; }
.frc-meta {
  display: flex;
  gap: 1rem;
  padding: 0 1rem 0.75rem;
  color: rgba(255,255,255,0.55);
  font-size: 0.78rem;
}

.hero-badge {
  position: absolute;
  background: rgba(255,255,255,0.1);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255,255,255,0.15);
  border-radius: 14px;
  padding: 0.75rem 1rem;
  display: flex;
  align-items: center;
  gap: 0.65rem;
  color: #fff;
  font-size: 0.8rem;
  z-index: 5;
  box-shadow: 0 8px 32px rgba(0,0,0,0.2);
  animation: float 4s ease-in-out infinite;
}
.hero-badge-1 { top: 20px; right: -10px; animation-delay: 0s; }
.hero-badge-2 { bottom: 60px; left: -20px; animation-delay: 2s; }
.badge-icon { font-size: 1.4rem; }
.badge-title { font-weight: 600; font-size: 0.82rem; }
.badge-sub { color: rgba(255,255,255,0.55); font-size: 0.75rem; }

@keyframes float {
  0%, 100% { transform: translateY(0); }
  50%       { transform: translateY(-8px); }
}

.hero-scroll-hint {
  position: absolute;
  bottom: 2rem;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.5rem;
  color: rgba(255,255,255,0.4);
  font-size: 0.78rem;
  cursor: pointer;
  transition: color 0.2s;
  z-index: 2;
}
.hero-scroll-hint:hover { color: rgba(255,255,255,0.7); }
.scroll-arrow {
  width: 20px;
  height: 20px;
  border-right: 2px solid currentColor;
  border-bottom: 2px solid currentColor;
  transform: rotate(45deg);
  animation: bounce 1.6s ease-in-out infinite;
}
@keyframes bounce {
  0%, 100% { transform: rotate(45deg) translateY(0); }
  50%       { transform: rotate(45deg) translateY(5px); }
}

/* ════════════════════════════════════════════════════════════
   CATEGORY STRIP
════════════════════════════════════════════════════════════ */
.category-strip {
  background: var(--ca-charcoal-soft);
  padding: 1.25rem 0;
  border-bottom: 1px solid rgba(255,255,255,0.06);
}
.category-track {
  display: flex;
  gap: 0.75rem;
  overflow-x: auto;
  padding-bottom: 4px;
  scrollbar-width: none;
}
.category-track::-webkit-scrollbar { display: none; }

.category-pill {
  display: inline-flex;
  align-items: center;
  gap: 0.4rem;
  background: rgba(255,255,255,0.06);
  color: rgba(255,255,255,0.7);
  border: 1px solid rgba(255,255,255,0.1);
  padding: 0.5rem 1.1rem;
  border-radius: 100px;
  font-size: 0.85rem;
  font-weight: 500;
  white-space: nowrap;
  cursor: pointer;
  transition: all 0.2s;
  font-family: var(--font-body);
  flex-shrink: 0;
}
.category-pill:hover {
  background: rgba(232,160,39,0.15);
  border-color: rgba(232,160,39,0.4);
  color: var(--ca-saffron-light);
}
.category-pill.active {
  background: var(--ca-saffron);
  border-color: var(--ca-saffron);
  color: #fff;
  box-shadow: 0 4px 16px rgba(232,160,39,0.4);
}
.cat-emoji { font-size: 1rem; }

/* ════════════════════════════════════════════════════════════
   SHARED LAYOUT HELPERS
════════════════════════════════════════════════════════════ */
.container {
  max-width: 1280px;
  margin: 0 auto;
  padding: 0 2rem;
}

.section-header {
  text-align: center;
  margin-bottom: 3.5rem;
}
.section-label {
  display: inline-block;
  background: rgba(232,160,39,0.12);
  color: var(--ca-saffron-dark);
  font-size: 0.8rem;
  font-weight: 700;
  letter-spacing: 0.1em;
  text-transform: uppercase;
  padding: 0.3rem 1rem;
  border-radius: 100px;
  margin-bottom: 1rem;
}
.section-title {
  font-family: var(--font-display);
  font-size: clamp(2rem, 4vw, 3rem);
  font-weight: 700;
  color: var(--ca-charcoal);
  line-height: 1.2;
  margin-bottom: 0.75rem;
}
.section-title em { color: var(--ca-saffron-dark); font-style: italic; }
.section-subtitle {
  color: var(--ca-text-secondary);
  font-size: 1rem;
  max-width: 560px;
  margin: 0 auto;
  line-height: 1.7;
}

/* ════════════════════════════════════════════════════════════
   FEATURED RECIPES
════════════════════════════════════════════════════════════ */
.featured-recipes {
  background: var(--ca-cream);
  padding: 6rem 0;
}

.recipe-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 2rem;
  margin-bottom: 3rem;
}

/* Premium Recipe Card */
.recipe-card-premium {
  background: #fff;
  border-radius: var(--ca-radius-lg);
  overflow: hidden;
  box-shadow: var(--ca-shadow-sm);
  border: 1px solid var(--ca-border);
  transition: transform 0.35s ease, box-shadow 0.35s ease;
  display: flex;
  flex-direction: column;
}
.recipe-card-premium:hover {
  transform: translateY(-8px);
  box-shadow: var(--ca-shadow-lg);
}

.rcp-image-wrap {
  position: relative;
  height: 220px;
  background: linear-gradient(135deg, var(--ca-cream-warm), var(--ca-cream));
  overflow: hidden;
  flex-shrink: 0;
}
.rcp-image {
  width: 100%; height: 100%;
  object-fit: cover;
  display: block;
  transition: transform 0.5s ease;
}
.recipe-card-premium:hover .rcp-image { transform: scale(1.07); }

.rcp-image-fallback {
  position: absolute; inset: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 4.5rem;
  background: linear-gradient(135deg, var(--ca-cream-warm) 0%, #ede0cc 100%);
}

.rcp-overlay {
  position: absolute; inset: 0;
  background: linear-gradient(to top, rgba(30,31,34,0.82) 0%, transparent 55%);
  display: flex;
  align-items: flex-end;
  justify-content: center;
  padding: 1.25rem;
  opacity: 0;
  transition: opacity 0.35s ease;
}
.recipe-card-premium:hover .rcp-overlay { opacity: 1; }

.rcp-quick-view {
  display: inline-block;
  background: var(--ca-saffron);
  color: #fff;
  font-weight: 700;
  font-size: 0.9rem;
  padding: 0.6rem 1.5rem;
  border-radius: 100px;
  text-decoration: none;
  transform: translateY(12px);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  box-shadow: 0 4px 16px rgba(232,160,39,0.5);
}
.recipe-card-premium:hover .rcp-quick-view {
  transform: translateY(0);
  box-shadow: 0 8px 24px rgba(232,160,39,0.6);
}

.rcp-badge {
  position: absolute;
  top: 1rem; left: 1rem;
  background: rgba(30,31,34,0.82);
  backdrop-filter: blur(8px);
  color: var(--ca-saffron-light);
  font-size: 0.72rem;
  font-weight: 700;
  padding: 0.3rem 0.75rem;
  border-radius: 100px;
  letter-spacing: 0.04em;
}

.rcp-body {
  padding: 1.5rem;
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 0.65rem;
}

.rcp-top {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 0.5rem;
}
.rcp-cats { display: flex; gap: 0.4rem; flex-wrap: wrap; }
.rcp-cat-tag {
  background: rgba(232,160,39,0.1);
  color: var(--ca-saffron-dark);
  font-size: 0.72rem;
  font-weight: 600;
  padding: 0.2rem 0.6rem;
  border-radius: 100px;
  white-space: nowrap;
}

.rcp-rating {
  display: flex;
  align-items: center;
  gap: 0.25rem;
  font-size: 0.82rem;
  font-weight: 600;
  color: var(--ca-text-primary);
  white-space: nowrap;
  flex-shrink: 0;
}
.star-filled { color: #FBBF24; font-size: 0.9rem; }
.rcp-count { color: var(--ca-text-muted); font-weight: 400; }

.rcp-title {
  font-family: var(--font-display);
  font-size: 1.2rem;
  font-weight: 700;
  color: var(--ca-charcoal);
  line-height: 1.3;
  margin: 0;
}

.rcp-desc {
  font-size: 0.88rem;
  color: var(--ca-text-secondary);
  line-height: 1.6;
  flex: 1;
}

.rcp-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 0.75rem;
  margin-top: 0.5rem;
  padding-top: 1rem;
  border-top: 1px solid var(--ca-border);
}
.rcp-meta-row { display: flex; gap: 0.5rem; flex-wrap: wrap; }
.rcp-meta-pill {
  font-size: 0.75rem;
  color: var(--ca-text-secondary);
  background: var(--ca-cream);
  padding: 0.25rem 0.65rem;
  border-radius: 100px;
  white-space: nowrap;
}
.rcp-btn {
  background: var(--ca-charcoal);
  color: #fff;
  font-size: 0.82rem;
  font-weight: 700;
  padding: 0.5rem 1.1rem;
  border-radius: 100px;
  text-decoration: none;
  transition: background 0.2s, transform 0.2s;
  white-space: nowrap;
  flex-shrink: 0;
}
.rcp-btn:hover {
  background: var(--ca-saffron-dark);
  transform: translateX(3px);
}

/* Skeleton */
.skeleton { pointer-events: none; }
.skeleton-img { background: linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%); background-size: 200% 100%; animation: shimmer 1.5s infinite; }
.skeleton-line { height: 14px; background: #ebebeb; border-radius: 4px; animation: shimmer 1.5s infinite; }
.w-60 { width: 60%; }
.w-90 { width: 90%; }
.w-40 { width: 40%; }
.mt-8 { margin-top: 8px; }
@keyframes shimmer { 0% { background-position: 200% 0; } 100% { background-position: -200% 0; } }

.loading-state {
  text-align: center;
  padding: 4rem;
  color: var(--ca-text-secondary);
}
.spinner {
  width: 44px; height: 44px;
  border: 3px solid var(--ca-cream-warm);
  border-top-color: var(--ca-saffron);
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
  margin: 0 auto 1rem;
}
@keyframes spin { to { transform: rotate(360deg); } }

.section-cta { text-align: center; }
.cta-outlined {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  border: 2px solid var(--ca-charcoal);
  color: var(--ca-charcoal);
  font-weight: 700;
  font-size: 1rem;
  padding: 0.85rem 2.5rem;
  border-radius: 100px;
  text-decoration: none;
  transition: all 0.25s;
}
.cta-outlined:hover {
  background: var(--ca-charcoal);
  color: #fff;
  transform: translateY(-2px);
}
.arrow-right { transition: transform 0.2s; }
.cta-outlined:hover .arrow-right { transform: translateX(4px); }

/* ════════════════════════════════════════════════════════════
   MEAL PLANNER
════════════════════════════════════════════════════════════ */
.meal-planner-section {
  background: linear-gradient(160deg, var(--ca-charcoal) 0%, var(--ca-charcoal-soft) 100%);
  padding: 7rem 0;
  overflow: hidden;
}

.planner-container {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 5rem;
  align-items: center;
}

.planner-text .section-label {
  background: rgba(232,160,39,0.15);
  color: var(--ca-saffron-light);
}
.planner-text .section-title {
  color: #fff;
  text-align: left;
}
.planner-text .section-title em { color: var(--ca-saffron); }
.planner-text .section-subtitle {
  color: rgba(255,255,255,0.6);
  text-align: left;
  margin: 0 0 2rem;
  max-width: 100%;
}

.planner-features { list-style: none; display: flex; flex-direction: column; gap: 1.25rem; }
.planner-feature-item {
  display: flex;
  align-items: flex-start;
  gap: 1rem;
}
.pf-icon {
  width: 44px; height: 44px;
  background: rgba(232,160,39,0.12);
  border: 1px solid rgba(232,160,39,0.2);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.2rem;
  flex-shrink: 0;
}
.pf-title { color: #fff; font-weight: 600; font-size: 0.95rem; margin-bottom: 2px; }
.pf-desc { color: rgba(255,255,255,0.5); font-size: 0.85rem; }

/* Mock Planner UI */
.planner-visual {
  position: relative;
}

.planner-ui-mock {
  background: rgba(255,255,255,0.04);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255,255,255,0.1);
  border-radius: 24px;
  padding: 1.75rem;
  box-shadow: 0 24px 64px rgba(0,0,0,0.4);
}

.pui-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.25rem;
}
.pui-title { color: #fff; font-weight: 700; font-size: 0.95rem; }
.pui-week { color: rgba(255,255,255,0.4); font-size: 0.78rem; }

.pui-grid {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 0.5rem;
  margin-bottom: 1.25rem;
}
.pui-day-col { display: flex; flex-direction: column; gap: 0.5rem; }
.pui-day-label {
  text-align: center;
  color: rgba(255,255,255,0.4);
  font-size: 0.7rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.05em;
}
.pui-meal-slot {
  border-radius: 12px;
  padding: 0.6rem 0.3rem;
  min-height: 80px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 0.25rem;
  transition: all 0.2s;
}
.slot-filled {
  background: rgba(232,160,39,0.12);
  border: 1px solid rgba(232,160,39,0.25);
}
.slot-empty {
  background: rgba(255,255,255,0.03);
  border: 1px dashed rgba(255,255,255,0.1);
}
.slot-empty:hover { border-color: rgba(232,160,39,0.4); cursor: pointer; }
.pui-meal-emoji { font-size: 1.5rem; }
.pui-meal-name { color: rgba(255,255,255,0.7); font-size: 0.62rem; text-align: center; line-height: 1.3; }
.pui-add-btn {
  color: rgba(255,255,255,0.2);
  font-size: 1.5rem;
  line-height: 1;
}

.pui-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 1rem;
  border-top: 1px solid rgba(255,255,255,0.08);
}
.pui-tag {
  color: var(--ca-sage-light);
  font-size: 0.78rem;
  font-weight: 600;
}
.pui-grocery-btn {
  background: linear-gradient(135deg, var(--ca-sage), var(--ca-sage-dark));
  color: #fff;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 8px;
  font-size: 0.78rem;
  font-weight: 600;
  cursor: pointer;
  font-family: var(--font-body);
}

.planner-floating-card {
  position: absolute;
  background: rgba(255,255,255,0.08);
  backdrop-filter: blur(16px);
  border: 1px solid rgba(255,255,255,0.12);
  border-radius: 14px;
  padding: 0.85rem 1.1rem;
  display: flex;
  align-items: center;
  gap: 0.75rem;
  color: #fff;
  font-size: 0.8rem;
  box-shadow: 0 8px 32px rgba(0,0,0,0.25);
  animation: float 5s ease-in-out infinite;
}
.pf-card-1 { top: -20px; right: -30px; animation-delay: 0.5s; }
.pf-card-2 { bottom: -20px; left: -30px; animation-delay: 2.5s; }
.pf-card-1 span, .pf-card-2 span { font-size: 1.5rem; }
.pfc-title { font-weight: 600; font-size: 0.82rem; margin-bottom: 2px; }
.pfc-sub { color: rgba(255,255,255,0.45); font-size: 0.73rem; }

/* ════════════════════════════════════════════════════════════
   WHY SECTION
════════════════════════════════════════════════════════════ */
.why-section {
  background: var(--ca-cream-warm);
  padding: 7rem 0;
}

.why-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 1.75rem;
}

.why-card {
  background: #fff;
  border-radius: var(--ca-radius-md);
  padding: 2rem;
  border: 1px solid var(--ca-border);
  transition: all 0.3s;
}
.why-card:hover {
  transform: translateY(-6px);
  box-shadow: var(--ca-shadow-md);
  border-color: rgba(232,160,39,0.3);
}

.why-icon-wrap {
  font-size: 2rem;
  margin-bottom: 1rem;
  width: 56px; height: 56px;
  background: rgba(232,160,39,0.1);
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
}
.why-title {
  font-family: var(--font-display);
  font-size: 1.1rem;
  font-weight: 600;
  color: var(--ca-charcoal);
  margin-bottom: 0.5rem;
}
.why-desc { font-size: 0.88rem; color: var(--ca-text-secondary); line-height: 1.65; }

/* ════════════════════════════════════════════════════════════
   CTA BANNER
════════════════════════════════════════════════════════════ */
.cta-banner {
  background: linear-gradient(135deg, var(--ca-saffron-dark) 0%, var(--ca-saffron) 50%, var(--ca-saffron-light) 100%);
  padding: 5rem 2rem;
}
.cta-banner-inner {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 2rem;
  flex-wrap: wrap;
}
.cta-banner-text h2 {
  font-family: var(--font-display);
  font-size: clamp(1.6rem, 3vw, 2.4rem);
  color: #fff;
  font-weight: 700;
  margin-bottom: 0.5rem;
}
.cta-banner-text h2 em { font-style: italic; }
.cta-banner-text p { color: rgba(255,255,255,0.8); font-size: 1rem; }
.cta-banner-actions { display: flex; gap: 1rem; flex-wrap: wrap; }

.cta-ghost {
  display: inline-flex;
  align-items: center;
  background: rgba(255,255,255,0.15);
  color: #fff;
  font-weight: 700;
  font-size: 1rem;
  padding: 0.85rem 2rem;
  border-radius: 100px;
  text-decoration: none;
  border: 2px solid rgba(255,255,255,0.5);
  transition: all 0.25s;
}
.cta-ghost:hover {
  background: rgba(255,255,255,0.25);
  transform: translateY(-2px);
}
.cta-banner .cta-primary {
  background: #fff;
  color: var(--ca-saffron-dark);
  box-shadow: 0 8px 24px rgba(0,0,0,0.15);
}
.cta-banner .cta-primary:hover {
  box-shadow: 0 14px 36px rgba(0,0,0,0.2);
}

/* ════════════════════════════════════════════════════════════
   FOOTER
════════════════════════════════════════════════════════════ */
.site-footer {
  background: var(--ca-charcoal);
  padding: 3rem 0;
}
.footer-inner {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1rem;
  text-align: center;
}
.footer-brand {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: #fff;
}
.footer-tagline { color: rgba(255,255,255,0.4); font-size: 0.85rem; }
.footer-links {
  display: flex;
  gap: 2rem;
}
.footer-links a {
  color: rgba(255,255,255,0.5);
  font-size: 0.85rem;
  text-decoration: none;
  transition: color 0.2s;
}
.footer-links a:hover { color: var(--ca-saffron); }
.footer-copy { color: rgba(255,255,255,0.25); font-size: 0.78rem; }

/* ════════════════════════════════════════════════════════════
   RESPONSIVE
════════════════════════════════════════════════════════════ */
@media (max-width: 1024px) {
  .hero-content { grid-template-columns: 1fr; text-align: center; gap: 3rem; }
  .hero-visual { display: none; }
  .hero-ctas { justify-content: center; }
  .hero-stats { justify-content: center; }
  .hero-subtext { margin: 0 auto 2.5rem; }

  .planner-container { grid-template-columns: 1fr; gap: 3rem; }
  .planner-text .section-title,
  .planner-text .section-subtitle { text-align: center; }
  .planner-features { max-width: 480px; margin: 0 auto; }
  .planner-floating-card { display: none; }

  .cta-banner-inner { text-align: center; flex-direction: column; align-items: center; }
}

@media (max-width: 768px) {
  .hamburger { display: flex; }

  .nav-links {
    display: none;
    position: fixed;
    top: 0; left: 0; right: 0;
    background: rgba(30,31,34,0.98);
    backdrop-filter: blur(20px);
    flex-direction: column;
    align-items: center;
    justify-content: center;
    gap: 1.25rem;
    padding: 6rem 2rem 3rem;
    min-height: 100vh;
    z-index: 999;
  }
  .nav-links.nav-open { display: flex; }
  .nav-item { font-size: 1.2rem; }

  .hero { padding: 7rem 1.5rem 4rem; }
  .hero-headline { font-size: 2.2rem; }
  .hero-stats { gap: 1.25rem; }

  .category-strip .container { padding: 0 1rem; }

  .recipe-grid { grid-template-columns: 1fr; }
  .featured-recipes { padding: 4rem 0; }

  .meal-planner-section { padding: 4rem 0; }
  .pui-grid { grid-template-columns: repeat(7, 1fr); gap: 0.3rem; }
  .pui-meal-slot { min-height: 64px; padding: 0.4rem 0.15rem; }
  .pui-meal-emoji { font-size: 1.1rem; }
  .pui-meal-name { font-size: 0.55rem; }

  .why-section { padding: 4rem 0; }
  .why-grid { grid-template-columns: 1fr; }

  .cta-banner { padding: 3.5rem 1.5rem; }
}

@media (max-width: 480px) {
  .hero-ctas { flex-direction: column; align-items: center; }
  .cta-primary, .cta-secondary { width: 100%; justify-content: center; }
  .hero-stats { flex-direction: column; gap: 1rem; }
  .stat-divider { width: 50%; height: 1px; }
  .pui-grid { gap: 0.2rem; }
  .pui-day-label { font-size: 0.6rem; }
}
</style>