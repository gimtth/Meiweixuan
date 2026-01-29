<template>
  <div class="order-manage-page">
    <div class="page-header">
      <h2>订单管理</h2>
      <div class="header-actions">
        <el-select 
          v-model="statusFilter" 
          placeholder="按状态筛选" 
          clearable
          @change="handleFilter"
          style="margin-right: 10px; width: 150px;"
        >
          <el-option
            v-for="item in statusOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
        
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          format="YYYY-MM-DD"
          value-format="YYYY-MM-DD"
          @change="handleFilter"
          style="width: 260px;"
        />
      </div>
    </div>
    
    <!-- 订单表格 -->
    <el-card class="table-card">
      <el-table
        :data="filteredOrders"
        border
        stripe
        style="width: 100%"
        v-loading="loading"
      >
        <el-table-column
          prop="id"
          label="订单号"
          width="80"
        />
        <el-table-column
          label="用户"
          width="120"
        >
          <template #default="scope">
            {{ scope.row.user ? scope.row.user.username : '未知用户' }}
          </template>
        </el-table-column>
        <el-table-column
          label="金额"
          width="100"
        >
          <template #default="scope">
            ¥{{ scope.row.amount ? scope.row.amount.toFixed(2) : '0.00' }}
          </template>
        </el-table-column>
        <el-table-column
          prop="status"
          label="状态"
          width="120"
        >
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="createTime"
          label="下单时间"
          width="180"
        >
          <template #default="scope">
            {{ formatDate(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column
          prop="address"
          label="配送地址"
          show-overflow-tooltip
        />
        <el-table-column
          label="操作"
          width="200"
        >
          <template #default="scope">
            <el-button
              size="small"
              @click="handleShowDetail(scope.row)"
            >
              详情
            </el-button>
            <el-button
              size="small"
              type="primary"
              @click="handleUpdateStatus(scope.row)"
              :disabled="scope.row.status === 'COMPLETED' || scope.row.status === 'CANCELLED' || 
                        scope.row.status === '已完成' || scope.row.status === '已取消'"
            >
              更新状态
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          background
          layout="prev, pager, next, sizes, total"
          :total="totalOrders"
          :page-size="pageSize"
          :current-page="currentPage"
          :page-sizes="[10, 20, 50, 100]"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
    
    <!-- 订单详情对话框 -->
    <el-dialog
      title="订单详情"
      v-model="detailDialogVisible"
      width="700px"
    >
      <div v-if="selectedOrder" class="order-detail">
        <div class="detail-header">
          <div class="order-info">
            <div class="info-item">
              <span class="label">订单号:</span>
              <span class="value">{{ selectedOrder.id }}</span>
            </div>
            <div class="info-item">
              <span class="label">下单时间:</span>
              <span class="value">{{ formatDate(selectedOrder.createTime) }}</span>
            </div>
            <div class="info-item">
              <span class="label">状态:</span>
              <span class="value">
                <el-tag :type="getStatusType(selectedOrder.status)">
                  {{ getStatusText(selectedOrder.status) }}
                </el-tag>
              </span>
            </div>
          </div>
          
          <div class="customer-info">
            <div class="info-item">
              <span class="label">用户:</span>
              <span class="value">{{ selectedOrder.user ? selectedOrder.user.username : '未知用户' }}</span>
            </div>
            <div class="info-item">
              <span class="label">联系电话:</span>
              <span class="value">{{ selectedOrder.phone || '无' }}</span>
            </div>
            <div class="info-item">
              <span class="label">配送地址:</span>
              <span class="value">{{ selectedOrder.address || '无' }}</span>
            </div>
          </div>
        </div>
        
        <div class="divider"></div>
        
        <!-- 订单商品列表 -->
        <div class="order-items">
          <h4>订单商品</h4>
          <el-table
            :data="selectedOrder.orderItems || []"
            border
            style="width: 100%"
          >
            <el-table-column
              prop="name"
              label="商品名称"
              width="180"
            />
            <el-table-column
              prop="price"
              label="单价"
              width="100"
            >
              <template #default="scope">
                ¥{{ scope.row.price ? scope.row.price.toFixed(2) : '0.00' }}
              </template>
            </el-table-column>
            <el-table-column
              prop="quantity"
              label="数量"
              width="80"
            />
            <el-table-column
              label="小计"
              width="120"
            >
              <template #default="scope">
                ¥{{ (scope.row.price && scope.row.quantity) ? (scope.row.price * scope.row.quantity).toFixed(2) : '0.00' }}
              </template>
            </el-table-column>
          </el-table>
          
          <div class="order-total">
            <span>订单总计: <strong>¥{{ selectedOrder.amount ? selectedOrder.amount.toFixed(2) : '0.00' }}</strong></span>
          </div>
        </div>
        
        <div class="divider"></div>
        
        <!-- 订单备注 -->
        <div class="order-note" v-if="selectedOrder.note">
          <h4>订单备注</h4>
          <p>{{ selectedOrder.note }}</p>
        </div>
      </div>
    </el-dialog>
    
    <!-- 更新状态对话框 -->
    <el-dialog
      title="更新订单状态"
      v-model="statusDialogVisible"
      width="500px"
    >
      <div v-if="selectedOrder" class="status-update">
        <p class="current-status">
          当前状态: 
          <el-tag :type="getStatusType(selectedOrder.status)">
            {{ getStatusText(selectedOrder.status) }}
          </el-tag>
        </p>
        
        <div class="status-select">
          <p>选择新状态:</p>
          <el-radio-group v-model="newStatus">
            <el-radio-button
              v-for="option in getAvailableStatusOptions(selectedOrder.status)"
              :key="option.value"
              :value="option.value"
            >
              {{ option.label }}
            </el-radio-button>
          </el-radio-group>
        </div>
      </div>
      <template #footer>
        <el-button @click="statusDialogVisible = false">取消</el-button>
        <el-button 
          type="primary" 
          @click="submitStatusUpdate" 
          :loading="submitting"
        >
          更新状态
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue';
import adminApi from '../../api/adminApi';
import { ElMessage, ElMessageBox } from 'element-plus';

export default {
  name: 'OrderManagePage',
  setup() {
    const loading = ref(true);
    const submitting = ref(false);
    const orders = ref([]);
    const totalOrders = ref(0);
    const currentPage = ref(1);
    const pageSize = ref(10);
    
    // 筛选相关
    const statusFilter = ref('');
    const dateRange = ref([]);
    
    // 对话框相关
    const detailDialogVisible = ref(false);
    const statusDialogVisible = ref(false);
    const selectedOrder = ref(null);
    const newStatus = ref('');
    
    // 订单状态选项
    const statusOptions = [
      { label: '待付款', value: 'PENDING' },
      { label: '已付款', value: 'PAID' },
      { label: '制作中', value: 'PREPARING' },
      { label: '配送中', value: 'DELIVERING' },
      { label: '已完成', value: 'COMPLETED' },
      { label: '已取消', value: 'CANCELLED' }
    ];
    
    // 获取订单列表
    const fetchOrders = async () => {
      try {
        loading.value = true;
        
        const params = {
          page: currentPage.value - 1,
          size: pageSize.value
        };
        
        if (statusFilter.value) {
          params.status = statusFilter.value;
        }
        
        if (dateRange.value && dateRange.value.length === 2) {
          params.startDate = dateRange.value[0];
          params.endDate = dateRange.value[1];
        }
        
        console.log('正在获取订单列表...');
        const response = await adminApi.orders.getAll(params);
        console.log('获取到的订单数据:', response);
        
        // 处理可能的不同响应格式
        if (Array.isArray(response)) {
          orders.value = response;
          totalOrders.value = response.length;
          console.log(`获取到 ${orders.value.length} 条订单数据`);
        } else if (response && typeof response === 'object') {
          if ('content' in response) {
            orders.value = response.content || [];
            totalOrders.value = response.totalElements || 0;
          } else {
            orders.value = response.data || response;
            totalOrders.value = orders.value.length;
          }
          console.log(`获取到 ${orders.value.length} 条订单数据`);
        } else {
          console.error('获取到的订单数据格式异常:', response);
          orders.value = [];
          totalOrders.value = 0;
        }
        
      } catch (error) {
        ElMessage.error('获取订单列表失败');
        console.error('获取订单列表错误:', error);
      } finally {
        loading.value = false;
      }
    };
    
    // 筛选后的订单列表
    const filteredOrders = computed(() => {
      // 处理订单数据，确保数据格式正确
      return orders.value.map(order => {
        // 确保每个订单对象都有必要的属性
        return {
          ...order,
          // 如果没有user对象，创建一个空对象
          user: order.user || { username: '未知用户' }
        };
      });
    });
    
    // 处理筛选条件变化
    const handleFilter = () => {
      currentPage.value = 1;
      fetchOrders();
    };
    
    // 页码变化处理
    const handleCurrentChange = (page) => {
      currentPage.value = page;
      fetchOrders();
    };
    
    // 每页数量变化处理
    const handleSizeChange = (size) => {
      pageSize.value = size;
      currentPage.value = 1;
      fetchOrders();
    };
    
    // 显示订单详情
    const handleShowDetail = async (order) => {
      try {
        // 获取详细订单信息
        const orderDetail = await adminApi.orders.getById(order.id);
        console.log('获取到的订单详情:', orderDetail);
        
        // 处理订单详情数据
        selectedOrder.value = {
          ...orderDetail,
          // 确保有user对象
          user: orderDetail.user || { username: '未知用户' },
          // 确保有orderItems数组
          orderItems: orderDetail.orderItems || []
        };
        
        detailDialogVisible.value = true;
      } catch (error) {
        ElMessage.error('获取订单详情失败');
        console.error('获取订单详情错误:', error);
      }
    };
    
    // 显示更新状态对话框
    const handleUpdateStatus = (order) => {
      selectedOrder.value = order;
      
      // 根据当前状态设置默认的下一状态
      const nextStatus = getNextStatus(order.status);
      newStatus.value = nextStatus || '';
      
      statusDialogVisible.value = true;
    };
    
    // 根据当前状态获取默认的下一状态
    const getNextStatus = (currentStatus) => {
      const statusFlow = {
        'PENDING': 'PAID',
        'PAID': 'PREPARING',
        'PREPARING': 'DELIVERING',
        'DELIVERING': 'COMPLETED'
      };
      
      return statusFlow[currentStatus] || '';
    };
    
    // 获取可用的状态选项
    const getAvailableStatusOptions = (currentStatus) => {
      // 已完成和已取消的订单不能再更改状态
      if (currentStatus === 'COMPLETED' || currentStatus === 'CANCELLED') {
        return [];
      }
      
      // 待付款的订单只能变为已付款或已取消
      if (currentStatus === 'PENDING') {
        return statusOptions.filter(option => 
          option.value === 'PAID' || option.value === 'CANCELLED'
        );
      }
      
      // 其他状态可以变为后续状态或取消
      const currentIndex = statusOptions.findIndex(option => option.value === currentStatus);
      if (currentIndex === -1) return statusOptions;
      
      return statusOptions.filter((option, index) => 
        index > currentIndex || option.value === 'CANCELLED'
      );
    };
    
    // 提交状态更新
    const submitStatusUpdate = async () => {
      if (!selectedOrder.value || !newStatus.value) return;
      
      try {
        submitting.value = true;
        
        await adminApi.orders.updateStatus(selectedOrder.value.id, { status: newStatus.value });
        
        ElMessage.success('订单状态更新成功');
        statusDialogVisible.value = false;
        
        // 刷新订单列表
        await fetchOrders();
        
      } catch (error) {
        ElMessage.error('更新订单状态失败');
        console.error(error);
      } finally {
        submitting.value = false;
      }
    };
    
    // 获取状态显示文本
    const getStatusText = (status) => {
      console.log('原始状态值:', status);
      
      // 处理数据库中可能存在的中文状态
      if (status === '待支付') return '待付款';
      if (status === '已支付待配送') return '已付款';
      if (status === '配送中') return '配送中';
      if (status === '已完成') return '已完成';
      if (status === '已取消') return '已取消';
      
      // 处理前端定义的英文状态
      const option = statusOptions.find(opt => opt.value === status);
      return option ? option.label : status;
    };
    
    // 获取状态标签类型
    const getStatusType = (status) => {
      // 处理数据库中可能存在的中文状态
      if (status === '待支付') return 'info';
      if (status === '已支付待配送') return 'primary';
      if (status === '配送中') return 'warning';
      if (status === '已完成') return 'success';
      if (status === '已取消') return 'danger';
      
      const typeMap = {
        'PENDING': 'info',
        'PAID': 'primary',
        'PREPARING': 'warning',
        'DELIVERING': 'success',
        'COMPLETED': 'success',
        'CANCELLED': 'danger'
      };
      
      return typeMap[status] || 'info';
    };
    
    // 格式化日期
    const formatDate = (dateString) => {
      if (!dateString) return '';
      
      const date = new Date(dateString);
      return date.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
      });
    };
    
    onMounted(() => {
      fetchOrders();
    });
    
    return {
      loading,
      submitting,
      orders,
      filteredOrders,
      totalOrders,
      currentPage,
      pageSize,
      statusFilter,
      dateRange,
      statusOptions,
      detailDialogVisible,
      statusDialogVisible,
      selectedOrder,
      newStatus,
      
      handleFilter,
      handleCurrentChange,
      handleSizeChange,
      handleShowDetail,
      handleUpdateStatus,
      getNextStatus,
      getAvailableStatusOptions,
      submitStatusUpdate,
      getStatusText,
      getStatusType,
      formatDate
    };
  }
};
</script>

<style scoped>
.order-manage-page {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
  font-size: 24px;
  font-weight: 500;
}

.header-actions {
  display: flex;
  align-items: center;
}

.table-card {
  margin-bottom: 20px;
}

.pagination-container {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

/* 订单详情样式 */
.order-detail {
  padding: 10px;
}

.detail-header {
  display: flex;
  justify-content: space-between;
}

.order-info,
.customer-info {
  flex: 1;
}

.info-item {
  margin-bottom: 10px;
  display: flex;
}

.info-item .label {
  width: 80px;
  color: #606266;
}

.info-item .value {
  color: #333;
}

.divider {
  height: 1px;
  background-color: #ebeef5;
  margin: 20px 0;
}

.order-items {
  margin-bottom: 20px;
}

.order-items h4,
.order-note h4 {
  margin-top: 0;
  margin-bottom: 15px;
  font-size: 16px;
  color: #333;
}

.order-total {
  margin-top: 15px;
  text-align: right;
  font-size: 16px;
}

.order-note p {
  margin: 0;
  white-space: pre-line;
  color: #606266;
}

/* 状态更新对话框 */
.status-update {
  padding: 10px;
}

.current-status {
  margin-bottom: 20px;
}

.status-select p {
  margin-bottom: 10px;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .header-actions {
    margin-top: 10px;
    width: 100%;
    flex-wrap: wrap;
  }
  
  .header-actions .el-select,
  .header-actions .el-date-picker {
    margin-top: 10px;
    margin-right: 0 !important;
    width: 100% !important;
  }
  
  .detail-header {
    flex-direction: column;
  }
  
  .order-info,
  .customer-info {
    margin-bottom: 15px;
  }
}
</style> 