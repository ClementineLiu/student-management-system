<template>
  <div>
    <!-- 搜索栏 -->
    <el-form :inline="true" :model="searchForm" size="small">
      <el-form-item label="学生">
        <el-select v-model="searchForm.studentId" placeholder="全部学生" clearable filterable>
          <el-option v-for="s in studentList" :key="s.id" :label="s.name + ' (' + s.studentId + ')'" :value="s.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="课程">
        <el-select v-model="searchForm.courseId" placeholder="全部课程" clearable filterable>
          <el-option v-for="c in courseList" :key="c.id" :label="c.courseName" :value="c.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="学期">
        <el-input v-model="searchForm.semester" placeholder="如：2025-2026-1" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSearch">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </el-form-item>
    </el-form>

    <div style="margin-bottom:15px">
      <el-button type="primary" size="small" @click="openDialog()">录入成绩</el-button>
      <el-button type="danger" size="small" :disabled="selection.length===0" @click="handleBatchDelete">批量删除</el-button>
    </div>

    <el-table :data="tableData" border stripe v-loading="loading" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column prop="studentNumber" label="学号" width="120" />
      <el-table-column prop="studentName" label="姓名" width="100" />
      <el-table-column prop="className" label="班级" min-width="150" />
      <el-table-column prop="courseName" label="课程" min-width="150" />
      <el-table-column prop="score" label="分数" width="80" align="center" />
      <el-table-column prop="semester" label="学期" width="130" />
      <el-table-column prop="examType" label="考试类型" width="100" align="center" />
      <el-table-column prop="addtime" label="录入时间" width="160" />
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

    <!-- 录入/编辑弹窗 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="500px" @close="resetForm">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px" size="small">
        <el-form-item label="学生" prop="studentId">
          <el-select v-model="form.studentId" placeholder="请选择学生" style="width:100%" filterable>
            <el-option v-for="s in studentList" :key="s.id" :label="s.name + ' (' + s.studentId + ')'" :value="s.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="课程" prop="courseId">
          <el-select v-model="form.courseId" placeholder="请选择课程" style="width:100%" filterable>
            <el-option v-for="c in courseList" :key="c.id" :label="c.courseName" :value="c.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="分数" prop="score">
          <el-input-number v-model="form.score" :min="0" :max="100" :step="0.5" style="width:100%" />
        </el-form-item>
        <el-form-item label="学期" prop="semester">
          <el-input v-model="form.semester" placeholder="如：2025-2026-1" />
        </el-form-item>
        <el-form-item label="考试类型" prop="examType">
          <el-select v-model="form.examType" style="width:100%">
            <el-option label="期末考试" value="期末考试" />
            <el-option label="期中考试" value="期中考试" />
            <el-option label="补考" value="补考" />
          </el-select>
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
  name: 'ScoreList',
  data() {
    return {
      searchForm: { studentId: '', courseId: '', semester: '' },
      tableData: [], studentList: [], courseList: [], loading: false,
      page: 1, limit: 10, total: 0, selection: [],
      dialogVisible: false, dialogTitle: '',
      form: { id: null, studentId: null, courseId: null, score: 0, semester: '', examType: '期末考试' },
      rules: {
        studentId: [{ required: true, message: '请选择学生', trigger: 'change' }],
        courseId: [{ required: true, message: '请选择课程', trigger: 'change' }],
        score: [{ required: true, message: '请输入分数', trigger: 'blur' }],
        semester: [{ required: true, message: '请输入学期', trigger: 'blur' }],
        examType: [{ required: true, message: '请选择考试类型', trigger: 'change' }]
      }
    }
  },
  mounted() {
    this.loadData()
    this.loadStudentList()
    this.loadCourseList()
  },
  methods: {
    loadData() {
      this.loading = true
      const params = { page: this.page, limit: this.limit }
      if (this.searchForm.studentId) params.studentId = this.searchForm.studentId
      if (this.searchForm.courseId) params.courseId = this.searchForm.courseId
      if (this.searchForm.semester) params.semester = this.searchForm.semester
      request.get('/score/page', { params }).then(res => {
        if (res.data) { this.tableData = res.data.list || []; this.total = res.data.total || 0 }
      }).finally(() => { this.loading = false })
    },
    loadStudentList() {
      request.get('/student/page', { params: { page: 1, limit: 9999 } }).then(res => {
        if (res.data) this.studentList = res.data.list || []
      }).catch(() => {})
    },
    loadCourseList() {
      request.get('/course/list').then(res => {
        if (res.data) this.courseList = res.data
      }).catch(() => {})
    },
    handleSearch() { this.page = 1; this.loadData() },
    resetSearch() { this.searchForm = { studentId: '', courseId: '', semester: '' }; this.page = 1; this.loadData() },
    handleSizeChange(v) { this.limit = v; this.loadData() },
    handlePageChange(v) { this.page = v; this.loadData() },
    handleSelectionChange(v) { this.selection = v },

    openDialog(row) {
      this.resetForm()
      if (row) { this.dialogTitle = '编辑成绩'; this.form = { ...row } }
      else { this.dialogTitle = '录入成绩' }
      this.dialogVisible = true
    },
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        const url = this.form.id ? '/score/update' : '/score/save'
        request.post(url, this.form).then(() => {
          this.$message.success('操作成功')
          this.dialogVisible = false
          this.loadData()
        })
      })
    },
    handleDelete(id) {
      this.$confirm('确定删除该成绩记录吗？', '提示', { type: 'warning' }).then(() => {
        request.post('/score/delete', [id]).then(() => { this.$message.success('删除成功'); this.loadData() })
      }).catch(() => {})
    },
    handleBatchDelete() {
      if (this.selection.length === 0) return
      this.$confirm(`确定删除选中的 ${this.selection.length} 条成绩吗？`, '提示', { type: 'warning' }).then(() => {
        request.post('/score/delete', this.selection.map(r => r.id)).then(() => {
          this.$message.success('批量删除成功'); this.loadData()
        })
      }).catch(() => {})
    },
    resetForm() {
      this.$nextTick(() => {
        this.form = { id: null, studentId: null, courseId: null, score: 0, semester: '', examType: '期末考试' }
        if (this.$refs.form) this.$refs.form.clearValidate()
      })
    }
  }
}
</script>
