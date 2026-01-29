<template>
  <header class="header">
    <div class="header-container">
      <div class="logo">
        <router-link to="/">
          <img :src="logoImage" alt="美味轩logo">
          <span>美味轩</span>
        </router-link>
      </div>
      
      <ul class="nav-links">
        <li class="nav-item" :class="{ 'active': $route.path === '/' }">
          <router-link to="/" class="nav-link">首页</router-link>
        </li>
        <li class="nav-item" :class="{ 'active': $route.path === '/menu' }">
          <router-link to="/menu" class="nav-link">菜单</router-link>
        </li>
        <li class="nav-item" :class="{ 'active': $route.path.includes('/activity') }">
          <router-link to="/activity" class="nav-link">优惠活动</router-link>
        </li>
        <li class="nav-item" :class="{ 'active': $route.path === '/about' }">
          <router-link to="/about" class="nav-link">关于我们</router-link>
        </li>
        <li class="nav-item" :class="{ 'active': $route.path === '/contact' }">
          <router-link to="/contact" class="nav-link">联系我们</router-link>
        </li>
      </ul>
      
      <div class="user-actions">
        <!-- 购物车按钮 -->
        <router-link to="/cart" class="cart-btn">
          <div class="cart-icon-container">
            <img :src="cartIcon" alt="购物车">
            <span class="cart-count" v-if="cartCount > 0">{{ cartCount }}</span>
          </div>
        </router-link>
        
        <!-- 未登录状态 -->
        <template v-if="!isLoggedIn">
          <router-link to="/login" class="btn-login">登录</router-link>
          <router-link to="/register" class="btn-register">注册</router-link>
        </template>
        
        <!-- 已登录状态 -->
        <div class="user-dropdown" v-else>
          <div class="dropdown-toggle" @click="toggleDropdown">
            <span class="username">{{ username }}</span>
            <i class="icon-down"></i>
          </div>
          
          <div class="dropdown-menu" v-if="dropdownVisible">
            <router-link to="/orders" class="dropdown-item" v-if="!isAdmin">我的订单</router-link>
            <router-link to="/profile" class="dropdown-item">个人中心</router-link>
            <div class="dropdown-divider"></div>
            <router-link to="/admin" class="dropdown-item" v-if="isAdmin">管理后台</router-link>
            <a href="javascript:void(0)" @click="logout" class="dropdown-item logout">退出登录</a>
          </div>
        </div>
      </div>
      
      <!-- 移动端购物车按钮 -->
      <router-link to="/cart" class="mobile-cart-btn" v-if="isLoggedIn">
        <div class="cart-icon-container">
          <img :src="cartIcon" alt="购物车">
          <span class="cart-count" v-if="cartCount > 0">{{ cartCount }}</span>
        </div>
      </router-link>
      
      <!-- 移动端菜单按钮 -->
      <div class="mobile-menu-toggle" @click="toggleMobileMenu">
        <span class="toggle-icon"></span>
      </div>
    </div>
    
    <!-- 移动端菜单 -->
    <div class="mobile-menu" :class="{ 'active': mobileMenuVisible }">
      <ul class="mobile-nav-list">
        <li class="mobile-nav-item">
          <router-link to="/" class="mobile-nav-link" @click="closeMobileMenu">首页</router-link>
        </li>
        <li class="mobile-nav-item">
          <router-link to="/menu" class="mobile-nav-link" @click="closeMobileMenu">菜单</router-link>
        </li>
        <li class="mobile-nav-item">
          <router-link to="/activity" class="mobile-nav-link" @click="closeMobileMenu">优惠活动</router-link>
        </li>
        <li class="mobile-nav-item">
          <router-link to="/about" class="mobile-nav-link" @click="closeMobileMenu">关于我们</router-link>
        </li>
        <li class="mobile-nav-item">
          <router-link to="/contact" class="mobile-nav-link" @click="closeMobileMenu">联系我们</router-link>
        </li>
        <li class="mobile-nav-item" v-if="isLoggedIn">
          <router-link to="/cart" class="mobile-nav-link" @click="closeMobileMenu">购物车</router-link>
        </li>
        <li class="mobile-nav-item" v-if="isLoggedIn && !isAdmin">
          <router-link to="/orders" class="mobile-nav-link" @click="closeMobileMenu">我的订单</router-link>
        </li>
        <li class="mobile-nav-item" v-if="isLoggedIn">
          <router-link to="/profile" class="mobile-nav-link" @click="closeMobileMenu">个人中心</router-link>
        </li>
        <li class="mobile-nav-item" v-if="isLoggedIn && isAdmin">
          <router-link to="/admin" class="mobile-nav-link" @click="closeMobileMenu">管理员后台</router-link>
        </li>
        <li class="mobile-nav-item" v-if="!isLoggedIn">
          <router-link to="/login" class="mobile-nav-link" @click="closeMobileMenu">登录</router-link>
        </li>
        <li class="mobile-nav-item" v-if="!isLoggedIn">
          <router-link to="/register" class="mobile-nav-link" @click="closeMobileMenu">注册</router-link>
        </li>
        <li class="mobile-nav-item" v-if="isLoggedIn">
          <a href="javascript:void(0)" @click="logoutAndCloseMobileMenu" class="mobile-nav-link logout">退出登录</a>
        </li>
      </ul>
    </div>
  </header>
