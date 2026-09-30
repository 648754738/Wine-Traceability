<template>
  <div class="app-container trace-home compact-home">
    <section class="hero-panel single-hero">
      <div class="hero-layout">
        <div class="hero-copy compact-hero">
          <p class="hero-kicker">Ningxia Wine Overview</p>
          <h1>宁夏葡萄酒信息总览</h1>
          <p class="hero-desc">
            首页集中展示产区、酒庄、地块、葡萄品种、产品类型和关键业务入口，方便快速了解系统基础信息与主要模块分布。
          </p>
          <div class="hero-tags">
            <span>产区层级</span>
            <span>酒庄分布</span>
            <span>地块风土</span>
            <span>葡萄品种</span>
            <span>产品种类</span>
            <span>业务入口</span>
          </div>
        </div>
        <el-card shadow="never" class="panel-card compact-panel hero-entry-card">
          <div slot="header" class="panel-header">
            <span>快速入口</span>
            <span class="panel-subtitle">常用模块一键直达</span>
          </div>
          <div class="quick-grid compact-grid hero-quick-grid">
            <div
              v-for="action in quickActions"
              :key="action.title"
              class="quick-item compact-quick hero-quick-item"
              @click="goPage(action.path)"
            >
              <div class="quick-title">{{ action.title }}</div>
              <div class="quick-desc">{{ action.desc }}</div>
            </div>
          </div>
        </el-card>
      </div>
    </section>

    <section class="metrics-grid compact-metrics">
      <div v-for="card in summaryCards" :key="card.label" class="metric-card compact-card">
        <div class="metric-top">
          <span class="metric-label">{{ card.label }}</span>
          <span class="metric-badge">{{ card.badge }}</span>
        </div>
        <div class="metric-value">{{ card.value }}</div>
        <div class="metric-tip">{{ card.tip }}</div>
      </div>
    </section>

    <el-row :gutter="12" class="section-row tight-row grid-row">
      <el-col :xs="24" :lg="12" class="grid-col">
        <el-card shadow="never" class="panel-card compact-panel">
          <div slot="header" class="panel-header">
            <span>产区覆盖情况</span>
            <span class="panel-subtitle">按地块数量查看重点产区</span>
          </div>
          <div ref="regionChart" class="chart-panel chart-medium" />
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="12" class="grid-col">
        <el-card shadow="never" class="panel-card compact-panel">
          <div slot="header" class="panel-header">
            <span>产品种类分布</span>
            <span class="panel-subtitle">按类别观察产品结构</span>
          </div>
          <div ref="categoryChart" class="chart-panel chart-medium" />
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="12" class="section-row tight-row grid-row">
      <el-col :xs="24" :sm="12" :lg="6" class="grid-col">
        <el-card shadow="never" class="panel-card compact-panel small-panel">
          <div slot="header" class="panel-header">
            <span>产区路径</span>
            <span class="panel-subtitle">系统中的产区层级</span>
          </div>
          <div class="region-paths compact-paths" v-if="regionPaths.length">
            <div v-for="path in regionPaths" :key="path" class="path-chip">{{ path }}</div>
          </div>
          <el-empty v-else description="暂无产区数据" :image-size="72" />
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :lg="6" class="grid-col">
        <el-card shadow="never" class="panel-card compact-panel small-panel">
          <div slot="header" class="panel-header">
            <span>代表酒庄</span>
            <span class="panel-subtitle">按产区查看酒庄</span>
          </div>
          <div v-if="featuredEnterprises.length" class="winery-list compact-list">
            <div v-for="item in featuredEnterprises" :key="item.enterpriseId" class="winery-item compact-item">
              <div class="winery-title">{{ item.enterpriseName }}</div>
              <div class="winery-meta">{{ item.regionName || '未设置产区' }}</div>
              <div class="winery-address">{{ item.address || '暂无地址信息' }}</div>
            </div>
          </div>
          <el-empty v-else description="暂无酒庄数据" :image-size="72" />
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :lg="6" class="grid-col">
        <el-card shadow="never" class="panel-card compact-panel small-panel">
          <div slot="header" class="panel-header">
            <span>葡萄品种</span>
            <span class="panel-subtitle">快速浏览主要品种</span>
          </div>
          <div v-if="featuredVarieties.length" class="variety-cloud compact-cloud">
            <div v-for="item in featuredVarieties" :key="item.varietyId" class="variety-chip compact-variety">
              <span class="variety-name">{{ item.varietyName }}</span>
              <span class="variety-en">{{ item.englishName || 'Wine Grape' }}</span>
            </div>
          </div>
          <el-empty v-else description="暂无品种数据" :image-size="72" />
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :lg="6" class="grid-col">
        <el-card shadow="never" class="panel-card compact-panel small-panel">
          <div slot="header" class="panel-header">
            <span>产品类型</span>
            <span class="panel-subtitle">首页重点展示的产品类别</span>
          </div>
          <div v-if="categoryList.length" class="category-list compact-category-list">
            <div v-for="item in categoryList" :key="item.name" class="category-item compact-category-item">
              <span>{{ item.name }}</span>
              <strong>{{ item.value }}</strong>
            </div>
          </div>
          <el-empty v-else description="暂无产品类型数据" :image-size="72" />
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="12" class="section-row tight-row grid-row">
      <el-col :xs="24" :lg="24" class="grid-col">
        <el-card shadow="never" class="panel-card compact-panel">
          <div slot="header" class="panel-header">
            <span>代表产地地块</span>
            <span class="panel-subtitle">突出地块名称、风土和主要品种</span>
          </div>
          <el-table :data="featuredPlots" border stripe class="table-panel compact-table">
            <el-table-column label="地块名称" prop="plotName" min-width="130" />
            <el-table-column label="所属酒庄" prop="enterpriseName" min-width="120" />
            <el-table-column label="所属产区" prop="regionName" min-width="130" />
            <el-table-column label="面积(公顷)" prop="areaHa" min-width="90" />
            <el-table-column label="土壤类型" prop="soilType" min-width="110" />
            <el-table-column label="主要品种" prop="plantedVariety" min-width="110" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'
