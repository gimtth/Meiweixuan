<template>
  <div class="about-page">
    <div class="banner-section">
      <div class="banner-content">
        <h1>关于我们</h1>
        <p>了解美味轩的品牌故事和企业文化</p>
      </div>
    </div>

    <main class="main-container">
      <!-- 品牌故事 -->
      <section class="about-section">
        <div class="section-title">
          <h2>品牌故事</h2>
          <div class="title-underline"></div>
        </div>
        
        <div class="about-content">
          <div class="about-image">
            <img src="@/assets/images/about-story.jpg" alt="美味轩餐厅">
          </div>
          <div class="about-text">
            <p>美味轩创立于2010年，是一家专注于提供正宗中式美食的餐饮企业。十余年来，我们坚持"以客为尊，以质为先"的经营理念，致力于为顾客提供最正宗的中华美食体验。</p>
            <p>从最初的一家小餐馆发展到如今的连锁品牌，美味轩始终坚守着对食材的严格把控和对烹饪工艺的精益求精。我们的主厨团队均有着二十年以上的烹饪经验，传承中华饮食文化精髓的同时，融入现代创新理念，打造出一道道经典与创新并存的美味佳肴。</p>
            <p>如今，美味轩已在全国拥有超过50家直营店和100家加盟店，服务顾客超过1000万人次，成为深受消费者喜爱的中式餐饮品牌。</p>
          </div>
        </div>
      </section>

      <!-- 企业文化 -->
      <section class="about-section">
        <div class="section-title">
          <h2>企业文化</h2>
          <div class="title-underline"></div>
        </div>
        
        <div class="culture-values">
          <div class="culture-item">
            <div class="culture-icon">
              <i class="fas fa-heart"></i>
            </div>
            <h3>企业使命</h3>
            <p>传承中华美食文化，为顾客创造美好味蕾体验</p>
          </div>
          
          <div class="culture-item">
            <div class="culture-icon">
              <i class="fas fa-eye"></i>
            </div>
            <h3>企业愿景</h3>
            <p>成为中国最受尊敬的餐饮品牌</p>
          </div>
          
          <div class="culture-item">
            <div class="culture-icon">
              <i class="fas fa-gem"></i>
            </div>
            <h3>核心价值观</h3>
            <p>诚信、创新、专业、共赢</p>
          </div>
        </div>
      </section>

      <!-- 荣誉资质 -->
      <section class="about-section">
        <div class="section-title">
          <h2>荣誉资质</h2>
          <div class="title-underline"></div>
        </div>
        
        <div class="honors-list">
          <div class="honor-item">
            <div class="honor-year">2022</div>
            <div class="honor-content">
              <h4>中国餐饮百强企业</h4>
              <p>由中国饭店协会颁发</p>
            </div>
          </div>
          
          <div class="honor-item">
            <div class="honor-year">2021</div>
            <div class="honor-content">
              <h4>年度最佳餐饮品牌</h4>
              <p>由全国餐饮行业评选委员会颁发</p>
            </div>
          </div>
          
          <div class="honor-item">
            <div class="honor-year">2020</div>
            <div class="honor-content">
              <h4>消费者满意度五星级企业</h4>
              <p>由中国消费者协会评选</p>
            </div>
          </div>
          
          <div class="honor-item">
            <div class="honor-year">2018</div>
            <div class="honor-content">
              <h4>中华餐饮名店</h4>
              <p>由中国烹饪协会颁发</p>
            </div>
          </div>
          
          <div class="honor-item">
            <div class="honor-year">2015</div>
            <div class="honor-content">
              <h4>食品安全示范单位</h4>
              <p>由国家市场监督管理总局评定</p>
            </div>
          </div>
        </div>
      </section>

      <!-- 发展历程 -->
      <section class="about-section">
        <div class="section-title">
          <h2>发展历程</h2>
          <div class="title-underline"></div>
        </div>
        
        <div class="timeline">
          <div class="timeline-item">
            <div class="timeline-year">2010</div>
            <div class="timeline-content">
              <h4>品牌创立</h4>
              <p>美味轩品牌在北京成立，开设第一家门店</p>
            </div>
          </div>
          
          <div class="timeline-item">
            <div class="timeline-year">2013</div>
            <div class="timeline-content">
              <h4>拓展阶段</h4>
              <p>在北京、上海、广州等一线城市开设10家直营店</p>
            </div>
          </div>
          
          <div class="timeline-item">
            <div class="timeline-year">2015</div>
            <div class="timeline-content">
              <h4>加盟启动</h4>
              <p>启动全国加盟计划，首批加盟店登陆20个城市</p>
            </div>
          </div>
          
          <div class="timeline-item">
            <div class="timeline-year">2018</div>
            <div class="timeline-content">
              <h4>数字化转型</h4>
              <p>推出线上点餐系统，实现线上线下一体化服务</p>
            </div>
          </div>
          
          <div class="timeline-item">
            <div class="timeline-year">2020</div>
            <div class="timeline-content">
              <h4>快速发展</h4>
              <p>全国门店突破100家，服务顾客超过500万人次</p>
            </div>
          </div>
          
          <div class="timeline-item">
            <div class="timeline-year">2023</div>
            <div class="timeline-content">
              <h4>现在</h4>
              <p>全国门店超过150家，成为中国知名餐饮品牌</p>
            </div>
          </div>
        </div>
      </section>
    </main>
  </div>
