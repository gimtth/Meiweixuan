<template>
  <footer class="footer">
    <div class="footer-container">
      <div class="footer-column">
        <h3>美味轩</h3>
        <p>传承中华美食文化，为您提供最正宗的中餐体验。</p>
        <div class="social-links">
          <a href="#"><img :src="wechatIcon" alt="微信" class="social-icon"></a>
          <a href="#"><img :src="weiboIcon" alt="微博" class="social-icon"></a>
          <a href="#"><img :src="qqIcon" alt="QQ" class="social-icon"></a>
          <a href="#"><img :src="douyinIcon" alt="抖音" class="social-icon"></a>
        </div>
      </div>
      
      <div class="footer-column">
        <h3>快速链接</h3>
        <ul class="footer-links">
          <li><router-link to="/">首页</router-link></li>
          <li><router-link to="/menu">菜单</router-link></li>
          <li><router-link to="/activity">优惠活动</router-link></li>
          <li><router-link to="/about">关于我们</router-link></li>
          <li><router-link to="/contact">联系我们</router-link></li>
        </ul>
      </div>
      
      <div class="footer-column">
        <h3>联系我们</h3>
        <ul class="footer-links">
          <li><i class="fas fa-map-marker-alt"></i> 北京市朝阳区美食街88号</li>
          <li><i class="fas fa-phone"></i> 400-888-9999</li>
          <li><i class="fas fa-envelope"></i> info@meiweixuan.com</li>
          <li><i class="fas fa-clock"></i> 10:00-22:00</li>
        </ul>
      </div>
      
      <div class="footer-column">
        <h3>微信公众号</h3>
        <img src="../assets/images/qrcode/qrcode.png" alt="微信公众号二维码" style="width: 150px;">
      </div>
    </div>
    
    <div class="copyright">
      &copy; 2023 美味轩餐饮管理有限公司 版权所有 | ICP备案号: 京ICP备12345678号
    </div>
  </footer>
</template>

<script>
import wechatIcon from '../assets/images/icon-wechat.png';
import qqIcon from '../assets/images/icon-qq.png';
import weiboIcon from '../assets/images/icon-weibo.png';
import douyinIcon from '../assets/images/icon-douyin.png';

export default {
  name: 'TheFooter',
  setup() {
    return {
      wechatIcon,
      qqIcon,
      weiboIcon,
      douyinIcon
    };
  }
}
</script>

<style scoped>
.footer {
  background-color: #222;
  color: #ddd;
  padding: 60px 0 20px;
}

.footer-container {
  width: 90%;
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 40px;
}

.footer-column h3 {
  color: white;
  margin-bottom: 20px;
  font-size: 18px;
}

.footer-links {
  list-style: none;
  padding: 0;
}

.footer-links li {
  margin-bottom: 10px;
}

.footer-links a {
  color: #aaa;
  text-decoration: none;
  transition: all 0.3s ease;
}

.footer-links a:hover {
  color: white;
  padding-left: 5px;
}

.footer-links i {
  margin-right: 10px;
  width: 16px;
  text-align: center;
}

.social-links {
  display: flex;
  margin-top: 20px;
}

.social-links a {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  background-color: #333;
  border-radius: 50%;
  margin-right: 10px;
  color: white;
  transition: all 0.3s ease;
}

.social-links a:hover {
  background-color: #e74c3c;
  transform: translateY(-3px);
}

.social-icon {
  width: 40px;
  height: 40px;
  object-fit: contain;
}

.copyright {
  text-align: center;
  margin-top: 40px;
  padding-top: 20px;
  border-top: 1px solid #444;
  color: #777;
  font-size: 14px;
  width: 90%;
  max-width: 1200px;
  margin-left: auto;
  margin-right: auto;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .footer-container {
    grid-template-columns: repeat(auto-fit, minmax(100%, 1fr));
  }
  
  .footer-column {
    text-align: center;
  }
  
  .social-links {
    justify-content: center;
  }
}
</style> 