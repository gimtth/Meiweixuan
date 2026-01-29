<template>
  <div class="home-page">
    <!-- 首页轮播图（保留原有轮播动画功能） -->
    <div class="banner-section">
      <el-carousel :interval="4000" type="card" height="400px">
        <el-carousel-item v-for="item in banners" :key="item.id">
          <div class="banner-item">
            <img :src="item.image" :alt="item.title" class="banner-img">
            <div class="banner-content">
              <h1>{{ item.title }}</h1>
              <p>{{ item.description }}</p>
              <router-link to="/menu" class="btn">立即点餐</router-link>
            </div>
          </div>
        </el-carousel-item>
      </el-carousel>
    </div>

    <!-- 主要内容区 -->
    <main class="main-container">
      <!-- 菜单部分 -->
      <section id="menu">
        <div class="section-title big-title">
          <h2>我们的菜单</h2>
          <p>从开胃小菜到主菜甜点，我们提供丰富多样的美食选择，满足您的各种口味需求</p>
        </div>
        
        <div class="menu-categories big-categories">
          <button class="category-btn" :class="{active: currentCategory === 'all'}" @click="filterCategory('all')">全部</button>
          <button
            v-for="cat in categories"
            :key="cat.id"
            class="category-btn"
            :class="{active: currentCategory === cat.id}"
            @click="filterCategory(cat.id)"
          >
            {{ cat.name }}
          </button>
        </div>
        
        <div v-if="loading.featured" class="loading-container">
          <el-skeleton :rows="3" animated />
        </div>

        
        <!-- 菜品列表 - 网格布局 -->
        <div class="dishes-grid">
          <div class="dish-card" v-for="dish in displayedDishes" :key="dish.id">
            <div class="dish-image">
              <img :src="formatImageUrl(dish.image)" :alt="dish.name">
            </div>
            <div class="dish-info">
              <div class="dish-header">
                <h3 class="dish-name">{{ dish.name }}</h3>
                <div class="dish-price">¥{{ dish.price }}</div>
              </div>
              <p class="dish-desc">{{ dish.description }}</p>
              <div class="dish-footer">
                <div class="dish-rating">★★★★☆</div>
                <button class="add-cart-btn" @click="addToCartFixed(dish)">
                  <span v-if="!cartStore.loading">加入购物车</span>
                  <span v-else>添加中...</span>
                </button>
              </div>
            </div>
          </div>
        </div>
      </section>
      
      <!-- 特色区域 -->
      <section class="features">
        <div class="feature">
          <div class="feature-icon">
            <i class="fas fa-bolt"></i>
          </div>
          <h3>快速配送</h3>
          <p>承诺30分钟内送达，超时免单，让您的美食体验无需等待</p>
        </div>
        
        <div class="feature">
          <div class="feature-icon">
            <i class="fas fa-leaf"></i>
          </div>
          <h3>新鲜食材</h3>
          <p>每日精选新鲜食材，保证菜品品质，让您吃得放心</p>
        </div>
        
        <div class="feature">
          <div class="feature-icon">
            <i class="fas fa-award"></i>
          </div>
          <h3>专业厨师</h3>
          <p>拥有20年经验的主厨团队，传承经典烹饪技艺</p>
        </div>
        
        <div class="feature">
          <div class="feature-icon">
            <i class="fas fa-percent"></i>
          </div>
          <h3>会员优惠</h3>
          <p>注册会员享9折优惠，积分可兑换精美礼品</p>
        </div>
      </section>

      <!-- 管理员后台入口按钮 (临时) -->
      
    </main>
  </div>
</template>

<script>
import { ref, reactive, onMounted, computed } from 'vue';
import { useCartStore } from '../store/cart';
import dishApi from '../api/dishApi';
import bannerApi from '../api/bannerApi';
import BannerImage from '../assets/banner.png';
import dishPlaceholder from '../assets/images/placeholders/dish-placeholder.jpg';
import { ElMessage } from 'element-plus';

