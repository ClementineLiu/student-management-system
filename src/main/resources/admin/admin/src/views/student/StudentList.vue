<template>
  <div>
    <!-- 搜索栏 -->
    <el-form :inline="true" :model="searchForm" size="small">
      <el-form-item label="学号">
        <el-input v-model="searchForm.studentId" placeholder="学号" clearable />
      </el-form-item>
      <el-form-item label="姓名">
        <el-input v-model="searchForm.name" placeholder="姓名" clearable />
      </el-form-item>
      <el-form-item label="班级">
        <el-select v-model="searchForm.classId" placeholder="全部班级" clearable filterable>
          <el-option v-for="c in classList" :key="c.id" :label="c.className" :value="c.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="性别">
        <el-select v-model="searchForm.gender" placeholder="全部" clearable>
          <el-option label="男" value="男" />
          <el-option label="女" value="女" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSearch">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </el-form-item>
    </el-form>

    <div style="margin-bottom:15px">
      <el-button type="primary" size="small" @click="openDialog()">新增学生</el-button>
      <el-button type="danger" size="small" :disabled="selection.length===0" @click="handleBatchDelete">批量删除</el-button>
    </div>

    <el-table :data="tableData" border stripe v-loading="loading" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column prop="studentId" label="学号" width="120" />
      <el-table-column prop="name" label="姓名" width="100" />
      <el-table-column prop="gender" label="性别" width="70" align="center" />
      <el-table-column prop="age" label="年龄" width="70" align="center" />
      <el-table-column prop="className" label="班级" min-width="150" />
      <el-table-column prop="phone" label="手机号" width="130" />
      <el-table-column prop="email" label="邮箱" width="180" />
      <el-table-column prop="enrollmentDate" label="入学日期" width="120" />
      <el-table-column label="操作" width="160" align="center" fixed="right">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="openDialog(scope.row)">编辑</el-button>
          <el-button type="text" size="small" style="color:#F56C6C" @click="handleDelete(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      style="margin-top:15px;text-align:right"
      :current-page="page" :page-sizes="[10,20,50]" :page-size="limit"
      :total="total" layout="total, sizes, prev, pager, next, jumper"
      @size-change="handleSizeChange" @current-change="handlePageChange"
    />

    <!-- 新增/编辑弹窗 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="580px" @close="resetForm">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px" size="small">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="学号" prop="studentId">
              <el-input v-model="form.studentId" placeholder="唯一学号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="姓名" prop="name">
              <el-input v-model="form.name" placeholder="学生姓名" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="性别">
              <el-select v-model="form.gender" style="width:100%">
                <el-option label="男" value="男" />
                <el-option label="女" value="女" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="年龄">
              <el-input-number v-model="form.age" :min="1" :max="100" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="班级">
          <el-select v-model="form.classId" placeholder="请选择班级" style="width:100%" filterable>
            <el-option v-for="c in classList" :key="c.id" :label="c.className" :value="c.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="form.phone" placeholder="手机号" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" placeholder="邮箱" />
        </el-form-item>
        <el-form-item label="家庭住址">
          <el-input v-model="form.address" placeholder="家庭住址" />
        </el-form-item>
        <el-form-item label="入学日期">
          <el-date-picker v-model="form.enrollmentDate" type="date" placeholder="选择日期" style="width:100%" value-format="yyyy-MM-dd" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from '../../utils/request'

export default {
  name: 'StudentList',
  data() {
    return {
      searchForm: { studentId: '', name: '', classId: '', gender: '' },
      tableData: [], classList: [], loading: false,
      page: 1, limit: 10, total: 0, selection: [],
      dialogVisible: false, dialogTitle: '',
      form: { id: null, studentId: '', name: '', gender: '男', age: null, classId: null, phone: '', email: '', address: '', enrollmentDate: '' },
      rules: {
        studentId: [{ required: true, message: '请输入学号', trigger: 'blur' }],
        name: [{ required: true, message: '请输入姓名', trigger: 'blur' }]
      }
    }
  },
  mounted() {
    this.loadData()
    this.loadClassList()
  },
  methods: {
    loadData() {
      this.loading = true
      request.get('/student/page', { params: { page: this.page, limit: this.limit, ...this.searchForm } }).then(res => {
        if (res.data) { this.tableData = res.data.list || []; this.total = res.data.total || 0 }
      }).finally(() => { this.loading = false })
    },
    loadClassList() {
      request.get('/classinfo/list').then(res => {
        if (res.data) this.classList = res.data
      })
    },
    handleSearch() { this.page = 1; this.loadData() },
    resetSearch() { this.searchForm = { studentId: '', name: '', classId: '', gender: '' }; this.page = 1; this.loadData() },
    handleSizeChange(v) { this.limit = v; this.loadData() },
    handlePageChange(v) { this.page = v; this.loadData() },
    handleSelectionChange(v) { this.selection = v },

    openDialog(row) {
      this.resetForm()
      if (row) { this.dialogTitle = '编辑学生'; this.form = { ...row } }
      else { this.dialogTitle = '新增学生' }
      this.dialogVisible = true
    },
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        const url = this.form.id ? '/student/update' : '/student/save'
        request.post(url, this.form).then(() => {
          this.$message.success('操作成功')
          this.dialogVisible = false
          this.loadData()
        })
      })
    },
    handleDelete(id) {
      this.$confirm('确定删除该学生吗？相关成绩也会被影响。', '提示', { type: 'warning' }).then(() => {
        request.post('/student/delete', [id]).then(() => { this.$message.success('删除成功'); this.loadData() })
      }).catch(() => {})
    },
    handleBatchDelete() {
      if (this.selection.length === 0) return
      this.$confirm(`确定删除选中的 ${this.selection.length} 名学生吗？`, '提示', { type: 'warning' }).then(() => {
        request.post('/student/delete', this.selection.map(r => r.id)).then(() => {
          this.$message.success('批量删除成功'); this.loadData()
        })
      }).catch(() => {})
    },
    resetForm() {
      this.$nextTick(() => {
        this.form = { id: null, studentId: '', name: '', gender: '男', age: null, classId: null, phone: '', email: '', address: '', enrollmentDate: '' }
        if (this.$refs.form) this.$refs.form.clearValidate()
      })
    }
  }
}
</script>
