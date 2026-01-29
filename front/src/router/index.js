import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../store/auth'

// 页面导入
import Home from '../views/Home.vue'
import Menu from '../views/Menu.vue'
import Cart from '../views/Cart.vue'
import Login from '../views/Login.vue'

// 路由配置
const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home,
    meta: { title: '首页' }
  },
  {
    path: '/menu',
    name: 'Menu',
    component: Menu,
    meta: { title: '菜单' }
  },
  {
    path: '/dish/:id',
    name: 'DishDetail',
    component: () => import('../views/DishDetail.vue'),
    meta: { title: '菜品详情' }
  },
  {
    path: '/cart',
    name: 'Cart',
    component: Cart,
    meta: { 
      title: '购物车',
      requiresAuth: true
    }
  },
  {
    path: '/checkout',
    name: 'Checkout',
    component: () => import('../views/Checkout.vue'),
    meta: { 
      title: '结算',
      requiresAuth: true
    }
  },
  {
    path: '/orders',
    name: 'Orders',
    component: () => import('../views/Orders.vue'),
    meta: { 
      title: '我的订单',
      requiresAuth: true
    }
  },
  {
    path: '/orders/:id',
    name: 'OrderDetail',
    component: () => import('../views/OrderDetail.vue'),
    meta: { 
      title: '订单详情',
      requiresAuth: true
    }
  },
  {
    path: '/profile',
    name: 'Profile',
    component: () => import('../views/Profile.vue'),
    meta: { 
      title: '个人中心',
      requiresAuth: true
    }
  },
  {
    path: '/login',
    name: 'Login',
    component: Login,
    meta: { title: '用户登录' }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue'),
    meta: { title: '用户注册' }
  },
  {
    path: '/activity',
    name: 'Activity',
    component: () => import('../views/Activity.vue'),
    meta: { title: '优惠活动' }
  },
  {
    path: '/about',
    name: 'About',
    component: () => import('../views/About.vue'),
    meta: { title: '关于我们' }
  },
  {
    path: '/contact',
    name: 'Contact',
    component: () => import('../views/Contact.vue'),
    meta: { title: '联系我们' }
  },
  {
    path: '/admin/login',
    name: 'AdminLogin',
    component: () => import('../views/admin/AdminLogin.vue'),
    meta: { title: '管理员登录' }
  },
  {
    path: '/admin',
    name: 'AdminDashboard',
    component: () => import('../views/admin/Dashboard.vue'),
    meta: { 
      title: '管理面板',
      requiresAuth: true,
      requiresAdmin: true
    },
    redirect: '/admin/statistics',
    children: [
      {
        path: 'statistics',
        name: 'AdminStatistics',
        component: () => import('../views/admin/Statistics.vue'),
        meta: { 
          title: '数据统计',
          requiresAuth: true,
          requiresAdmin: true
        }
      },
      {
        path: 'categories',
        name: 'AdminCategories',
        component: () => import('../views/admin/CategoryManage.vue'),
        meta: { 
          title: '分类管理',
          requiresAuth: true,
          requiresAdmin: true
        }
      },
      {
        path: 'dishes',
        name: 'AdminDishes',
        component: () => import('../views/admin/DishManage.vue'),
        meta: { 
          title: '菜品管理',
          requiresAuth: true,
          requiresAdmin: true
        }
      },
      {
        path: 'orders',
        name: 'AdminOrders',
        component: () => import('../views/admin/OrderManage.vue'),
        meta: { 
          title: '订单管理',
          requiresAuth: true,
          requiresAdmin: true
        }
      },
      {
        path: 'users',
        name: 'AdminUsers',
        component: () => import('../views/admin/UserManage.vue'),
        meta: { 
          title: '用户管理',
          requiresAuth: true,
          requiresAdmin: true
        }
      }
    ]
  },
  {
    path: '/404',
    name: 'NotFound',
    component: () => import('../views/NotFound.vue'),
    meta: { title: '页面未找到' }
  },
  {
    path: '/:catchAll(.*)',
    redirect: '/404'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition;
    } else {
      return { top: 0 };
    }
  }
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // 设置页面标题
  if (to.meta.title) {
    document.title = `${to.meta.title} - 美味轩`;
  }
  
  const authStore = useAuthStore();
  
  // 需要认证的路由
  if (to.meta.requiresAuth && !authStore.isLoggedIn) {
    next({
      path: '/login',
      query: { redirect: to.fullPath }
    });
    return;
  }
  
  // 需要管理员权限的路由
  if (to.meta.requiresAdmin && !authStore.isAdmin) {
    next('/404');
    return;
  }
  
  // 已登录用户访问登录/注册页面，重定向到首页
  if (authStore.isLoggedIn && (to.name === 'Login' || to.name === 'Register')) {
    next('/');
    return;
  }
  
  // 确保未登录用户可以访问首页
  if (to.path === '/' || !to.meta.requiresAuth) {
    next();
    return;
  }
  
  next();
})

export default router 