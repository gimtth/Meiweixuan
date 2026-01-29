import http from './http';

const orderApi = {
  /**
   * 创建订单
   * @param {Object} orderData - 订单数据
   * @param {string} orderData.address - 送餐地址
   * @param {string} orderData.phone - 联系电话
   * @param {string} orderData.note - 订单备注
   * @returns {Promise<Object>} 创建的订单
   */
  createOrder(orderData) {
    return http.post('/orders', orderData);
  },

  /**
   * 获取用户订单列表
   * @param {Object} params - 查询参数
   * @param {number} params.page - 页码
   * @param {number} params.size - 每页数量
   * @returns {Promise<Object>} 订单分页数据
   */
  getUserOrders(params = { page: 1, size: 10 }) {
    // 真实后端接口请求，假设接口为GET /orders，支持分页参数
    return http.get('/orders', { params });
  },
  
  /**
   * 获取订单详情
   * @param {number|string} orderId - 订单ID
   * @returns {Promise<Object>} 订单详情
   */
  getOrderById(orderId) {
    // 强制转换为Number类型
    return http.get(`/orders/${Number(orderId)}`);
  },
  
  /**
   * 取消订单
   * @param {number|string} orderId - 订单ID
   * @returns {Promise<Object>} 操作结果
   */
  cancelOrder(orderId) {
    return http.put(`/orders/${Number(orderId)}/cancel`);
  },
  
  /**
   * 确认收货
   * @param {number|string} orderId - 订单ID
   * @returns {Promise<Object>} 操作结果
   */
  confirmOrderReceived(orderId) {
    return http.put(`/orders/${Number(orderId)}/confirm`);
  },
  
  /**
   * 【管理员】获取所有订单
   * @returns {Promise<Array>} 订单列表
   */
  getAllOrders() {
    return http.get('/admin/orders');
  },
  
  /**
   * 【管理员】更新订单状态
   * @param {number|string} orderId - 订单ID
   * @param {string} status - 订单状态
   * @returns {Promise<Object>} 更新后的订单
   */
  updateOrderStatus(orderId, status) {
    return http.put(`/admin/orders/${Number(orderId)}/status`, { status });
  }
};

export default orderApi; 