<template>
  <div>
    <el-form :inline="true" :model="searchForm" size="small">
      <el-form-item label="课程名称">
        <el-input v-model="searchForm.courseName" placeholder="请输入课程名称" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSearch">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </el-form-item>
    </el-form>

    <div style="margin-bottom:15px">
      <el-button type="primary" size="small" @click="openDialog()">新增课程</el-button>
      <el-button type="danger" size="small" :disabled="selection.length===0" @click="handleBatchDelete">批量删除</el-button>
    </div>

    <el-table :data="tableData" border stripe v-loading="loading" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column prop="id" label="ID" width="80" align="center" />
      <el-table-column prop="courseName" label="课程名称" min-width="150" />
      <el-table-column prop="credit" label="学分" width="80" align="center" />
      <el-table-column prop="teacher" label="任课教师" width="120" />
      <el-table-column prop="description" label="课程描述" min-width="200" show-overflow-tooltip />
      <el-table-column prop="addtime" label="创建时间" width="180" />
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

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="500px" @close="resetForm">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px" size="small">
        <el-form-item label="课程名称" prop="courseName">
          <el-input v-model="form.courseName" placeholder="如：高等数学" />
        </el-form-item>
        <el-form-item label="学分" prop="credit">
          <el-input-number v-model="form.credit" :min="0.5" :max="10" :step="0.5" />
        </el-form-item>
        <el-form-item label="任课教师" prop="teacher">
          <el-input v-model="form.teacher" placeholder="如：张老师" />
        </el-form-item>
        <el-form-item label="课程描述">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="课程简要描述" />
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
  name: 'CourseList',
  data() {
    return {
      searchForm: { courseName: '' },
      tableData: [], loading: false,
      page: 1, limit: 10, total: 0, selection: [],
      dialogVisible: false, dialogTitle: '',
      form: { id: null, courseName: '', credit: 1.0, teacher: '', description: '' },
      rules: { courseName: [{ required: true, message: '请输入课程名称', trigger: 'blur' }] }
    }
  },
  mounted() { this.loadData() },
  methods: {
    loadData() {
      this.loading = true
      request.get('/course/page', { params: { page: this.page, limit: this.limit, ...this.searchForm } }).then(res => {
        if (res.data) { this.tableData = res.data.list || []; this.total = res.data.total || 0 }
      }).finally(() => { this.loading = false })
    },
    handleSearch() { this.page = 1; this.loadData() },
    resetSearch() { this.searchForm = { courseName: '' }; this.page = 1; this.loadData() },
    handleSizeChange(v) { this.limit = v; this.loadData() },
    handlePageChange(v) { this.page = v; this.loadData() },
    handleSelectionChange(v) { this.selection = v },

    openDialog(row) {
      this.resetForm()
      if (row) { this.dialogTitle = '编辑课程'; this.form = { ...row } }
      else { this.dialogTitle = '新增课程' }
      this.dialogVisible = true
    },
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        const url = this.form.id ? '/course/update' : '/course/save'
        request.post(url, this.form).then(() => {
          this.$message.success('操作成功')
          this.dialogVisible = false
          this.loadData()
        })
      })
    },
    handleDelete(id) {
      this.$confirm('确定删除该课程吗？', '提示', { type: 'warning' }).then(() => {
        request.post('/course/delete', [id]).then(() => { this.$message.success('删除成功'); this.loadData() })
      }).catch(() => {})
    },
    handleBatchDelete() {
      if (this.selection.length === 0) return
      this.$confirm(`确定删除选中的 ${this.selection.length} 门课程吗？`, '提示', { type: 'warning' }).then(() => {
        request.post('/course/delete', this.selection.map(r => r.id)).then(() => {
          this.$message.success('批量删除成功'); this.loadData()
        })
      }).catch(() => {})
    },
    resetForm() {
      this.$nextTick(() => {
        this.form = { id: null, courseName: '', credit: 1.0, teacher: '', description: '' }
        if (this.$refs.form) this.$refs.form.clearValidate()
      })
    }
  }
}
</script>
