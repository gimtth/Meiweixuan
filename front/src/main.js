import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import './assets/styles/global.css'
import './assets/main.css'
import './assets/css/base.css'
import { useAuthStore } from './store/auth'

const app = createApp(App)
const pinia = createPinia()

app.use(router)
app.use(pinia)

// 初始化auth store，清理无效的登录状态
const authStore = useAuthStore()
authStore.init()

app.use(ElementPlus, {
  locale: zhCn
})

app.mount('#app')
