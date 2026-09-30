<template>
  <div class="app-container trace-stats-page">
    <section class="hero-panel">
      <div class="hero-copy">
        <p class="hero-kicker">Operations Monitor</p>
        <h1>葡萄酒溯源统计监控</h1>
        <p class="hero-desc">
          从查询热度、产品关注度、消费者评分、数据完整率到可疑反馈，集中展示宁夏葡萄酒溯源系统的运行状态，帮助管理者进行日常监控与汇报展示。
        </p>
        <div class="hero-tags">
          <span>查询趋势</span>
          <span>产品热度</span>
          <span>评分分布</span>
          <span>完整率监控</span>
          <span>异常反馈</span>
        </div>
      </div>
      <div class="hero-side">
        <div class="insight-panel">
          <div class="insight-title">监控重点</div>
          <div class="insight-list">
            <div v-for="item in insightList" :key="item.title" class="insight-item">
              <div class="insight-name">{{ item.title }}</div>
              <div class="insight-desc">{{ item.desc }}</div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <section class="metrics-grid">
      <div v-for="card in metricCards" :key="card.label" class="metric-card">
        <div class="metric-top">
          <span class="metric-label">{{ card.label }}</span>
          <span class="metric-badge">{{ card.badge }}</span>
        </div>
        <div class="metric-value">{{ card.value }}</div>
        <div class="metric-tip">{{ card.tip }}</div>
      </div>
    </section>

    <el-row :gutter="18" class="section-row">
      <el-col :xs="24" :xl="14">
        <el-card shadow="never" class="panel-card">
          <div slot="header" class="panel-header">
            <span>近 7 日溯源查询趋势</span>
            <span class="panel-subtitle">反映扫码热度变化</span>
          </div>
          <div ref="trendChart" class="chart-panel chart-tall" />
        </el-card>
      </el-col>
      <el-col :xs="24" :xl="10">
        <el-card shadow="never" class="panel-card">
          <div slot="header" class="panel-header">
            <span>热门产品查询排行</span>
            <span class="panel-subtitle">用于观察市场关注焦点</span>
          </div>
          <div ref="productChart" class="chart-panel chart-tall" />
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="18" class="section-row">
      <el-col :xs="24" :xl="9">
        <el-card shadow="never" class="panel-card">
          <div slot="header" class="panel-header">
            <span>消费者评分分布</span>
            <span class="panel-subtitle">衡量产品口碑表现</span>
          </div>
          <div ref="ratingChart" class="chart-panel" />
        </el-card>
      </el-col>
      <el-col :xs="24" :xl="8">
        <el-card shadow="never" class="panel-card">
          <div slot="header" class="panel-header">
            <span>追溯数据完整率</span>
            <span class="panel-subtitle">衡量链路记录是否完善</span>
          </div>
          <div ref="coverageChart" class="chart-panel" />
        </el-card>
      </el-col>
      <el-col :xs="24" :xl="7">
        <el-card shadow="never" class="panel-card stat-story-card">
          <div slot="header" class="panel-header">
            <span>运营解读</span>
            <span class="panel-subtitle">适合汇报时直接使用</span>
          </div>
          <div class="story-list">
            <div class="story-item">
              <span class="story-label">今日扫码活跃度</span>
              <strong>{{ overview.todayQueries || 0 }}</strong>
              <p>用于观察当天市场传播和终端扫码活跃度。</p>
            </div>
            <div class="story-item">
              <span class="story-label">风险反馈数量</span>
              <strong>{{ overview.suspiciousFeedback || 0 }}</strong>
              <p>优先关注可疑产品反馈，便于及时排查异常流通。</p>
            </div>
            <div class="story-item">
              <span class="story-label">平均评分</span>
              <strong>{{ averageRatingText }}</strong>
              <p>可作为产品体验和消费者满意度的直观指标。</p>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="18" class="section-row">
      <el-col :xs="24" :xl="11">
        <el-card shadow="never" class="panel-card health-card">
          <div slot="header" class="panel-header">
            <span>模块健康度</span>
            <span class="panel-subtitle">按业务模块查看覆盖情况</span>
          </div>
          <div v-for="item in normalizedCoverage" :key="item.displayName" class="coverage-item">
            <div class="coverage-head">
              <div>
                <div class="coverage-name">{{ item.displayName }}</div>
                <div class="coverage-meta">{{ item.covered }}/{{ item.total }} 个批次已覆盖</div>
              </div>
              <div class="coverage-rate">{{ item.rate }}%</div>
            </div>
            <el-progress
              :percentage="Number(item.rate)"
              :stroke-width="16"
              :color="item.color"
              :show-text="false"
            />
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :xl="13">
        <el-card shadow="never" class="panel-card">
          <div slot="header" class="panel-header">
            <span>近期可疑反馈</span>
            <span class="panel-subtitle">用于快速识别异常产品</span>
          </div>
          <el-table :data="recentSuspicious" border stripe class="table-panel">
            <el-table-column label="溯源码" prop="traceCode" min-width="150" />
            <el-table-column label="酒庄" prop="enterpriseName" min-width="140" />
            <el-table-column label="产品" prop="productName" min-width="140" />
            <el-table-column label="反馈内容" prop="content" min-width="230" show-overflow-tooltip />
            <el-table-column label="状态" width="100">
              <template slot-scope="scope">
                <el-tag :type="scope.row.status === '1' ? 'success' : 'warning'" size="mini">
                  {{ scope.row.status === '1' ? '已处理' : '待处理' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="反馈时间" prop="createTime" min-width="170" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'
require('echarts/theme/macarons')
import { getStatsDashboard } from '@/api/trace/stats'

const COVERAGE_PRESET = [
  { displayName: '工序记录', color: '#7c2d2d' },
  { displayName: '质检记录', color: '#c87d3a' },
  { displayName: '灌装记录', color: '#355e3b' },
  { displayName: '仓储记录', color: '#7b6a58' }
]

export default {
  name: 'TraceStats',
  data() {
    return {
      dashboard: {
        overview: {},
        queryTrend: [],
        topProducts: [],
        feedbackRatings: [],
        coverage: [],
        recentSuspicious: []
      },
      trendChart: null,
      productChart: null,
      ratingChart: null,
      coverageChart: null,
      insightList: [
        { title: '查询热度', desc: '观察近 7 日扫码趋势，判断市场传播和终端关注变化。' },
        { title: '产品关注度', desc: '结合热门产品排行，定位当前最受关注的产品线。' },
        { title: '风险线索', desc: '将可疑反馈单独聚合，方便企业后台快速响应处理。' }
      ]
    }
  },
  computed: {
    overview() {
      return this.dashboard.overview || {}
    },
    averageRatingText() {
      return Number(this.overview.averageRating || 0).toFixed(2)
    },
    metricCards() {
      return [
        { label: '累计查询', value: this.overview.totalQueries || 0, badge: '扫码热度', tip: '展示消费者累计发起溯源查询的总量。' },
        { label: '今日查询', value: this.overview.todayQueries || 0, badge: '今日动态', tip: '反映当天市场终端和消费者扫码的活跃程度。' },
        { label: '累计反馈', value: this.overview.totalFeedback || 0, badge: '互动量', tip: '包含评分、文字评价和可疑产品举报。' },
        { label: '可疑反馈', value: this.overview.suspiciousFeedback || 0, badge: '风险提醒', tip: '用于提醒管理员重点关注疑似异常产品线索。' },
        { label: '平均评分', value: this.averageRatingText, badge: '口碑指数', tip: '可用于快速判断整体消费反馈质量。' },
        { label: '生产批次', value: this.overview.batchCount || 0, badge: '数据基数', tip: '当前纳入溯源管理并用于统计的生产批次数量。' }
      ]
    },
    normalizedCoverage() {
      const list = this.dashboard.coverage || []
      return COVERAGE_PRESET.map((preset, index) => {
        const row = list[index] || {}
        return {
          displayName: preset.displayName,
          color: preset.color,
          covered: row.covered || 0,
          total: row.total || 0,
          rate: Number(row.rate || 0)
        }
      })
    },
    recentSuspicious() {
      return this.dashboard.recentSuspicious || []
    }
  },
  mounted() {
    this.loadDashboard()
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize)
    this.disposeCharts()
  },
  methods: {
    loadDashboard() {
      getStatsDashboard().then(response => {
        this.dashboard = response.data || {}
        this.$nextTick(() => {
          this.renderCharts()
        })
      })
    },
    renderCharts() {
      this.renderTrendChart()
      this.renderProductChart()
      this.renderRatingChart()
      this.renderCoverageChart()
    },
    renderTrendChart() {
      if (!this.$refs.trendChart) {
        return
      }
      if (!this.trendChart) {
        this.trendChart = echarts.init(this.$refs.trendChart, 'macarons')
      }
      const trend = this.dashboard.queryTrend || []
      this.trendChart.setOption({
        color: ['#8f2d2d'],
        tooltip: { trigger: 'axis' },
        grid: { left: 42, right: 20, top: 28, bottom: 28 },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: trend.map(item => item.label),
          axisLine: { lineStyle: { color: '#9f8c84' } }
        },
        yAxis: {
          type: 'value',
          splitLine: { lineStyle: { color: '#efe7df' } }
        },
        series: [{
          name: '查询次数',
          type: 'line',
          smooth: true,
          symbolSize: 8,
          data: trend.map(item => item.value),
          areaStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: 'rgba(143,45,45,0.35)' },
              { offset: 1, color: 'rgba(143,45,45,0.05)' }
            ])
          },
          lineStyle: { width: 3 }
        }]
      })
    },
    renderProductChart() {
      if (!this.$refs.productChart) {
        return
      }
      if (!this.productChart) {
        this.productChart = echarts.init(this.$refs.productChart, 'macarons')
      }
      const data = this.dashboard.topProducts || []
      this.productChart.setOption({
        color: ['#c87d3a'],
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
        grid: { left: 52, right: 20, top: 24, bottom: 60 },
        xAxis: {
          type: 'category',
          data: data.map(item => item.name),
          axisLabel: { interval: 0, rotate: 18, color: '#6c5a52' }
        },
        yAxis: {
          type: 'value',
          splitLine: { lineStyle: { color: '#efe7df' } }
        },
        series: [{
          name: '查询次数',
          type: 'bar',
          barWidth: '45%',
          data: data.map(item => item.value),
          itemStyle: {
            borderRadius: [8, 8, 0, 0]
          }
        }]
      })
    },
    renderRatingChart() {
      if (!this.$refs.ratingChart) {
        return
      }
      if (!this.ratingChart) {
        this.ratingChart = echarts.init(this.$refs.ratingChart, 'macarons')
      }
      const data = this.dashboard.feedbackRatings || []
      this.ratingChart.setOption({
        color: ['#7b3f00', '#c87d3a', '#d7a86e', '#6d8f72', '#355e3b'],
        tooltip: { trigger: 'item' },
        legend: { bottom: 0, textStyle: { color: '#6c5a52' } },
        series: [{
          name: '评分分布',
          type: 'pie',
          radius: ['42%', '72%'],
          center: ['50%', '42%'],
          label: { formatter: '{b}\n{d}%' },
          data
        }]
      })
    },
    renderCoverageChart() {
      if (!this.$refs.coverageChart) {
        return
      }
      if (!this.coverageChart) {
        this.coverageChart = echarts.init(this.$refs.coverageChart, 'macarons')
      }
      const list = this.normalizedCoverage
      this.coverageChart.setOption({
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
        grid: { left: 70, right: 24, top: 22, bottom: 24 },
        xAxis: {
          type: 'value',
          max: 100,
          splitLine: { lineStyle: { color: '#efe7df' } },
          axisLabel: { formatter: '{value}%' }
        },
        yAxis: {
          type: 'category',
          data: list.map(item => item.displayName),
          axisTick: { show: false },
          axisLine: { show: false }
        },
        series: [{
          name: '完整率',
          type: 'bar',
          barWidth: 16,
          data: list.map(item => ({
            value: item.rate,
            itemStyle: {
              color: item.color,
              borderRadius: 10
            }
          }))
        }]
      })
    },
    handleResize() {
      this.trendChart && this.trendChart.resize()
      this.productChart && this.productChart.resize()
      this.ratingChart && this.ratingChart.resize()
      this.coverageChart && this.coverageChart.resize()
    },
    disposeCharts() {
      this.trendChart && this.trendChart.dispose()
      this.productChart && this.productChart.dispose()
      this.ratingChart && this.ratingChart.dispose()
      this.coverageChart && this.coverageChart.dispose()
      this.trendChart = null
      this.productChart = null
      this.ratingChart = null
      this.coverageChart = null
    }
  }
}
</script>

