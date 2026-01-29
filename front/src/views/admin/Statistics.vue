<template>
  <div class="statistics-page">
    <div v-if="loading" class="loading-container">
      <el-skeleton :rows="10" animated />
    </div>
    
    <div v-else class="statistics-content">
      <!-- 数据卡片 -->
      <div class="stat-cards">
        <div class="stat-card">
          <div class="card-icon orders">
            <i class="el-icon-document"></i>
          </div>
          <div class="card-content">
            <h3>订单数量</h3>
            <div class="card-value">{{ stats.totalOrders }}</div>
            <div class="card-trend" :class="{ 'up': stats.orderTrend > 0, 'down': stats.orderTrend < 0 }">
              <span>{{ stats.orderTrend > 0 ? '+' : '' }}{{ stats.orderTrend }}%</span>
              <i :class="stats.orderTrend >= 0 ? 'el-icon-top' : 'el-icon-bottom'"></i>
            </div>
          </div>
        </div>
        
        <div class="stat-card">
          <div class="card-icon sales">
            <i class="el-icon-money"></i>
          </div>
          <div class="card-content">
            <h3>总销售额</h3>
            <div class="card-value">¥{{ stats.totalSales.toFixed(2) }}</div>
            <div class="card-trend" :class="{ 'up': stats.saleTrend > 0, 'down': stats.saleTrend < 0 }">
              <span>{{ stats.saleTrend > 0 ? '+' : '' }}{{ stats.saleTrend }}%</span>
              <i :class="stats.saleTrend >= 0 ? 'el-icon-top' : 'el-icon-bottom'"></i>
            </div>
          </div>
        </div>
        
        <div class="stat-card">
          <div class="card-icon users">
            <i class="el-icon-user"></i>
          </div>
          <div class="card-content">
            <h3>用户数量</h3>
            <div class="card-value">{{ stats.totalUsers }}</div>
            <div class="card-trend" :class="{ 'up': stats.userTrend > 0, 'down': stats.userTrend < 0 }">
              <span>{{ stats.userTrend > 0 ? '+' : '' }}{{ stats.userTrend }}%</span>
              <i :class="stats.userTrend >= 0 ? 'el-icon-top' : 'el-icon-bottom'"></i>
            </div>
          </div>
        </div>
        
        <div class="stat-card">
          <div class="card-icon dishes">
            <i class="el-icon-dish"></i>
          </div>
          <div class="card-content">
            <h3>菜品数量</h3>
            <div class="card-value">{{ stats.totalDishes }}</div>
            <div class="card-trend">
              <span>共 {{ stats.categoryCount }} 个分类</span>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 订单统计图表 -->
      <div class="chart-container">
        <div class="chart-header">
          <h3>近7天订单量</h3>
          <div class="chart-legend">
            <div class="legend-item">
              <div class="legend-color" style="background-color: #e53935;"></div>
              <span>订单数</span>
            </div>
            <div class="legend-item">
              <div class="legend-color" style="background-color: #4caf50;"></div>
              <span>收入(¥100)</span>
            </div>
          </div>
        </div>
        <div class="chart-body" ref="ordersChartRef">
          <!-- 图表将在此处渲染 -->
        </div>
      </div>
      
      <!-- 热门菜品和分类统计 -->
      <div class="stats-row">
        <div class="top-dishes">
          <h3>热门菜品</h3>
          <div class="top-list">
            <div v-for="(dish, index) in stats.topDishes" :key="dish.id" class="top-item">
              <div class="rank">{{ index + 1 }}</div>
              <div class="info">
                <div class="name">{{ dish.name }}</div>
                <div class="meta">{{ dish.count }}次点餐 / ¥{{ dish.amount.toFixed(2) }}</div>
              </div>
              <div class="value">{{ dish.percent }}%</div>
            </div>
          </div>
        </div>
        
        <div class="category-stats">
          <h3>分类销售占比</h3>
          <div class="pie-chart" ref="categoryChartRef">
            <!-- 饼图将在此处渲染 -->
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, onUnmounted } from 'vue';
import * as echarts from 'echarts/core';
import { BarChart, PieChart } from 'echarts/charts';
import { 
  TitleComponent, 
  TooltipComponent, 
  GridComponent, 
  LegendComponent 
} from 'echarts/components';
import { CanvasRenderer } from 'echarts/renderers';
import { ElMessage } from 'element-plus';
import axios from 'axios';
import adminApi from '../../api/adminApi';

// 注册必要的组件
echarts.use([
  TitleComponent, 
  TooltipComponent, 
  GridComponent, 
  LegendComponent,
  BarChart,
  PieChart,
  CanvasRenderer
]);

