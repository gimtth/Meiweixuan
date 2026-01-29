import http from './http';

const cartApi = {
  /**
   * 获取用户购物车
   * @returns {Promise<Object>} 购物车数据
   */
  getCart() {
    return http.get('/cart');
  },

  /**
   * 添加菜品到购物车
   * @param {Object} item - 购物车项
   * @param {number} item.dishId - 菜品ID
   * @param {number} item.quantity - 数量
   * @returns {Promise<Object>} 更新后的购物车
   */
  addToCart(item) {
    return http.post('/cart/add', item);
  },

  /**
   * 更新购物车项数量
   * @param {Object} item - 购物车项
   * @param {number} item.cartItemId - 购物车项ID
   * @param {number} item.quantity - 数量
   * @returns {Promise<Object>} 更新后的购物车
   */
  updateCartItem(item) {
    return http.put('/cart/update', item);
  },

  /**
   * 删除购物车项
   * @param {number} cartItemId - 购物车项ID
   * @returns {Promise<Object>} 更新后的购物车
   */
  removeCartItem(cartItemId) {
    return http.delete(`/cart/item/${cartItemId}`);
  },

  /**
   * 清空购物车
   * @returns {Promise<Object>} 操作结果
   */
  clearCart() {
    return http.delete('/cart/clear');
  }
};

export default cartApi; 