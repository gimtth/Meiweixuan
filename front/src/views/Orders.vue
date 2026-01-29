<template>
  <div class="orders-page">
    <div class="container">
      <div class="page-title">
        <h1>我的订单</h1>
      </div>
      
      <!-- 加载中状态 -->
      <div v-if="loading" class="loading-state">
        <p>加载中...</p>
      </div>
      
      <!-- 错误状态 -->
      <div v-else-if="error" class="error-state">
        <p>{{ error }}</p>
        <button class="btn-primary" @click="fetchOrders">重试</button>
      </div>
      
      <!-- 订单为空状态 -->
      <div v-else-if="orders.length === 0" class="empty-state">
        <div class="empty-icon"></div>
        <h2>暂无订单</h2>
        <p>您还没有下过订单，快去点餐吧</p>
        <router-link to="/menu" class="btn-primary">去点餐</router-link>
      </div>
      
      <!-- 订单列表 -->
      <div v-else class="orders-list">
        <div class="order-card" v-for="order in orders" :key="order.id">
          <div class="order-header">
            <div class="order-info">
              <span class="order-number">订单号：{{ order.orderNumber }}</span>
              <span class="order-time">下单时间：{{ formatDate(order.createTime) }}</span>
            </div>
            
            <div class="order-status">
              <OrderStatus :status="order.status" />
            </div>
          </div>
          
          <div class="order-content">
            <div class="order-items">
              <div class="order-item" v-for="(item, index) in (order.orderItems || []).slice(0, 3)" :key="index">
                <img :src="formatImageUrl(item.dish?.imageUrl || item.dish?.image)" :alt="item.dish?.name || '菜品图片'">
                <div class="item-info">
                  <h3>{{ item.dish?.name || item.name }}</h3>
                  <p>¥{{ (item.price !== undefined && item.price !== null && !isNaN(item.price)) ? Number(item.price).toFixed(2) : '--' }} × {{ item.quantity ?? '--' }}</p>
                </div>
              </div>
              
              <div class="more-items" v-if="(order.orderItems || []).length > 3">
                等共 {{ (order.orderItems || []).length }} 件商品
              </div>
            </div>
            
            <div class="order-summary">
              <div class="total-price">
                <span>总价：</span>
                <span class="price">
                  ¥{{
                    (order.orderItems && order.orderItems.length > 0)
                      ? order.orderItems.reduce((sum, item) => {
                          const price = Number(item.price);
                          const quantity = Number(item.quantity);
                          if (!isNaN(price) && !isNaN(quantity)) {
                            return sum + price * quantity;
                          }
                          return sum;
                        }, 0).toFixed(2)
                      : '--'
                  }}
                </span>
              </div>
              
              <router-link :to="`/orders/${order.id}`" class="btn-secondary">
                查看详情
              </router-link>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '../store/auth';
import OrderStatus from '../components/OrderStatus.vue';
import orderApi from '../api/orderApi';
import { ElMessage } from 'element-plus';
import dishSmallPlaceholder from '../assets/images/placeholders/dish-small-placeholder.jpg';

export default {
  name: 'OrdersPage',
  components: {
    OrderStatus
  },
  setup() {
    const router = useRouter();
    const authStore = useAuthStore();
    
    const orders = ref([]);
    const loading = ref(true);
    const error = ref(null);
    
    // 格式化日期
    const formatDate = (dateString) => {
      if (!dateString) return '';
      
      const date = new Date(dateString);
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      const hours = String(date.getHours()).padStart(2, '0');
      const minutes = String(date.getMinutes()).padStart(2, '0');
      
      return `${year}-${month}-${day} ${hours}:${minutes}`;
    };

    // 统一图片路径处理
    const formatImageUrl = (url) => {
      if (!url) return dishSmallPlaceholder;
      if (url.startsWith('http')) return url;
      if (!url.startsWith('/api/')) return '/api' + url;
      return url;
    };
    
    // 获取用户订单
    const fetchOrders = async () => {
      try {
        loading.value = true;
        error.value = null;
        // 直接用res（http.js返回数组）
        const res = await orderApi.getUserOrders();
        orders.value = res;
        // 按创建时间降序排列
        orders.value.sort((a, b) => new Date(b.createTime) - new Date(a.createTime));
      } catch (err) {
        error.value = '获取订单失败，请稍后再试';
        console.error('Failed to fetch orders:', err);
      } finally {
        loading.value = false;
      }
    };
    
    onMounted(() => {
      // 检查是否已登录
      if (!authStore.isLoggedIn) {
        ElMessage.warning('请先登录');
        router.push('/login');
        return;
      }
      // 设置favicon
      const link = document.querySelector('link[rel="icon"]') || document.createElement('link');
      link.rel = 'icon';
      link.type = 'image/x-icon';
      link.href = '/favicon.ico';
      document.head.appendChild(link);
      fetchOrders();
    });
    
    return {
      orders,
      loading,
      error,
      formatDate,
      fetchOrders,
      dishSmallPlaceholder,
      formatImageUrl
    };
  }
};
</script>

