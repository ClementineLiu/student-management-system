<template>
  <div>
    <!-- 搜索栏 -->
    <el-form :inline="true" :model="searchForm" size="small">
      <el-form-item label="班级名称">
        <el-input v-model="searchForm.className" placeholder="请输入班级名称" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSearch">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮 -->
    <div style="margin-bottom:15px">
      <el-button type="primary" size="small" @click="openDialog()">新增班级</el-button>
      <el-button type="danger" size="small" :disabled="selection.length===0" @click="handleBatchDelete">批量删除</el-button>
    </div>

    <!-- 表格 -->
    <el-table :data="tableData" border stripe v-loading="loading" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column prop="id" label="ID" width="80" align="center" />
      <el-table-column prop="className" label="班级名称" min-width="150" />
      <el-table-column prop="department" label="所属专业/院系" min-width="150" />
      <el-table-column prop="grade" label="年级" width="120" />
      <el-table-column prop="addtime" label="创建时间" width="180" />
      <el-table-column label="操作" width="160" align="center" fixed="right">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="openDialog(scope.row)">编辑</el-button>
          <el-button type="text" size="small" style="color:#F56C6C" @click="handleDelete(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      style="margin-top:15px;text-align:right"
      :current-page="page"
      :page-sizes="[10,20,50]"
      :page-size="limit"
      :total="total"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="handleSizeChange"
      @current-change="handlePageChange"
    />

    <!-- 新增/编辑弹窗 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="500px" @close="resetForm">
      <el-form ref="form" :model="form" :rules="rules" label-width="110px" size="small">
        <el-form-item label="班级名称" prop="className">
          <el-input v-model="form.className" placeholder="如：计算机科学2024-1班" />
        </el-form-item>
        <el-form-item label="所属专业/院系" prop="department">
          <el-input v-model="form.department" placeholder="如：计算机科学与技术学院" />
        </el-form-item>
        <el-form-item label="年级" prop="grade">
          <el-input v-model="form.grade" placeholder="如：2024级" />
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
  name: 'ClassList',
  data() {
    return {
      searchForm: { className: '' },
      tableData: [], loading: false,
      page: 1, limit: 10, total: 0,
      selection: [],
      dialogVisible: false, dialogTitle: '', form: { id: null, className: '', department: '', grade: '' },
      rules: { className: [{ required: true, message: '请输入班级名称', trigger: 'blur' }] }
    }
  },
  mounted() { this.loadData() },
  methods: {
    loadData() {
      this.loading = true
      request.get('/classinfo/page', { params: { page: this.page, limit: this.limit, ...this.searchForm } }).then(res => {
        if (res.data) {
          this.tableData = res.data.list || []
          this.total = res.data.total || 0
        }
      }).finally(() => { this.loading = false })
    },
    handleSearch() { this.page = 1; this.loadData() },
    resetSearch() { this.searchForm = { className: '' }; this.page = 1; this.loadData() },
    handleSizeChange(v) { this.limit = v; this.loadData() },
    handlePageChange(v) { this.page = v; this.loadData() },
    handleSelectionChange(v) { this.selection = v },

    openDialog(row) {
      this.resetForm()
      if (row) {
        this.dialogTitle = '编辑班级'
        this.form = { ...row }
      } else {
        this.dialogTitle = '新增班级'
      }
      this.dialogVisible = true
    },
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        const url = this.form.id ? '/classinfo/update' : '/classinfo/save'
        request.post(url, this.form).then(() => {
          this.$message.success('操作成功')
          this.dialogVisible = false
          this.loadData()
        })
      })
    },
    handleDelete(id) {
      this.$confirm('确定删除该班级吗？', '提示', { type: 'warning' }).then(() => {
        request.post('/classinfo/delete', [id]).then(() => {
          this.$message.success('删除成功')
          this.loadData()
        })
      }).catch(() => {})
    },
    handleBatchDelete() {
      if (this.selection.length === 0) return
      this.$confirm(`确定删除选中的 ${this.selection.length} 个班级吗？`, '提示', { type: 'warning' }).then(() => {
        request.post('/classinfo/delete', this.selection.map(r => r.id)).then(() => {
          this.$message.success('批量删除成功')
          this.loadData()
        })
      }).catch(() => {})
    },
    resetForm() {
      this.$nextTick(() => {
        this.form = { id: null, className: '', department: '', grade: '' }
        if (this.$refs.form) this.$refs.form.clearValidate()
      })
    }
  }
}
</script>
