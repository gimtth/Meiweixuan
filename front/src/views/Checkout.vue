<template>
  <div class="checkout-page">
    <div class="container">
      <div class="checkout-header">
        <h1>订单结算</h1>
      </div>

      <div v-if="loading" class="loading-container">
        <el-skeleton :rows="5" animated />
      </div>

      <div v-else-if="cartStore.isEmpty" class="empty-cart">
        <el-empty description="购物车为空，无法结算">
          <template #extra>
            <router-link to="/menu">
              <el-button type="primary">去点餐</el-button>
            </router-link>
          </template>
        </el-empty>
      </div>

      <div v-else class="checkout-content">
        <div class="checkout-form">
          <h2 class="section-title">配送信息</h2>
          
          <el-form 
            ref="orderFormRef" 
            :model="orderForm" 
            :rules="rules" 
            label-width="80px"
          >
            <el-form-item label="收货人" prop="name">
              <el-input v-model="orderForm.name" placeholder="请输入收货人姓名" />
            </el-form-item>
            
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="orderForm.phone" placeholder="请输入联系电话" />
            </el-form-item>
            
            <el-form-item label="地址" prop="address">
              <el-input v-model="orderForm.address" placeholder="请输入详细地址" />
            </el-form-item>
            
            <el-form-item label="备注">
              <el-input 
                v-model="orderForm.note" 
                type="textarea" 
                :rows="3"
                placeholder="如有特殊要求，请在此备注" 
              />
            </el-form-item>
          </el-form>
          
          <h2 class="section-title">订单详情</h2>
          
          <div class="order-items">
            <div v-for="item in cartStore.items" :key="item.id" class="order-item">
              <div class="item-info">
                <span class="item-name">{{ item.dish.name }}</span>
                <span class="item-price">¥{{ item.dish.price.toFixed(2) }} × {{ item.quantity }}</span>
              </div>
              <div class="item-total">¥{{ (item.dish.price * item.quantity).toFixed(2) }}</div>
            </div>
          </div>
          
          <h2 class="section-title">支付方式</h2>
          
          <div class="payment-methods">
            <el-radio-group v-model="orderForm.paymentMethod">
              <el-radio :value="'cash'">货到付款</el-radio>
              <el-radio :value="'online'">在线支付(模拟)</el-radio>
            </el-radio-group>
          </div>
        </div>
        
        <div class="order-summary">
          <h3>订单摘要</h3>
          
          <div class="summary-row">
            <span>商品金额:</span>
            <span>¥{{ cartStore.totalAmount }}</span>
          </div>
          
          <div class="summary-row">
            <span>配送费:</span>
            <span>¥{{ deliveryFee.toFixed(2) }}</span>
          </div>
          
          <div class="summary-row total">
            <span>总计:</span>
            <span>¥{{ totalAmount }}</span>
          </div>
          
          <div class="submit-row">
            <el-button type="primary" :loading="submitting" @click="submitOrder">
              提交订单
            </el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage, ElMessageBox } from 'element-plus';
import { useCartStore } from '../store/cart';
import { useAuthStore } from '../store/auth';
import orderApi from '../api/orderApi';

const cartStore = useCartStore();
const authStore = useAuthStore();
const router = useRouter();
const loading = ref(false);
const submitting = ref(false);
const deliveryFee = ref(5);

// 表单ref
const orderFormRef = ref(null);
const orderForm = ref({
  name: '',
  phone: '',
  address: '',
  note: '',
  paymentMethod: 'cash'
});

// 表单验证规则
const rules = {
  name: [
    { required: true, message: '请输入收货人姓名', trigger: 'blur' },
    { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  address: [
    { required: true, message: '请输入详细地址', trigger: 'blur' },
    { min: 5, max: 100, message: '长度在 5 到 100 个字符', trigger: 'blur' }
  ]
};

const totalAmount = computed(() => {
  return (parseFloat(cartStore.totalAmount) + deliveryFee.value).toFixed(2);
});

const initUserInfo = () => {
  if (authStore.user) {
    loading.value = true;
    const user = authStore.user;
    orderForm.value.name = user.name || '';
    orderForm.value.phone = user.phone || '';
    orderForm.value.address = user.address || '';
    loading.value = false;
  }
};

const submitOrder = async () => {
  if (cartStore.isEmpty) {
    ElMessage.warning('购物车为空，无法提交订单');
    return;
  }
  try {
    submitting.value = true;
    const orderData = {
      name: orderForm.value.name,
      phone: orderForm.value.phone,
      address: orderForm.value.address,
      note: orderForm.value.note,
      paymentMethod: orderForm.value.paymentMethod
    };
    const response = await orderApi.createOrder(orderData);
    ElMessageBox.alert(
      `订单已成功提交，订单号: ${response.orderNumber}`,
      '下单成功',
      {
        confirmButtonText: '查看订单',
        callback: () => {
          cartStore.clearCart();
          router.push(`/orders/${response.id}`);
        }
      }
    );
  } catch (err) {
    ElMessage.error(err.message || '提交订单失败，请稍后重试');
  } finally {
    submitting.value = false;
  }
};

onMounted(() => {
  if (!cartStore.items.length) {
    cartStore.fetchCart();
  }
  initUserInfo();
});
</script>

<style scoped>
.checkout-page {
  padding: 30px 0;
  min-height: calc(100vh - 200px);
}

.container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 0 15px;
}

.checkout-header {
  margin-bottom: 30px;
}

.checkout-header h1 {
  font-size: 28px;
  font-weight: 700;
  margin: 0;
  color: var(--primary-color, #e53935);
}

.loading-container,
.empty-cart {
  padding: 30px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.empty-cart {
  text-align: center;
  padding: 50px 0;
}

.checkout-content {
  display: flex;
  gap: 30px;
}

.checkout-form {
  flex: 1;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.section-title {
  font-size: 18px;
  margin: 0 0 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #eee;
  color: #333;
}

.order-items {
  margin-bottom: 30px;
}

.order-item {
  display: flex;
  justify-content: space-between;
  padding: 10px 0;
  border-bottom: 1px dashed #eee;
}

.order-item:last-child {
  border-bottom: none;
}

.item-info {
  display: flex;
  flex-direction: column;
}

.item-name {
  font-weight: 500;
  margin-bottom: 5px;
}

.item-price {
  font-size: 14px;
  color: #666;
}

.item-total {
  font-weight: 600;
  color: var(--primary-color, #e53935);
}

.payment-methods {
  margin-bottom: 20px;
}

.order-summary {
  width: 300px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  padding: 20px;
  align-self: flex-start;
}

.order-summary h3 {
  font-size: 18px;
  margin: 0 0 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #eee;
}

.summary-row {
  display: flex;
  justify-content: space-between;
  padding: 10px 0;
  border-bottom: 1px solid #eee;
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

.submit-row {
  margin-top: 30px;
}

.submit-row .el-button {
  width: 100%;
  padding: 12px;
}

@media (max-width: 768px) {
  .checkout-content {
    flex-direction: column;
  }
  
  .order-summary {
    width: auto;
    order: -1;
    margin-bottom: 20px;
  }
}
</style> 