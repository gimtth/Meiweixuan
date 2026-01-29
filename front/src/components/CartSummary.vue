<template>
  <div class="cart-summary">
    <div class="summary-info">
      <div class="summary-row">
        <span>商品数量:</span>
        <span>{{ totalItems }} 件</span>
      </div>
      
      <div class="summary-row">
        <span>商品总价:</span>
        <span class="price">¥{{ totalPrice.toFixed(2) }}</span>
      </div>
    </div>
    
    <div class="summary-actions">
      <button class="clear-btn" @click="clearCart" :disabled="totalItems === 0">
        清空购物车
      </button>
      
      <button class="checkout-btn" @click="checkout" :disabled="totalItems === 0">
        去结算
      </button>
    </div>
  </div>
</template>

<script>
import { useCartStore } from '../store/cart';
import { useRouter } from 'vue-router';

export default {
  name: 'CartSummary',
  props: {
    totalItems: {
      type: Number,
      required: true
    },
    totalPrice: {
      type: Number,
      required: true
    }
  },
  setup() {
    const cartStore = useCartStore();
    const router = useRouter();
    
    const clearCart = () => {
      if (confirm('确定要清空购物车吗？')) {
        cartStore.clearCart();
      }
    };
    
    const checkout = () => {
      router.push('/checkout');
    };
    
    return {
      clearCart,
      checkout
    };
  }
};
</script>

<style scoped>
.cart-summary {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.summary-info {
  margin-bottom: 20px;
}

.summary-row {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
  font-size: 16px;
}

.summary-row:last-child {
  margin-bottom: 0;
  padding-top: 10px;
  border-top: 1px dashed #eee;
}

.price {
  font-size: 20px;
  font-weight: bold;
  color: var(--primary-color, #e53935);
}

.summary-actions {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}

.clear-btn, .checkout-btn {
  padding: 10px 20px;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
  transition: all 0.3s;
  border: none;
}

.clear-btn {
  background-color: #f5f5f5;
  color: #666;
}

.clear-btn:hover:not(:disabled) {
  background-color: #e0e0e0;
}

.checkout-btn {
  background-color: var(--primary-color, #e53935);
  color: white;
  font-weight: bold;
  flex-grow: 1;
  margin-left: 10px;
}

.checkout-btn:hover:not(:disabled) {
  background-color: var(--primary-dark, #c62828);
}

.clear-btn:disabled, .checkout-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
</style> 