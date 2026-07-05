<template>
  <el-container class="layout-container">
    <!-- 侧边栏 -->
    <el-aside width="220px" class="sidebar">
      <div class="logo">
        <h2>学生管理系统</h2>
      </div>
      <el-menu
        :default-active="activeMenu"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
        router
      >
        <el-menu-item index="/home">
          <i class="el-icon-s-home"></i>
          <span>系统首页</span>
        </el-menu-item>
        <el-menu-item index="/student">
          <i class="el-icon-user"></i>
          <span>学生信息管理</span>
        </el-menu-item>
        <el-menu-item index="/class">
          <i class="el-icon-school"></i>
          <span>班级管理</span>
        </el-menu-item>
        <el-menu-item index="/course">
          <i class="el-icon-reading"></i>
          <span>课程管理</span>
        </el-menu-item>
        <el-menu-item index="/score">
          <i class="el-icon-s-data"></i>
          <span>成绩管理</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <!-- 右侧主体 -->
    <el-container>
      <!-- 顶部栏 -->
      <el-header class="header">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item>{{ currentTitle }}</el-breadcrumb-item>
        </el-breadcrumb>
        <div class="header-right">
          <span class="username">{{ username }}</span>
          <el-button type="text" @click="logout">退出登录</el-button>
        </div>
      </el-header>

      <!-- 主内容区 -->
      <el-main class="main-content">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
export default {
  name: 'Layout',
  computed: {
    activeMenu() {
      return this.$route.path
    },
    currentTitle() {
      return this.$route.meta && this.$route.meta.title ? this.$route.meta.title : ''
    },
    username() {
      return localStorage.getItem('username') || '管理员'
    }
  },
  methods: {
    logout() {
      this.$confirm('确定要退出登录吗？', '提示', { type: 'warning' }).then(() => {
        localStorage.removeItem('token')
        localStorage.removeItem('username')
        localStorage.removeItem('role')
        this.$router.push('/login')
      }).catch(() => {})
    }
  }
}
</script>

<style scoped>
.layout-container {
  height: 100vh;
}
.sidebar {
  background-color: #304156;
  overflow-y: auto;
}
.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #2b3a4a;
}
.logo h2 {
  color: #fff;
  font-size: 18px;
  margin: 0;
}
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #fff;
  border-bottom: 1px solid #e6e6e6;
  box-shadow: 0 1px 4px rgba(0,0,0,0.08);
}
.header-right {
  display: flex;
  align-items: center;
  gap: 10px;
}
.username {
  color: #666;
}
.main-content {
  background-color: #f0f2f5;
  padding: 20px;
}
</style>
