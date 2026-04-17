import { defineStore } from 'pinia'
import authApi from '../api/authApi'
import router from '../router'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    user: null,
    token: localStorage.getItem('token') || localStorage.getItem('adminToken') || null,
    isAdmin: localStorage.getItem('isAdmin') === 'true' || false
  }),
  
  getters: {
    isLoggedIn: (state) => !!state.token,
    userRole: (state) => state.isAdmin ? 'ROLE_ADMIN' : (state.user?.role || null)
  },
  
  actions: {
    // 初始化方法，检查登录状态的有效性
    init() {
      if (this.token && !this.user) {
        this.user = {
          id: this.isAdmin ? localStorage.getItem('adminId') : localStorage.getItem('userId'),
          username: this.isAdmin ? localStorage.getItem('adminUsername') : localStorage.getItem('username'),
          name: this.isAdmin ? localStorage.getItem('adminName') : localStorage.getItem('userName'),
          phone: this.isAdmin ? localStorage.getItem('adminPhone') : localStorage.getItem('userPhone'),
          address: localStorage.getItem('userAddress'),
          role: this.isAdmin ? 'ROLE_ADMIN' : localStorage.getItem('userRole')
        }
      }
    },
    
    // 清除所有认证数据
    clearAllAuthData() {
      this.user = null;
      this.token = null;
      this.isAdmin = false;
      
      // 清除所有localStorage中的认证数据
      localStorage.removeItem('token');
      localStorage.removeItem('userId');
      localStorage.removeItem('username');
      localStorage.removeItem('userName');
      localStorage.removeItem('userPhone');
      localStorage.removeItem('userAddress');
      localStorage.removeItem('userRole');
      localStorage.removeItem('adminToken');
      localStorage.removeItem('adminId');
      localStorage.removeItem('adminUsername');
      localStorage.removeItem('adminName');
      localStorage.removeItem('adminPhone');
      localStorage.removeItem('isAdmin');
    },
    
    async login(credentials) {
      try {
        console.log('用户登录请求:', credentials)
        const response = await authApi.login(credentials)
        console.log('用户登录成功，响应:', response)
        this.setUserAuthData(response)
        return response
      } catch (error) {
        console.error('用户登录失败:', error)
        throw error
      }
    },
    
    async register(userData) {
      return authApi.register(userData)
    },
    
    async adminLogin(credentials) {
      try {
        console.log('管理员登录请求:', credentials)
        const response = await authApi.adminLogin(credentials)
        console.log('管理员登录成功，响应:', response)
        
        // 检查响应格式是否正确
        if (!response || typeof response !== 'object') {
          console.error('服务器返回格式不正确:', response)
          throw new Error('服务器返回格式不正确')
        }
        
        // 检查必须字段
        if (!response.token) {
          console.error('响应缺少必要字段:', response)
          throw new Error('服务器响应缺少令牌信息')
        }
        
        this.setAdminAuthData(response)
        
        // 登录成功后直接跳转到管理后台
        router.push('/admin/statistics')
        
        return response
      } catch (error) {
        console.error('管理员登录失败:', error)
        throw error
      }
    },
    
    async checkAdminAuth() {
      try {
        console.log('检查管理员认证状态');
        const response = await authApi.checkAdminAuth();
        console.log('管理员认证检查结果:', response);
        return response;
      } catch (error) {
        console.error('管理员认证检查失败:', error);
        throw error;
      }
    },
    
    // 设置普通用户认证数据
    setUserAuthData(jwtResponse) {
      console.log('设置用户认证数据:', jwtResponse)
      
      // 从后端返回的token和user信息中提取
      const token = jwtResponse.token
      const user = jwtResponse.user || {}
      
      this.token = token
      
      this.user = {
        id: user.id || jwtResponse.id,
        username: user.username || jwtResponse.username,
        name: user.name || jwtResponse.name,
        phone: user.phone || jwtResponse.phone,
        address: user.address || jwtResponse.address,
        role: user.role || jwtResponse.role || 'ROLE_USER'
      }
      
      this.isAdmin = false
      
      // 存储用户数据
      localStorage.setItem('token', token)
      localStorage.setItem('userId', user.id || jwtResponse.id || '')
      localStorage.setItem('username', user.username || jwtResponse.username)
      localStorage.setItem('userName', user.name)
      localStorage.setItem('userPhone', user.phone || jwtResponse.phone || '')
      localStorage.setItem('userAddress', user.address || jwtResponse.address || '')
      localStorage.setItem('userRole', user.role || jwtResponse.role || 'ROLE_USER')
      localStorage.setItem('isAdmin', 'false')
      
      // 清除管理员数据
      localStorage.removeItem('adminToken')
      localStorage.removeItem('adminId')
      localStorage.removeItem('adminUsername')
      localStorage.removeItem('adminName')
    },
    
    // 设置管理员认证数据
    setAdminAuthData(adminJwtResponse) {
      console.log('设置管理员认证数据:', adminJwtResponse)
      
      // 从后端返回的token和admin信息中提取
      const token = adminJwtResponse.token
      const admin = adminJwtResponse.admin || {}
      
      this.token = token
      
      this.user = {
        id: admin.id || adminJwtResponse.id,
        username: admin.username || adminJwtResponse.username,
        name: admin.name || adminJwtResponse.name,
        phone: admin.phone || adminJwtResponse.phone,
        role: 'ROLE_ADMIN'
      }
      
      this.isAdmin = true
      
      // 存储管理员数据
      localStorage.setItem('adminToken', token)
      localStorage.setItem('adminId', admin.id || adminJwtResponse.id || '')
      localStorage.setItem('adminUsername', admin.username || adminJwtResponse.username)
      localStorage.setItem('adminName', admin.name || admin.username || adminJwtResponse.username || '')
      localStorage.setItem('adminPhone', admin.phone || adminJwtResponse.phone || '')
      localStorage.setItem('isAdmin', 'true')
      
      // 清除普通用户数据
      localStorage.removeItem('token')
      localStorage.removeItem('userId')
      localStorage.removeItem('username')
      localStorage.removeItem('userRole')
    },
    
    logout() {
      const wasAdmin = this.isAdmin
      this.clearAllAuthData();
      
      if (wasAdmin) {
        router.push('/admin/login');
      } else {
        router.push('/login');
      }
    }
  }
}) 