</template>

<script>
export default {
  name: 'AboutPage',
  setup() {
    return {
      // bannerImg
    };
  }
};
</script>

<style scoped>
.about-page {
  padding-bottom: 60px;
}

.banner-section {
  background: linear-gradient(rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.5)), url('../assets/images/about-bg.jpg');
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

.about-section {
  margin-bottom: 50px;
  padding-bottom: 30px;
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
  padding: 30px;
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

.about-content {
  display: flex;
  flex-wrap: wrap;
  gap: 30px;
  align-items: center;
}

.about-image {
  flex: 1;
  min-width: 300px;
}

.about-image img {
  width: 100%;
  border-radius: 8px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.about-text {
  flex: 2;
  min-width: 300px;
}

.about-text p {
  margin-bottom: 15px;
  line-height: 1.8;
  color: #555;
}

.culture-values {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 30px;
  margin-top: 30px;
}

.culture-item {
  flex: 1;
  min-width: 250px;
  text-align: center;
  padding: 30px 20px;
  border-radius: 8px;
  background-color: #f9f9f9;
  transition: all 0.3s ease;
}

.culture-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
}

.culture-icon {
  font-size: 40px;
  color: #e74c3c;
  margin-bottom: 15px;
}

.culture-item h3 {
  font-size: 20px;
  margin-bottom: 10px;
  color: #333;
}

.culture-item p {
  color: #666;
  line-height: 1.6;
}

.honors-list {
  margin-top: 30px;
}

.honor-item {
  display: flex;
  margin-bottom: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.honor-item:hover {
  transform: translateX(5px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.honor-year {
  width: 100px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #e74c3c;
  color: white;
  font-size: 20px;
  font-weight: bold;
}

.honor-content {
  padding: 15px 20px;
  flex: 1;
}

.honor-content h4 {
  font-size: 18px;
  margin-bottom: 5px;
  color: #333;
}

.honor-content p {
  color: #777;
  font-size: 14px;
}

.timeline {
  position: relative;
  margin-top: 40px;
  padding-left: 40px;
}

.timeline::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 4px;
  height: 100%;
  background-color: #e74c3c;
  border-radius: 2px;
}

.timeline-item {
  position: relative;
  margin-bottom: 30px;
  padding-bottom: 20px;
}

.timeline-item::before {
  content: '';
  position: absolute;
  top: 10px;
  left: -44px;
  width: 12px;
  height: 12px;
  border-radius: 50%;
  background-color: white;
  border: 3px solid #e74c3c;
}

.timeline-year {
  font-size: 18px;
  font-weight: bold;
  color: #e74c3c;
  margin-bottom: 10px;
}

.timeline-content h4 {
  font-size: 18px;
  margin-bottom: 5px;
  color: #333;
}

.timeline-content p {
  color: #666;
  line-height: 1.6;
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
  
  .about-content {
    flex-direction: column;
  }
  
  .culture-values {
    flex-direction: column;
  }
  
  .honor-year {
    width: 80px;
    font-size: 16px;
  }
  
  .timeline {
    padding-left: 30px;
  }
  
  .timeline-item::before {
    left: -34px;
  }
}
</style> 