<template>
  <div class="dish-card">
    <div class="dish-image">
      <img v-if="dish.image" :src="dish.image" :alt="dish.name" />
      <div v-else class="placeholder-image">
        <span>{{ dish.name.substr(0, 1) }}</span>
      </div>
      <div class="dish-badge" v-if="dish.hot">热销</div>
    </div>
    
    <div class="dish-info">
      <h3 class="dish-name">{{ dish.name }}</h3>
      <p class="dish-description">{{ dishDescription }}</p>
      
      <div class="dish-footer">
        <div class="dish-price">
          <span class="current-price">¥{{ dish.price.toFixed(2) }}</span>
          <span v-if="dish.originalPrice" class="original-price">¥{{ dish.originalPrice.toFixed(2) }}</span>
        </div>
        
        <div class="dish-actions">
          <router-link :to="`/dish/${dish.id}`" class="detail-link">
            <i class="el-icon-info"></i>
          </router-link>
          
          <button class="add-to-cart-btn" @click.stop="addToCart">
            <i class="el-icon-shopping-cart-2"></i>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { computed } from 'vue';

export default {
  name: 'DishCard',
  props: {
    dish: {
      type: Object,
      required: true
    }
  },
  setup(props, { emit }) {
    const dishDescription = computed(() => {
      return props.dish.description 
        ? (props.dish.description.length > 50 
            ? props.dish.description.slice(0, 50) + '...' 
            : props.dish.description)
        : '暂无描述';
    });
    
    const addToCart = (event) => {
      event.preventDefault();
      emit('add-to-cart', props.dish, 1);
    };
    
    return {
      dishDescription,
      addToCart
    };
  }
};
</script>

<style scoped>
.dish-card {
  background-color: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s, box-shadow 0.3s;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.dish-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.dish-image {
  position: relative;
  padding-top: 60%;
  overflow: hidden;
}

.dish-image img {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
}

.placeholder-image {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: #e0e0e0;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 36px;
  font-weight: bold;
  color: #999;
}

.dish-badge {
  position: absolute;
  top: 10px;
  right: 10px;
  background-color: var(--primary-color, #e53935);
  color: white;
  font-size: 12px;
  font-weight: 500;
  padding: 4px 8px;
  border-radius: 4px;
}

.dish-info {
  padding: 15px;
  display: flex;
  flex-direction: column;
  flex-grow: 1;
}

.dish-name {
  font-size: 18px;
  font-weight: 600;
  margin: 0 0 8px;
  color: #333;
}

.dish-description {
  font-size: 14px;
  color: #666;
  margin: 0 0 15px;
  line-height: 1.4;
  flex-grow: 1;
}

.dish-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: auto;
}

.dish-price {
  display: flex;
  flex-direction: column;
}

.current-price {
  font-size: 18px;
  font-weight: 600;
  color: var(--primary-color, #e53935);
}

.original-price {
  font-size: 12px;
  color: #999;
  text-decoration: line-through;
  margin-top: 2px;
}

.dish-actions {
  display: flex;
  gap: 8px;
}

.detail-link,
.add-to-cart-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  border-radius: 50%;
  cursor: pointer;
  transition: background-color 0.3s;
  border: none;
  background-color: #f5f5f5;
  color: #666;
}

.detail-link:hover {
  background-color: #e0e0e0;
  color: #333;
}

.add-to-cart-btn {
  background-color: var(--primary-color, #e53935);
  color: white;
}

.add-to-cart-btn:hover {
  background-color: var(--primary-dark, #c62828);
}

.dish-card:hover .dish-image img {
  transform: scale(1.05);
}
</style> 