export default {
  name: 'HomePage',
  setup() {
    const cartStore = useCartStore();
    const defaultImage = dishPlaceholder;
    
    const loading = reactive({
      banners: true,
      featured: true,
      categories: true
    });
    
    // 轮播图数据改为从API获取
    const banners = ref([
      {
        title: '美味轩特色美食',
        description: '精选优质食材，传承经典烹饪技艺，为您带来最正宗的中餐体验。现在下单，享受30分钟内快速送达服务。',
        image: BannerImage
      }
    ]);
    
    // 获取轮播图数据
    const fetchBanners = async () => {
      try {
        loading.banners = true;
        const result = await bannerApi.getBanners();
        // 如果API返回数据，则使用API数据，否则保留默认数据
        if (result && result.length > 0) {
          // 修正图片路径，添加基础URL
          banners.value = result.map(banner => ({
            ...banner,
            image: banner.image.startsWith('http') ? banner.image : `http://localhost:8082/api/${banner.image.startsWith('/') ? banner.image.substring(1) : banner.image}`
          }));
        } else {
          // 使用默认轮播图数据
          banners.value = [
            {
              id: 1,
              title: '美味佳肴，尽在掌握',
              description: '享受我们精心烹制的各式美食，随时随地下单',
              image: 'http://localhost:8082/api/images/dishes/banner1.jpg',
              link: '/menu'
            },
            {
              id: 2,
              title: '限时优惠，立享折扣',
              description: '多种优惠活动同步进行，下单立减',
              image: 'http://localhost:8082/api/images/dishes/banner2.jpg',
              link: '/activity'
            },
            {
              id: 3,
              title: '新品上市',
              description: '尝鲜新品，享受美食盛宴',
              image: 'http://localhost:8082/api/images/dishes/banner3.jpg',
              link: '/menu'
            }
          ];
        }
      } catch (err) {
        console.error('获取轮播图数据失败:', err);
        // 使用默认轮播图数据
        banners.value = [
          {
            id: 1,
            title: '美味佳肴，尽在掌握',
            description: '享受我们精心烹制的各式美食，随时随地下单',
            image: 'http://localhost:8082/api/images/dishes/banner1.jpg',
            link: '/menu'
          },
          {
            id: 2,
            title: '限时优惠，立享折扣',
            description: '多种优惠活动同步进行，下单立减',
            image: 'http://localhost:8082/api/images/dishes/banner2.jpg',
            link: '/activity'
          },
          {
            id: 3,
            title: '新品上市',
            description: '尝鲜新品，享受美食盛宴',
            image: 'http://localhost:8082/api/images/dishes/banner3.jpg',
            link: '/menu'
          }
        ];
      } finally {
        loading.banners = false;
      }
    };
    
    const featuredDishes = ref([]); // 全部菜品
    const categories = ref([]);
    const currentCategory = ref('all');

    // 计算属性：根据当前分类筛选菜品
    const displayedDishes = computed(() => {
      if (currentCategory.value === 'all') {
        return featuredDishes.value;
      } else {
        // 注意类型统一
        return featuredDishes.value.filter(dish => dish.categoryId === Number(currentCategory.value));
      }
    });

    const filterCategory = (categoryId) => {
      currentCategory.value = categoryId === 'all' ? 'all' : Number(categoryId);
    };

    // 获取全部菜品
    const fetchFeaturedDishes = async () => {
      try {
        loading.featured = true;
        const result = await dishApi.getAllDishes();
        if (result && result.length > 0) {
          featuredDishes.value = result;
        } else {
          featuredDishes.value = [];
        }
      } catch (err) {
        console.error('获取菜品失败:', err);
        featuredDishes.value = [];
      } finally {
        loading.featured = false;
      }
    };

    // 获取全部分类
    const fetchCategories = async () => {
      try {
        loading.categories = true;
        const result = await dishApi.getCategories();
        if (result && result.length > 0) {
          categories.value = result;
        } else {
          categories.value = [];
        }
      } catch (err) {
        console.error('获取菜品分类失败:', err);
        categories.value = [];
      } finally {
        loading.categories = false;
      }
    };
    
    // 添加到购物车
    const addToCart = async (dish) => {
      try {
        console.log("开始添加到购物车:", dish);
        await cartStore.addToCart(dish, 1);
      } catch (err) {
        console.error("添加到购物车失败:", err);
        ElMessage.error('添加到购物车失败');
      }
    };
    
    // 添加固定菜品到购物车
    const addToCartFixed = async (dish) => {
      try {
        console.log("开始添加固定菜品到购物车:", dish);
        // 确保dish中有所有必要属性
        if (!dish.id) {
          ElMessage.error('菜品数据不完整');
          return;
        }
        await cartStore.addToCart(dish, 1);
      } catch (err) {
        console.error("添加固定菜品到购物车失败:", err);
        ElMessage.error('添加到购物车失败');
      }
    };
    
    // 获取分类图标
    const getCategoryIcon = (categoryName) => {
      const icons = {
        '热菜': 'el-icon-hot-water',
        '凉菜': 'el-icon-cold-drink',
        '主食': 'el-icon-food',
        '小吃': 'el-icon-dessert',
        '饮品': 'el-icon-coffee',
        '甜点': 'el-icon-ice-cream',
        '汤品': 'el-icon-dish'
      };
      
      // 根据分类名匹配图标，如果没有匹配则使用默认图标
      return icons[categoryName] || 'el-icon-dish-1';
    };
    
    // 文本截断函数
    const truncate = (text, length) => {
      if (!text) return '';
      return text.length > length ? text.slice(0, length) + '...' : text;
    };
    
    // 图片路径处理，自动补全 /api 前缀
    const formatImageUrl = (url) => {
      if (!url) return defaultImage;
      if (url.startsWith('http')) return url;
      if (!url.startsWith('/api/')) return '/api' + url;
      return url;
    };
    
    onMounted(() => {
      fetchBanners();
      fetchFeaturedDishes();
      fetchCategories();
    });
    
    return {
      banners,
      featuredDishes,
      displayedDishes,
      categories,
      currentCategory,
      loading,
      defaultImage,
      cartStore,
      addToCart,
      addToCartFixed,
      getCategoryIcon,
      truncate,
      filterCategory,
      formatImageUrl
    };
  }
};
</script>

