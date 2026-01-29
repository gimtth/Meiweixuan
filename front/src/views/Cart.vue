<template>
  <div class="cart-page">
    <div class="container">
      <div class="cart-header">
        <h1>我的购物车</h1>
        <p v-if="!cartStore.isEmpty">
          共有 <span class="highlight">{{ cartStore.totalItems }}</span> 件菜品
        </p>
      </div>

      <div v-if="cartStore.loading" class="loading-container">
        <el-skeleton :rows="5" animated />
      </div>

      <div v-else-if="cartStore.error" class="error-message">
        <el-alert :title="cartStore.error" type="error" :closable="false" />
        <el-button type="primary" @click="cartStore.fetchCart" class="retry-button">
          重试
        </el-button>
      </div>

      <div v-else-if="cartStore.isEmpty" class="empty-cart">
        <el-empty description="购物车空空如也">
          <template #extra>
            <router-link to="/menu">
              <el-button type="primary">去点餐</el-button>
            </router-link>
          </template>
        </el-empty>
      </div>

      <div v-else class="cart-content">
        <div class="cart-items">
          <div v-for="item in cartStore.items" :key="item.id" class="cart-item">
            <div class="item-image">
              <img :src="item.dish.image || defaultImage" :alt="item.dish.name" />
            </div>

            <div class="item-details">
              <h3 class="item-name">{{ item.dish.name }}</h3>
              <p class="item-price">¥{{ item.dish.price.toFixed(2) }}</p>
            </div>

            <div class="item-quantity">
              <el-input-number 
                v-model="item.quantity" 
                :min="1" 
                :max="99" 
                size="small"
                @change="(value) => updateQuantity(item.id, value)"
              />
            </div>

            <div class="item-total">
              ¥{{ (item.dish.price * item.quantity).toFixed(2) }}
            </div>

            <div class="item-actions">
              <el-button 
                type="danger" 
                icon="el-icon-delete" 
                circle 
                size="small"
                @click="removeItem(item.id)"
              />
            </div>
          </div>
        </div>

        <div class="cart-summary">
          <div class="summary-row">
            <span>菜品数量:</span>
            <span>{{ cartStore.totalItems }} 件</span>
          </div>
          <div class="summary-row">
            <span>菜品金额:</span>
            <span>¥{{ cartStore.totalAmount }}</span>
          </div>
          <div class="summary-row">
            <span>配送费:</span>
            <span>¥{{ deliveryFee.toFixed(2) }}</span>
          </div>
          <div class="summary-row total">
            <span>合计:</span>
            <span>¥{{ totalWithDelivery }}</span>
          </div>
          
          <div class="summary-actions">
            <el-button type="info" @click="cartStore.clearCart">
              清空购物车
            </el-button>
            <el-button type="primary" @click="proceedToCheckout">
              去结算
            </el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { computed, onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import { useCartStore } from '../store/cart';
import { ElMessageBox, ElMessage } from 'element-plus';
import dishPlaceholder from '../assets/images/placeholders/dish-placeholder.jpg';

export default {
  name: 'CartPage',
  setup() {
    const cartStore = useCartStore();
    const router = useRouter();
    const defaultImage = dishPlaceholder;
    const deliveryFee = ref(5); // 配送费
    
    const totalWithDelivery = computed(() => {
      return (parseFloat(cartStore.totalAmount) + deliveryFee.value).toFixed(2);
    });
    
    const updateQuantity = async (itemId, quantity) => {
      try {
        console.log('更新购物车项数量:', itemId, quantity);
        await cartStore.updateQuantity(itemId, quantity);
      } catch (err) {
        console.error('更新数量失败:', err);
        ElMessage.error('更新数量失败，请重试');
      }
    };
    
    const removeItem = (itemId) => {
      ElMessageBox.confirm(
        '确定从购物车中移除该菜品？',
        '提示',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      ).then(async () => {
        try {
          console.log('删除购物车项:', itemId);
          await cartStore.removeItem(itemId);
        } catch (err) {
          console.error('删除购物车项失败:', err);
          ElMessage.error('删除失败，请重试');
        }
      }).catch(() => {});
    };
    
    const proceedToCheckout = () => {
      if (cartStore.isEmpty) {
        ElMessage.warning('购物车为空，请先添加菜品');
        return;
      }
      router.push('/checkout');
    };
    
    onMounted(async () => {
      console.log('购物车页面加载，开始获取购物车数据');
      try {
        await cartStore.fetchCart();
        console.log('购物车数据加载完成:', cartStore.items);
      } catch (err) {
        console.error('加载购物车数据失败:', err);
      }
    });
    
    return {
      cartStore,
      defaultImage,
      deliveryFee,
      totalWithDelivery,
      updateQuantity,
      removeItem,
      proceedToCheckout
    };
  }
};
</script>

<style scoped>
.cart-page {
  padding: 30px 0;
  min-height: calc(100vh - 200px);
}

.container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 0 15px;
}

.cart-header {
  margin-bottom: 30px;
}

.cart-header h1 {
  font-size: 28px;
  font-weight: 700;
  margin: 0 0 10px;
  color: var(--primary-color, #e53935);
}

.cart-header p {
  margin: 0;
  color: #666;
}

.highlight {
  font-weight: 600;
  color: var(--primary-color, #e53935);
}

.loading-container {
  padding: 20px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.error-message {
  text-align: center;
  padding: 30px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.retry-button {
  margin-top: 15px;
}

.empty-cart {
  padding: 50px 0;
  text-align: center;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.cart-content {
  display: flex;
  gap: 30px;
}

.cart-items {
  flex: 1;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.cart-item {
  display: flex;
  align-items: center;
  padding: 15px 0;
  border-bottom: 1px solid #eee;
}

.cart-item:last-child {
  border-bottom: none;
}

.item-image {
  width: 80px;
  height: 80px;
  border-radius: 8px;
  overflow: hidden;
  margin-right: 15px;
}

.item-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.item-details {
  flex: 1;
}

.item-name {
  font-size: 16px;
  font-weight: 600;
  margin: 0 0 5px;
  color: #333;
}

.item-price {
  margin: 0;
  color: #666;
  font-size: 14px;
}

.item-quantity {
  width: 120px;
  margin: 0 15px;
}

.item-total {
  width: 80px;
  font-size: 16px;
  font-weight: 600;
  color: var(--primary-color, #e53935);
  text-align: right;
  margin-right: 15px;
}

.cart-summary {
  width: 300px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  padding: 20px;
  align-self: flex-start;
}

.summary-row {
  display: flex;
  justify-content: space-between;
  padding: 10px 0;
  border-bottom: 1px solid #eee;
  font-size: 14px;
  color: #666;
}

.summary-row:last-of-type {
  border-bottom: none;
}

.summary-row.total {
  font-size: 18px;
  font-weight: 600;
  color: var(--primary-color, #e53935);
  padding-top: 15px;
  margin-top: 5px;
  border-top: 1px solid #eee;
}

.summary-actions {
  margin-top: 20px;
  display: flex;
  justify-content: space-between;
}

@media (max-width: 768px) {
  .cart-content {
    flex-direction: column;
  }
  
  .cart-summary {
    width: 100%;
    margin-top: 20px;
  }
  
  .cart-item {
    flex-wrap: wrap;
  }
  
  .item-image {
    margin-bottom: 10px;
  }
  
  .item-details, .item-quantity, .item-total {
    width: 100%;
    margin: 5px 0;
  }
}
</style> 