</template>

<script>
import { ref, computed, onMounted, onUnmounted } from 'vue';
import { useAuthStore } from '../store/auth';
import { useCartStore } from '../store/cart';
import logoImage from '../assets/images/logo.png';
import cartIcon from '../assets/images/icon-cart.png';

export default {
  name: 'TheHeader',
  setup() {
    const authStore = useAuthStore();
    const cartStore = useCartStore();
    
    const dropdownVisible = ref(false);
    const mobileMenuVisible = ref(false);
    
    // 计算属性
    const isLoggedIn = computed(() => authStore.isLoggedIn);
    const username = computed(() => authStore.user?.username || '用户');
    const cartCount = computed(() => cartStore.totalItems);
    const isAdmin = computed(() => authStore.isAdmin);
    
    // 切换用户下拉菜单
    const toggleDropdown = () => {
      dropdownVisible.value = !dropdownVisible.value;
    };
    
    // 切换移动端菜单
    const toggleMobileMenu = () => {
      mobileMenuVisible.value = !mobileMenuVisible.value;
      if (mobileMenuVisible.value) {
        document.body.style.overflow = 'hidden';
      } else {
        document.body.style.overflow = '';
      }
    };
    
    // 关闭移动端菜单
    const closeMobileMenu = () => {
      mobileMenuVisible.value = false;
      document.body.style.overflow = '';
    };
    
    // 退出登录
    const logout = () => {
      authStore.logout();
      dropdownVisible.value = false;
    };
    
    // 退出登录并关闭移动端菜单
    const logoutAndCloseMobileMenu = () => {
      authStore.logout();
      closeMobileMenu();
    };
    
    // 点击其他地方关闭下拉菜单
    const closeDropdownOnClickOutside = (event) => {
      const dropdown = document.querySelector('.user-dropdown');
      if (dropdown && !dropdown.contains(event.target)) {
        dropdownVisible.value = false;
      }
    };
    
    onMounted(() => {
      document.addEventListener('click', closeDropdownOnClickOutside);
      
      // 如果用户已登录，加载购物车数据
      if (authStore.isLoggedIn) {
        cartStore.fetchCartItems();
      }
    });
    
    onUnmounted(() => {
      document.removeEventListener('click', closeDropdownOnClickOutside);
    });
    
    return {
      isLoggedIn,
      username,
      cartCount,
      isAdmin,
      dropdownVisible,
      mobileMenuVisible,
      toggleDropdown,
      toggleMobileMenu,
      closeMobileMenu,
      logout,
      logoutAndCloseMobileMenu,
      logoImage,
      cartIcon
    };
  }
};
</script>

<style scoped>
.header {
  background-color: #e74c3c;
  color: white;
  padding: 15px 0;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-container {
  width: 90%;
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo {
  font-size: 28px;
  font-weight: bold;
  display: flex;
  align-items: center;
}

.logo a {
  display: flex;
  align-items: center;
  text-decoration: none;
  color: white;
}

.logo img {
  height: 40px;
  margin-right: 10px;
}

.nav-links {
  display: flex;
  list-style: none;
  margin: 0;
  padding: 0;
}

.nav-item {
  margin-left: 25px;
  position: relative;
}

.nav-link {
  font-weight: 500;
  padding: 5px 0;
  color: white;
  text-decoration: none;
  transition: all 0.3s ease;
}

.nav-links li a:hover {
  opacity: 0.8;
}

.nav-links li.active a {
  border-bottom: 2px solid white;
}

.user-actions {
  display: flex;
  align-items: center;
}

/* 购物车按钮 */
.cart-btn {
  margin-right: 20px;
  text-decoration: none;
}

.cart-icon-container {
  position: relative;
  width: 45px;
  height: 45px;
  background-color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
  box-shadow: 0 3px 8px rgba(0, 0, 0, 0.2);
}

.cart-icon-container:hover {
  transform: scale(1.1);
  box-shadow: 0 5px 12px rgba(0, 0, 0, 0.3);
}

.cart-icon-container img {
  height: 24px;
  width: 24px;
}

.cart-count {
  position: absolute;
  top: -8px;
  right: -8px;
  background-color: #f39c12;
  color: white;
  border-radius: 50%;
  width: 20px;
  height: 20px;
  font-size: 12px;
  font-weight: bold;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.15);
  }
  100% {
    transform: scale(1);
  }
}

