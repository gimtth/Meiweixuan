import http from './http'

// 用户登录
export const login = (data) => {
  return http.post('/auth/login', data)
}

// 用户注册
export const register = (data) => {
  return http.post('/auth/register', data)
}

// 管理员登录
export const adminLogin = (data) => {
  return http.post('/admin/auth/login', data)
}

// 获取当前用户信息
export const getCurrentUser = () => {
  return http.get('/users/current')
}

// 修改用户信息
export const updateUserInfo = (userId, data) => {
  return http.put(`/users/${userId}`, data)
}

// 修改密码
export const changePassword = (data) => {
  return http.post('/users/change-password', data)
} 