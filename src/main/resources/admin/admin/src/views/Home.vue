<template>
  <div>
    <!-- 统计卡片 -->
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon" style="background:#409EFF"><i class="el-icon-user"></i></div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.studentCount }}</div>
            <div class="stat-label">学生总数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon" style="background:#67C23A"><i class="el-icon-school"></i></div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.classCount }}</div>
            <div class="stat-label">班级总数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon" style="background:#E6A23C"><i class="el-icon-reading"></i></div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.courseCount }}</div>
            <div class="stat-label">课程总数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon" style="background:#F56C6C"><i class="el-icon-s-data"></i></div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.avgScore }}</div>
            <div class="stat-label">平均成绩</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" style="margin-top:20px">
      <el-col :span="12">
        <el-card>
          <div slot="header"><span>各班级学生人数</span></div>
          <div ref="pieChart" style="height:300px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div slot="header"><span>各课程平均分</span></div>
          <div ref="barChart" style="height:300px"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row style="margin-top:20px">
      <el-col :span="24">
        <el-card>
          <div slot="header"><span>成绩区间分布</span></div>
          <div ref="distChart" style="height:300px"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import request from '../utils/request'
import * as echarts from 'echarts'

export default {
  name: 'Home',
  data() {
    return {
      stats: { studentCount: 0, classCount: 0, courseCount: 0, avgScore: '--' }
    }
  },
  mounted() {
    this.loadStats()
  },
  methods: {
    loadStats() {
      // 并发请求统计数据
      Promise.all([
        request.get('/cal/student/id'),
        request.get('/cal/class_info/id'),
        request.get('/cal/course/id'),
        request.get('/cal/score/score'),
        request.get('/group/student/class_id'),
        request.get('/value/score/course_id/score')
      ]).then(([sRes, cRes, coRes, aRes, gRes, vRes]) => {
        // 总数统计
        if (sRes.data && sRes.data.count !== undefined) this.stats.studentCount = sRes.data.count
        if (cRes.data && cRes.data.count !== undefined) this.stats.classCount = cRes.data.count
        if (coRes.data && coRes.data.count !== undefined) this.stats.courseCount = coRes.data.count
        if (aRes.data && aRes.data.avg !== undefined) this.stats.avgScore = parseFloat(aRes.data.avg).toFixed(1)

        // 饼图 - 各班级人数
        this.initPieChart(gRes.data || {})

        // 柱状图 - 各课程平均分
        this.initBarChart(vRes.data || {})

        // 成绩区间分布（静态模拟，等CommonController实现后替换）
        this.initDistChart()
      }).catch(() => {
        // 首次启动可能还没有数据，忽略错误
      })
    },
    initPieChart(data) {
      const chart = echarts.init(this.$refs.pieChart)
      const keys = Object.keys(data)
      if (keys.length === 0) {
        chart.setOption({ title: { text: '暂无数据', left: 'center', top: 'center', textStyle: { color: '#999', fontSize: 14 } } })
        return
      }
      chart.setOption({
        tooltip: { trigger: 'item', formatter: '{b}: {c}人 ({d}%)' },
        legend: { orient: 'vertical', right: 10, top: 'center' },
        series: [{
          type: 'pie', radius: ['40%', '70%'], center: ['40%', '50%'],
          data: keys.map(k => ({ name: k, value: data[k] })),
          emphasis: { itemStyle: { shadowBlur: 10, shadowOffsetX: 0, shadowColor: 'rgba(0,0,0,0.5)' } }
        }]
      })
    },
    initBarChart(data) {
      const chart = echarts.init(this.$refs.barChart)
      const keys = Object.keys(data)
      if (keys.length === 0) {
        chart.setOption({ title: { text: '暂无数据', left: 'center', top: 'center', textStyle: { color: '#999', fontSize: 14 } } })
        return
      }
      chart.setOption({
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: keys, axisLabel: { rotate: 15 } },
        yAxis: { type: 'value', name: '平均分', max: 100 },
        series: [{
          type: 'bar', data: keys.map(k => parseFloat(data[k]).toFixed(1)),
          itemStyle: { color: '#409EFF' }
        }]
      })
    },
    initDistChart() {
      const chart = echarts.init(this.$refs.distChart)
      request.get('/scoreDistribution').then(res => {
        if (!res.data) {
          chart.setOption({ title: { text: '暂无数据', left: 'center', top: 'center', textStyle: { color: '#999', fontSize: 14 } } })
          return
        }
        const data = res.data
        const values = [
          data.range_0_59 || 0,
          data.range_60_69 || 0,
          data.range_70_79 || 0,
          data.range_80_89 || 0,
          data.range_90_100 || 0
        ]
        chart.setOption({
          tooltip: { trigger: 'axis' },
          xAxis: { type: 'category', data: ['0-59', '60-69', '70-79', '80-89', '90-100'] },
          yAxis: { type: 'value', name: '人数', minInterval: 1 },
          series: [{
            type: 'bar', data: values,
            itemStyle: { color: '#67C23A' },
            label: { show: true, position: 'top' }
          }]
        })
      }).catch(() => {
        chart.setOption({ title: { text: '暂无数据', left: 'center', top: 'center', textStyle: { color: '#999', fontSize: 14 } } })
      })
    }
  },
  beforeDestroy() {
    // 组件销毁前清理 echarts 实例
    if (this.$refs.pieChart) echarts.dispose(this.$refs.pieChart)
    if (this.$refs.barChart) echarts.dispose(this.$refs.barChart)
    if (this.$refs.distChart) echarts.dispose(this.$refs.distChart)
  }
}
</script>

<style scoped>
.stat-card {
  cursor: pointer;
}
.stat-card ::v-deep .el-card__body {
  display: flex;
  align-items: center;
  padding: 20px;
}
.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16px;
  flex-shrink: 0;
}
.stat-icon i {
  font-size: 28px;
  color: #fff;
}
.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #333;
}
.stat-label {
  font-size: 14px;
  color: #999;
  margin-top: 4px;
}
</style>