.btn-login,
.btn-register {
  padding: 8px 15px;
  margin-left: 10px;
  text-decoration: none;
  font-size: 14px;
  border-radius: 4px;
  transition: all 0.3s;
}

.btn-login {
  color: #e74c3c;
  border: 1px solid white;
  background-color: white;
}

.btn-login:hover {
  background-color: #f8f8f8;
}

.btn-register {
  color: white;
  background-color: transparent;
  border: 1px solid white;
}

.btn-register:hover {
  background-color: rgba(255, 255, 255, 0.1);
}

.user-dropdown {
  position: relative;
}

.dropdown-toggle {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 5px 10px;
  border-radius: 4px;
  transition: background-color 0.3s;
  color: white;
}

.dropdown-toggle:hover {
  background-color: rgba(255, 255, 255, 0.1);
}

.username {
  margin-right: 5px;
}

.icon-down {
  display: inline-block;
  width: 12px;
  height: 12px;
  background-image: url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="white"><path d="M7 10l5 5 5-5H7z"/></svg>');
  background-repeat: no-repeat;
  background-position: center;
}

.dropdown-menu {
  position: absolute;
  top: 100%;
  right: 0;
  z-index: 1000;
  margin-top: 10px;
  min-width: 150px;
  background-color: white;
  border-radius: 4px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  padding: 5px 0;
}

.dropdown-item {
  display: block;
  padding: 10px 15px;
  color: #333;
  text-decoration: none;
  transition: background-color 0.3s;
}

.dropdown-item:hover {
  background-color: #f5f5f5;
}

.dropdown-divider {
  height: 1px;
  margin: 5px 0;
  background-color: #eee;
}

.logout {
  color: var(--error-color, #f44336);
}

/* 移动端专用购物车按钮 */
.mobile-cart-btn {
  display: none;
  position: absolute;
  right: 60px;
  top: 15px;
}

/* 移动端菜单按钮 */
.mobile-menu-toggle {
  display: none;
  cursor: pointer;
  width: 30px;
  height: 30px;
  position: relative;
}

.toggle-icon,
.toggle-icon:before,
.toggle-icon:after {
  position: absolute;
  width: 100%;
  height: 2px;
  background-color: white;
  transition: all 0.3s;
}

.toggle-icon {
  top: 50%;
  transform: translateY(-50%);
}

.toggle-icon:before {
  content: '';
  top: -8px;
}

.toggle-icon:after {
  content: '';
  bottom: -8px;
}

.mobile-menu {
  display: none;
  position: fixed;
  top: 70px;
  left: 0;
  width: 100%;
  height: calc(100vh - 70px);
  background-color: white;
  z-index: 999;
  transform: translateX(100%);
  transition: transform 0.3s;
  overflow-y: auto;
}

.mobile-menu.active {
  transform: translateX(0);
}

.mobile-nav-list {
  list-style: none;
  padding: 20px;
  margin: 0;
}

.mobile-nav-item {
  margin-bottom: 15px;
}

.mobile-nav-link {
  display: block;
  padding: 15px;
  color: #333;
  text-decoration: none;
  font-size: 16px;
  border-radius: 4px;
  transition: all 0.3s;
}

.mobile-nav-link:hover {
  background-color: #f5f5f5;
}

.mobile-nav-link.logout {
  color: var(--error-color, #f44336);
}

/* 响应式调整 */
@media (max-width: 768px) {
  .header-container {
    flex-direction: column;
  }
  
  .logo {
    margin-bottom: 15px;
  }
  
  .nav-links {
    display: none;
  }
  
  .btn-login,
  .btn-register,
  .user-dropdown,
  .cart-btn {
    display: none;
  }
  
  .mobile-menu-toggle {
    display: block;
    position: absolute;
    right: 15px;
    top: 15px;
  }
  
  .mobile-menu {
    display: block;
  }
  
  .mobile-cart-btn {
    display: block;
  }
}


</style> 