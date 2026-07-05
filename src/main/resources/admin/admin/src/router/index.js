import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

// 布局页面
const Layout = () => import('../views/Layout.vue')

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/',
    component: Layout,
    redirect: '/home',
    children: [
      {
        path: 'home',
        name: 'Home',
        component: () => import('../views/Home.vue'),
        meta: { title: '系统首页', icon: 'el-icon-s-home' }
      },
      {
        path: 'student',
        name: 'Student',
        component: () => import('../views/student/StudentList.vue'),
        meta: { title: '学生信息管理', icon: 'el-icon-user' }
      },
      {
        path: 'class',
        name: 'ClassInfo',
        component: () => import('../views/classinfo/ClassList.vue'),
        meta: { title: '班级管理', icon: 'el-icon-school' }
      },
      {
        path: 'course',
        name: 'Course',
        component: () => import('../views/course/CourseList.vue'),
        meta: { title: '课程管理', icon: 'el-icon-reading' }
      },
      {
        path: 'score',
        name: 'Score',
        component: () => import('../views/score/ScoreList.vue'),
        meta: { title: '成绩管理', icon: 'el-icon-s-data' }
      }
    ]
  },
  {
    path: '*',
    redirect: '/'
  }
]

const router = new VueRouter({
  mode: 'hash',
  routes
})

// 路由守卫：未登录自动跳转登录页
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.path !== '/login' && !token) {
    next('/login')
  } else if (to.path === '/login' && token) {
    next('/home')
  } else {
    next()
  }
})

export default router
