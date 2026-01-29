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
    // 添加userType参数，表明是普通用户登录
    const loginData = {
      ...credentials,
      userType: 'user'
    };
    console.log('用户登录请求：', loginData);
    return http.post('/yonghu/login', loginData, {
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'X-Requested-With': 'XMLHttpRequest'
      }
    })
      .catch(error => {
        // 增强错误处理
        let errorMessage = '用户登录失败';
        
        if (error.response) {
          const status = error.response.status;
          
          switch (status) {
            case 400:
              errorMessage = '用户账号或密码错误';
              break;
            case 401:
              errorMessage = '用户认证失败';
              break;
            case 403:
              errorMessage = '没有用户权限';
              break;
            case 500:
              errorMessage = '服务器内部错误';
              break;
          }
          
          // 优先使用服务器返回的错误信息
          if (error.response.data && error.response.data.message) {
            errorMessage = error.response.data.message;
          }
        } else if (error.request) {
          errorMessage = '无法连接到服务器，请检查网络';
        } else {
          errorMessage = error.message || errorMessage;
        }
        
        console.error('用户登录错误:', errorMessage);
        throw new Error(errorMessage);
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
    // 添加userType参数，表明是管理员登录
    const loginData = {
      ...credentials,
      userType: 'admin'
    };
    console.log('管理员登录请求：', loginData);
    return http.post('/admin/login', loginData, {
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'X-Requested-With': 'XMLHttpRequest'
      }
    })
      .catch(error => {
        // 增强错误处理
        let errorMessage = '管理员登录失败';
        
        if (error.response) {
          const status = error.response.status;
          
          switch (status) {
            case 400:
              errorMessage = '管理员账号或密码错误';
              break;
            case 401:
              errorMessage = '管理员认证失败';
              break;
            case 403:
              errorMessage = '没有管理员权限';
              break;
            case 500:
              errorMessage = '服务器内部错误';
              break;
          }
          
          // 优先使用服务器返回的错误信息
          if (error.response.data && error.response.data.message) {
            errorMessage = error.response.data.message;
          }
        } else if (error.request) {
          errorMessage = '无法连接到服务器，请检查网络';
        } else {
          errorMessage = error.message || errorMessage;
        }
        
        console.error('管理员登录错误:', errorMessage);
        throw new Error(errorMessage);
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