<script>
import TheHeader from './components/TheHeader.vue'
import TheFooter from './components/TheFooter.vue'
import { useRoute } from 'vue-router'

export default {
  name: 'App',
  components: {
    TheHeader,
    TheFooter
  },
  setup() {
    const route = useRoute()
    return { route }
  }
}
</script>

<template>
  <div class="app-container">
    <TheHeader />
    <main class="main-content">
      <router-view />
    </main>
    <TheFooter v-if="!route.path.startsWith('/admin')" />
  </div>
</template>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html, body {
  font-family: 'Microsoft YaHei', Arial, sans-serif;
  height: 100%;
  width: 100%;
}

.app-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.main-content {
  flex: 1;
}
</style>