require('echarts/theme/macarons')
import { listRegion } from '@/api/trace/region'
import { listEnterprise } from '@/api/trace/enterprise'
import { listPlot } from '@/api/trace/plot'
import { listVariety } from '@/api/trace/variety'
import { listProduct } from '@/api/trace/product'

export default {
  name: 'Index',
  data() {
    return {
      regionTree: [],
      enterpriseList: [],
      plotList: [],
      varietyList: [],
      productList: [],
      regionChart: null,
      categoryChart: null,
      quickActions: [
        { title: '产区管理', desc: '维护多级产区层级', path: '/trace/region' },
        { title: '酒庄管理', desc: '查看酒庄与所属产区', path: '/trace/enterprise' },
        { title: '地块管理', desc: '维护产地坐标与风土', path: '/trace/plot' },
        { title: '产品管理', desc: '查看产品类别与配方', path: '/trace/product' }
      ]
    }
  },
  computed: {
    flattenedRegions() {
      return this.flattenRegionTree(this.regionTree)
    },
    regionPaths() {
      return this.flattenedRegions.filter(item => item.path).map(item => item.path).slice(0, 12)
    },
    summaryCards() {
      return [
        { label: '产区数量', value: this.flattenedRegions.length, badge: 'Region', tip: '系统中已维护的产区层级节点总数。' },
        { label: '酒庄数量', value: this.enterpriseList.length, badge: 'Winery', tip: '已纳入系统管理的酒庄主体数量。' },
        { label: '地块数量', value: this.plotList.length, badge: 'Plot', tip: '可追溯的葡萄种植地块总数。' },
        { label: '葡萄品种', value: this.varietyList.length, badge: 'Variety', tip: '系统中已维护的葡萄品种种类数量。' },
        { label: '产品数量', value: this.productList.length, badge: 'Product', tip: '酒庄已定义的产品总数。' },
        { label: '产品类别', value: this.categoryList.length, badge: 'Category', tip: '根据产品类别自动归纳出的种类数量。' }
      ]
    },
    featuredEnterprises() {
      return this.enterpriseList.slice(0, 5)
    },
    featuredVarieties() {
      return this.varietyList.slice(0, 8)
    },
    featuredPlots() {
      return this.plotList.slice(0, 8)
    },
    categoryList() {
      const map = {}
      this.productList.forEach(item => {
        const key = item.category || '未分类'
        map[key] = (map[key] || 0) + 1
      })
      return Object.keys(map).map(key => ({ name: key, value: map[key] })).sort((a, b) => b.value - a.value)
    },
    regionPlotList() {
      const map = {}
      this.plotList.forEach(item => {
        const key = item.regionName || '未设置产区'
        map[key] = (map[key] || 0) + 1
      })
      return Object.keys(map).map(key => ({ name: key, value: map[key] })).sort((a, b) => b.value - a.value).slice(0, 8)
    }
  },
  mounted() {
    this.loadHomeData()
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize)
    this.disposeCharts()
  },
  methods: {
    loadHomeData() {
      Promise.all([
        listRegion(),
        listEnterprise({ pageNum: 1, pageSize: 999 }),
        listPlot({ pageNum: 1, pageSize: 999 }),
        listVariety({ pageNum: 1, pageSize: 999 }),
        listProduct({ pageNum: 1, pageSize: 999 })
      ]).then(([regionRes, enterpriseRes, plotRes, varietyRes, productRes]) => {
        this.regionTree = regionRes.data || []
        this.enterpriseList = enterpriseRes.rows || []
        this.plotList = plotRes.rows || []
        this.varietyList = varietyRes.rows || []
        this.productList = productRes.rows || []
        this.$nextTick(() => {
          this.renderCharts()
        })
      })
    },
    flattenRegionTree(nodes, parentPath = '') {
      let result = []
      ;(nodes || []).forEach(node => {
        const path = parentPath ? parentPath + ' / ' + node.regionName : node.regionName
        result.push({
          regionId: node.regionId,
          regionName: node.regionName,
          path
        })
        if (node.children && node.children.length) {
          result = result.concat(this.flattenRegionTree(node.children, path))
        }
      })
      return result
    },
    renderCharts() {
      this.renderRegionChart()
      this.renderCategoryChart()
    },
    renderRegionChart() {
      if (!this.$refs.regionChart) {
        return
      }
      if (!this.regionChart) {
        this.regionChart = echarts.init(this.$refs.regionChart, 'macarons')
      }
      const list = this.regionPlotList
      this.regionChart.setOption({
        color: ['#7c2d2d'],
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
        grid: { left: 56, right: 20, top: 20, bottom: 24 },
        xAxis: {
          type: 'value',
          splitLine: { lineStyle: { color: '#efe7df' } }
        },
        yAxis: {
          type: 'category',
          data: list.map(item => item.name),
          axisTick: { show: false },
          axisLine: { show: false }
        },
        series: [{
          name: '地块数量',
          type: 'bar',
          barWidth: 16,
          data: list.map(item => item.value),
          itemStyle: {
            borderRadius: 10
          }
        }]
      })
    },
    renderCategoryChart() {
      if (!this.$refs.categoryChart) {
        return
      }
      if (!this.categoryChart) {
        this.categoryChart = echarts.init(this.$refs.categoryChart, 'macarons')
      }
      const list = this.categoryList
      this.categoryChart.setOption({
        color: ['#7b3f00', '#c87d3a', '#d7a86e', '#6d8f72', '#355e3b', '#9f8c84'],
        tooltip: { trigger: 'item' },
        legend: { bottom: 0, textStyle: { color: '#6c5a52' } },
        series: [{
          name: '产品类别',
          type: 'pie',
          radius: ['42%', '70%'],
          center: ['50%', '40%'],
          label: { formatter: '{b}\n{d}%' },
          data: list
        }]
      })
    },
    handleResize() {
      this.regionChart && this.regionChart.resize()
      this.categoryChart && this.categoryChart.resize()
    },
    disposeCharts() {
      this.regionChart && this.regionChart.dispose()
      this.categoryChart && this.categoryChart.dispose()
      this.regionChart = null
      this.categoryChart = null
    },
    goPage(path) {
      this.$router.push(path)
    }
  }
}
</script>

