import http from './http'

// 创建订单
export const createOrder = (data) => {
  return http.post('/orders/create', data)
}

// 获取用户订单列表
export const getOrders = () => {
  return http.get('/orders')
}

// 获取订单详情
export const getOrderById = (id) => {
  return http.get(`/orders/${id}`)
}

// 以下是管理员接口
// 获取所有订单
export const getAllOrders = (params) => {
  return http.get('/admin/orders', { params })
}

// 更新订单状态
export const updateOrderStatus = (id, status) => {
  return http.put(`/admin/orders/${id}/status`, { status })
} 