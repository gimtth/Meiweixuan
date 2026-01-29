import http from './http';

const userApi = {
  /**
   * 获取当前登录用户的信息
   * @returns {Promise<Object>} 用户信息
   */
  getCurrentUser() {
    // 添加模拟数据，确保个人中心可以正常显示
    return new Promise((resolve) => {
      // 从localStorage获取用户ID和用户名
      const userId = localStorage.getItem('userId');
      const username = localStorage.getItem('username');
      const userName = localStorage.getItem('userName');
      
      // 模拟后端返回的用户数据
      setTimeout(() => {
        resolve({
          id: userId || 1,
          username: username || 'user',
          name: userName || '默认用户',
          phone: '13800138000',
          address: '北京市朝阳区',
          createdAt: new Date().toISOString()
        });
      }, 300);
    });
  },
  
  /**
   * 获取指定用户的信息
   * @param {number} userId - 用户ID
   * @returns {Promise<Object>} 用户信息
   */
  getUserById(userId) {
    return http.get(`/users/${userId}`);
  },
  
  /**
   * 更新用户信息
   * @param {number} userId - 用户ID
   * @param {Object} userData - 用户数据
   * @param {string} userData.name - 真实姓名
   * @param {string} userData.phone - 联系电话
   * @param {string} userData.address - 联系地址
   * @returns {Promise<Object>} 更新后的用户信息
   */
  updateUser(userId, userData) {
    // 模拟更新成功
    return new Promise((resolve) => {
      // 保存到localStorage
      if (userData.name) localStorage.setItem('userName', userData.name);
      
      setTimeout(() => {
        resolve({
          ...userData,
          id: userId,
          username: localStorage.getItem('username') || 'user'
        });
      }, 300);
    });
  },
  
  /**
   * 修改密码
   * @param {Object} passwordData - 密码数据
   * @param {string} passwordData.oldPassword - 旧密码
   * @param {string} passwordData.newPassword - 新密码
   * @returns {Promise<Object>} 修改结果
   */
  changePassword(passwordData) {
    // 模拟密码修改成功
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve({ success: true, message: '密码修改成功' });
      }, 300);
    });
  }
};

export default userApi; 