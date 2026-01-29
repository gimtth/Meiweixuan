<template>
  <div class="login-page">
    <div class="container">
      <div class="login-container">
        <div class="login-header">
          <h2>用户登录</h2>
          <p>欢迎回来！请登录您的账号</p>
        </div>
        
        <form @submit.prevent="handleLogin" class="login-form">
          <div class="form-group">
            <label for="username">用户名</label>
            <input 
              type="text" 
              id="username" 
              v-model="username" 
              placeholder="请输入用户名" 
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
          <p>还没有账号？<router-link to="/register">立即注册</router-link></p>
        </div>
        
        <div class="login-divider">
          <span>或</span>
        </div>
        
        <button class="admin-login-button" @click="adminLogin">
          管理员登录
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '../store/auth';

export default {
  name: 'LoginPage',
  setup() {
    const username = ref('');
    const password = ref('');
    const loading = ref(false);
    const error = ref('');
    
    const router = useRouter();
    const authStore = useAuthStore();
    
    // 如果用户已登录，重定向到首页
    if (authStore.isLoggedIn) {
      router.push('/');
    }
    
    const handleLogin = async () => {
      try {
        loading.value = true;
        error.value = '';
        
        console.log('尝试用户登录:', username.value);
        
        // 普通用户登录
        await authStore.login({
          username: username.value,
          password: password.value
        });
        
        console.log('用户登录成功，准备跳转');
        
        // 登录成功，跳转到首页
        router.push('/');
      } catch (err) {
        console.error('用户登录失败:', err);
        error.value = err.message || '登录失败，请检查用户名和密码';
      } finally {
        loading.value = false;
      }
    };
    
    const adminLogin = () => {
      router.push('/admin/login');
    };
    
    return {
      username,
      password,
      loading,
      error,
      handleLogin,
      adminLogin
    };
  }
};
</script>

<style scoped>
.login-page {
  min-height: calc(100vh - 200px);
  display: flex;
  align-items: center;
  padding: 40px 0;
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

.login-button,
.admin-login-button {
  width: 100%;
  padding: 12px;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.3s;
}

.login-button {
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
}

.login-footer a {
  color: var(--primary-color, #e53935);
  text-decoration: none;
  font-weight: 500;
}

.login-footer a:hover {
  text-decoration: underline;
}

.login-divider {
  margin: 25px 0;
  text-align: center;
  position: relative;
}

.login-divider:before {
  content: '';
  position: absolute;
  top: 50%;
  left: 0;
  right: 0;
  height: 1px;
  background-color: #eee;
}

.login-divider span {
  position: relative;
  display: inline-block;
  padding: 0 10px;
  background-color: white;
  color: #999;
}

.admin-login-button {
  background-color: #f5f5f5;
  color: #333;
}

.admin-login-button:hover {
  background-color: #e0e0e0;
}
</style> 