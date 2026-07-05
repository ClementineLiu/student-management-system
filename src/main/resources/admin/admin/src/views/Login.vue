<template>
  <div class="login-container">
    <div class="login-card">
      <h2 class="login-title">学生管理系统</h2>
      <p class="login-subtitle">管理员登录</p>
      <el-form ref="loginForm" :model="form" :rules="rules" label-width="0">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" prefix-icon="el-icon-user" size="medium" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" prefix-icon="el-icon-lock"
                    size="medium" @keyup.enter.native="handleLogin" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" style="width:100%" size="medium" @click="handleLogin">
            {{ loading ? '登录中...' : '登 录' }}
          </el-button>
        </el-form-item>
      </el-form>
      <p class="login-hint">默认账户：admin / admin123</p>
    </div>
  </div>
</template>

<script>
import request from '../utils/request'

export default {
  name: 'Login',
  data() {
    return {
      form: { username: '', password: '' },
      rules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
      },
      loading: false
    }
  },
  methods: {
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (!valid) return
        this.loading = true
        request.post('/users/login', this.form).then(res => {
          if (res.data && res.data.token) {
            localStorage.setItem('token', res.data.token)
            localStorage.setItem('username', res.data.username)
            localStorage.setItem('role', res.data.role)
            this.$message.success('登录成功')
            this.$router.push('/home')
          }
        }).finally(() => {
          this.loading = false
        })
      })
    }
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}
.login-card {
  width: 400px;
  padding: 40px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);
}
.login-title {
  text-align: center;
  color: #333;
  margin-bottom: 8px;
  font-size: 24px;
}
.login-subtitle {
  text-align: center;
  color: #999;
  margin-bottom: 30px;
}
.login-hint {
  text-align: center;
  color: #bbb;
  font-size: 12px;
  margin-top: 10px;
}
</style>
