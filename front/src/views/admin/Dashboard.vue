<template>
  <div class="admin-dashboard">
    <div class="sidebar">
      <div class="logo">
        <h2>网上订餐系统</h2>
        <p>管理后台</p>
      </div>
      
      <div class="menu">
        <router-link 
          :to="{ name: 'AdminStatistics' }" 
          active-class="active"
          class="menu-item"
        >
          <i class="el-icon-data-analysis"></i> 数据统计
        </router-link>
        
        <router-link 
          :to="{ name: 'AdminOrders' }" 
          active-class="active"
          class="menu-item"
        >
          <i class="el-icon-document"></i> 订单管理
        </router-link>
        
        <router-link 
          :to="{ name: 'AdminDishes' }" 
          active-class="active"
          class="menu-item"
        >
          <i class="el-icon-dish"></i> 菜品管理
        </router-link>
        
        <router-link 
          :to="{ name: 'AdminCategories' }" 
          active-class="active"
          class="menu-item"
        >
          <i class="el-icon-collection"></i> 分类管理
        </router-link>
        
        <router-link 
          :to="{ name: 'AdminUsers' }" 
          active-class="active"
          class="menu-item"
        >
          <i class="el-icon-user"></i> 用户管理
        </router-link>
      </div>
      
      <div class="sidebar-footer">
        <div class="user-info">
          <span>{{ username }}</span>
          <small>管理员</small>
        </div>
        <button class="logout-btn" @click="logout">
          <i class="el-icon-switch-button"></i> 退出
        </button>
      </div>
    </div>
    
    <div class="main-content">
      <div class="header">
        <div class="header-left">
          <h2>{{ pageTitle }}</h2>
        </div>
        <div class="header-right">
          <span class="time">{{ currentTime }}</span>
        </div>
      </div>
      
      <div class="content">
        <!-- 路由视图 -->
        <router-view></router-view>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted, onBeforeUnmount } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useAuthStore } from '../../store/auth';
import { ElMessage, ElMessageBox } from 'element-plus';

export default {
  name: 'AdminDashboard',
  setup() {
    const route = useRoute();
    const router = useRouter();
    const authStore = useAuthStore();
    
    // 检查登录状态和权限
    if (!authStore.isLoggedIn || !authStore.isAdmin) {
      ElMessage.error('请先登录管理员账号');
      router.push('/admin/login');
    }
    
    const username = computed(() => authStore.user?.username || '');
    const currentTime = ref(new Date().toLocaleString());
    
    // 定时更新时间
    let timeInterval;
    
    const updateTime = () => {
      currentTime.value = new Date().toLocaleString();
    };
    
    // 页面标题
    const pageTitle = computed(() => {
      return route.meta.title || '管理面板';
    });
    
    // 登出方法
    const logout = () => {
      ElMessageBox.confirm(
        '确定要退出登录吗？',
        '提示',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      ).then(() => {
        authStore.logout();
        router.push('/');
        ElMessage.success('已成功退出登录');
      }).catch(() => {});
    };
    
    onMounted(() => {
      timeInterval = setInterval(updateTime, 1000);
    });
    
    onBeforeUnmount(() => {
      if (timeInterval) {
        clearInterval(timeInterval);
      }
    });
    
    return {
      username,
      currentTime,
      pageTitle,
      logout
    };
  }
};
</script>

<style scoped>
.admin-dashboard {
  display: flex;
  min-height: 100vh;
}

.sidebar {
  width: 250px;
  background-color: #2c3e50;
  color: white;
  display: flex;
  flex-direction: column;
  position: fixed;
  height: 100vh;
  overflow-y: auto;
}

.logo {
  padding: 20px 15px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  text-align: center;
}

.logo h2 {
  margin: 0 0 5px;
  font-size: 20px;
  font-weight: 700;
}

.logo p {
  margin: 0;
  font-size: 14px;
  opacity: 0.7;
}

.menu {
  padding: 20px 0;
  flex-grow: 1;
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 15px 20px;
  color: rgba(255, 255, 255, 0.7);
  text-decoration: none;
  transition: all 0.3s;
}

.menu-item:hover {
  background-color: rgba(255, 255, 255, 0.1);
  color: white;
}

.menu-item.active {
  background-color: var(--primary-color, #e53935);
  color: white;
}

.menu-item i {
  margin-right: 10px;
  font-size: 18px;
}

.sidebar-footer {
  padding: 15px;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
}

.user-info {
  display: flex;
  flex-direction: column;
  margin-bottom: 10px;
}

.user-info span {
  font-weight: 500;
}

.user-info small {
  opacity: 0.7;
  font-size: 12px;
}

.logout-btn {
  width: 100%;
  background-color: rgba(255, 255, 255, 0.1);
  border: none;
  color: white;
  padding: 8px 10px;
  border-radius: 4px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background-color 0.3s;
}

.logout-btn:hover {
  background-color: rgba(255, 255, 255, 0.2);
}

.logout-btn i {
  margin-right: 5px;
}

.main-content {
  flex: 1;
  margin-left: 250px;
  padding: 20px;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #eee;
}

.header-left h2 {
  margin: 0;
  font-size: 24px;
  color: #333;
}

.time {
  font-size: 14px;
  color: #666;
}

.content {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  padding: 20px;
  min-height: calc(100vh - 130px);
}
</style> 