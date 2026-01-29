<template>
  <div class="category-list">
    <h2 class="category-title">菜品分类</h2>
    
    <ul class="category-items">
      <li 
        class="category-item" 
        :class="{ active: selectedCategoryId === null }"
        @click="selectCategory(null)"
      >
        全部菜品
      </li>
      
      <li 
        v-for="category in categories" 
        :key="category.id"
        class="category-item"
        :class="{ active: selectedCategoryId === category.id }"
        @click="selectCategory(category.id)"
      >
        {{ category.name }}
        <span class="category-count" v-if="category.dishCount">({{ category.dishCount }})</span>
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  name: 'CategoryList',
  props: {
    categories: {
      type: Array,
      required: true
    },
    selectedCategoryId: {
      type: Number,
      default: null
    }
  },
  setup(props, { emit }) {
    const selectCategory = (categoryId) => {
      emit('category-selected', categoryId);
    };
    
    return {
      selectCategory
    };
  }
};
</script>

<style scoped>
.category-list {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.category-title {
  font-size: 18px;
  color: #333;
  margin: 0 0 15px;
  padding-bottom: 10px;
  border-bottom: 1px solid #eee;
}

.category-items {
  list-style: none;
  padding: 0;
  margin: 0;
}

.category-item {
  padding: 10px 15px;
  margin-bottom: 5px;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s, color 0.3s;
  font-size: 15px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.category-item:hover {
  background-color: #f5f5f5;
}

.category-item.active {
  background-color: var(--primary-color, #e53935);
  color: white;
}

.category-item.active .category-count {
  background-color: rgba(255, 255, 255, 0.2);
}

.category-count {
  font-size: 12px;
  background-color: #f0f0f0;
  border-radius: 10px;
  padding: 2px 6px;
}
</style> 