<template>
  <div class="app-container trace-query-page">
    <el-card shadow="never" class="query-hero">
      <div class="query-header">
        <div class="query-copy">
          <p class="query-kicker">Traceability Explorer</p>
          <h2>单瓶葡萄酒全链路溯源查询</h2>
          <p>
            通过溯源码还原这瓶酒从地块、酿酒车间、橡木桶到灌装、仓储和物流的关键节点，让系统展示更直观、更有讲述性。
          </p>
        </div>
        <div class="query-search">
          <el-form :inline="true" :model="form" @submit.native.prevent>
            <el-form-item>
              <el-input
                v-model="form.traceCode"
                clearable
                size="medium"
                class="trace-input"
                placeholder="请输入溯源码，如 YSH100001"
                @keyup.enter.native="handleQuery"
              />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" size="medium" icon="el-icon-search" :loading="loading" @click="handleQuery">
                查询溯源
              </el-button>
            </el-form-item>
          </el-form>
          <div class="demo-row">
            <span class="demo-label">示例码</span>
            <el-button v-for="code in demoCodes" :key="code" size="mini" plain @click="fillDemo(code)">{{ code }}</el-button>
          </div>
        </div>
      </div>
    </el-card>

    <div v-if="traceInfo" class="result-wrapper">
      <section class="summary-banner">
        <div class="banner-main">
          <div class="banner-label">当前溯源码</div>
          <div class="banner-code">{{ traceInfo.traceCode }}</div>
          <div class="banner-product">
            <span>{{ traceInfo.productName || '未命名产品' }}</span>
            <el-tag size="mini" :type="traceInfo.isQualified === '1' ? 'success' : 'warning'">
              {{ traceInfo.isQualified === '1' ? '质检合格' : '待复核' }}
            </el-tag>
          </div>
          <div class="banner-meta">
            <span>{{ traceInfo.enterpriseName || '未设置酒庄' }}</span>
            <span>{{ traceInfo.regionPath || '未设置产区路径' }}</span>
          </div>
        </div>
        <div class="banner-side">
          <div class="summary-mini">
            <span>批次号</span>
            <strong>{{ traceInfo.batchNo || '--' }}</strong>
          </div>
          <div class="summary-mini">
            <span>灌装日期</span>
            <strong>{{ formatDate(traceInfo.bottlingDate) }}</strong>
          </div>
          <div class="summary-mini">
            <span>瓶型规格</span>
            <strong>{{ traceInfo.bottleSpec || '--' }}</strong>
          </div>
          <div class="summary-mini">
            <span>来源容器</span>
            <strong>{{ traceInfo.sourceContainerCode || '--' }}</strong>
          </div>
        </div>
      </section>

      <section class="snapshot-grid">
        <div v-for="card in snapshotCards" :key="card.label" class="snapshot-card">
          <div class="snapshot-label">{{ card.label }}</div>
          <div class="snapshot-value">{{ card.value }}</div>
          <div class="snapshot-tip">{{ card.tip }}</div>
        </div>
      </section>

      <el-row :gutter="16" class="showcase-row">
        <el-col :xs="24" :lg="10" :xl="10">
          <el-card shadow="never" class="panel-card timeline-card showcase-card">
            <div slot="header" class="panel-header">
              <span>全链路时间线</span>
              <span class="panel-subtitle">按时间顺序串起一瓶酒的形成过程</span>
            </div>
            <el-timeline class="trace-timeline">
              <el-timeline-item
                v-for="item in timelineItems"
                :key="item.key"
                :timestamp="item.timeLabel"
                :type="item.type"
                placement="top"
              >
                <div class="timeline-title">{{ item.title }}</div>
                <div class="timeline-desc">{{ item.desc }}</div>
              </el-timeline-item>
            </el-timeline>
          </el-card>
        </el-col>
        <el-col :xs="24" :sm="12" :lg="7" :xl="7">
          <el-card shadow="never" class="panel-card metric-card showcase-card">
            <div slot="header" class="panel-header">
              <span>工序耗时图</span>
              <span class="panel-subtitle">帮助展示酿造步骤节奏</span>
            </div>
            <div ref="processChart" class="chart-panel" />
          </el-card>
        </el-col>
        <el-col :xs="24" :sm="12" :lg="7" :xl="7">
          <el-card shadow="never" class="panel-card metric-card showcase-card">
            <div slot="header" class="panel-header">
              <span>仓储流转图</span>
              <span class="panel-subtitle">查看入库出库数量变化</span>
            </div>
            <div ref="warehouseChart" class="chart-panel" />
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="16" class="section-row">
        <el-col :xs="24" :md="12" :xl="12">
          <el-card shadow="never" class="panel-card">
            <div slot="header" class="panel-header">
              <span>基础信息</span>
              <span class="panel-subtitle">产区、地块、车间和质检概览</span>
            </div>
            <div class="info-grid">
              <div class="info-item"><span>产区路径</span><strong>{{ traceInfo.regionPath || '--' }}</strong></div>
              <div class="info-item"><span>酒庄名称</span><strong>{{ traceInfo.enterpriseName || '--' }}</strong></div>
              <div class="info-item"><span>地块名称</span><strong>{{ traceInfo.plotName || '--' }}</strong></div>
              <div class="info-item"><span>GPS 坐标</span><strong>{{ traceInfo.gpsCoordinates || '--' }}</strong></div>
              <div class="info-item"><span>酿酒车间</span><strong>{{ traceInfo.workshopName || '--' }}</strong></div>
              <div class="info-item"><span>进入车间时间</span><strong>{{ formatDateTime(traceInfo.entryWorkshopTime) }}</strong></div>
              <div class="info-item"><span>检测日期</span><strong>{{ formatDate(traceInfo.inspectionDate) }}</strong></div>
              <div class="info-item"><span>检测机构</span><strong>{{ traceInfo.inspector || '--' }}</strong></div>
            </div>
          </el-card>
        </el-col>
        <el-col :xs="24" :md="12" :xl="12">
          <el-card shadow="never" class="panel-card">
            <div slot="header" class="panel-header">
              <span>灌装与物流摘要</span>
              <span class="panel-subtitle">灌装节点和销售流向快速查看</span>
            </div>
            <div class="info-grid">
              <div class="info-item"><span>来源容器</span><strong>{{ traceInfo.sourceContainerCode || '--' }}</strong></div>
              <div class="info-item"><span>容器类型</span><strong>{{ traceInfo.sourceContainerType || '--' }}</strong></div>
              <div class="info-item"><span>容器位置</span><strong>{{ traceInfo.sourceLocationDetail || '--' }}</strong></div>
              <div class="info-item"><span>灌装日期</span><strong>{{ formatDate(traceInfo.bottlingDate) }}</strong></div>
              <div class="info-item"><span>瓶型规格</span><strong>{{ traceInfo.bottleSpec || '--' }}</strong></div>
              <div class="info-item"><span>瓶盖码</span><strong>{{ traceInfo.capCode || '--' }}</strong></div>
              <div class="info-item"><span>仓储节点数</span><strong>{{ (traceInfo.warehouseEvents || []).length }}</strong></div>
              <div class="info-item"><span>当前物流状态</span><strong>{{ latestWarehouseText }}</strong></div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="16" class="section-row">
        <el-col :xs="24" :md="12" :xl="8">
          <el-card shadow="never" class="panel-card table-panel">
            <div slot="header" class="panel-header">
              <span>工序明细</span>
              <span class="panel-subtitle">从发酵到陈酿的关键步骤</span>
            </div>
            <div class="table-wrap">
              <el-table :data="traceInfo.processList || []" border stripe size="mini" class="compact-table" max-height="360">
                <el-table-column label="工序类型" prop="processType" min-width="120" />
                <el-table-column label="开始时间" prop="startTime" min-width="160" />
                <el-table-column label="结束时间" prop="endTime" min-width="160" />
                <el-table-column label="温控范围" prop="tempControl" min-width="120" />
                <el-table-column label="操作人" prop="operatorName" min-width="100" />
              </el-table>
            </div>
          </el-card>
        </el-col>
        <el-col :xs="24" :md="12" :xl="8">
          <el-card shadow="never" class="panel-card table-panel">
            <div slot="header" class="panel-header">
              <span>容器流转明细</span>
              <span class="panel-subtitle">查看桶罐使用路径和装入量</span>
            </div>
            <div class="table-wrap">
              <el-table :data="traceInfo.usageList || []" border stripe size="mini" class="compact-table" max-height="360">
                <el-table-column label="容器编号" prop="containerCode" min-width="120" />
                <el-table-column label="容器类型" prop="containerType" min-width="120" />
                <el-table-column label="入桶时间" prop="startTime" min-width="160" />
                <el-table-column label="出桶时间" prop="endTime" min-width="160" />
                <el-table-column label="装入量(L)" prop="fillQuantityL" min-width="110" />
              </el-table>
            </div>
          </el-card>
        </el-col>
        <el-col :xs="24" :xl="8">
          <el-card shadow="never" class="panel-card warehouse-panel">
            <div slot="header" class="panel-header">
              <span>仓储与物流节点</span>
              <span class="panel-subtitle">按节点展示单据、数量和物流去向</span>
            </div>
            <div v-if="(traceInfo.warehouseEvents || []).length" class="warehouse-grid">
              <div v-for="(item, index) in traceInfo.warehouseEvents" :key="item.billNo || index" class="warehouse-record">
                <div class="warehouse-record-head">
                  <el-tag size="mini" :type="item.billType === '出库' ? 'danger' : 'success'">
                    {{ item.billType || '仓储节点' }}
                  </el-tag>
                  <span class="warehouse-time">{{ formatDateTime(item.operateTime || item.shipDate) }}</span>
                </div>
                <div class="warehouse-title">{{ item.billNo || '未生成单据编号' }}</div>
                <div class="warehouse-meta">
                  <span>数量 {{ item.quantity || 0 }}</span>
                  <span v-if="item.carrier">{{ item.carrier }}</span>
                </div>
                <div class="warehouse-desc">
                  <span v-if="item.destination">目的地：{{ item.destination }}</span>
                  <span v-if="item.trackingNo">运单号：{{ item.trackingNo }}</span>
                </div>
              </div>
            </div>
            <el-empty v-else :image-size="76" description="暂无仓储物流节点" />
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="16" class="section-row">
        <el-col :xs="24" :xl="24">
          <el-card shadow="never" class="panel-card feedback-card feedback-bottom-card">
            <div slot="header" class="panel-header">
              <span>消费者反馈</span>
              <span class="panel-subtitle">支持评分、评价和异常举报</span>
            </div>
            <el-form ref="feedbackForm" :model="feedbackForm" label-width="74px" size="small" class="compact-feedback-form">
              <el-form-item label="评分">
                <el-rate v-model="feedbackForm.rating" :max="5" />
              </el-form-item>
              <el-form-item label="反馈内容">
                <el-input v-model="feedbackForm.content" type="textarea" :rows="5" placeholder="请输入评价内容或异常情况说明" />
              </el-form-item>
              <el-form-item label="可疑产品">
                <el-switch
                  v-model="feedbackForm.isSuspicious"
                  active-value="1"
                  inactive-value="0"
                  active-text="是"
                  inactive-text="否"
                />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" :disabled="!traceInfo.traceCode" @click="submitFeedback">提交反馈</el-button>
                <el-button @click="resetFeedback">重置</el-button>
              </el-form-item>
            </el-form>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <el-empty v-else class="query-empty" description="输入溯源码后可查看该瓶酒的全链路时间线、工序图表和仓储物流信息" />
  </div>
