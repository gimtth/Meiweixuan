import http from './http'

// 获取购物车列表
export const getCartItems = () => {
  return http.get('/cart')
}

// 添加商品到购物车
export const addToCart = (data) => {
  return http.post('/cart/add', data)
}

// 更新购物车商品数量
export const updateCartItem = (data) => {
  return http.put('/cart/update', data)
}

// 从购物车移除商品
export const removeFromCart = (id) => {
  return http.delete(`/cart/item/${id}`)
}

// 清空购物车
export const clearCart = () => {
  return http.delete('/cart/clear')
} 