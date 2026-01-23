<script setup lang="ts">
import { computed, ref } from 'vue';

// Define props with defaults
const props = withDefaults(defineProps<{
  modelValue?: number;
  max?: number;
  editable?: boolean;
  size?: string;
}>(), {
  modelValue: 0,
  max: 5,
  editable: false,
  size: '24px'
});

// Define emits
const emit = defineEmits<{
  (e: 'update:modelValue', value: number): void;
}>();

// Derived readonly state (for internal logic clarity)
const isReadonly = computed(() => !props.editable);

// State for hover effect
const hoverValue = ref(0);

// Computed property to determine which value to display (hover or actual)
const displayValue = computed(() => {
  return hoverValue.value > 0 ? hoverValue.value : props.modelValue;
});

// Handle mouse events
const handleMouseEnter = (star: number) => {
  if (props.editable) {
    hoverValue.value = star;
  }
};

const handleMouseLeave = () => {
  if (props.editable) {
    hoverValue.value = 0;
  }
};

const handleClick = (star: number) => {
  if (props.editable) {
    emit('update:modelValue', star);
  }
};
</script>

<template>
  <div 
    class="star-rating" 
    :class="{ 'is-readonly': isReadonly, 'is-editable': editable }"
    @mouseleave="handleMouseLeave"
    role="slider"
    :aria-valuenow="modelValue"
    :aria-valuemin="0"
    :aria-valuemax="max"
    aria-label="Star rating"
  >
    <button
      v-for="star in max"
      :key="star"
      type="button"
      class="star-btn"
      :class="{ 'active': star <= displayValue, 'is-animating': editable && star === hoverValue }"
      :style="{ width: size, height: size }"
      @mouseenter="handleMouseEnter(star)"
      @click="handleClick(star)"
      :disabled="isReadonly"
      :aria-label="`Rate ${star} out of ${max} stars`"
    >
      <svg 
        viewBox="0 0 24 24" 
        xmlns="http://www.w3.org/2000/svg" 
        fill="currentColor"
        class="star-icon"
      >
        <path d="M12 17.27L18.18 21l-1.64-7.03L22 9.24l-7.19-.61L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21z"/>
      </svg>
    </button>
  </div>
</template>

<style scoped>
.star-rating {
  display: inline-flex;
  align-items: center;
  gap: 2px;
}

.star-btn {
  background: none;
  border: none;
  padding: 0;
  margin: 0;
  cursor: pointer;
  color: #e4e5e9; /* Inactive color (light gray) */
  transition: color 0.2s ease, transform 0.1s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.star-btn.active {
  color: #ffc107; /* Gold color */
}

/* Hover effects only for editable state */
.star-btn:not(:disabled):hover {
  transform: scale(1.1);
}

.is-readonly .star-btn {
  cursor: default;
}

/* Premium glow effect on hover for the active stars when editable */
.star-rating.is-editable .star-btn.active {
  filter: drop-shadow(0 0 2px rgba(255, 193, 7, 0.5));
}

.star-icon {
  width: 100%;
  height: 100%;
}
</style>