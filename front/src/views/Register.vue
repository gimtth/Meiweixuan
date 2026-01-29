<template>
  <div class="register-page">
    <div class="container">
      <div class="register-container">
        <div class="register-header">
          <h2>用户注册</h2>
          <p>注册成为会员，享受更多优惠</p>
        </div>
        
        <form @submit.prevent="handleRegister" class="register-form">
          <div class="form-group">
            <label for="username">用户名</label>
            <input 
              type="text" 
              id="username" 
              v-model="formData.username" 
              placeholder="请输入用户名" 
              required
            />
          </div>
          
          <div class="form-group">
            <label for="password">密码</label>
            <input 
              type="password" 
              id="password" 
              v-model="formData.password" 
              placeholder="请输入密码" 
              required
            />
          </div>
          
          <div class="form-group">
            <label for="confirmPassword">确认密码</label>
            <input 
              type="password" 
              id="confirmPassword" 
              v-model="confirmPassword" 
              placeholder="请再次输入密码" 
              required
            />
          </div>
          
          <div class="form-group">
            <label for="name">真实姓名</label>
            <input 
              type="text" 
              id="name" 
              v-model="formData.name" 
              placeholder="请输入真实姓名" 
              required
            />
          </div>
          
          <div class="form-group">
            <label for="phone">联系电话</label>
            <input 
              type="tel" 
              id="phone" 
              v-model="formData.phone" 
              placeholder="请输入联系电话" 
              required
            />
          </div>
          
          <div class="form-group" v-if="formData.role === 'user'">
            <label for="address">收货地址</label>
            <input 
              type="text" 
              id="address" 
              v-model="formData.address" 
              placeholder="请输入收货地址" 
              required
            />
          </div>
          
          <div class="form-group">
            <label>用户类型</label>
            <div class="role-selection">
              <div 
                class="role-option" 
                :class="{ active: formData.role === 'user' }" 
                @click="formData.role = 'user'"
              >
                <i class="fas fa-user"></i>
                <span>普通用户</span>
              </div>
              <div 
                class="role-option" 
                :class="{ active: formData.role === 'admin' }" 
                @click="formData.role = 'admin'"
              >
                <i class="fas fa-user-shield"></i>
                <span>管理员</span>
              </div>
            </div>
          </div>
          
          <div v-if="error" class="error-message">
            {{ error }}
          </div>
          
          <button type="submit" class="register-button" :disabled="loading">
            {{ loading ? '注册中...' : '注册' }}
          </button>
        </form>
        
        <div class="register-footer">
          <p>已有账号？<router-link to="/login">立即登录</router-link></p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '../store/auth';
import { ElMessage } from 'element-plus';

export default {
  name: 'RegisterPage',
  setup() {
    const formData = reactive({
      username: '',
      password: '',
      name: '',
      phone: '',
      address: '',
      role: 'user' // 默认为普通用户
    });
    
    const confirmPassword = ref('');
    const loading = ref(false);
    const error = ref('');
    
    const router = useRouter();
    const authStore = useAuthStore();
    
    const handleRegister = async () => {
      // 验证两次密码是否一致
      if (formData.password !== confirmPassword.value) {
        error.value = '两次输入的密码不一致';
        return;
      }
      
      try {
        loading.value = true;
        error.value = '';
        
        // 注册请求中添加角色信息
        await authStore.register({...formData});
        
        ElMessage.success(`${formData.role === 'admin' ? '管理员' : '用户'}注册成功`);
        router.push('/login');
      } catch (err) {
        error.value = err.message || '注册失败，请稍后再试';
      } finally {
        loading.value = false;
      }
    };
    
    return {
      formData,
      confirmPassword,
      loading,
      error,
      handleRegister
    };
  }
};
</script>

<style scoped>
.register-page {
  min-height: calc(100vh - 200px);
  padding: 40px 0;
}

.register-container {
  max-width: 550px;
  margin: 0 auto;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  padding: 40px;
}

.register-header {
  text-align: center;
  margin-bottom: 30px;
}

.register-header h2 {
  font-size: 28px;
  margin: 0 0 10px;
  color: #333;
}

.register-header p {
  color: #666;
  margin: 0;
}

.register-form {
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

.form-group input,
.form-group textarea {
  width: 100%;
  padding: 12px 15px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 16px;
  transition: border-color 0.3s;
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

/* 角色选择样式 */
.role-selection {
  display: flex;
  gap: 15px;
  margin-bottom: 15px;
}

.role-option {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 15px;
  border: 2px solid #ddd;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.role-option i {
  font-size: 30px;
  margin-bottom: 10px;
  color: #666;
}

.role-option span {
  font-weight: 500;
}

.role-option.active {
  border-color: #e74c3c;
  background-color: rgba(231, 76, 60, 0.05);
}

.role-option.active i {
  color: #e74c3c;
}

.role-option:hover {
  transform: translateY(-3px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.error-message {
  color: #e53935;
  margin-bottom: 15px;
  font-size: 14px;
}

.register-button {
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

.register-button:hover:not(:disabled) {
  background-color: var(--primary-dark, #c62828);
}

.register-button:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.register-footer {
  text-align: center;
  margin-top: 20px;
}

.register-footer a {
  color: var(--primary-color, #e53935);
  text-decoration: none;
  font-weight: 500;
}

.register-footer a:hover {
  text-decoration: underline;
}
</style> 