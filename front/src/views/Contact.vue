<template>
  <div class="contact-page">
    <div class="banner-section">
      <div class="banner-content">
        <h1>联系我们</h1>
        <p>欢迎随时与我们联系，我们期待听到您的声音</p>
      </div>
    </div>

    <main class="main-container">
      <div class="contact-container">
        <!-- 联系信息 -->
        <div class="contact-info">
          <div class="section-title">
            <h2>联系方式</h2>
            <div class="title-underline"></div>
          </div>
          
          <div class="info-list">
            <div class="info-item">
              <div class="info-icon">
                <i class="fas fa-map-marker-alt"></i>
              </div>
              <div class="info-content">
                <h3>总部地址</h3>
                <p>北京市朝阳区美食街88号</p>
              </div>
            </div>
            
            <div class="info-item">
              <div class="info-icon">
                <i class="fas fa-phone"></i>
              </div>
              <div class="info-content">
                <h3>咨询电话</h3>
                <p>400-888-9999（7*24小时）</p>
              </div>
            </div>
            
            <div class="info-item">
              <div class="info-icon">
                <i class="fas fa-envelope"></i>
              </div>
              <div class="info-content">
                <h3>电子邮箱</h3>
                <p>info@meiweixuan.com</p>
              </div>
            </div>
            
            <div class="info-item">
              <div class="info-icon">
                <i class="fas fa-clock"></i>
              </div>
              <div class="info-content">
                <h3>营业时间</h3>
                <p>周一至周日 10:00-22:00</p>
              </div>
            </div>
          </div>
          
          <div class="social-media">
            <h3>社交媒体</h3>
            <div class="social-links">
              <a href="#" class="social-link">
                <i class="fab fa-weixin"></i>
              </a>
              <a href="#" class="social-link">
                <i class="fab fa-weibo"></i>
              </a>
              <a href="#" class="social-link">
                <i class="fab fa-qq"></i>
              </a>
              <a href="#" class="social-link">
                <i class="fab fa-tiktok"></i>
              </a>
            </div>
          </div>
        </div>
        
        <!-- 联系表单 -->
        <div class="contact-form-container">
          <div class="section-title">
            <h2>在线留言</h2>
            <div class="title-underline"></div>
          </div>
          
          <form class="contact-form" @submit.prevent="submitForm">
            <div class="form-group">
              <label for="name">姓名</label>
              <input 
                type="text" 
                id="name" 
                v-model="formData.name" 
                placeholder="请输入您的姓名" 
                required
              />
            </div>
            
            <div class="form-group">
              <label for="email">邮箱</label>
              <input 
                type="email" 
                id="email" 
                v-model="formData.email" 
                placeholder="请输入您的邮箱" 
                required
              />
            </div>
            
            <div class="form-group">
              <label for="phone">手机号码</label>
              <input 
                type="tel" 
                id="phone" 
                v-model="formData.phone" 
                placeholder="请输入您的手机号码" 
                required
              />
            </div>
            
            <div class="form-group">
              <label for="subject">主题</label>
              <input 
                type="text" 
                id="subject" 
                v-model="formData.subject" 
                placeholder="请输入留言主题" 
                required
              />
            </div>
            
            <div class="form-group">
              <label for="message">留言内容</label>
              <textarea 
                id="message" 
                v-model="formData.message" 
                placeholder="请输入您的留言内容" 
                rows="5" 
                required
              ></textarea>
            </div>
            
            <button type="submit" class="submit-btn" :disabled="submitting">
              {{ submitting ? '提交中...' : '提交留言' }}
            </button>
            
            <div v-if="submitMessage" class="submit-message" :class="{ success: submitSuccess }">
              {{ submitMessage }}
            </div>
          </form>
        </div>
      </div>
      
      <!-- 门店地图 -->
      <div class="store-map-container">
        <div class="section-title">
          <h2>门店地图</h2>
          <div class="title-underline"></div>
        </div>
        
        <div class="map-container">
          <div class="map-placeholder">
            <div id="amap-container" style="width:100%;height:400px;"></div>
          </div>
        </div>
      </div>
      
      <!-- 常见问题 -->
      <div class="faq-container">
        <div class="section-title">
          <h2>常见问题</h2>
          <div class="title-underline"></div>
        </div>
        
        <div class="faq-list">
          <div class="faq-item" v-for="(faq, index) in faqs" :key="index">
            <div class="faq-question" @click="toggleFaq(index)">
              <h3>{{ faq.question }}</h3>
              <i class="fas" :class="faq.open ? 'fa-chevron-up' : 'fa-chevron-down'"></i>
            </div>
            <div class="faq-answer" :class="{ active: faq.open }">
              <p>{{ faq.answer }}</p>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import AMapLoader from '@amap/amap-jsapi-loader';

