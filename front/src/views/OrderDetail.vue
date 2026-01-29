<template>
  <div class="order-detail-page">
    <div class="container">
      <div class="page-title">
        <h1>订单详情</h1>
      </div>
      
      <!-- 加载中状态 -->
      <div v-if="loading" class="loading-state">
        <p>加载中...</p>
      </div>
      
      <!-- 错误状态 -->
      <div v-else-if="error" class="error-state">
        <p>{{ error }}</p>
        <button class="btn-primary" @click="fetchOrderDetail">重试</button>
      </div>
      
      <!-- 订单详情 -->
      <div v-else-if="order" class="order-detail-content">
        <!-- 订单状态 -->
        <div class="status-card">
          <div class="status-header">
            <h2>订单状态</h2>
          </div>
          
          <div class="status-content">
            <OrderStatus :status="order.status" :showTimeline="true" />
            
            <div class="status-info">
              <p>
                订单号：<span class="highlight">{{ order.orderNumber }}</span>
              </p>
              <p>
                下单时间：{{ formatDate(order.createTime) }}
              </p>
              <p v-if="order.status !== '待处理'">
                更新时间：{{ formatDate(order.updateTime) }}
              </p>
            </div>
          </div>
        </div>
        
        <!-- 商品信息 -->
        <div class="items-card">
          <div class="card-header">
            <h2>商品信息</h2>
          </div>
          
          <div class="items-list">
            <div class="items-header">
              <span class="col-item">商品</span>
              <span class="col-price">单价</span>
              <span class="col-quantity">数量</span>
              <span class="col-subtotal">小计</span>
            </div>
            
            <div v-if="(order.orderItems || []).length === 0" class="empty-items">
              <p>暂无订单商品信息</p>
            </div>
            
            <div class="item-row" v-for="(item, index) in (order.orderItems || [])" :key="index">
              <div class="col-item">
                <img :src="formatImageUrl(item.dish?.imageUrl || item.dish?.image || item.image)" :alt="item.dish?.name || item.name || '菜品图片'">
                <div class="item-info">
                  <h3>{{ item.dish?.name || item.name }}</h3>
                </div>
              </div>
              <div class="col-price">¥{{ (item.price !== undefined && item.price !== null && !isNaN(item.price)) ? Number(item.price).toFixed(2) : '--' }}</div>
              <div class="col-quantity">{{ item.quantity ?? '--' }}</div>
              <div class="col-subtotal">¥{{ ((item.price !== undefined && item.price !== null && !isNaN(item.price) && item.quantity !== undefined && item.quantity !== null && !isNaN(item.quantity)) ? (Number(item.price) * Number(item.quantity)).toFixed(2) : '--') }}</div>
            </div>
          </div>
          
          <div class="order-total">
            <span>总价：</span>
            <span class="total-price">
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
        </div>
        
        <!-- 配送信息 -->
        <div class="delivery-card">
          <div class="card-header">
            <h2>配送信息</h2>
          </div>
          
          <div class="delivery-info">
            <div class="info-item">
              <span class="info-label">收货人：</span>
              <span class="info-value">{{ order.user?.name || '--' }}</span>
            </div>
            
            <div class="info-item">
              <span class="info-label">联系电话：</span>
              <span class="info-value">{{ order.user?.phone || order.phone || '--' }}</span>
            </div>
            
            <div class="info-item">
              <span class="info-label">收货地址：</span>
              <span class="info-value">{{ order.address || '--' }}</span>
            </div>
            
            <div class="info-item" v-if="order.note">
              <span class="info-label">订单备注：</span>
              <span class="info-value">{{ order.note }}</span>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 返回按钮 -->
      <div class="back-to-orders">
        <router-link to="/orders" class="btn-secondary">
          <i class="icon-back"></i>
          返回订单列表
        </router-link>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useAuthStore } from '../store/auth';
import OrderStatus from '../components/OrderStatus.vue';
import orderApi from '../api/orderApi';
import { ElMessage } from 'element-plus';
import dishSmallPlaceholder from '../assets/images/placeholders/dish-small-placeholder.jpg';

