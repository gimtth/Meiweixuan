<template>
  <div class="profile-page">
    <div class="container">
      <div class="page-title">
        <h1>个人中心</h1>
      </div>
      
      <!-- 加载中状态 -->
      <div v-if="loading" class="loading-state">
        <p>加载中...</p>
      </div>
      
      <!-- 错误状态 -->
      <div v-else-if="error" class="error-state">
        <p>{{ error }}</p>
        <button class="btn-primary" @click="fetchUserInfo">重试</button>
      </div>
      
      <!-- 个人信息内容 -->
      <div v-else class="profile-content">
        <!-- 导航标签页 -->
        <div class="profile-nav">
          <div
            class="nav-item"
            :class="{ active: activeTab === 'info' }"
            @click="activeTab = 'info'"
          >
            个人资料
          </div>
          <div
            class="nav-item"
            :class="{ active: activeTab === 'password' }"
            @click="activeTab = 'password'"
          >
            修改密码
          </div>
        </div>
        
        <!-- 个人资料表单 -->
        <div v-if="activeTab === 'info'" class="profile-form">
          <form @submit.prevent="updateProfile">
            <div class="form-group">
              <label for="username">用户名</label>
              <input
                type="text"
                id="username"
                v-model="userInfo.username"
                disabled
              />
              <small class="form-hint">用户名不可修改</small>
            </div>
            
            <div class="form-group">
              <label for="name">真实姓名</label>
              <input
                type="text"
                id="name"
                v-model="userInfo.name"
                placeholder="请输入您的真实姓名"
                required
              />
            </div>
            
            <div class="form-group">
              <label for="phone">联系电话</label>
              <input
                type="tel"
                id="phone"
                v-model="userInfo.phone"
                placeholder="请输入您的联系电话"
                required
              />
            </div>
            
            <div class="form-group">
              <label for="address">收货地址</label>
              <textarea
                id="address"
                v-model="userInfo.address"
                placeholder="请输入您的收货地址"
                required
              ></textarea>
            </div>
            
            <div v-if="infoError" class="error-message">
              {{ infoError }}
            </div>
            
            <button type="submit" class="btn-submit" :disabled="infoSubmitting">
              {{ infoSubmitting ? '保存中...' : '保存修改' }}
            </button>
          </form>
        </div>
        
        <!-- 修改密码表单 -->
        <div v-else-if="activeTab === 'password'" class="profile-form">
          <form @submit.prevent="updatePassword">
            <div class="form-group">
              <label for="oldPassword">当前密码</label>
              <input
                type="password"
                id="oldPassword"
                v-model="passwordForm.oldPassword"
                placeholder="请输入当前密码"
                required
              />
            </div>
            
            <div class="form-group">
              <label for="newPassword">新密码</label>
              <input
                type="password"
                id="newPassword"
                v-model="passwordForm.newPassword"
                placeholder="请输入新密码"
                required
                minlength="6"
              />
              <small class="form-hint">密码长度至少6位</small>
            </div>
            
            <div class="form-group">
              <label for="confirmPassword">确认新密码</label>
              <input
                type="password"
                id="confirmPassword"
                v-model="passwordForm.confirmPassword"
                placeholder="请再次输入新密码"
                required
              />
            </div>
            
            <div v-if="passwordError" class="error-message">
              {{ passwordError }}
            </div>
            
            <button type="submit" class="btn-submit" :disabled="passwordSubmitting">
              {{ passwordSubmitting ? '更新中...' : '更新密码' }}
            </button>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '../store/auth';
import userApi from '../api/userApi';
import { ElMessage } from 'element-plus';

