import axios from 'axios'
import router from '../router'
import { ElMessage } from 'element-plus'

// 创建axios实例
const http = axios.create({
  baseURL: '/api',  // 修改为包含/api前缀的路径
  timeout: 5000,  // 将超时时间从10秒减少到5秒
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json',
    'X-Requested-With': 'XMLHttpRequest'
  },
  withCredentials: true
})

// 请求拦截器
http.interceptors.request.use(
  config => {
    // 检查是否是管理员API请求
    const isAdminRequest = config.url.startsWith('/admin/')
    
    // 根据请求类型选择不同的token
    let token
    if (isAdminRequest) {
      token = localStorage.getItem('adminToken')
      console.log(`管理员请求: ${config.url}`)
    } else {
      token = localStorage.getItem('token')
      console.log(`普通用户请求: ${config.url}`)
    }
    
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
      console.log(`请求: ${config.url}, 添加Token: Bearer ${token.substring(0, 20)}...`)
    } else {
      console.log(`请求: ${config.url}, 无Token`)
    }

    // 增强日志 - 打印请求参数
    if (config.data) {
      console.log(`请求参数: ${JSON.stringify(config.data)}`)
    }
    
    return config
  },
  error => {
    console.error('请求拦截器错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
http.interceptors.response.use(
  response => {
    // 处理后端ApiResponse格式，提取data字段
    const apiResponse = response.data;
    
    // 增强日志 - 打印完整响应
    console.log(`响应数据: ${JSON.stringify(apiResponse)}`);
    
    // 兼容后端直接返回数组的情况
    if (Array.isArray(apiResponse)) {
      return apiResponse;
    }
    // 兼容后端直接返回对象且无code字段的情况
    if (typeof apiResponse === 'object' && !('code' in apiResponse)) {
      return apiResponse;
    }
    // 处理R<Map>格式响应
    if (apiResponse.code === 200) {
      console.log(`响应成功: ${response.config.url}`);
      return apiResponse.data;
    } else {
      // 如果后端返回错误码
      console.error(`响应错误: ${response.config.url}, 错误码: ${apiResponse.code}, 消息: ${apiResponse.message}`);
      ElMessage.error(apiResponse.message || '请求失败');
      return Promise.reject(new Error(apiResponse.message || '请求失败'));
    }
  },
  error => {
    // 处理CORS错误
    if (error.message === 'Network Error') {
      console.error('CORS错误或网络连接问题，请检查服务器CORS配置');
      ElMessage.error('无法连接到服务器，可能是网络问题或CORS配置问题');
      return Promise.reject(new Error('无法连接到服务器，可能是CORS配置问题'));
    }
    
    const { response } = error
    
    if (response) {
      console.error(`请求失败: ${response.config?.url}, 状态码: ${response.status}`)
      
      // 增强日志 - 打印错误响应详情
      if (response.data) {
        console.error(`错误响应详情: ${JSON.stringify(response.data)}`)
      }
      
      if (response.status === 401) {
        console.warn('未授权访问，清除本地存储并跳转到登录页')
        // 检查是否为管理员API
        if (response.config?.url?.includes('/admin/')) {
          localStorage.removeItem('adminToken')
          localStorage.removeItem('adminId')
          localStorage.removeItem('adminUsername')
          localStorage.removeItem('adminName')
          localStorage.removeItem('isAdmin')
          router.push('/admin/login')
          ElMessage.error('管理员登录已过期，请重新登录');
        } else {
          localStorage.removeItem('token')
          localStorage.removeItem('userId')
          localStorage.removeItem('username')
          localStorage.removeItem('userRole')
          router.push('/login')
          ElMessage.error('登录已过期，请重新登录');
        }
      } else if (response.status === 403) {
        console.warn('禁止访问，可能是权限不足')
        ElMessage.error('权限不足，无法访问')
      } else if (response.status === 405) {
        console.warn('请求方法不允许')
        ElMessage.error('请求方法不允许，请联系管理员')
      } else if (response.status === 404) {
        console.warn('请求的资源不存在')
        ElMessage.error('请求的资源不存在，可能是接口尚未实现')
      } else if (response.status >= 500) {
        console.warn('服务器内部错误')
        ElMessage.error('服务器内部错误，请稍后再试')
      }
      
      // 尝试处理ApiResponse格式的错误
      if (response.data) {
        if (response.data.message) {
          return Promise.reject(new Error(response.data.message));
        } else if (typeof response.data === 'string') {
          return Promise.reject(new Error(response.data));
        }
      }
      
      return Promise.reject(new Error(response.statusText || '服务器错误'))
    }
    
    console.error('网络错误:', error.message)
    ElMessage.error('网络连接错误，请检查网络设置');
    return Promise.reject(new Error(error.message || '网络错误'))
  }
)

export default http 