<style scoped>
.home-page {
  padding-bottom: 40px;
}

.main-container {
  width: 90%;
  max-width: 1200px;
  margin: 40px auto;
}

.banner-section {
  margin: 20px 0 40px 0;
}

.banner-item {
  height: 100%;
  border-radius: 8px;
  display: flex;
  align-items: center;
  position: relative;
  overflow: hidden;
}

.banner-img {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  z-index: 0;
}

.banner-item::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(to right, rgba(0,0,0,0.7) 0%, rgba(0,0,0,0.3) 50%, rgba(0,0,0,0) 100%);
  border-radius: 8px;
  z-index: 1;
}

.banner-content {
  position: relative;
  z-index: 2;
  padding: 0 40px;
  max-width: 500px;
  color: white;
}

.banner-content h1 {
  font-size: 48px;
  margin-bottom: 20px;
}

.banner-content p {
  font-size: 20px;
  margin-bottom: 30px;
}

.btn {
  display: inline-block;
  padding: 12px 30px;
  background-color: #e74c3c;
  color: white;
  border-radius: 30px;
  font-weight: bold;
  text-decoration: none;
  transition: all 0.3s ease;
}

.btn:hover {
  background-color: #c0392b;
  transform: translateY(-2px);
}

.section-title {
  text-align: center;
  margin-bottom: 40px;
}

/* 放大标题 */
.big-title h2 {
  font-size: 42px;
  color: #333;
  margin-bottom: 20px;
  font-weight: bold;
}

.big-title p {
  font-size: 18px;
  color: #777;
  max-width: 800px;
  margin: 0 auto;
}

/* 菜单分类 */
.menu-categories {
  display: flex;
  justify-content: center;
  margin-bottom: 30px;
  flex-wrap: wrap;
}

/* 放大分类按钮 */
.big-categories {
  margin-bottom: 50px;
}

.big-categories .category-btn {
  padding: 12px 25px;
  margin: 0 15px 15px 0;
  font-size: 16px;
  font-weight: 500;
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

/* 菜品网格布局 - 更新为图片列表格式 */
.dishes-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 30px;
}

.dish-card {
  background-color: white;
  border-radius: 10px;
  overflow: hidden;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  animation: fadeIn 0.5s ease-out;
  display: flex;
  flex-direction: column;
}

.dish-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.15);
}

.dish-image {
  height: 250px;
  overflow: hidden;
}

.dish-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.dish-card:hover .dish-image img {
  transform: scale(1.1);
}

.dish-info {
  padding: 15px;
  display: flex;
  flex-direction: column;
  flex: 1;
}

.dish-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.dish-name {
  font-size: 20px;
  font-weight: bold;
  margin: 0;
  color: #333;
}

.dish-price {
  font-size: 20px;
  font-weight: bold;
  color: #e74c3c;
}

.dish-desc {
  color: #666;
  margin-bottom: 20px;
  line-height: 1.5;
  flex-grow: 1;
  font-size: 14px;
}

.dish-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: auto;
}

.dish-rating {
  color: #f39c12;
  font-size: 14px;
}

.add-cart-btn {
  background-color: #e74c3c;
  color: white;
  border: none;
  padding: 8px 15px;
  border-radius: 20px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.3s ease;
}

.add-cart-btn:hover {
  background-color: #c0392b;
  transform: scale(1.05);
}

/* 特色区域 */
.features {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 30px;
  margin: 60px 0;
}

.feature {
  text-align: center;
  padding: 30px 20px;
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.feature:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
}

.feature-icon {
  font-size: 40px;
  color: #e74c3c;
  margin-bottom: 20px;
}

.feature h3 {
  margin-bottom: 15px;
  font-size: 20px;
}

.feature p {
  color: #777;
}

.loading-container,
.empty-message {
  padding: 30px;
  text-align: center;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.empty-message {
  color: #999;
}

.admin-entry-btn {
  display: inline-block;
  background-color: #2c3e50;
  color: white;
  padding: 12px 25px;
  border-radius: 5px;
  text-decoration: none;
  font-weight: bold;
  transition: all 0.3s ease;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.admin-entry-btn i {
  margin-right: 8px;
}

.admin-entry-btn:hover {
  background-color: #34495e;
  transform: translateY(-3px);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
}

/* 响应式设计修改 */
@media (max-width: 768px) {
  .dishes-grid {
    grid-template-columns: 1fr;
  }
  
  .dish-card {
    max-width: 100%;
  }
  
  .dish-footer {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
  
  .add-cart-btn {
    width: 100%;
    text-align: center;
  }
}
</style>