</template>

<script>
import * as echarts from 'echarts'
require('echarts/theme/macarons')
import { queryTraceCode } from '@/api/trace/query'
import { addPublicFeedback } from '@/api/trace/feedback'

export default {
  name: 'TraceQuery',
  data() {
    return {
      loading: false,
      form: {
        traceCode: ''
      },
      traceInfo: null,
      demoCodes: ['YSH100001', 'YSH101250', 'HK200001', 'YSGD300001'],
      feedbackForm: {
        rating: 5,
        content: '',
        isSuspicious: '0'
      },
      processChart: null,
      warehouseChart: null
    }
  },
  computed: {
    snapshotCards() {
      const info = this.traceInfo || {}
      return [
        {
          label: '酿造工序数',
          value: (info.processList || []).length,
          tip: '已记录的生产工序节点数量'
        },
        {
          label: '容器流转数',
          value: (info.usageList || []).length,
          tip: '批次在桶罐中的使用和切换次数'
        },
        {
          label: '仓储节点数',
          value: (info.warehouseEvents || []).length,
          tip: '成品入出库和物流节点总数'
        },
        {
          label: '最近质检状态',
          value: info.isQualified === '1' ? '合格' : '待复核',
          tip: '取最近一条质检报告状态'
        }
      ]
    },
    timelineItems() {
      if (!this.traceInfo) {
        return []
      }
      const info = this.traceInfo
      const items = []
      if (info.entryWorkshopTime) {
        items.push({
          key: 'entry',
          time: this.toTime(info.entryWorkshopTime),
          timeLabel: this.formatDateTime(info.entryWorkshopTime),
          title: '进入酿酒车间',
          desc: [info.workshopName, info.plotName].filter(Boolean).join(' | ') || '已记录入车间节点',
          type: 'primary'
        })
      }
      ;(info.processList || []).forEach((item, index) => {
        items.push({
          key: 'process-' + index,
          time: this.toTime(item.startTime || item.endTime),
          timeLabel: this.formatDateTime(item.startTime || item.endTime),
          title: item.processType || '生产工序',
          desc: [
            item.tempControl ? '温控：' + item.tempControl : '',
            item.operatorName ? '操作人：' + item.operatorName : '',
            item.remarks ? '备注：' + item.remarks : ''
          ].filter(Boolean).join('；') || '已记录工序详情',
          type: 'success'
        })
      })
      ;(info.usageList || []).forEach((item, index) => {
        items.push({
          key: 'usage-' + index,
          time: this.toTime(item.startTime || item.endTime),
          timeLabel: this.formatDateTime(item.startTime || item.endTime),
          title: '容器流转：' + [item.containerType, item.containerCode].filter(Boolean).join(' / '),
          desc: [
            item.fillQuantityL ? '装入量：' + item.fillQuantityL + 'L' : '',
            item.endTime ? '结束时间：' + this.formatDateTime(item.endTime) : '',
            item.remarks ? '备注：' + item.remarks : ''
          ].filter(Boolean).join('；') || '已记录容器使用信息',
          type: 'warning'
        })
      })
      if (info.bottlingDate) {
        items.push({
          key: 'bottling',
          time: this.toTime(info.bottlingDate),
          timeLabel: this.formatDateTime(info.bottlingDate),
          title: '完成灌装',
          desc: [info.bottleSpec, info.capCode].filter(Boolean).join(' | ') || '已生成灌装记录',
          type: 'danger'
        })
      }
      ;(info.warehouseEvents || []).forEach((item, index) => {
        items.push({
          key: 'warehouse-' + index,
          time: this.toTime(item.operateTime || item.shipDate),
          timeLabel: this.formatDateTime(item.operateTime || item.shipDate),
          title: item.billType === '出库' ? '销售出库' : '成品入库',
          desc: [
            item.billNo ? '单据：' + item.billNo : '',
            item.quantity !== undefined && item.quantity !== null ? '数量：' + item.quantity : '',
            item.destination ? '目的地：' + item.destination : '',
            item.trackingNo ? '运单号：' + item.trackingNo : ''
          ].filter(Boolean).join('；') || '已记录仓储物流信息',
          type: item.billType === '出库' ? 'danger' : 'info'
        })
      })
      return items.sort((a, b) => a.time - b.time)
    },
    latestWarehouseText() {
      const list = (this.traceInfo && this.traceInfo.warehouseEvents) || []
      if (!list.length) {
        return '--'
      }
      const latest = list[list.length - 1]
      return latest.billType === '出库'
        ? [latest.destination, latest.trackingNo].filter(Boolean).join(' / ') || '已出库'
        : '已入库待发运'
    }
  },
  mounted() {
    this.initFromRoute()
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize)
    this.disposeCharts()
  },
  watch: {
    '$route.query.traceCode': function() {
      this.initFromRoute()
    }
  },
  methods: {
    initFromRoute() {
      const routeCode = String(this.$route.query.traceCode || '').trim()
      if (routeCode) {
        const shouldQuery = routeCode !== this.form.traceCode || !this.traceInfo || this.traceInfo.traceCode !== routeCode
        this.form.traceCode = routeCode
        if (shouldQuery) {
          this.handleQuery(false)
        }
      }
    },
    handleQuery(syncRoute = true) {
      const traceCode = String(this.form.traceCode || '').trim()
      if (!traceCode) {
        this.$message.warning('请输入溯源码')
        return
      }
      this.form.traceCode = traceCode
      if (syncRoute && this.$route.query.traceCode !== traceCode) {
        this.$router.replace({
          path: this.$route.path,
          query: {
            ...this.$route.query,
            traceCode
          }
        })
      }
      this.loading = true
      queryTraceCode(traceCode).then(response => {
        this.traceInfo = response.data || null
        this.resetFeedback()
        this.$nextTick(() => {
          this.renderCharts()
        })
      }).finally(() => {
        this.loading = false
      })
    },
    fillDemo(code) {
      this.form.traceCode = code
      this.handleQuery()
    },
    renderCharts() {
      this.renderProcessChart()
      this.renderWarehouseChart()
    },
    renderProcessChart() {
      if (!this.$refs.processChart || !this.traceInfo) {
        return
      }
      if (!this.processChart) {
        this.processChart = echarts.init(this.$refs.processChart, 'macarons')
      }
      const list = (this.traceInfo.processList || []).map(item => {
        return {
          name: item.processType || '未命名工序',
          value: this.calcDuration(item.startTime, item.endTime)
        }
      })
      this.processChart.setOption({
        color: ['#8f2d2d'],
        tooltip: {
          trigger: 'axis',
          axisPointer: { type: 'shadow' },
          formatter(params) {
            const row = params && params[0]
            if (!row) {
              return ''
            }
            return row.name + '<br/>耗时：' + row.value + ' 天'
          }
        },
        grid: { left: 50, right: 24, top: 20, bottom: 32 },
        xAxis: {
          type: 'value',
          splitLine: { lineStyle: { color: '#efe5dc' } }
        },
        yAxis: {
          type: 'category',
          data: list.map(item => item.name),
          axisTick: { show: false }
        },
        series: [{
          name: '耗时',
          type: 'bar',
          barWidth: 16,
          data: list.map(item => item.value),
          itemStyle: {
            borderRadius: 10
          }
        }]
      })
    },
    renderWarehouseChart() {
      if (!this.$refs.warehouseChart || !this.traceInfo) {
        return
      }
      if (!this.warehouseChart) {
        this.warehouseChart = echarts.init(this.$refs.warehouseChart, 'macarons')
      }
      const list = this.traceInfo.warehouseEvents || []
      this.warehouseChart.setOption({
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
        grid: { left: 40, right: 24, top: 28, bottom: 50 },
        xAxis: {
          type: 'category',
          data: list.map(item => this.shortWarehouseLabel(item)),
          axisLabel: { interval: 0, rotate: 18, color: '#6c5a52' }
        },
        yAxis: {
          type: 'value',
          splitLine: { lineStyle: { color: '#efe5dc' } }
        },
        series: [{
          name: '数量',
          type: 'bar',
          barWidth: '42%',
          data: list.map(item => {
            return {
              value: item.quantity || 0,
              itemStyle: {
                color: item.billType === '出库' ? '#c87d3a' : '#355e3b',
                borderRadius: [8, 8, 0, 0]
              }
            }
          })
        }]
      })
    },
    calcDuration(start, end) {
      const startTime = this.toTime(start)
      const endTime = this.toTime(end)
      if (!startTime || !endTime || endTime < startTime) {
        return 0
      }
      return Number(((endTime - startTime) / (24 * 60 * 60 * 1000)).toFixed(1))
    },
    shortWarehouseLabel(item) {
      const date = this.formatDate(item.operateTime || item.shipDate)
      return [date, item.billType].filter(Boolean).join('\n')
    },
    submitFeedback() {
      if (!this.traceInfo || !this.traceInfo.traceCode) {
        this.$message.warning('请先查询有效的溯源码')
        return
      }
      addPublicFeedback({
        traceCode: this.traceInfo.traceCode,
        bottlingId: this.traceInfo.bottlingId,
        rating: this.feedbackForm.rating,
        content: this.feedbackForm.content,
        isSuspicious: this.feedbackForm.isSuspicious
      }).then(() => {
        this.$message.success('反馈提交成功')
        this.resetFeedback()
      })
    },
    resetFeedback() {
      this.feedbackForm = {
        rating: 5,
        content: '',
        isSuspicious: '0'
      }
    },
    formatDate(value) {
      if (!value) {
        return '--'
      }
      const date = new Date(value)
      if (isNaN(date.getTime())) {
        return value
      }
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      return year + '-' + month + '-' + day
    },
    formatDateTime(value) {
      if (!value) {
        return '--'
      }
      const date = new Date(value)
      if (isNaN(date.getTime())) {
        return value
      }
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      const hour = String(date.getHours()).padStart(2, '0')
      const minute = String(date.getMinutes()).padStart(2, '0')
      return year + '-' + month + '-' + day + ' ' + hour + ':' + minute
    },
    toTime(value) {
      if (!value) {
        return 0
      }
      const time = new Date(value).getTime()
      return isNaN(time) ? 0 : time
    },
    handleResize() {
      this.processChart && this.processChart.resize()
      this.warehouseChart && this.warehouseChart.resize()
    },
    disposeCharts() {
      this.processChart && this.processChart.dispose()
      this.warehouseChart && this.warehouseChart.dispose()
      this.processChart = null
      this.warehouseChart = null
    }
  }
}
</script>

