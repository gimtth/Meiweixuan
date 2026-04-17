import http from './http';

const authApi = {
  /**
   * 用户登录
   * @param {Object} credentials - 登录凭证
   * @param {string} credentials.username - 用户名
   * @param {string} credentials.password - 密码
   * @returns {Promise<Object>} 包含token和用户信息的响应
   */
  login(credentials) {
    const loginData = {
      ...credentials,
      userType: 'user'
    }
    return http.post('/yonghu/login', loginData, {
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'X-Requested-With': 'XMLHttpRequest'
      }
    });
  },
  
  /**
   * 用户注册
   * @param {Object} userData - 用户数据
   * @param {string} userData.username - 用户名
   * @param {string} userData.password - 密码
   * @param {string} userData.name - 真实姓名
   * @param {string} userData.phone - 联系电话
   * @param {string} userData.address - 联系地址
   * @returns {Promise<Object>} 包含token和用户信息的响应
   */
  register(userData) {
    return http.post('/yonghu/register', userData, {
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'X-Requested-With': 'XMLHttpRequest'
      }
    });
  },
  
  /**
   * 管理员登录
   * @param {Object} credentials - 登录凭证
   * @param {string} credentials.username - 用户名
   * @param {string} credentials.password - 密码
   * @returns {Promise<Object>} 包含token和用户信息的响应
   */
  adminLogin(credentials) {
    const loginData = {
      ...credentials,
      userType: 'admin'
    }
    return http.post('/admin/login', loginData, {
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'X-Requested-With': 'XMLHttpRequest'
      }
    });
  },
  
  /**
   * 检查管理员认证状态
   * @returns {Promise<Object>} 返回检查结果
   */
  checkAdminAuth() {
    return http.get('/admin/auth/check');
  }
};

export default authApi; 