<style scoped>
.trace-stats-page {
  --wine: #7c2d2d;
  --wine-deep: #4c1d1d;
  --barrel: #c87d3a;
  --leaf: #355e3b;
  --line: #e8ddd2;
  background:
    radial-gradient(circle at top right, rgba(201, 133, 66, 0.16), transparent 28%),
    radial-gradient(circle at left bottom, rgba(124, 45, 45, 0.12), transparent 24%),
    linear-gradient(180deg, #fcfaf7 0%, #f8f3ed 100%);
  min-height: calc(100vh - 84px);
}

.hero-panel {
  display: grid;
  grid-template-columns: minmax(0, 1.4fr) minmax(320px, 0.9fr);
  gap: 18px;
  margin-bottom: 18px;
}

.hero-copy,
.insight-panel {
  border-radius: 24px;
  padding: 28px;
  min-height: 268px;
}

.hero-copy {
  color: #fff8f3;
  background:
    linear-gradient(135deg, rgba(76, 29, 29, 0.98), rgba(124, 45, 45, 0.92) 60%, rgba(162, 63, 44, 0.9)),
    linear-gradient(180deg, #7c2d2d, #4c1d1d);
  box-shadow: 0 16px 40px rgba(94, 31, 31, 0.18);
}

.hero-kicker {
  margin: 0 0 12px;
  font-size: 12px;
  letter-spacing: 0.18em;
  text-transform: uppercase;
  color: rgba(255, 248, 243, 0.72);
}

.hero-copy h1 {
  margin: 0;
  font-size: 32px;
  line-height: 1.25;
  font-weight: 700;
}

.hero-desc {
  margin: 18px 0 22px;
  font-size: 15px;
  line-height: 1.9;
  color: rgba(255, 248, 243, 0.88);
}

.hero-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.hero-tags span {
  padding: 8px 14px;
  border-radius: 999px;
  border: 1px solid rgba(255, 255, 255, 0.14);
  background: rgba(255, 255, 255, 0.08);
  font-size: 12px;
}

.insight-panel {
  background: linear-gradient(180deg, #fffdfb, #f7f0e8);
  border: 1px solid rgba(124, 45, 45, 0.08);
  box-shadow: 0 16px 40px rgba(83, 53, 34, 0.08);
}

.insight-title {
  margin-bottom: 18px;
  font-size: 18px;
  font-weight: 700;
  color: #4e3027;
}

.insight-list {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.insight-item {
  padding: 14px 16px;
  border-radius: 16px;
  background: linear-gradient(180deg, #fffdfa, #f9f2ea);
  border: 1px solid var(--line);
}

.insight-name {
  margin-bottom: 6px;
  font-size: 14px;
  font-weight: 600;
  color: #4b3127;
}

.insight-desc {
  font-size: 12px;
  line-height: 1.7;
  color: #816b61;
}

.metrics-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(210px, 1fr));
  gap: 16px;
  margin-bottom: 18px;
}

.metric-card {
  border-radius: 20px;
  padding: 20px 20px 18px;
  background: rgba(255, 255, 255, 0.88);
  border: 1px solid rgba(124, 45, 45, 0.08);
  box-shadow: 0 10px 30px rgba(67, 42, 25, 0.06);
}

.metric-top {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 14px;
}

.metric-label {
  font-size: 13px;
  color: #7b685f;
}

.metric-badge {
  padding: 4px 10px;
  border-radius: 999px;
  font-size: 12px;
  color: var(--wine);
  background: rgba(124, 45, 45, 0.08);
}

.metric-value {
  font-size: 34px;
  line-height: 1.1;
  font-weight: 700;
  color: #2f231f;
}

.metric-tip {
  margin-top: 12px;
  font-size: 12px;
  line-height: 1.7;
  color: #8b766d;
}

.section-row {
  margin-bottom: 18px;
}

.panel-card {
  border: none;
  border-radius: 20px;
  overflow: hidden;
  background: rgba(255, 255, 255, 0.92);
  box-shadow: 0 14px 32px rgba(71, 49, 35, 0.06);
}

.panel-card /deep/ .el-card__body {
  padding: 20px;
}

.panel-header {
  display: flex;
  justify-content: space-between;
  align-items: baseline;
  gap: 12px;
  font-weight: 600;
  color: #382620;
}

.panel-subtitle {
  font-size: 12px;
  color: #9a8479;
}

.chart-panel {
  height: 300px;
}

.chart-tall {
  height: 340px;
}

.stat-story-card {
  min-height: 100%;
}

.story-list {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.story-item {
  padding: 16px 18px;
  border-radius: 16px;
  background: linear-gradient(180deg, #fffdfa, #f9f2ea);
  border: 1px solid var(--line);
}

.story-label {
  display: block;
  margin-bottom: 10px;
  font-size: 12px;
  color: #8f796f;
}

.story-item strong {
  display: block;
  margin-bottom: 8px;
  font-size: 28px;
  color: #3d2821;
}

.story-item p {
  margin: 0;
  font-size: 12px;
  line-height: 1.8;
  color: #7e6a60;
}

.health-card {
  min-height: 100%;
}

.coverage-item + .coverage-item {
  margin-top: 18px;
}

.coverage-head {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
  gap: 12px;
}

.coverage-name {
  font-size: 14px;
  font-weight: 600;
  color: #4b3127;
}

.coverage-meta {
  margin-top: 4px;
  font-size: 12px;
  color: #8a7268;
}

.coverage-rate {
  font-size: 20px;
  font-weight: 700;
  color: #35241e;
}

.table-panel /deep/ .el-table__header-wrapper th {
  background: #fbf5ef;
  color: #5b463d;
}

@media (max-width: 1280px) {
  .hero-panel {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .hero-copy,
  .insight-panel {
    padding: 22px;
    min-height: auto;
  }

  .hero-copy h1 {
    font-size: 26px;
  }
}
</style>
