<template>
  <div class="cart-item">
    <div class="item-image">
      <img :src="item.dish.imageUrl || dishSmallPlaceholder" :alt="item.dish.name">
    </div>
    
    <div class="item-info">
      <h3 class="item-name">{{ item.dish.name }}</h3>
      <p class="item-price">¥{{ item.dish.price.toFixed(2) }}</p>
    </div>
    
    <div class="item-quantity">
      <button class="quantity-btn" @click="decreaseQuantity" :disabled="item.quantity <= 1">-</button>
      <span class="quantity-value">{{ item.quantity }}</span>
      <button class="quantity-btn" @click="increaseQuantity">+</button>
    </div>
    
    <div class="item-subtotal">
      ¥{{ calculateSubtotal() }}
    </div>
    
    <div class="item-actions">
      <button class="remove-btn" @click="removeItem">
        <i class="icon-delete"></i>
      </button>
    </div>
  </div>
</template>

<script>
import { useCartStore } from '../store/cart';
import dishSmallPlaceholder from '../assets/images/placeholders/dish-small-placeholder.jpg';

export default {
  name: 'CartItem',
  props: {
    item: {
      type: Object,
      required: true
    }
  },
  setup(props) {
    const cartStore = useCartStore();
    
    const calculateSubtotal = () => {
      return (props.item.dish.price * props.item.quantity).toFixed(2);
    };
    
    const increaseQuantity = () => {
      cartStore.updateCartItem(props.item.dish.id, props.item.quantity + 1);
    };
    
    const decreaseQuantity = () => {
      if (props.item.quantity > 1) {
        cartStore.updateCartItem(props.item.dish.id, props.item.quantity - 1);
      }
    };
    
    const removeItem = () => {
      cartStore.removeFromCart(props.item.dish.id);
    };
    
    return {
      calculateSubtotal,
      increaseQuantity,
      decreaseQuantity,
      removeItem,
      dishSmallPlaceholder
    };
  }
};
</script>

<style scoped>
.cart-item {
  display: flex;
  align-items: center;
  padding: 15px;
  border-bottom: 1px solid #eee;
  transition: background-color 0.3s;
}

.cart-item:hover {
  background-color: #f9f9f9;
}

.item-image {
  width: 80px;
  height: 80px;
  border-radius: 4px;
  overflow: hidden;
  margin-right: 15px;
  flex-shrink: 0;
}

.item-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.item-info {
  flex: 1;
  min-width: 0;
}

.item-name {
  margin: 0 0 5px;
  font-size: 16px;
  color: #333;
}

.item-price {
  margin: 0;
  font-size: 14px;
  color: #666;
}

.item-quantity {
  display: flex;
  align-items: center;
  margin: 0 20px;
}

.quantity-btn {
  width: 28px;
  height: 28px;
  border-radius: 4px;
  border: 1px solid #ddd;
  background-color: white;
  font-size: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s;
}

.quantity-btn:hover:not(:disabled) {
  background-color: #f0f0f0;
  border-color: #ccc;
}

.quantity-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.quantity-value {
  margin: 0 10px;
  min-width: 30px;
  text-align: center;
  font-size: 15px;
}

.item-subtotal {
  font-weight: bold;
  font-size: 16px;
  color: var(--primary-color, #e53935);
  margin: 0 20px;
  min-width: 80px;
  text-align: right;
}

.item-actions {
  margin-left: 15px;
}

.remove-btn {
  background: transparent;
  border: none;
  cursor: pointer;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.remove-btn:hover {
  background-color: rgba(0, 0, 0, 0.05);
}

.icon-delete {
  display: inline-block;
  width: 20px;
  height: 20px;
  background-image: url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="%23999"><path d="M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12zM19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z"/></svg>');
  background-repeat: no-repeat;
  background-position: center;
}
</style> 