<template>
  <div class="category-manage">
    <div class="page-header">
      <h1>分类管理</h1>
      <el-button type="primary" @click="showAddDialog">添加分类</el-button>
    </div>
    
    <!-- 分类列表 -->
    <el-table
      v-loading="loading"
      :data="categories"
      border
      style="width: 100%"
    >
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="分类名称" />
      <el-table-column prop="description" label="描述" show-overflow-tooltip />
      <el-table-column label="操作" width="200">
        <template #default="scope">
          <el-button
            size="small"
            type="primary"
            @click="showEditDialog(scope.row)"
          >
            编辑
          </el-button>
          <el-button
            size="small"
            type="danger"
            @click="confirmDelete(scope.row)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加/编辑分类对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑分类' : '添加分类'"
      width="500px"
    >
      <el-form
        ref="categoryForm"
        :model="currentCategory"
        :rules="rules"
        label-width="80px"
      >
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="currentCategory.name" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input
            v-model="currentCategory.description"
            type="textarea"
            placeholder="请输入分类描述"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSave" :loading="saveLoading">
            保存
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import adminApi from '../../api/adminApi';

export default {
  name: 'CategoryManage',
  setup() {
    // 状态
    const loading = ref(false);
    const saveLoading = ref(false);
    const categories = ref([]);
    const dialogVisible = ref(false);
    const isEdit = ref(false);
    const currentCategory = reactive({
      id: null,
      name: '',
      description: ''
    });
    
    // 验证规则
    const rules = {
      name: [
        { required: true, message: '请输入分类名称', trigger: 'blur' },
        { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
      ]
    };
    
    // 初始化数据
    const fetchCategories = async () => {
      loading.value = true;
      try {
        categories.value = await adminApi.categories.getAll();
      } catch (error) {
        ElMessage.error('获取分类列表失败');
        console.error(error);
      } finally {
        loading.value = false;
      }
    };
    
    // 显示添加对话框
    const showAddDialog = () => {
      isEdit.value = false;
      Object.assign(currentCategory, {
        id: null,
        name: '',
        description: ''
      });
      dialogVisible.value = true;
    };
    
    // 显示编辑对话框
    const showEditDialog = (category) => {
      isEdit.value = true;
      Object.assign(currentCategory, {
        id: category.id,
        name: category.name,
        description: category.description || ''
      });
      dialogVisible.value = true;
    };
    
    // 保存分类
    const handleSave = async () => {
      saveLoading.value = true;
      try {
        if (isEdit.value) {
          await adminApi.categories.update(currentCategory.id, currentCategory);
          ElMessage.success('分类更新成功');
        } else {
          await adminApi.categories.create(currentCategory);
          ElMessage.success('分类添加成功');
        }
        dialogVisible.value = false;
        fetchCategories();
      } catch (error) {
        ElMessage.error(isEdit.value ? '更新分类失败' : '添加分类失败');
        console.error(error);
      } finally {
        saveLoading.value = false;
      }
    };
    
    // 确认删除
    const confirmDelete = (category) => {
      ElMessageBox.confirm(
        `确定要删除分类"${category.name}"吗？删除后无法恢复，且该分类下的菜品也将无法访问。`,
        '警告',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      )
        .then(async () => {
          loading.value = true;
          try {
            await adminApi.categories.delete(category.id);
            ElMessage.success('分类删除成功');
            fetchCategories();
          } catch (error) {
            ElMessage.error('删除分类失败');
            console.error(error);
          } finally {
            loading.value = false;
          }
        })
        .catch(() => {
          ElMessage.info('已取消删除');
        });
    };
    
    // 生命周期钩子
    onMounted(() => {
      fetchCategories();
    });
    
    return {
      loading,
      saveLoading,
      categories,
      dialogVisible,
      isEdit,
      currentCategory,
      rules,
      showAddDialog,
      showEditDialog,
      handleSave,
      confirmDelete
    };
  }
};
</script>

<style scoped>
.category-manage {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h1 {
  margin: 0;
  font-size: 24px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
}
</style> 