export default {
  name: 'ContactPage',
  setup() {
    const formData = reactive({
      name: '',
      email: '',
      phone: '',
      subject: '',
      message: ''
    });
    
    const submitting = ref(false);
    const submitMessage = ref('');
    const submitSuccess = ref(false);
    
    const faqs = reactive([
      {
        question: '美味轩的配送范围是多少？',
        answer: '我们的配送范围为门店5公里范围内，部分特殊区域可能会有所调整，具体可咨询客服。',
        open: false
      },
      {
        question: '如何成为美味轩的会员？',
        answer: '您可以通过我们的官方网站或APP注册成为会员，享受会员专属优惠。',
        open: false
      },
      {
        question: '如何申请加盟美味轩？',
        answer: '请通过本页面的联系表单提交加盟申请，或致电我们的加盟热线400-888-9999，我们的加盟顾问会与您联系。',
        open: false
      },
      {
        question: '美味轩是否提供团体订餐服务？',
        answer: '是的，我们提供企业团餐、会议餐、庆典餐等团体订餐服务，可根据需求定制菜单，请提前24小时预订。',
        open: false
      },
      {
        question: '如何投诉或提出建议？',
        answer: '您可以通过本页面的联系表单提交投诉或建议，也可以拨打我们的客服热线400-888-9999，我们将认真处理您的反馈。',
        open: false
      }
    ]);
    
    // 提交表单
    const submitForm = async () => {
      submitting.value = true;
      submitMessage.value = '';
      
      try {
        // 模拟API请求
        await new Promise(resolve => setTimeout(resolve, 1500));
        
        // 模拟成功响应
        submitSuccess.value = true;
        submitMessage.value = '留言提交成功，我们将尽快与您联系！';
        ElMessage.success('提交成功');
        
        // 清空表单
        Object.keys(formData).forEach(key => {
          formData[key] = '';
        });
      } catch (error) {
        submitSuccess.value = false;
        submitMessage.value = '提交失败，请稍后再试或直接致电我们。';
        ElMessage.error('提交失败');
      } finally {
        submitting.value = false;
      }
    };
    
    // 切换FAQ展开/收起状态
    const toggleFaq = (index) => {
      faqs[index].open = !faqs[index].open;
    };
    
    const bannerImg = `${window.location.origin}/images/dishes/banner3.jpg`;
    
    // 高德地图加载
    onMounted(() => {
      const mapDiv = document.getElementById('amap-container');
      if (mapDiv && !mapDiv.hasChildNodes()) {
        AMapLoader.load({
          key: '5391ae68974c01c12c6f603a50f65f1b',
          version: '2.0',
          plugins: [],
        }).then((AMap) => {
          new AMap.Map('amap-container', {
            zoom: 15,
            center: [116.486409, 39.921489], // 可根据实际门店经纬度调整
          });
        });
      }
    });
    
    return {
      bannerImg,
      formData,
      submitting,
      submitMessage,
      submitSuccess,
      faqs,
      submitForm,
      toggleFaq
    };
  }
};
</script>

<style scoped>
.contact-page {
  padding-bottom: 60px;
}

.banner-section {
  background: linear-gradient(rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.5)), url('../assets/images/contact-bg.jpg');
  background-size: cover;
  background-position: center;
  height: 300px;
  display: flex;
  align-items: center;
  color: white;
  text-align: center;
}

.banner-content {
  width: 90%;
  max-width: 1200px;
  margin: 0 auto;
}

.banner-content h1 {
  font-size: 48px;
  margin-bottom: 20px;
}

