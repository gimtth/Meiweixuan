import http from './http'

// 获取菜品列表
export const getDishes = (params) => {
  return http.get('/dishes', { params })
}

// 获取菜品详情
export const getDishById = (id) => {
  return http.get(`/dishes/${id}`)
}

// 获取菜品分类
export const getCategories = () => {
  return http.get('/categories')
}

// 以下是管理员接口
// 添加菜品
export const addDish = (data) => {
  return http.post('/admin/dishes', data)
}

// 更新菜品
export const updateDish = (id, data) => {
  return http.put(`/admin/dishes/${id}`, data)
}

// 删除菜品
export const deleteDish = (id) => {
  return http.delete(`/admin/dishes/${id}`)
} 