<style scoped>
.orders-page {
  padding: 40px 0;
}

.page-title {
  margin-bottom: 30px;
}

.page-title h1 {
  font-size: 32px;
  margin: 0;
  position: relative;
  display: inline-block;
  padding-bottom: 10px;
}

.page-title h1:after {
  content: '';
  position: absolute;
  left: 0;
  bottom: 0;
  width: 50px;
  height: 3px;
  background-color: var(--primary-color, #e53935);
}

.loading-state,
.error-state,
.empty-state {
  text-align: center;
  padding: 50px 0;
  font-size: 16px;
  color: #666;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.empty-icon {
  width: 80px;
  height: 80px;
  margin: 0 auto 20px;
  background-image: url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="%23e0e0e0"><path d="M19 5v14H5V5h14m0-2H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm-4.86 8.86l-3 3.87L9 13.14 6 17h12l-3.86-5.14z"/></svg>');
  background-repeat: no-repeat;
  background-position: center;
}

.empty-state h2 {
  font-size: 24px;
  margin: 0 0 10px;
  color: #333;
}

.empty-state p {
  margin-bottom: 20px;
}

.btn-primary {
  display: inline-block;
  background-color: var(--primary-color, #e53935);
  color: white;
  padding: 10px 20px;
  border-radius: 4px;
  border: none;
  text-decoration: none;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.3s;
}

.btn-primary:hover {
  background-color: var(--primary-dark, #c62828);
}

.btn-secondary {
  display: inline-block;
  background-color: #f5f5f5;
  color: #333;
  padding: 8px 15px;
  border-radius: 4px;
  text-decoration: none;
  font-size: 14px;
  transition: background-color 0.3s;
}

.btn-secondary:hover {
  background-color: #e0e0e0;
}

.orders-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.order-card {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  background-color: #f9f9f9;
  border-bottom: 1px solid #eee;
}

.order-info {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.order-number {
  font-weight: 500;
  color: #333;
}

.order-time {
  font-size: 14px;
  color: #666;
}

.order-content {
  padding: 20px;
  display: flex;
  gap: 20px;
}

.order-items {
  flex: 1;
}

.order-item {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}

.order-item img {
  width: 60px;
  height: 60px;
  border-radius: 4px;
  object-fit: cover;
  margin-right: 15px;
}

.item-info h3 {
  margin: 0 0 5px;
  font-size: 16px;
  color: #333;
}

.item-info p {
  margin: 0;
  font-size: 14px;
  color: #666;
}

.more-items {
  font-size: 14px;
  color: #999;
  margin-top: 10px;
}

.order-summary {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  justify-content: space-between;
  min-width: 150px;
}

.total-price {
  font-size: 16px;
  margin-bottom: 15px;
}

.price {
  font-weight: bold;
  color: var(--primary-color, #e53935);
}

/* 响应式调整 */
@media (max-width: 768px) {
  .order-content {
    flex-direction: column;
  }
  
  .order-summary {
    flex-direction: row;
    align-items: center;
    min-width: auto;
    margin-top: 15px;
    border-top: 1px solid #eee;
    padding-top: 15px;
  }
  
  .total-price {
    margin-bottom: 0;
  }
}
</style>