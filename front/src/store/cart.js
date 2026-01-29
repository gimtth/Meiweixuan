import { defineStore } from 'pinia'
import cartApi from '../api/cartApi'
import { ElMessage } from 'element-plus'

export const useCartStore = defineStore('cart', {
  state: () => ({
    items: [],
    loading: false,
    error: null
  }),
  
  getters: {
    totalItems: (state) => state.items.reduce((sum, item) => sum + item.quantity, 0),
    
    totalAmount: (state) => state.items.reduce(
      (sum, item) => sum + (item.dish.price * item.quantity), 
      0
    ).toFixed(2),
    
    isEmpty: (state) => state.items.length === 0
  },
  
  actions: {
    async fetchCart() {
      this.loading = true
      this.error = null
      
      try {
        const response = await cartApi.getCart()
        console.log('获取购物车响应:', response)
        if (response && response.items) {
          this.items = response.items.map(item => ({
            ...item,
            id: item.id || item.cartItemId || item.cart_id
          }))
        } else {
          this.items = []
        }
      } catch (err) {
        console.error('获取购物车失败:', err)
        this.error = err.message || '获取购物车失败'
        ElMessage.error(this.error)
      } finally {
        this.loading = false
      }
    },
    
    async addToCart(dish, quantity = 1) {
      this.loading = true
      
      try {
        console.log('添加到购物车:', dish, quantity)
        
        // 确保菜品有正确的ID，并且为数字类型
        let dishId = dish.id || (typeof dish === 'string' ? dish : null)
        dishId = Number(dishId)
        if (!dishId || isNaN(dishId)) {
          throw new Error('菜品ID无效')
        }
        
        const response = await cartApi.addToCart({ 
          dishId: dishId, 
          quantity 
        })
        
        console.log('添加购物车响应:', response)
        
        if (response && response.items) {
          this.items = response.items
          ElMessage.success(`已将 ${dish.name} 添加到购物车`)
        } else {
          throw new Error('添加购物车返回数据格式错误')
        }
      } catch (err) {
        console.error('添加购物车失败:', err)
        ElMessage.error(err.message || '添加到购物车失败')
      } finally {
        this.loading = false
      }
    },
    
    async updateQuantity(cartItemId, quantity) {
      if (quantity <= 0) {
        return this.removeItem(cartItemId)
      }
      
      this.loading = true
      
      try {
        const response = await cartApi.updateCartItem({
          cartItemId,
          quantity
        })
        
        if (response && response.items) {
          this.items = response.items
          ElMessage.success('购物车已更新')
        } else {
          throw new Error('更新购物车返回数据格式错误')
        }
      } catch (err) {
        console.error('更新购物车失败:', err)
        ElMessage.error(err.message || '更新购物车失败')
      } finally {
        this.loading = false
      }
    },
    
    async removeItem(cartItemId) {
      this.loading = true
      
      try {
        const response = await cartApi.removeCartItem(cartItemId)
        
        if (response && response.items) {
          this.items = response.items
          ElMessage.success('商品已从购物车移除')
        } else {
          throw new Error('移除商品返回数据格式错误')
        }
      } catch (err) {
        console.error('移除商品失败:', err)
        ElMessage.error(err.message || '移除商品失败')
      } finally {
        this.loading = false
      }
    },
    
    async clearCart() {
      this.loading = true
      
      try {
        await cartApi.clearCart()
        this.items = []
        ElMessage.success('购物车已清空')
      } catch (err) {
        console.error('清空购物车失败:', err)
        ElMessage.error(err.message || '清空购物车失败')
      } finally {
        this.loading = false
      }
    },
    
    resetState() {
      this.items = []
      this.loading = false
      this.error = null
    }
  }
}) 