import http from './http';

const dishApi = {
  /**
   * 获取所有菜品分类
   * @returns {Promise<Array>} 分类列表
   */
    getCategories() {    return http.get('/caipinfenlei');  },
  
  /**
   * 获取分类详情
   * @param {number} categoryId - 分类ID
   * @returns {Promise<Object>} 分类详情
   */
    getCategoryById(categoryId) {    return http.get(`/caipinfenlei/${categoryId}`);  },
  
  /**
   * 获取所有菜品
   * @returns {Promise<Array>} 菜品列表
   */
    getAllDishes() {    return http.get('/caipin');  },
  
  /**
   * 获取指定分类的菜品
   * @param {number} categoryId - 分类ID
   * @returns {Promise<Array>} 菜品列表
   */
    getDishesByCategory(categoryId) {    return http.get(`/caipin/category/${categoryId}`);  },
  
  /**
   * 获取菜品详情
   * @param {number} dishId - 菜品ID
   * @returns {Promise<Object>} 菜品详情
   */
    getDishById(dishId) {    return http.get(`/caipin/${dishId}`);  },
  
  /**
   * 搜索菜品
   * @param {string} keyword - 搜索关键词
   * @returns {Promise<Array>} 菜品列表
   */
    searchDishes(keyword) {    return http.get(`/caipin/search?keyword=${encodeURIComponent(keyword)}`);  }
};

export default dishApi; 