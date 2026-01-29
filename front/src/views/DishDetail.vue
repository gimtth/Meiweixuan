<template>
  <div class="dish-detail-page">
    <div class="container">
      <!-- 加载中状态 -->
      <div v-if="loading" class="loading-state">
        <p>加载中...</p>
      </div>
      
      <!-- 错误状态 -->
      <div v-else-if="error" class="error-state">
        <p>{{ error }}</p>
        <button class="btn-secondary" @click="fetchDishDetails">重试</button>
      </div>
      
      <!-- 菜品详情内容 -->
      <div v-else-if="dish" class="dish-detail-content">
        <div class="dish-media">
          <img :src="dish.imageUrl || restaurantPlaceholder" :alt="dish.name">
        </div>
        
        <div class="dish-info">
          <h1 class="dish-name">{{ dish.name }}</h1>
          
          <div class="dish-meta">
            <span class="dish-category">分类：{{ dish.category ? dish.category.name : '未分类' }}</span>
            <span class="dish-stock" :class="{ 'out-of-stock': dish.stock <= 0 }">
              {{ dish.stock > 0 ? `库存：${dish.stock}` : '已售罄' }}
            </span>
          </div>
          
          <div class="dish-price">
            <span class="current-price">¥{{ dish.price.toFixed(2) }}</span>
          </div>
          
          <div class="dish-description">
            <h3>菜品描述</h3>
            <p>{{ dish.description || '暂无描述' }}</p>
          </div>
          
          <div class="dish-actions">
            <div class="quantity-control">
              <button class="quantity-btn" @click="decreaseQuantity" :disabled="quantity <= 1">-</button>
              <input type="number" v-model.number="quantity" min="1" :max="dish.stock" :disabled="dish.stock <= 0">
              <button class="quantity-btn" @click="increaseQuantity" :disabled="dish.stock <= 0 || quantity >= dish.stock">+</button>
            </div>
            
            <button 
              class="add-to-cart-btn" 
              @click="addToCart" 
              :disabled="dish.stock <= 0"
            >
              <i class="icon-cart"></i>
              加入购物车
            </button>
          </div>
        </div>
      </div>
      
      <!-- 返回菜单按钮 -->
      <div class="back-to-menu">
        <router-link to="/menu" class="btn-secondary">
          <i class="icon-back"></i>
          返回菜单
        </router-link>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useCartStore } from '../store/cart';
import dishApi from '../api/dishApi';
import { ElMessage } from 'element-plus';
import restaurantPlaceholder from '../assets/images/placeholders/restaurant-placeholder.jpg';

export default {
  name: 'DishDetailPage',
  setup() {
    const route = useRoute();
    const router = useRouter();
    const cartStore = useCartStore();
    
    const dish = ref(null);
    const loading = ref(true);
    const error = ref(null);
    const quantity = ref(1);
    
    const fetchDishDetails = async () => {
      const dishId = route.params.id;
      
      if (!dishId) {
        router.push('/menu');
        return;
      }
      
      try {
        loading.value = true;
        error.value = null;
        
        dish.value = await dishApi.getDishById(Number(dishId));
        
      } catch (err) {
        error.value = '获取菜品信息失败，请稍后再试';
        console.error('Failed to fetch dish details:', err);
      } finally {
        loading.value = false;
      }
    };
    
    const increaseQuantity = () => {
      if (dish.value && quantity.value < dish.value.stock) {
        quantity.value++;
      }
    };
    
    const decreaseQuantity = () => {
      if (quantity.value > 1) {
        quantity.value--;
      }
    };
    
    const addToCart = async () => {
      if (dish.value && dish.value.stock > 0) {
        try {
          await cartStore.addToCart(dish.value.id, quantity.value);
          ElMessage.success(`已将${quantity.value}份${dish.value.name}加入购物车`);
        } catch (err) {
          ElMessage.error('加入购物车失败，请稍后再试');
        }
      }
    };
    
    onMounted(fetchDishDetails);
    
    return {
      dish,
      loading,
      error,
      quantity,
      fetchDishDetails,
      increaseQuantity,
      decreaseQuantity,
      addToCart,
      restaurantPlaceholder
    };
  }
};
</script>

<style scoped>
.dish-detail-page {
  padding: 40px 0;
}

