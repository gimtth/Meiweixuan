import http from './http';

const bannerApi = {
  /**
   * 获取首页轮播图数据
   * @returns {Promise<Array>} 轮播图列表
   */
  getBanners() {
    return http.get('/banner');
  }
};

export default bannerApi; 