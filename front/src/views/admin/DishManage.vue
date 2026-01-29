<template>
  <div class="dish-manage">
    <div class="page-header">
      <h1>菜品管理</h1>
      <el-button type="primary" @click="showAddDialog">添加菜品</el-button>
    </div>

    <!-- 过滤器 -->
    <div class="filters">
      <el-select
        v-model="filter.categoryId"
        placeholder="按分类筛选"
        clearable
        @change="handleFilterChange"
      >
        <el-option
          v-for="category in categories"
          :key="category.id"
          :label="category.name"
          :value="category.id"
        />
      </el-select>
      
      <el-input
        v-model="filter.search"
        placeholder="搜索菜品名称"
        clearable
        class="search-input"
        @clear="handleFilterChange"
        @keyup.enter="handleFilterChange"
      >
        <template #append>
          <el-button @click="handleFilterChange">
            <el-icon><Search /></el-icon>
          </el-button>
        </template>
      </el-input>
    </div>
    
    <!-- 菜品列表 -->
    <el-table
      v-loading="loading"
      :data="dishes"
      border
      style="width: 100%"
    >
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column label="菜品图片" width="120">
        <template #default="scope">
          <el-image
            :src="scope.row.image || dishSmallPlaceholder"
            style="width: 60px; height: 60px"
            fit="cover"
          />
        </template>
      </el-table-column>
      <el-table-column prop="name" label="菜品名称" />
      <el-table-column label="分类" width="120">
        <template #default="scope">
          <el-tag>{{ getCategoryName(scope.row.categoryId) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="price" label="价格" width="120">
        <template #default="scope">
          ¥{{ scope.row.price.toFixed(2) }}
        </template>
      </el-table-column>
      <el-table-column prop="stock" label="库存" width="100" />
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

    <!-- 添加/编辑菜品对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑菜品' : '添加菜品'"
      width="600px"
    >
      <el-form
        ref="dishForm"
        :model="currentDish"
        :rules="rules"
        label-width="80px"
      >
        <el-form-item label="菜品名称" prop="name">
          <el-input v-model="currentDish.name" placeholder="请输入菜品名称" />
        </el-form-item>
        <el-form-item label="分类" prop="categoryId">
          <el-select v-model="currentDish.categoryId" placeholder="请选择分类">
            <el-option
              v-for="category in categories"
              :key="category.id"
              :label="category.name"
              :value="category.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="价格" prop="price">
          <el-input-number
            v-model="currentDish.price"
            :min="0"
            :precision="2"
            :step="0.5"
          />
        </el-form-item>
        <el-form-item label="库存" prop="stock">
          <el-input-number
            v-model="currentDish.stock"
            :min="0"
            :precision="0"
            :step="1"
          />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input
            v-model="currentDish.description"
            type="textarea"
            placeholder="请输入菜品描述"
          />
        </el-form-item>
        <el-form-item label="图片URL" prop="image">
          <el-input v-model="currentDish.image" placeholder="请输入图片URL" />
          <el-upload
            class="upload-demo"
            action="/images/dishes/"
            :show-file-list="false"
            :on-success="(res, file) => handleUploadSuccess(res, file)"
            :before-upload="beforeUpload"
          >
            <el-button size="small" type="primary">上传图片</el-button>
          </el-upload>
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
import { ref, reactive, onMounted, computed } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { Search } from '@element-plus/icons-vue';
import adminApi from '../../api/adminApi';
import dishApi from '../../api/dishApi';
import dishSmallPlaceholder from '../../assets/images/placeholders/dish-small-placeholder.jpg';

export default {
  name: 'DishManage',
  components: {
    Search
  },
  setup() {
    // 状态
    const loading = ref(false);
    const saveLoading = ref(false);
    const dishes = ref([]);
    const categories = ref([]);
    const dialogVisible = ref(false);
    const isEdit = ref(false);
    const filter = reactive({
      categoryId: '',
      search: ''
    });
    
    const currentDish = reactive({
      id: null,
      name: '',
      categoryId: '',
      price: 0,
      stock: 0,
      description: '',
      image: ''
    });
    
    // 验证规则
    const rules = {
      name: [
        { required: true, message: '请输入菜品名称', trigger: 'blur' },
        { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
      ],
      categoryId: [
        { required: true, message: '请选择分类', trigger: 'change' }
      ],
      price: [
        { required: true, message: '请输入价格', trigger: 'blur' }
      ],
      stock: [
        { required: true, message: '请输入库存', trigger: 'blur' }
      ]
    };
    
    // 初始化数据
    const fetchCategories = async () => {
      try {
        categories.value = await adminApi.categories.getAll();
      } catch (error) {
        ElMessage.error('获取分类列表失败');
        console.error(error);
      }
    };
    
    const fetchDishes = async () => {
      loading.value = true;
      try {
        if (filter.categoryId) {
          dishes.value = await adminApi.dishes.getByCategory(filter.categoryId);
        } else {
          dishes.value = await adminApi.dishes.getAll();
        }
        
        // 简单的本地搜索过滤
        if (filter.search) {
          const searchLower = filter.search.toLowerCase();
          dishes.value = dishes.value.filter(dish => 
            dish.name.toLowerCase().includes(searchLower) || 
            dish.description.toLowerCase().includes(searchLower)
          );
        }
      } catch (error) {
        ElMessage.error('获取菜品列表失败');
        console.error(error);
      } finally {
        loading.value = false;
      }
    };
    
    // 显示添加对话框
    const showAddDialog = () => {
      isEdit.value = false;
      Object.assign(currentDish, {
        id: null,
        name: '',
        categoryId: categories.value.length > 0 ? categories.value[0].id : '',
        price: 0,
        stock: 0,
        description: '',
        image: ''
      });
      dialogVisible.value = true;
    };
    
    // 显示编辑对话框
    const showEditDialog = (dish) => {
      isEdit.value = true;
      Object.assign(currentDish, {
        id: dish.id,
        name: dish.name,
        categoryId: dish.categoryId,
        price: dish.price,
        stock: dish.stock,
        description: dish.description || '',
        image: dish.image || ''
      });
      dialogVisible.value = true;
    };
    
    // 保存菜品
    const handleSave = async () => {
      saveLoading.value = true;
      try {
        if (isEdit.value) {
          await adminApi.dishes.update(currentDish.id, currentDish);
          ElMessage.success('菜品更新成功');
        } else {
          await adminApi.dishes.create(currentDish);
          ElMessage.success('菜品添加成功');
        }
        dialogVisible.value = false;
        fetchDishes();
      } catch (error) {
        ElMessage.error(isEdit.value ? '更新菜品失败' : '添加菜品失败');
        console.error(error);
      } finally {
        saveLoading.value = false;
      }
    };
    
    // 确认删除
    const confirmDelete = (dish) => {
      ElMessageBox.confirm(
        `确定要删除菜品"${dish.name}"吗？删除后无法恢复。`,
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
            await adminApi.dishes.delete(dish.id);
            ElMessage.success('菜品删除成功');
            fetchDishes();
          } catch (error) {
            ElMessage.error('删除菜品失败');
            console.error(error);
          } finally {
            loading.value = false;
          }
        })
        .catch(() => {
          ElMessage.info('已取消删除');
        });
    };
    
    // 处理筛选变化
    const handleFilterChange = () => {
      fetchDishes();
    };
    
    // 获取分类名称
    const getCategoryName = (categoryId) => {
      const category = categories.value.find(c => c.id === categoryId);
      return category ? category.name : '未知分类';
    };
    
    // 上传前校验
    const beforeUpload = (file) => {
      const isJPG = file.type === 'image/jpeg' || file.type === 'image/png';
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isJPG) {
        ElMessage.error('只能上传 JPG/PNG 格式图片!');
      }
      if (!isLt2M) {
        ElMessage.error('图片大小不能超过 2MB!');
      }
      return isJPG && isLt2M;
    };
    // 上传成功回调
    const handleUploadSuccess = (res, file) => {
      // 假设后端返回图片URL在 res.url
      currentDish.image = res.url || (res[0] && res[0].url) || file.response?.url || '';
      if (currentDish.image) {
        ElMessage.success('图片上传成功');
      } else {
        ElMessage.error('图片上传失败');
      }
    };
    
    // 生命周期钩子
    onMounted(async () => {
      await fetchCategories();
      fetchDishes();
    });
    
    return {
      loading,
      saveLoading,
      dishes,
      categories,
      dialogVisible,
      isEdit,
      currentDish,
      filter,
      rules,
      showAddDialog,
      showEditDialog,
      handleSave,
      confirmDelete,
      handleFilterChange,
      getCategoryName,
      dishSmallPlaceholder,
      beforeUpload,
      handleUploadSuccess
    };
  }
};
</script>

<style scoped>
.dish-manage {
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

.filters {
  display: flex;
  margin-bottom: 20px;
  gap: 15px;
}

.search-input {
  width: 300px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
}
</style> 