export default {
  name: 'StatisticsPage',
  setup() {
    const loading = ref(true);
    const ordersChartRef = ref(null);
    const categoryChartRef = ref(null);
    let ordersChart = null;
    let categoryChart = null;
    
    // 模拟统计数据
    const stats = ref({
      totalOrders: 0,
      totalSales: 0,
      totalUsers: 0,
      totalDishes: 0,
      categoryCount: 0,
      orderTrend: 0,
      saleTrend: 0,
      userTrend: 0,
      topDishes: [],
      categoryStats: [],
      recentOrders: []
    });
    
    // 获取统计数据
    const fetchStatistics = async () => {
      try {
        loading.value = true;
        // 调用后端接口获取统计数据
        const res = await adminApi.getStatisticsOverview();
        // 兼容Result包装
        const data = res.data || res;
        stats.value = {
          totalOrders: data.totalOrders || 0,
          totalSales: Number(data.totalSales || 0),
          totalUsers: data.totalUsers || 0,
          totalDishes: data.totalDishes || 0,
          categoryCount: data.categoryCount || 0,
          orderTrend: data.orderTrend || 0,
          saleTrend: data.saleTrend || 0,
          userTrend: data.userTrend || 0,
          topDishes: data.topDishes || [],
          categoryStats: data.categoryStats || [],
          recentOrders: data.recentOrders || []
        };
      } catch (err) {
        console.error('获取统计数据失败:', err);
        ElMessage.error('获取统计数据失败');
      } finally {
        loading.value = false;
      }
    };
    
    // 初始化订单图表
    const initOrdersChart = () => {
      if (!ordersChartRef.value) return;
      
      ordersChart = echarts.init(ordersChartRef.value);
      
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: stats.value.recentOrders.map(item => item.date.slice(5)),
          axisTick: {
            alignWithLabel: true
          }
        },
        yAxis: [
          {
            type: 'value',
            name: '订单数',
            position: 'left',
            axisLine: {
              show: true,
              lineStyle: {
                color: '#e53935'
              }
            },
            axisLabel: {
              formatter: '{value}'
            }
          },
          {
            type: 'value',
            name: '收入',
            position: 'right',
            axisLine: {
              show: true,
              lineStyle: {
                color: '#4caf50'
              }
            },
            axisLabel: {
              formatter: '{value}'
            }
          }
        ],
        series: [
          {
            name: '订单数',
            type: 'bar',
            data: stats.value.recentOrders.map(item => item.count),
            itemStyle: {
              color: '#e53935'
            }
          },
          {
            name: '收入',
            type: 'bar',
            yAxisIndex: 1,
            data: stats.value.recentOrders.map(item => item.amount / 100),
            itemStyle: {
              color: '#4caf50'
            }
          }
        ]
      };
      
      ordersChart.setOption(option);
    };
    
    // 初始化分类饼图
    const initCategoryChart = () => {
      if (!categoryChartRef.value) return;
      
      categoryChart = echarts.init(categoryChartRef.value);
      
      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          right: 10,
          top: 'center',
          data: stats.value.categoryStats.map(item => item.name)
        },
        series: [
          {
            name: '分类销售',
            type: 'pie',
            radius: ['50%', '70%'],
            avoidLabelOverlap: false,
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '18',
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: stats.value.categoryStats.map(item => ({
              value: item.value,
              name: item.name
            }))
          }
        ]
      };
      
      categoryChart.setOption(option);
    };
    
    // 处理窗口大小变化
    const handleResize = () => {
      ordersChart && ordersChart.resize();
      categoryChart && categoryChart.resize();
    };
    
    onMounted(async () => {
      await fetchStatistics();
      // 等待DOM更新
      setTimeout(() => {
        initOrdersChart();
        initCategoryChart();
        
        window.addEventListener('resize', handleResize);
      }, 0);
    });
    
    onUnmounted(() => {
      window.removeEventListener('resize', handleResize);
      ordersChart && ordersChart.dispose();
      categoryChart && categoryChart.dispose();
    });
    
    return {
      loading,
      stats,
      ordersChartRef,
      categoryChartRef
    };
  }
};
</script>

<style scoped>
.statistics-page {
  width: 100%;
}

.loading-container {
  padding: 20px;
}

.stat-cards {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 30px;
}

.stat-card {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  padding: 20px;
  display: flex;
  align-items: center;
}

.card-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20px;
  font-size: 28px;
  color: white;
}

.card-icon.orders {
  background-color: #e53935;
}

.card-icon.sales {
  background-color: #4caf50;
}

.card-icon.users {
  background-color: #2196f3;
}

.card-icon.dishes {
  background-color: #ff9800;
}

.card-content {
  flex: 1;
}

.card-content h3 {
  margin: 0 0 8px;
  font-size: 14px;
  color: #666;
  font-weight: normal;
}

.card-value {
  font-size: 24px;
  font-weight: 600;
  margin-bottom: 5px;
  color: #333;
}

.card-trend {
  font-size: 12px;
  color: #999;
  display: flex;
  align-items: center;
}

.card-trend.up {
  color: #4caf50;
}

.card-trend.down {
  color: #e53935;
}

.card-trend i {
  margin-left: 5px;
}

.chart-container {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  padding: 20px;
  margin-bottom: 30px;
}

.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.chart-header h3 {
  margin: 0;
  font-size: 16px;
  color: #333;
}

.chart-legend {
  display: flex;
  gap: 15px;
}

.legend-item {
  display: flex;
  align-items: center;
}

.legend-color {
  width: 12px;
  height: 12px;
  border-radius: 2px;
  margin-right: 6px;
}

.chart-body {
  height: 300px;
}

.stats-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
  margin-bottom: 30px;
}

.top-dishes, .category-stats {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  padding: 20px;
}

.top-dishes h3, .category-stats h3 {
  margin: 0 0 20px;
  font-size: 16px;
  color: #333;
}

.top-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.top-item {
  display: flex;
  align-items: center;
}

.rank {
  width: 24px;
  height: 24px;
  background-color: #f5f5f5;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  font-size: 14px;
  margin-right: 10px;
}

.top-item:nth-child(1) .rank {
  background-color: #f9a825;
  color: white;
}

.top-item:nth-child(2) .rank {
  background-color: #bdbdbd;
  color: white;
}

.top-item:nth-child(3) .rank {
  background-color: #bf8970;
  color: white;
}

.info {
  flex: 1;
}

.name {
  font-weight: 500;
  color: #333;
  margin-bottom: 3px;
}

.meta {
  font-size: 12px;
  color: #999;
}

.value {
  font-weight: 600;
  color: #e53935;
}

.pie-chart {
  height: 300px;
}

@media (max-width: 1200px) {
  .stat-cards {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .stats-row {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 576px) {
  .stat-cards {
    grid-template-columns: 1fr;
  }
}
</style> 