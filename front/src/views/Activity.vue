<template>
  <div class="activity-page">
    <div class="banner-section">
      <div class="banner-content">
        <h1>优惠活动</h1>
        <p>精彩活动不断，多重优惠等你来</p>
      </div>
    </div>

    <main class="main-container">
      <div class="section-title">
        <h2>限时优惠</h2>
        <p>多种优惠套餐，满足您的味蕾需求</p>
      </div>

      <div class="activity-list">
        <!-- 活动卡片1 -->
        <div class="activity-card">
          <div class="activity-image">
            <img src="@/assets/images/activity1.jpg" alt="新客满减">
            <div class="activity-tag">新客专享</div>
          </div>
          <div class="activity-info">
            <h3>新客满减</h3>
            <p class="activity-desc">新用户首单满68元立减30元，满128元立减50元</p>
            <p class="activity-time">活动时间: 2023.6.1 - 2023.12.31</p>
            <div class="activity-footer">
              <router-link to="/menu" class="activity-btn">立即点餐</router-link>
            </div>
          </div>
        </div>

        <!-- 活动卡片2 -->
        <div class="activity-card">
          <div class="activity-image">
            <img src="@/assets/images/activity2.jpg" alt="双人套餐">
            <div class="activity-tag">热门推荐</div>
          </div>
          <div class="activity-info">
            <h3>双人套餐特惠</h3>
            <p class="activity-desc">双人套餐原价188元，现在只需158元</p>
            <p class="activity-time">活动时间: 2023.6.1 - 2023.12.31</p>
            <div class="activity-footer">
              <router-link to="/menu" class="activity-btn">立即点餐</router-link>
            </div>
          </div>
        </div>

        <!-- 活动卡片3 -->
        <div class="activity-card">
          <div class="activity-image">
            <img src="@/assets/images/activity3.jpg" alt="会员日特惠">
            <div class="activity-tag">会员专享</div>
          </div>
          <div class="activity-info">
            <h3>会员日特惠</h3>
            <p class="activity-desc">每周二、周四会员日，全场菜品8.5折</p>
            <p class="activity-time">活动时间: 2023.6.1 - 2023.12.31</p>
            <div class="activity-footer">
              <router-link to="/menu" class="activity-btn">立即点餐</router-link>
            </div>
          </div>
        </div>

        <!-- 活动卡片4 -->
        <div class="activity-card">
          <div class="activity-image">
            <img src="@/assets/images/activity4.jpg" alt="满减优惠">
            <div class="activity-tag">限时优惠</div>
          </div>
          <div class="activity-info">
            <h3>满减优惠</h3>
            <p class="activity-desc">单笔订单满100元减15元，满200元减30元，满300元减50元</p>
            <p class="activity-time">活动时间: 2023.6.1 - 2023.12.31</p>
            <div class="activity-footer">
              <router-link to="/menu" class="activity-btn">立即点餐</router-link>
            </div>
          </div>
        </div>
      </div>

      <div class="activity-rules">
        <h3>活动规则</h3>
        <ul>
          <li>所有优惠活动不可同时使用</li>
          <li>新客满减仅限新用户首单使用</li>
          <li>活动最终解释权归美味轩所有</li>
          <li>如有疑问，请联系客服电话：400-888-9999</li>
        </ul>
      </div>
    </main>
  </div>
</template>

<style scoped>
.activity-page {
  padding-bottom: 60px;
}

.banner-section {
  background: linear-gradient(rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.5)), url('../assets/images/activity-bg.jpg');
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
  margin-bottom: 40px;
}

.section-title h2 {
  font-size: 32px;
  color: #333;
  margin-bottom: 15px;
}

.section-title p {
  color: #777;
  max-width: 700px;
  margin: 0 auto;
}

.activity-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 30px;
  margin-bottom: 50px;
}

.activity-card {
  background-color: white;
  border-radius: 10px;
  overflow: hidden;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.activity-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.15);
}

.activity-image {
  height: 250px;
  position: relative;
  overflow: hidden;
}

.activity-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.activity-card:hover .activity-image img {
  transform: scale(1.1);
}

.activity-tag {
  position: absolute;
  top: 10px;
  right: 10px;
  background-color: #e74c3c;
  color: white;
  padding: 5px 10px;
  border-radius: 4px;
  font-size: 14px;
  font-weight: bold;
}

.activity-info {
  padding: 10px;
}

.activity-info h3 {
  font-size: 20px;
  margin-bottom: 10px;
  color: #333;
}

.activity-desc {
  color: #666;
  margin-bottom: 15px;
  line-height: 1.6;
}

.activity-time {
  color: #999;
  font-size: 14px;
  margin-bottom: 20px;
}

.activity-footer {
  display: flex;
  justify-content: flex-end;
}

.activity-btn {
  background-color: #e74c3c;
  color: white;
  border: none;
  padding: 8px 15px;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  text-decoration: none;
  font-size: 14px;
}

.activity-btn:hover {
  background-color: #c0392b;
}

.activity-rules {
  background-color: white;
  border-radius: 10px;
  padding: 30px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
}

.activity-rules h3 {
  font-size: 20px;
  margin-bottom: 15px;
  color: #333;
}

.activity-rules ul {
  padding-left: 20px;
}

.activity-rules li {
  margin-bottom: 10px;
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
  
  .activity-list {
    grid-template-columns: 1fr;
  }
}
</style> 