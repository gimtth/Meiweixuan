import http from './http';

const adminApi = {
  // 分类管理API
  categories: {
    /**
     * 获取所有分类
     * @returns {Promise<Array>} 分类列表
     */
    getAll() {
      return http.get('/admin/categories');
    },
    
    /**
     * 获取分类详情
     * @param {number} id - 分类ID
     * @returns {Promise<Object>} 分类详情
     */
    getById(id) {
      return http.get(`/admin/categories/${id}`);
    },
    
    /**
     * 创建分类
     * @param {Object} category - 分类信息
     * @returns {Promise<Object>} 创建的分类
     */
    create(category) {
      return http.post('/admin/categories', category);
    },
    
    /**
     * 更新分类
     * @param {number} id - 分类ID
     * @param {Object} category - 分类信息
     * @returns {Promise<Object>} 更新后的分类
     */
    update(id, category) {
      return http.put(`/admin/categories/${id}`, category);
    },
    
    /**
     * 删除分类
     * @param {number} id - 分类ID
     * @returns {Promise<Object>} 操作结果
     */
    delete(id) {
      return http.delete(`/admin/categories/${id}`);
    }
  },
  
  // 菜品管理API
  dishes: {
    /**
     * 获取所有菜品
     * @returns {Promise<Array>} 菜品列表
     */
    getAll() {
      return http.get('/admin/dishes');
    },
    
    /**
     * 获取菜品详情
     * @param {number} id - 菜品ID
     * @returns {Promise<Object>} 菜品详情
     */
    getById(id) {
      return http.get(`/admin/dishes/${id}`);
    },
    
    /**
     * 获取指定分类的菜品
     * @param {number} categoryId - 分类ID
     * @returns {Promise<Array>} 菜品列表
     */
    getByCategory(categoryId) {
      return http.get(`/admin/dishes/category/${categoryId}`);
    },
    
    /**
     * 创建菜品
     * @param {Object} dish - 菜品信息
     * @returns {Promise<Object>} 创建的菜品
     */
    create(dish) {
      return http.post('/admin/dishes', dish);
    },
    
    /**
     * 更新菜品
     * @param {number} id - 菜品ID
     * @param {Object} dish - 菜品信息
     * @returns {Promise<Object>} 更新后的菜品
     */
    update(id, dish) {
      return http.put(`/admin/dishes/${id}`, dish);
    },
    
    /**
     * 删除菜品
     * @param {number} id - 菜品ID
     * @returns {Promise<Object>} 操作结果
     */
    delete(id) {
      return http.delete(`/admin/dishes/${id}`);
    }
  },
  
  // 用户管理API
  users: {
    /**
     * 获取所有用户
     * @returns {Promise<Array>} 用户列表
     */
    getAll() {
      return http.get('/admin/users');
    },
    
    /**
     * 获取用户详情
     * @param {number} userId - 用户ID
     * @returns {Promise<Object>} 用户详情
     */
    getById(userId) {
      return http.get(`/admin/users/${userId}`);
    },
    
    /**
     * 更新用户信息
     * @param {number} userId - 用户ID
     * @param {Object} userData - 用户数据
     * @returns {Promise<Object>} 更新后的用户
     */
    update(userId, userData) {
      return http.put(`/admin/users/${userId}`, userData);
    },
    
    /**
     * 删除用户
     * @param {number} userId - 用户ID
     * @returns {Promise<Object>} 删除结果
     */
    delete(userId) {
      return http.delete(`/admin/users/${userId}`);
    },
    
    /**
     * 更新用户角色
     * @param {number} id - 用户ID
     * @param {Object} role - 角色信息 {role: 'ROLE_ADMIN'} 或 {role: 'ROLE_USER'}
     * @returns {Promise<Object>} 操作结果
     */
    updateRole(id, role) {
      return http.put(`/admin/users/${id}/role`, role);
    }
  },
  
  // 订单管理API
  orders: {
    /**
     * 获取所有订单
     * @returns {Promise<Array>} 订单列表
     */
    getAll() {
      return http.get('/admin/orders');
    },
    
    /**
     * 获取订单详情
     * @param {number} id - 订单ID
     * @returns {Promise<Object>} 订单详情
     */
    getById(id) {
      return http.get(`/admin/orders/${id}`);
    },
    
    /**
     * 更新订单状态
     * @param {number} id - 订单ID
     * @param {Object} status - 状态信息 {status: '已处理'} 或 {status: '已配送'} 等
     * @returns {Promise<Object>} 操作结果
     */
    updateStatus(id, status) {
      return http.put(`/admin/orders/${id}/status`, status);
    }
  },
  
  // ===== 统计相关 =====
  /**
   * 获取订单统计数据
   * @param {string} startDate - 开始日期 YYYY-MM-DD
   * @param {string} endDate - 结束日期 YYYY-MM-DD
   * @returns {Promise<Object>} 订单统计
   */
  getOrderStatistics(startDate = '', endDate = '') {
    const params = {};
    if (startDate) params.startDate = startDate;
    if (endDate) params.endDate = endDate;
    
    return http.get('/admin/stats/orders', { params });
  },
  
  /**
   * 获取用户统计数据
   * @param {string} startDate - 开始日期 YYYY-MM-DD
   * @param {string} endDate - 结束日期 YYYY-MM-DD
   * @returns {Promise<Object>} 用户统计
   */
  getUserStatistics(startDate = '', endDate = '') {
    const params = {};
    if (startDate) params.startDate = startDate;
    if (endDate) params.endDate = endDate;
    
    return http.get('/admin/stats/users', { params });
  },
  
  /**
   * 获取菜品销量统计
   * @returns {Promise<Object>} 菜品销量统计
   */
  getDishStatistics() {
    return http.get('/admin/stats/dishes');
  },
  
  /**
   * 获取数据统计总览
   * @returns {Promise<Object>} 统计总览
   */
  getStatisticsOverview() {
    return http.get('/admin/stats/overview');
  }
};

export default adminApi; 