<style scoped>
.trace-home {
  --wine: #7c2d2d;
  --barrel: #c98542;
  --line: #e8ddd2;
  background:
    radial-gradient(circle at top right, rgba(201, 133, 66, 0.16), transparent 28%),
    radial-gradient(circle at left bottom, rgba(124, 45, 45, 0.12), transparent 24%),
    linear-gradient(180deg, #fcfaf7 0%, #f8f3ed 100%);
  min-height: calc(100vh - 84px);
}

.single-hero {
  margin-bottom: 12px;
}

.hero-layout {
  display: grid;
  grid-template-columns: minmax(0, 1.45fr) minmax(360px, 0.95fr);
  gap: 12px;
  align-items: stretch;
}

.compact-hero {
  min-height: auto;
  padding: 24px 28px;
  border-radius: 22px;
}

.hero-copy {
  color: #fff8f3;
  background:
    linear-gradient(135deg, rgba(76, 29, 29, 0.98), rgba(124, 45, 45, 0.92) 60%, rgba(162, 63, 44, 0.9)),
    linear-gradient(180deg, #7c2d2d, #4c1d1d);
  box-shadow: 0 16px 40px rgba(94, 31, 31, 0.18);
}

.hero-kicker {
  margin: 0 0 10px;
  font-size: 12px;
  letter-spacing: 0.18em;
  text-transform: uppercase;
  color: rgba(255, 248, 243, 0.72);
}

.hero-copy h1 {
  margin: 0;
  font-size: 30px;
  line-height: 1.2;
  font-weight: 700;
}

.hero-desc {
  max-width: 760px;
  margin: 14px 0 18px;
  font-size: 14px;
  line-height: 1.8;
  color: rgba(255, 248, 243, 0.88);
}

.hero-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.hero-tags span {
  padding: 7px 12px;
  border-radius: 999px;
  border: 1px solid rgba(255, 255, 255, 0.14);
  background: rgba(255, 255, 255, 0.08);
  font-size: 12px;
}

.compact-metrics {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
  gap: 12px;
  margin-bottom: 12px;
}

.compact-card {
  padding: 16px 16px 14px;
  border-radius: 18px;
}

.metric-card {
  background: rgba(255, 255, 255, 0.9);
  border: 1px solid rgba(124, 45, 45, 0.08);
  box-shadow: 0 10px 30px rgba(67, 42, 25, 0.06);
}

.metric-top {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.metric-label {
  font-size: 13px;
  color: #7b685f;
}

.metric-badge {
  padding: 3px 9px;
  border-radius: 999px;
  font-size: 11px;
  color: var(--wine);
  background: rgba(124, 45, 45, 0.08);
}

.metric-value {
  font-size: 30px;
  line-height: 1.1;
  font-weight: 700;
  color: #2f231f;
}

.metric-tip {
  margin-top: 8px;
  font-size: 12px;
  line-height: 1.6;
  color: #8b766d;
}

.tight-row {
  margin-bottom: 12px;
}

.grid-row {
  display: flex;
  flex-wrap: wrap;
}

.grid-col {
  display: flex;
  margin-bottom: 12px;
}

.panel-card {
  border: none;
  border-radius: 18px;
  overflow: hidden;
  background: rgba(255, 255, 255, 0.94);
  box-shadow: 0 12px 28px rgba(71, 49, 35, 0.06);
}

.compact-panel /deep/ .el-card__body,
.panel-card /deep/ .el-card__body {
  padding: 16px;
}

.compact-panel {
  width: 100%;
}

.panel-header {
  display: flex;
  justify-content: space-between;
  align-items: baseline;
  gap: 10px;
  font-weight: 600;
  color: #382620;
}

.panel-subtitle {
  font-size: 12px;
  color: #9a8479;
}

.chart-medium {
  height: 260px;
}

.small-panel {
  height: 100%;
}

.small-panel /deep/ .el-card__body {
  height: calc(100% - 56px);
  display: flex;
  flex-direction: column;
}

.fill-card {
  height: 100%;
}

.hero-entry-card {
  height: 100%;
}

.hero-entry-card /deep/ .el-card__body {
  display: flex;
  flex: 1;
  flex-direction: column;
}

.compact-paths {
  gap: 10px;
  align-content: flex-start;
}

.path-chip {
  padding: 8px 12px;
  border-radius: 12px;
  background: linear-gradient(180deg, #fffdfa, #f9f2ea);
  border: 1px solid var(--line);
  color: #5a4338;
  font-size: 12px;
  line-height: 1.55;
}

.region-paths,
.variety-cloud {
  display: flex;
  flex-wrap: wrap;
}

.compact-list {
  display: grid;
  gap: 10px;
  align-content: flex-start;
}

.compact-item {
  padding: 12px 14px;
  border-radius: 14px;
  background: linear-gradient(180deg, #fffdfa, #f9f2ea);
  border: 1px solid var(--line);
}

.winery-title {
  font-size: 14px;
  font-weight: 600;
  color: #452d25;
}

.winery-meta {
  margin-top: 5px;
  font-size: 12px;
  color: #8d776c;
}

.winery-address {
  margin-top: 6px;
  font-size: 12px;
  line-height: 1.6;
  color: #7b665c;
}

.compact-cloud {
  gap: 10px;
  align-content: flex-start;
}

.compact-variety {
  min-width: 110px;
  padding: 12px;
  border-radius: 14px;
  background: linear-gradient(180deg, #fff8f1, #f8efe4);
  border: 1px solid rgba(201, 133, 66, 0.18);
}

.variety-name {
  display: block;
  font-size: 13px;
  font-weight: 600;
  color: #4f3127;
}

.variety-en {
  display: block;
  margin-top: 5px;
  font-size: 11px;
  color: #8b7368;
}

.table-panel /deep/ .el-table__header-wrapper th {
  background: #fbf5ef;
  color: #5b463d;
}

.compact-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 10px;
}

.hero-quick-grid {
  grid-template-columns: 1fr 1fr;
  align-content: flex-start;
}

.single-column-grid {
  grid-template-columns: 1fr;
}

.compact-quick {
  min-height: 84px;
  padding: 14px;
  border-radius: 14px;
  cursor: pointer;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
  background: linear-gradient(180deg, #fff8f1, #f8efe4);
  border: 1px solid rgba(201, 133, 66, 0.18);
}

.compact-quick:hover {
  transform: translateY(-2px);
  box-shadow: 0 10px 18px rgba(105, 69, 39, 0.08);
}

.hero-quick-item {
  min-height: 78px;
}

.quick-title {
  margin-bottom: 6px;
  font-size: 14px;
  font-weight: 600;
  color: #4b3127;
}

.quick-desc {
  font-size: 12px;
  line-height: 1.6;
  color: #8a7167;
}

.compact-category-list {
  display: grid;
  gap: 10px;
  align-content: flex-start;
}

.compact-category-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 12px;
  border-radius: 12px;
  background: linear-gradient(180deg, #fffdfa, #f9f2ea);
  border: 1px solid var(--line);
  color: #5a4338;
}

.compact-category-item strong {
  color: #3e2a24;
  font-size: 16px;
}

@media (max-width: 768px) {
  .hero-layout {
    grid-template-columns: 1fr;
  }

  .compact-hero {
    padding: 20px;
  }

  .hero-copy h1 {
    font-size: 24px;
  }

  .compact-grid {
    grid-template-columns: 1fr;
  }

  .grid-col {
    margin-bottom: 10px;
  }
}
</style>