.banner-content p {
  font-size: 20px;
  margin-bottom: 30px;
  max-width: 700px;
  margin-left: auto;
  margin-right: auto;
}

.main-container {
  width: 90%;
  max-width: 1200px;
  margin: 40px auto;
}

.section-title {
  text-align: center;
  margin-bottom: 30px;
  position: relative;
}

.section-title h2 {
  font-size: 28px;
  color: #333;
  margin-bottom: 15px;
  font-weight: bold;
}

.title-underline {
  width: 60px;
  height: 3px;
  background-color: #e74c3c;
  margin: 0 auto;
}

.contact-container {
  display: flex;
  flex-wrap: wrap;
  gap: 30px;
  margin-bottom: 50px;
}

.contact-info,
.contact-form-container {
  flex: 1;
  min-width: 300px;
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
  padding: 30px;
}

.info-list {
  margin-top: 30px;
}

.info-item {
  display: flex;
  margin-bottom: 25px;
  align-items: flex-start;
}

.info-icon {
  width: 50px;
  height: 50px;
  background-color: #e74c3c;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  color: white;
  font-size: 20px;
}

.info-content h3 {
  font-size: 18px;
  margin-bottom: 5px;
  color: #333;
}

.info-content p {
  color: #666;
  line-height: 1.5;
}

.social-media {
  margin-top: 30px;
}

.social-media h3 {
  font-size: 18px;
  margin-bottom: 15px;
  color: #333;
}

.social-links {
  display: flex;
  gap: 15px;
}

.social-link {
  width: 40px;
  height: 40px;
  background-color: #e74c3c;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 20px;
  transition: all 0.3s ease;
  text-decoration: none;
}

.social-link:hover {
  transform: translateY(-3px);
  box-shadow: 0 5px 10px rgba(0, 0, 0, 0.1);
  background-color: #c0392b;
}

.contact-form {
  margin-top: 30px;
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
  border-radius: 5px;
  font-size: 16px;
  transition: border-color 0.3s;
}

.form-group input:focus,
.form-group textarea:focus {
  outline: none;
  border-color: #e74c3c;
}

.submit-btn {
  width: 100%;
  padding: 12px 0;
  background-color: #e74c3c;
  color: white;
  border: none;
  border-radius: 5px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.3s;
}

.submit-btn:hover:not(:disabled) {
  background-color: #c0392b;
}

.submit-btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.submit-message {
  margin-top: 15px;
  padding: 10px;
  border-radius: 5px;
  text-align: center;
  background-color: #f8d7da;
  color: #721c24;
}

.submit-message.success {
  background-color: #d4edda;
  color: #155724;
}

.store-map-container,
.faq-container {
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
  padding: 30px;
  margin-bottom: 50px;
}

.map-container {
  margin-top: 30px;
}

.map-placeholder {
  position: relative;
  height: 400px;
  border-radius: 8px;
  overflow: hidden;
}

.map-placeholder img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.map-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
}

.map-overlay p {
  color: white;
  font-size: 18px;
  text-align: center;
  padding: 20px;
}

.faq-list {
  margin-top: 30px;
}

.faq-item {
  border-bottom: 1px solid #eee;
}

.faq-question {
  padding: 15px 0;
  display: flex;
  justify-content: space-between;
  align-items: center;
  cursor: pointer;
}

.faq-question h3 {
  font-size: 18px;
  color: #333;
  margin: 0;
}

.faq-question i {
  color: #e74c3c;
  transition: transform 0.3s ease;
}

.faq-answer {
  max-height: 0;
  overflow: hidden;
  transition: max-height 0.3s ease;
}

.faq-answer.active {
  max-height: 200px;
  padding-bottom: 15px;
}

.faq-answer p {
  color: #666;
  line-height: 1.6;
  margin: 0;
}

@media (max-width: 768px) {
  .banner-section {
    height: 250px;
  }
  
  .banner-content h1 {
    font-size: 36px;
  }
  
  .banner-content p {
    font-size: 16px;
  }
  
  .contact-container {
    flex-direction: column;
  }
  
  .info-icon {
    width: 40px;
    height: 40px;
    font-size: 16px;
  }
  
  .map-placeholder {
    height: 300px;
  }
}
</style>