export default {
  name: 'ProfilePage',
  setup() {
    const router = useRouter();
    const authStore = useAuthStore();
    
    const activeTab = ref('info');
    const loading = ref(true);
    const error = ref(null);
    
    // 个人资料表单
    const userInfo = reactive({
      id: null,
      username: '',
      name: '',
      phone: '',
      address: ''
    });
    const infoSubmitting = ref(false);
    const infoError = ref('');
    
    // 修改密码表单
    const passwordForm = reactive({
      oldPassword: '',
      newPassword: '',
      confirmPassword: ''
    });
    const passwordSubmitting = ref(false);
    const passwordError = ref('');
    
    // 获取用户信息
    const fetchUserInfo = async () => {
      try {
        loading.value = true;
        error.value = null;
        
        const userData = await userApi.getCurrentUser();
        
        // 填充表单
        userInfo.id = userData.id;
        userInfo.username = userData.username;
        userInfo.name = userData.name || '';
        userInfo.phone = userData.phone || '';
        userInfo.address = userData.address || '';
      } catch (err) {
        error.value = '获取用户信息失败，请稍后再试';
        console.error('Failed to fetch user info:', err);
      } finally {
        loading.value = false;
      }
    };
    
    // 更新个人资料
    const updateProfile = async () => {
      try {
        infoSubmitting.value = true;
        infoError.value = '';
        
        const updatedData = {
          name: userInfo.name,
          phone: userInfo.phone,
          address: userInfo.address
        };
        
        await userApi.updateUser(userInfo.id, updatedData);
        
        ElMessage.success('个人资料更新成功');
      } catch (err) {
        infoError.value = err.message || '更新失败，请稍后再试';
      } finally {
        infoSubmitting.value = false;
      }
    };
    
    // 更新密码
    const updatePassword = async () => {
      // 验证两次密码是否一致
      if (passwordForm.newPassword !== passwordForm.confirmPassword) {
        passwordError.value = '两次输入的密码不一致';
        return;
      }
      
      try {
        passwordSubmitting.value = true;
        passwordError.value = '';
        
        await userApi.changePassword({
          oldPassword: passwordForm.oldPassword,
          newPassword: passwordForm.newPassword
        });
        
        // 清空表单
        passwordForm.oldPassword = '';
        passwordForm.newPassword = '';
        passwordForm.confirmPassword = '';
        
        ElMessage.success('密码修改成功');
      } catch (err) {
        passwordError.value = err.message || '密码修改失败，请稍后再试';
      } finally {
        passwordSubmitting.value = false;
      }
    };
    
    onMounted(() => {
      // 检查是否已登录
      if (!authStore.isLoggedIn) {
        ElMessage.warning('请先登录');
        router.push('/login');
        return;
      }
      
      fetchUserInfo();
    });
    
    return {
      activeTab,
      loading,
      error,
      userInfo,
      infoSubmitting,
      infoError,
      passwordForm,
      passwordSubmitting,
      passwordError,
      fetchUserInfo,
      updateProfile,
      updatePassword
    };
  }
};
</script>

<style scoped>
.profile-page {
  padding: 40px 0;
}

.page-title {
  margin-bottom: 30px;
}

.page-title h1 {
  font-size: 32px;
  margin: 0;
  position: relative;
  display: inline-block;
  padding-bottom: 10px;
}

.page-title h1:after {
  content: '';
  position: absolute;
  left: 0;
  bottom: 0;
  width: 50px;
  height: 3px;
  background-color: var(--primary-color, #e53935);
}

.loading-state,
.error-state {
  text-align: center;
  padding: 50px 0;
  font-size: 16px;
  color: #666;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.btn-primary {
  display: inline-block;
  background-color: var(--primary-color, #e53935);
  color: white;
  padding: 10px 20px;
  border-radius: 4px;
  border: none;
  text-decoration: none;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.3s;
  margin-top: 15px;
}

.btn-primary:hover {
  background-color: var(--primary-dark, #c62828);
}

.profile-content {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.profile-nav {
  display: flex;
  border-bottom: 1px solid #eee;
}

.nav-item {
  padding: 15px 20px;
  font-size: 16px;
  cursor: pointer;
  transition: all 0.3s;
}

.nav-item:hover {
  background-color: #f9f9f9;
}

.nav-item.active {
  color: var(--primary-color, #e53935);
  font-weight: 500;
  position: relative;
}

.nav-item.active:after {
  content: '';
  position: absolute;
  left: 0;
  right: 0;
  bottom: -1px;
  height: 2px;
  background-color: var(--primary-color, #e53935);
}

.profile-form {
  padding: 30px;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: #333;
}

.form-group input,
.form-group textarea {
  width: 100%;
  padding: 12px 15px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 16px;
  transition: border-color 0.3s;
}

.form-group input:disabled {
  background-color: #f5f5f5;
  cursor: not-allowed;
}

.form-group textarea {
  height: 100px;
  resize: vertical;
}

.form-group input:focus,
.form-group textarea:focus {
  outline: none;
  border-color: var(--primary-color, #e53935);
}

.form-hint {
  display: block;
  font-size: 13px;
  color: #999;
  margin-top: 5px;
}

.error-message {
  color: #e53935;
  margin-bottom: 15px;
  font-size: 14px;
}

.btn-submit {
  background-color: var(--primary-color, #e53935);
  color: white;
  padding: 12px 25px;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.3s;
}

.btn-submit:hover:not(:disabled) {
  background-color: var(--primary-dark, #c62828);
}

.btn-submit:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

/* 响应式调整 */
@media (max-width: 576px) {
  .profile-nav {
    flex-direction: column;
  }
  
  .nav-item.active:after {
    bottom: 0;
    right: auto;
    width: 3px;
    height: 100%;
    top: 0;
  }
}
</style> 