<style scoped>
.trace-query-page {
  --wine: #7c2d2d;
  --wine-deep: #4c1d1d;
  --barrel: #c87d3a;
  --leaf: #355e3b;
  --sand: #f8f2ec;
  background:
    radial-gradient(circle at 100% 0, rgba(200, 125, 58, 0.12), transparent 24%),
    linear-gradient(180deg, #fcfaf7 0%, #f7f2ec 100%);
  min-height: calc(100vh - 84px);
}

.query-hero {
  margin-bottom: 14px;
  border: none;
  border-radius: 24px;
  overflow: hidden;
  background:
    linear-gradient(135deg, rgba(76, 29, 29, 0.98), rgba(124, 45, 45, 0.92) 58%, rgba(162, 63, 44, 0.9)),
    linear-gradient(180deg, #7c2d2d, #4c1d1d);
  box-shadow: 0 20px 40px rgba(94, 31, 31, 0.18);
}

.query-hero /deep/ .el-card__body {
  padding: 24px;
}

.query-header {
  display: grid;
  grid-template-columns: minmax(0, 1.2fr) minmax(340px, 0.9fr);
  gap: 18px;
  align-items: center;
}

.query-copy {
  color: #fff8f3;
}

.query-kicker {
  margin: 0 0 10px;
  font-size: 12px;
  letter-spacing: 0.18em;
  text-transform: uppercase;
  color: rgba(255, 248, 243, 0.7);
}

.query-copy h2 {
  margin: 0 0 10px;
  font-size: 28px;
  font-weight: 700;
  color: #fff8f3;
}

.query-copy p {
  margin: 0;
  line-height: 1.75;
  color: rgba(255, 248, 243, 0.88);
}

.query-search {
  padding: 18px;
  border-radius: 20px;
  background: rgba(255, 255, 255, 0.08);
  border: 1px solid rgba(255, 255, 255, 0.12);
}

.trace-input {
  width: 280px;
}

.query-search /deep/ .el-input__inner {
  border: none;
}

.demo-row {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  align-items: center;
}

.demo-label {
  font-size: 12px;
  color: rgba(255, 248, 243, 0.72);
}

.result-wrapper {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.summary-banner {
  display: grid;
  grid-template-columns: minmax(0, 1.2fr) minmax(320px, 0.9fr);
  gap: 14px;
  padding: 18px 20px;
  border-radius: 24px;
  background: linear-gradient(180deg, #fffdfb, #f8f1e9);
  border: 1px solid rgba(124, 45, 45, 0.08);
  box-shadow: 0 16px 32px rgba(76, 48, 31, 0.06);
}

.banner-label {
  margin-bottom: 8px;
  font-size: 12px;
  color: #8f776a;
}

.banner-code {
  font-size: 30px;
  line-height: 1.1;
  font-weight: 700;
  color: #3f2720;
}

.banner-product {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  align-items: center;
  margin-top: 10px;
  font-size: 18px;
  font-weight: 600;
  color: var(--wine);
}

.banner-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-top: 10px;
  color: #7f695f;
  font-size: 13px;
}

.banner-side {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 10px;
}

.summary-mini {
  padding: 14px 16px;
  border-radius: 16px;
  background: #fff;
  border: 1px solid #efe5dc;
}

.summary-mini span {
  display: block;
  margin-bottom: 6px;
  font-size: 12px;
  color: #8d786c;
}

.summary-mini strong {
  font-size: 15px;
  line-height: 1.4;
  color: #3e2a24;
}

.snapshot-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
  gap: 12px;
}

.snapshot-card {
  padding: 15px 16px;
  border-radius: 16px;
  background: rgba(255, 255, 255, 0.92);
  border: 1px solid rgba(124, 45, 45, 0.08);
  box-shadow: 0 12px 28px rgba(76, 48, 31, 0.05);
}

.snapshot-label {
  font-size: 13px;
  color: #8a7568;
}

.snapshot-value {
  margin: 8px 0 6px;
  font-size: 26px;
  font-weight: 700;
  color: #36241f;
}

.snapshot-tip {
  font-size: 12px;
  line-height: 1.55;
  color: #917c72;
}

.panel-card {
  border: none;
  border-radius: 18px;
  background: rgba(255, 255, 255, 0.92);
  box-shadow: 0 14px 32px rgba(71, 49, 35, 0.06);
}

.panel-card /deep/ .el-card__body {
  padding: 16px;
}

.panel-card /deep/ .el-card__header {
  padding: 14px 16px 0;
}

.showcase-card {
  height: 520px;
}

.showcase-card /deep/ .el-card__body {
  height: calc(100% - 52px);
  display: flex;
  flex-direction: column;
}

.metric-card,
.feedback-card,
.table-panel,
.warehouse-panel {
  height: 100%;
}

.panel-header {
  display: flex;
  justify-content: space-between;
  align-items: baseline;
  gap: 10px;
  color: #382620;
  font-weight: 600;
}

.panel-subtitle {
  font-size: 12px;
  color: #9a8479;
}

.timeline-card {
  min-height: 100%;
}

.trace-timeline {
  flex: 1;
  padding-top: 4px;
  max-height: none;
  overflow-y: auto;
}

.trace-timeline /deep/ .el-timeline-item {
  padding-bottom: 12px;
}

.trace-timeline /deep/ .el-timeline-item__timestamp {
  font-size: 12px;
  line-height: 1.35;
}

.trace-timeline /deep/ .el-timeline-item__node {
  width: 12px;
  height: 12px;
  left: 0;
}

.trace-timeline /deep/ .el-timeline-item__tail {
  left: 5px;
}

.timeline-title {
  margin-bottom: 4px;
  font-size: 13px;
  font-weight: 600;
  color: #402821;
}

.timeline-desc {
  font-size: 12px;
  line-height: 1.6;
  color: #7a655c;
}

.chart-panel {
  width: 100%;
  height: 100%;
  min-height: 420px;
}

.section-row {
  margin-top: 0;
}

.info-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 10px;
}

.info-item {
  padding: 12px 14px;
  border-radius: 14px;
  background: linear-gradient(180deg, #fffdfa, #f8f1e9);
  border: 1px solid #efe5dc;
}

.info-item span {
  display: block;
  margin-bottom: 6px;
  font-size: 12px;
  color: #8f796f;
}

.info-item strong {
  display: block;
  font-size: 13px;
  line-height: 1.55;
  color: #3d2821;
  word-break: break-word;
}

.table-panel /deep/ .el-card__body,
.warehouse-panel /deep/ .el-card__body {
  display: flex;
  flex-direction: column;
}

.table-wrap {
  flex: 1;
}

.compact-table /deep/ th,
.compact-table /deep/ td {
  padding: 8px 0;
}

.warehouse-grid {
  display: grid;
  gap: 10px;
  max-height: 360px;
  overflow-y: auto;
  padding-right: 4px;
}

.warehouse-record {
  padding: 12px 14px;
  border-radius: 14px;
  border: 1px solid #efe5dc;
  background: linear-gradient(180deg, #fffdfa, #f8f1e9);
}

.warehouse-record-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 10px;
  margin-bottom: 8px;
}

.warehouse-time {
  font-size: 12px;
  color: #8b776c;
}

.warehouse-title {
  margin-bottom: 8px;
  font-size: 14px;
  font-weight: 600;
  color: #3a2721;
}

.warehouse-meta,
.warehouse-desc {
  display: flex;
  flex-wrap: wrap;
  gap: 8px 12px;
  font-size: 12px;
  line-height: 1.6;
  color: #7d695f;
}

.compact-feedback-form /deep/ .el-form-item {
  margin-bottom: 14px;
}

.feedback-bottom-card /deep/ .el-card__body {
  display: block;
}

.feedback-bottom-card .compact-feedback-form {
  width: 100%;
  max-width: none;
}

.compact-feedback-form /deep/ .el-textarea__inner {
  min-height: 96px !important;
}

.query-empty {
  margin-top: 26px;
  padding: 28px 0;
  border-radius: 24px;
  background: rgba(255, 255, 255, 0.88);
}

@media (max-width: 1280px) {
  .query-header,
  .summary-banner {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .query-hero /deep/ .el-card__body {
    padding: 18px;
  }

  .query-copy h2 {
    font-size: 22px;
  }

  .trace-input {
    width: 100%;
  }

  .banner-code {
    font-size: 24px;
  }

  .banner-side,
  .info-grid {
    grid-template-columns: 1fr;
  }

  .trace-timeline {
    max-height: none;
  }
}

@media (max-width: 1200px) {
  .showcase-card {
    height: auto;
  }

  .showcase-card /deep/ .el-card__body {
    height: auto;
  }

  .chart-panel {
    height: 300px;
    min-height: 300px;
  }
}
</style>
