<template>
  <div class="admin-login-page">
    <div class="container">
      <div class="login-container">
        <div class="login-header">
          <h2>管理员登录</h2>
          <p>请输入管理员账号和密码</p>
        </div>
        
        <form @submit.prevent="handleLogin" class="login-form">
          <div class="form-group">
            <label for="username">管理员账号</label>
            <input 
              type="text" 
              id="username" 
              v-model="username" 
              placeholder="请输入管理员账号" 
              required
            />
          </div>
          
          <div class="form-group">
            <label for="password">密码</label>
            <input 
              type="password" 
              id="password" 
              v-model="password" 
              placeholder="请输入密码" 
              required
            />
          </div>
          
          <div v-if="error" class="error-message">
            {{ error }}
          </div>
          
          <button type="submit" class="login-button" :disabled="loading">
            {{ loading ? '登录中...' : '登录' }}
          </button>
        </form>
        
        <div class="login-footer">
          <router-link to="/login" class="back-link">
            <i class="icon-back"></i>
            返回用户登录
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '../../store/auth';
import { ElMessage } from 'element-plus';
import authApi from '../../api/authApi';

export default {
  name: 'AdminLoginPage',
  setup() {
    const username = ref('');
    const password = ref('');
    const loading = ref(false);
    const error = ref('');
    
    const router = useRouter();
    const authStore = useAuthStore();
    
    // 删除默认管理员账号密码
    username.value = '';
    password.value = '';
    
    const handleLogin = async () => {
      try {
        loading.value = true;
        error.value = '';
        
        console.log('尝试管理员登录:', username.value);
        
        // 管理员登录请求
        await authStore.adminLogin({
          username: username.value,
          password: password.value
        });
        
        console.log('管理员登录成功，准备跳转');
        
        // 登录成功后的跳转已在store中处理
      } catch (err) {
        console.error('管理员登录失败:', err);
        error.value = err.message || '管理员登录失败，请检查账号和密码';
      } finally {
        loading.value = false;
      }
    };
    
    return {
      username,
      password,
      loading,
      error,
      handleLogin
    };
  }
};
</script>

<style scoped>
.admin-login-page {
  min-height: calc(100vh - 200px);
  padding: 40px 0;
  background-color: #f5f5f5;
}

.login-container {
  max-width: 450px;
  margin: 0 auto;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  padding: 40px;
}

.login-header {
  text-align: center;
  margin-bottom: 30px;
}

.login-header h2 {
  font-size: 28px;
  margin: 0 0 10px;
  color: #333;
}

.login-header p {
  color: #666;
  margin: 0;
}

.login-form {
  margin-bottom: 20px;
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

.form-group input {
  width: 100%;
  padding: 12px 15px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 16px;
  transition: border-color 0.3s;
}

.form-group input:focus {
  outline: none;
  border-color: var(--primary-color, #e53935);
}

.error-message {
  color: #e53935;
  margin-bottom: 15px;
  font-size: 14px;
}

.tip-message {
  background-color: #f9f9f9;
  border-radius: 4px;
  padding: 10px;
  margin-bottom: 15px;
  font-size: 14px;
  border-left: 3px solid #e53935;
}

.tip-message p {
  margin: 5px 0;
  color: #666;
}

.login-button {
  width: 100%;
  padding: 12px;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.3s;
  background-color: var(--primary-color, #e53935);
  color: white;
}

.login-button:hover:not(:disabled) {
  background-color: var(--primary-dark, #c62828);
}

.login-button:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.login-footer {
  text-align: center;
  margin-top: 20px;
}

.back-link {
  display: inline-flex;
  align-items: center;
  color: #666;
  text-decoration: none;
  font-weight: 500;
  transition: color 0.3s;
}

.back-link:hover {
  color: var(--primary-color, #e53935);
}

.icon-back {
  display: inline-block;
  width: 16px;
  height: 16px;
  margin-right: 5px;
  background-image: url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="%23666"><path d="M20 11H7.83l5.59-5.59L12 4l-8 8 8 8 1.41-1.41L7.83 13H20v-2z"/></svg>');
  background-repeat: no-repeat;
  background-position: center;
}
</style> 