export default {
  name: 'OrderDetailPage',
  components: {
    OrderStatus
  },
  setup() {
    const route = useRoute();
    const router = useRouter();
    const authStore = useAuthStore();
    
    const order = ref(null);
    const loading = ref(true);
    const error = ref(null);
    
    // 格式化日期
    const formatDate = (dateString) => {
      if (!dateString) return '无';
      
      const date = new Date(dateString);
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      const hours = String(date.getHours()).padStart(2, '0');
      const minutes = String(date.getMinutes()).padStart(2, '0');
      
      return `${year}-${month}-${day} ${hours}:${minutes}`;
    };
    
    // 图片路径处理，自动补全 /api 前缀
    const formatImageUrl = (url) => {
      if (!url) return dishSmallPlaceholder;
      if (url.startsWith('http')) return url;
      if (!url.startsWith('/api/')) return '/api' + url;
      return url;
    };
    
    // 获取订单详情
    const fetchOrderDetail = async () => {
      const orderId = route.params.id;
      
      if (!orderId) {
        router.push('/orders');
        return;
      }
      
      try {
        loading.value = true;
        error.value = null;
        
        console.log(`获取订单详情：ID=${orderId}`);
        const orderData = await orderApi.getOrderById(orderId);
        console.log('获取到的订单详情数据:', orderData);
        
        // 确保orderItems存在
        if (!orderData.orderItems) {
          orderData.orderItems = [];
        }
        
        order.value = orderData;
      } catch (err) {
        error.value = '获取订单详情失败，请稍后再试';
        console.error('获取订单详情失败:', err);
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
      
      fetchOrderDetail();
    });
    
    return {
      order,
      loading,
      error,
      formatDate,
      fetchOrderDetail,
      dishSmallPlaceholder,
      formatImageUrl
    };
  }
};
</script>

<style scoped>
.order-detail-page {
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

.btn-primary,
.btn-secondary {
  display: inline-block;
  padding: 10px 20px;
  border-radius: 4px;
  font-size: 16px;
  text-decoration: none;
  margin-top: 15px;
  transition: background-color 0.3s;
}

.btn-primary {
  background-color: var(--primary-color, #e53935);
  color: white;
  border: none;
}

.btn-primary:hover {
  background-color: var(--primary-dark, #c62828);
}

.btn-secondary {
  background-color: #f5f5f5;
  color: #333;
  border: none;
}

.btn-secondary:hover {
  background-color: #e0e0e0;
}

.order-detail-content {
  display: flex;
  flex-direction: column;
  gap: 20px;
  margin-bottom: 20px;
}

.status-card,
.items-card,
.delivery-card {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.card-header,
.status-header {
  padding: 15px 20px;
  border-bottom: 1px solid #eee;
}

.card-header h2,
.status-header h2 {
  margin: 0;
  font-size: 18px;
  color: #333;
}

.status-content {
  padding: 20px;
  display: flex;
  gap: 30px;
}

.status-info {
  flex: 1;
}

.status-info p {
  margin: 0 0 10px;
  color: #666;
}

.highlight {
  font-weight: bold;
  color: var(--primary-color, #e53935);
}

.items-list {
  padding: 0 20px;
}

.items-header {
  display: flex;
  padding: 15px 0;
  border-bottom: 1px solid #eee;
  font-weight: 500;
  color: #333;
}

.item-row {
  display: flex;
  padding: 15px 0;
  border-bottom: 1px solid #f5f5f5;
  align-items: center;
}

.col-item {
  flex: 2;
  display: flex;
  align-items: center;
}

.col-price,
.col-quantity,
.col-subtotal {
  flex: 1;
  text-align: center;
}

.col-item img {
  width: 60px;
  height: 60px;
  border-radius: 4px;
  object-fit: cover;
  margin-right: 15px;
}

.item-info h3 {
  margin: 0;
  font-size: 16px;
  color: #333;
}

.order-total {
  padding: 15px 20px;
  text-align: right;
  font-size: 16px;
  border-top: 1px solid #eee;
}

.total-price {
  font-weight: bold;
  font-size: 20px;
  color: var(--primary-color, #e53935);
}

.delivery-info {
  padding: 20px;
}

.info-item {
  margin-bottom: 15px;
  display: flex;
}

.info-item:last-child {
  margin-bottom: 0;
}

.info-label {
  width: 100px;
  color: #666;
}

.info-value {
  flex: 1;
}

.empty-items {
  text-align: center;
  padding: 30px 0;
  color: #999;
  font-style: italic;
}

.back-to-orders {
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
  .status-content {
    flex-direction: column;
    gap: 20px;
  }
  
  .items-header {
    display: none;
  }
  
  .item-row {
    flex-wrap: wrap;
    padding: 15px 0;
  }
  
  .col-item {
    flex: 0 0 100%;
    margin-bottom: 10px;
  }
  
  .col-price,
  .col-quantity,
  .col-subtotal {
    flex: 1;
    text-align: center;
  }
  
  .col-price:before {
    content: '单价: ';
    color: #999;
  }
  
  .col-quantity:before {
    content: '数量: ';
    color: #999;
  }
  
  .col-subtotal:before {
    content: '小计: ';
    color: #999;
  }
}
</style> 