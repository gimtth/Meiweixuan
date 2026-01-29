<template>
  <div class="menu-page">
    <div class="banner-section">
      <div class="banner-content">
        <h1>我们的菜单</h1>
        <p>精选优质食材，传承经典烹饪技艺，为您带来最正宗的中餐体验</p>
      </div>
    </div>

    <main class="main-container">
      <div class="section-title">
        <h1>美味轩特色菜品</h1>
        <p>从开胃小菜到主菜甜点，我们提供丰富多样的美食选择，满足您的各种口味需求</p>
      </div>
      
      <div class="menu-categories">
        <button class="category-btn" :class="{ active: currentCategory === 'all' }" @click="filterCategory('all')">全部</button>
        <button class="category-btn" :class="{ active: currentCategory === 'hot' }" @click="filterCategory('hot')">热门推荐</button>
        <button class="category-btn" :class="{ active: currentCategory === 'special' }" @click="filterCategory('special')">特色炒菜</button>
        <button class="category-btn" :class="{ active: currentCategory === 'soup' }" @click="filterCategory('soup')">汤类</button>
        <button class="category-btn" :class="{ active: currentCategory === 'staple' }" @click="filterCategory('staple')">主食</button>
        <button class="category-btn" :class="{ active: currentCategory === 'drink' }" @click="filterCategory('drink')">饮品</button>
      </div>

      <div v-if="loading" class="loading-container">
        <el-skeleton :rows="3" animated />
      </div>


      <!-- 菜单项 -->
      <div class="menu-items">
        <div v-if="displayedDishes.length === 0" class="empty-message">暂无菜品</div>
        <div v-for="dish in displayedDishes" :key="dish.id" class="menu-item">
          <div class="item-image">
            <img :src="dish.image || defaultImage" :alt="dish.name" class="dish-img">
          </div>
          <div class="item-info">
            <div class="item-header">
              <div class="item-name">{{ dish.name }}</div>
              <div class="item-price">¥{{ dish.price.toFixed(2) }}</div>
            </div>
            <div class="item-description">
              {{ dish.description }}
            </div>
            <div class="item-footer">
              <div class="item-rating">★★★★☆ (128)</div>
              <button class="add-to-cart" @click="addToCart(dish)">加入购物车</button>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
import { ref, reactive, computed, onMounted } from 'vue';
import { useCartStore } from '../store/cart';
import { ElMessage } from 'element-plus';
import restaurantPlaceholder from '../assets/images/placeholders/restaurant-placeholder.jpg';
import menuBgImage from '../assets/images/menu-bg.jpg';
import dishApi from '../api/dishApi';

export default {
  name: 'MenuPage',
  setup() {
    const cartStore = useCartStore();
    const loading = ref(false);
    const dishes = ref([]);
    const currentCategory = ref('all');
    const defaultImage = restaurantPlaceholder;

    // 从后端获取菜品数据（展示所有菜品）
    const fetchDishes = async () => {
      try {
        loading.value = true;
        const response = await dishApi.getAllDishes();
        console.log('后端返回菜品数据:', response); // 调试输出
        dishes.value = response;
      } catch (error) {
        console.error('获取菜品列表失败:', error);
        ElMessage.error('获取菜品列表失败，请稍后再试');
      } finally {
        loading.value = false;
      }
    };

    // 分类ID映射（与数据库分类保持一致）
    const categoryIdMap = {
      hot: 1,        // 热门推荐
      special: 2,    // 特色炒菜
      soup: 3,       // 汤类
      staple: 4,     // 主食
      drink: 5       // 饮品
    };

    // 分类切换方法（修复未定义问题）
    const filterCategory = (category) => {
      currentCategory.value = category;
    };

    // 根据当前选择的分类筛选菜品
    const displayedDishes = computed(() => {
      if (currentCategory.value === 'all') {
        return dishes.value;
      } else {
        return dishes.value.filter(dish => dish.categoryId === categoryIdMap[currentCategory.value]);
      }
    });

    // 添加到购物车
    const addToCart = (dish) => {
      cartStore.addToCart(dish);
      ElMessage.success(`已将 ${dish.name} 加入购物车`);
    };

    onMounted(() => {
      fetchDishes();
    });

    return {
      loading,
      dishes,
      displayedDishes,
      currentCategory,
      defaultImage,
      filterCategory,
      addToCart,
      menuBgImage
    };
  }
};
</script>

<style scoped>
.menu-page {
  padding-bottom: 60px;
}

.banner-section {
  background: linear-gradient(rgba(0, 0, 0, 0.6), rgba(0, 0, 0, 0.6)), url('../assets/images/menu-bg.jpg');
  background-size: cover;
  background-position: center;
  height: 300px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  text-align: center;
  margin-bottom: 40px;
}

.banner-content {
  width: 90%;
  max-width: 1200px;
  margin: 0 auto;
}

.banner-content h1 {
  font-size: 48px;
  margin-bottom: 20px;
}

.banner-content p {
  font-size: 20px;
  margin-bottom: 30px;
  max-width: 700px;
  margin-left: auto;
  margin-right: auto;
}

.main-container {
  width: 90%;
  max-width: 1200px;
  margin: 40px auto;
}

.section-title {
  text-align: center;
  margin-bottom: 40px;
}

.section-title h2 {
  font-size: 32px;
  color: #333;
  margin-bottom: 15px;
}

.section-title p {
  color: #777;
  max-width: 700px;
  margin: 0 auto;
}

.menu-categories {
  display: flex;
  justify-content: center;
  margin-bottom: 30px;
  flex-wrap: wrap;
}

.category-btn {
  padding: 10px 20px;
  margin: 0 10px 10px 0;
  background-color: white;
  border: 1px solid #ddd;
  border-radius: 30px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.category-btn:hover, .category-btn.active {
  background-color: #e74c3c;
  color: white;
  border-color: #e74c3c;
}

.loading-container {
  padding: 40px 0;
}

.empty-message {
  text-align: center;
  padding: 40px 0;
  color: #777;
  font-size: 18px;
}

.menu-items {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 30px;
}

.menu-item {
  background-color: white;
  border-radius: 10px;
  overflow: hidden;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.menu-item:hover {
  transform: translateY(-10px);
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.15);
}

.item-image {
  height: 250px;
  background-size: cover;
  background-position: center;
}

.item-info {
  padding: 15px;
}

.item-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}

.item-name {
  font-size: 20px;
  font-weight: bold;
}

.item-price {
  color: #e74c3c;
  font-weight: bold;
  font-size: 20px;
}

.item-description {
  color: #777;
  margin-bottom: 20px;
  font-size: 14px;
}

.item-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.item-rating {
  color: #f39c12;
  font-weight: bold;
}

.add-to-cart {
  background-color: #e74c3c;
  color: white;
  border: none;
  padding: 8px 15px;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.add-to-cart:hover {
  background-color: #c0392b;
}

.dish-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 8px;
}

@media (max-width: 768px) {
  .banner-section {
    height: 250px;
  }
  
  .banner-content h1 {
    font-size: 36px;
  }
  
  .banner-content p {
    font-size: 16px;
  }
  
  .menu-items {
    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
    gap: 20px;
  }
}
</style> 