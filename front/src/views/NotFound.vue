<template>
  <div class="not-found-page">
    <div class="container">
      <div class="error-content">
        <h1>404</h1>
        <h2>页面未找到</h2>
        <p>您访问的页面不存在或已被移除</p>
        <router-link to="/" class="back-home-btn">
          返回首页
        </router-link>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'NotFoundPage'
}
</script>

<style scoped>
.not-found-page {
  min-height: calc(100vh - 200px);
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
  padding: 40px 0;
}

.error-content {
  max-width: 500px;
}

.error-content h1 {
  font-size: 120px;
  margin: 0;
  color: var(--primary-color, #e53935);
  line-height: 1;
}

.error-content h2 {
  font-size: 32px;
  margin: 10px 0 20px;
  color: #333;
}

.error-content p {
  font-size: 18px;
  color: #666;
  margin-bottom: 30px;
}

.back-home-btn {
  display: inline-block;
  padding: 12px 30px;
  background-color: var(--primary-color, #e53935);
  color: white;
  text-decoration: none;
  border-radius: 4px;
  font-size: 16px;
  transition: background-color 0.3s;
}

.back-home-btn:hover {
  background-color: var(--primary-dark, #c62828);
}
</style> 