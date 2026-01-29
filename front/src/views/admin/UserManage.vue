<template>
  <div class="user-manage-page">
    <div class="page-header">
      <h2>用户管理</h2>
      <div class="header-actions">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索用户名/姓名/联系方式"
          prefix-icon="el-icon-search"
          clearable
          @input="handleSearchInput"
        />
      </div>
    </div>
    
    <!-- 用户表格 -->
    <el-card class="table-card">
      <el-table
        :data="filteredUsers"
        border
        stripe
        style="width: 100%"
        v-loading="loading"
      >
        <el-table-column
          prop="id"
          label="ID"
          width="80"
        />
        <el-table-column
          prop="username"
          label="用户名"
          width="150"
        />
        <el-table-column
          prop="name"
          label="真实姓名"
          width="150"
        />
        <el-table-column
          prop="phone"
          label="联系电话"
          width="150"
        />
        <el-table-column
          prop="role"
          label="角色"
          width="120"
        >
          <template #default="scope">
            <el-tag
              :type="scope.row.role === 'ROLE_ADMIN' ? 'danger' : 'success'"
            >
              {{ scope.row.role === 'ROLE_ADMIN' ? '管理员' : '普通用户' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
        >
          <template #default="scope">
            <el-button
              size="small"
              @click="handleShowUserDetail(scope.row)"
            >
              详情
            </el-button>
            <el-button
              size="small"
              type="primary"
              @click="handleEditUser(scope.row)"
            >
              编辑
            </el-button>
            <el-button
              size="small"
              type="danger"
              @click="handleDeleteUser(scope.row)"
              :disabled="scope.row.role === 'ROLE_ADMIN'"
            >
              删除
            </el-button>
            <el-button
              size="small"
              :type="scope.row.role === 'ROLE_ADMIN' ? 'info' : 'warning'"
              @click="handleToggleRole(scope.row)"
              :disabled="currentUserId === scope.row.id"
            >
              {{ scope.row.role === 'ROLE_ADMIN' ? '降级为用户' : '提升为管理员' }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    
    <!-- 用户详情对话框 -->
    <el-dialog
      title="用户详情"
      v-model="detailDialogVisible"
      width="500px"
    >
      <div v-if="selectedUser" class="user-detail">
        <div class="detail-item">
          <span class="item-label">ID:</span>
          <span class="item-value">{{ selectedUser.id }}</span>
        </div>
        <div class="detail-item">
          <span class="item-label">用户名:</span>
          <span class="item-value">{{ selectedUser.username }}</span>
        </div>
        <div class="detail-item">
          <span class="item-label">真实姓名:</span>
          <span class="item-value">{{ selectedUser.name || '未设置' }}</span>
        </div>
        <div class="detail-item">
          <span class="item-label">联系电话:</span>
          <span class="item-value">{{ selectedUser.phone || '未设置' }}</span>
        </div>
        <div class="detail-item">
          <span class="item-label">收货地址:</span>
          <span class="item-value">{{ selectedUser.address || '未设置' }}</span>
        </div>
        <div class="detail-item">
          <span class="item-label">角色:</span>
          <span class="item-value">
            <el-tag
              :type="selectedUser.role === 'ROLE_ADMIN' ? 'danger' : 'success'"
            >
              {{ selectedUser.role === 'ROLE_ADMIN' ? '管理员' : '普通用户' }}
            </el-tag>
          </span>
        </div>
        <div class="detail-item">
          <span class="item-label">注册时间:</span>
          <span class="item-value">{{ formatDate(selectedUser.createdAt) }}</span>
        </div>
      </div>
    </el-dialog>
    
    <!-- 编辑用户对话框 -->
    <el-dialog
      title="编辑用户"
      v-model="editDialogVisible"
      width="500px"
    >
      <el-form
        v-if="editingUser"
        :model="editingUser"
        label-width="100px"
        :rules="userRules"
        ref="editFormRef"
      >
        <el-form-item label="用户名" prop="username">
          <el-input v-model="editingUser.username" disabled />
        </el-form-item>
        <el-form-item label="真实姓名" prop="name">
          <el-input v-model="editingUser.name" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="editingUser.phone" />
        </el-form-item>
        <el-form-item label="收货地址" prop="address">
          <el-input v-model="editingUser.address" type="textarea" rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitEditUser" :loading="submitting">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, computed, reactive, onMounted } from 'vue';
import { useAuthStore } from '../../store/auth';
import adminApi from '../../api/adminApi';
import { ElMessage, ElMessageBox } from 'element-plus';

export default {
  name: 'UserManagePage',
  setup() {
    const loading = ref(true);
    const users = ref([]);
    const searchKeyword = ref('');
    const submitting = ref(false);
    
    // 对话框状态
    const detailDialogVisible = ref(false);
    const editDialogVisible = ref(false);
    const selectedUser = ref(null);
    const editingUser = ref(null);
    
    // 当前管理员ID
    const authStore = useAuthStore();
    const currentUserId = computed(() => authStore.user?.id);
    
    // 表单验证规则
    const userRules = {
      name: [
        { required: true, message: '请输入真实姓名', trigger: 'blur' },
        { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
      ],
      phone: [
        { required: true, message: '请输入联系电话', trigger: 'blur' },
        { pattern: /^1[3-9]\d{9}$/, message: '请输入有效的手机号码', trigger: 'blur' }
      ],
      address: [
        { required: true, message: '请输入收货地址', trigger: 'blur' },
        { min: 5, max: 100, message: '长度在 5 到 100 个字符', trigger: 'blur' }
      ]
    };
    
    // 获取用户列表
    const fetchUsers = async () => {
      try {
        loading.value = true;
        users.value = await adminApi.users.getAll();
      } catch (error) {
        ElMessage.error('获取用户列表失败');
        console.error(error);
      } finally {
        loading.value = false;
      }
    };
    
    // 根据关键词过滤用户
    const filteredUsers = computed(() => {
      if (!searchKeyword.value) {
        return users.value;
      }
      
      const keyword = searchKeyword.value.toLowerCase();
      return users.value.filter(user => {
        return (
          (user.username && user.username.toLowerCase().includes(keyword)) ||
          (user.name && user.name.toLowerCase().includes(keyword)) ||
          (user.phone && user.phone.includes(keyword))
        );
      });
    });
    
    // 搜索输入处理
    const handleSearchInput = () => {
      // 可以加入防抖逻辑
    };
    
    // 显示用户详情
    const handleShowUserDetail = (user) => {
      selectedUser.value = user;
      detailDialogVisible.value = true;
    };
    
    // 编辑用户
    const handleEditUser = (user) => {
      editingUser.value = { ...user };
      editDialogVisible.value = true;
    };
    
    // 提交编辑
    const submitEditUser = async () => {
      try {
        submitting.value = true;
        
        await adminApi.users.update(editingUser.value.id, {
          name: editingUser.value.name,
          phone: editingUser.value.phone,
          address: editingUser.value.address
        });
        
        ElMessage.success('用户信息更新成功');
        editDialogVisible.value = false;
        
        // 刷新用户列表
        await fetchUsers();
        
      } catch (error) {
        ElMessage.error('更新用户信息失败');
        console.error(error);
      } finally {
        submitting.value = false;
      }
    };
    
    // 删除用户
    const handleDeleteUser = (user) => {
      // 管理员不能被删除
      if (user.role === 'ROLE_ADMIN') {
        ElMessage.warning('不能删除管理员账户');
        return;
      }
      
      ElMessageBox.confirm(
        `确定要删除用户 "${user.username}" 吗？此操作不可逆。`,
        '删除确认',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      ).then(async () => {
        try {
          await adminApi.users.delete(user.id);
          ElMessage.success('用户删除成功');
          
          // 刷新用户列表
          await fetchUsers();
        } catch (error) {
          ElMessage.error('删除用户失败');
          console.error(error);
        }
      }).catch(() => {});
    };
    
    // 切换用户角色
    const handleToggleRole = (user) => {
      // 不能修改自己的角色
      if (user.id === currentUserId.value) {
        ElMessage.warning('不能修改自己的角色');
        return;
      }
      
      const newRole = user.role === 'ROLE_ADMIN' ? 'ROLE_USER' : 'ROLE_ADMIN';
      const actionText = user.role === 'ROLE_ADMIN' ? '降级为普通用户' : '提升为管理员';
      
      ElMessageBox.confirm(
        `确定要将用户 "${user.username}" ${actionText}吗？`,
        '修改角色',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      ).then(async () => {
        try {
          await adminApi.users.updateRole(user.id, newRole);
          ElMessage.success(`用户角色已${user.role === 'ROLE_ADMIN' ? '降级' : '提升'}`);
          
          // 刷新用户列表
          await fetchUsers();
        } catch (error) {
          ElMessage.error('修改用户角色失败');
          console.error(error);
        }
      }).catch(() => {});
    };
    
    // 格式化日期
    const formatDate = (dateString) => {
      if (!dateString) return '未知';
      
      const date = new Date(dateString);
      return date.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
      });
    };
    
    onMounted(() => {
      fetchUsers();
    });
    
    return {
      loading,
      users,
      searchKeyword,
      filteredUsers,
      detailDialogVisible,
      editDialogVisible,
      selectedUser,
      editingUser,
      submitting,
      userRules,
      currentUserId,
      
      handleSearchInput,
      handleShowUserDetail,
      handleEditUser,
      submitEditUser,
      handleDeleteUser,
      handleToggleRole,
      formatDate
    };
  }
};
</script>

<style scoped>
.user-manage-page {
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

.header-actions .el-input {
  width: 300px;
}

.table-card {
  margin-bottom: 20px;
}

.user-detail {
  padding: 10px;
}

.detail-item {
  margin-bottom: 15px;
  display: flex;
}

.item-label {
  width: 100px;
  color: #606266;
  text-align: right;
  padding-right: 12px;
}

.item-value {
  flex: 1;
  color: #333;
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
  }
  
  .header-actions .el-input {
    width: 100%;
  }
}
</style> 