.loading-state,
.error-state {
  text-align: center;
  padding: 50px 0;
  font-size: 16px;
  color: #666;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.btn-secondary {
  display: inline-block;
  background-color: #f5f5f5;
  color: #333;
  padding: 8px 15px;
  border-radius: 4px;
  border: none;
  text-decoration: none;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.3s;
  margin-top: 10px;
}

.btn-secondary:hover {
  background-color: #e0e0e0;
}

.dish-detail-content {
  display: flex;
  flex-wrap: wrap;
  gap: 30px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 30px;
  margin-bottom: 20px;
}

.dish-media {
  flex: 0 0 45%;
}

.dish-media img {
  width: 100%;
  border-radius: 8px;
  object-fit: cover;
}

.dish-info {
  flex: 1;
}

.dish-name {
  font-size: 32px;
  margin: 0 0 15px;
  color: #333;
}

.dish-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  margin-bottom: 20px;
  font-size: 14px;
  color: #666;
}

.dish-category,
.dish-stock {
  padding: 5px 10px;
  background-color: #f5f5f5;
  border-radius: 4px;
}

.out-of-stock {
  color: white;
  background-color: #9e9e9e;
}

.dish-price {
  margin-bottom: 20px;
}

.current-price {
  font-size: 28px;
  font-weight: bold;
  color: var(--primary-color, #e53935);
}

.dish-description {
  margin-bottom: 30px;
}

.dish-description h3 {
  font-size: 18px;
  margin: 0 0 10px;
  color: #333;
}

.dish-description p {
  font-size: 15px;
  line-height: 1.6;
  color: #666;
}

.dish-actions {
  display: flex;
  align-items: center;
  gap: 20px;
}

.quantity-control {
  display: flex;
  align-items: center;
  border: 1px solid #ddd;
  border-radius: 4px;
  overflow: hidden;
}

.quantity-btn {
  width: 36px;
  height: 36px;
  border: none;
  background-color: #f5f5f5;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.quantity-btn:hover:not(:disabled) {
  background-color: #e0e0e0;
}

.quantity-btn:disabled {
  color: #ccc;
  cursor: not-allowed;
}

.quantity-control input {
  width: 50px;
  height: 36px;
  text-align: center;
  border: none;
  border-left: 1px solid #ddd;
  border-right: 1px solid #ddd;
  font-size: 16px;
  
}

.quantity-control input::-webkit-outer-spin-button,
.quantity-control input::-webkit-inner-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

.add-to-cart-btn {
  flex-grow: 1;
  background-color: var(--primary-color, #e53935);
  color: white;
  border: none;
  border-radius: 4px;
  padding: 10px 15px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s;
  display: flex;
  align-items: center;
  justify-content: center;
}

.add-to-cart-btn:hover:not(:disabled) {
  background-color: var(--primary-dark, #c62828);
}

.add-to-cart-btn:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.icon-cart {
  display: inline-block;
  width: 20px;
  height: 20px;
  margin-right: 8px;
  background-image: url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="white"><path d="M7 18c-1.1 0-1.99.9-1.99 2S5.9 22 7 22s2-.9 2-2-.9-2-2-2zM1 3c0 .55.45 1 1 1h1l3.6 7.59-1.35 2.44C4.52 15.37 5.48 17 7 17h11c.55 0 1-.45 1-1s-.45-1-1-1H7l1.1-2h7.45c.75 0 1.41-.41 1.75-1.03l3.58-6.49c.37-.66-.11-1.48-.87-1.48H5.21l-.67-1.43c-.16-.35-.52-.57-.9-.57H2c-.55 0-1 .45-1 1zm16 15c-1.1 0-1.99.9-1.99 2s.89 2 1.99 2 2-.9 2-2-.9-2-2-2z"/></svg>');
  background-repeat: no-repeat;
  background-position: center;
}

.back-to-menu {
  margin-top: 20px;
}

.icon-back {
  display: inline-block;
  width: 16px;
  height: 16px;
  margin-right: 5px;
  background-image: url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="%23333"><path d="M20 11H7.83l5.59-5.59L12 4l-8 8 8 8 1.41-1.41L7.83 13H20v-2z"/></svg>');
  background-repeat: no-repeat;
  background-position: center;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .dish-detail-content {
    flex-direction: column;
  }
  
  .dish-media {
    flex: 0 0 100%;
  }
}
</style> 