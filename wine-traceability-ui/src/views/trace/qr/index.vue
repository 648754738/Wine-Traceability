<template>
  <div class="app-container qr-manage-page">
    <el-card shadow="never" class="qr-hero">
      <div class="qr-hero__content">
        <div>
          <p class="qr-hero__kicker">QR Code Studio</p>
          <h2>二维码独立管理</h2>
          <p>这里专门处理灌装批次的二维码生成、导出、打印和溯源页跳转，不再放在灌装信息修改弹窗中。</p>
        </div>
        <div class="qr-hero__stats">
          <div class="qr-stat">
            <span>当前页记录</span>
            <strong>{{ bottlingList.length }}</strong>
          </div>
          <div class="qr-stat">
            <span>已生成</span>
            <strong>{{ generatedCount }}</strong>
          </div>
          <div class="qr-stat">
            <span>待生成</span>
            <strong>{{ pendingCount }}</strong>
          </div>
        </div>
      </div>
    </el-card>

    <el-form ref="queryForm" :model="queryParams" size="small" :inline="true" v-show="showSearch" label-width="90px" class="query-form">
      <el-form-item label="生产批次" prop="batchId">
        <el-select v-model="queryParams.batchId" placeholder="请选择批次" clearable filterable>
          <el-option v-for="item in batchOptions" :key="item.batchId" :label="item.batchNo" :value="item.batchId" />
        </el-select>
      </el-form-item>
      <el-form-item label="溯源码前缀" prop="traceCodePrefix">
        <el-input v-model="queryParams.traceCodePrefix" placeholder="请输入前缀" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-alert
      class="qr-alert"
      type="info"
      :closable="false"
      title="操作规则：优先对已勾选记录生效；如果没有勾选，则默认作用于当前页列表。"
      show-icon
    />

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-picture-outline"
          size="mini"
          :disabled="!selectedRows.length"
          @click="handleGenerateSelected"
          v-hasPermi="['trace:bottling:edit']"
        >
          选中生成
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-finished"
          size="mini"
          :disabled="!bottlingList.length"
          @click="handleGenerateCurrentPage"
          v-hasPermi="['trace:bottling:edit']"
        >
          当前页一键生成
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-document"
          size="mini"
          :disabled="!workingRows.length"
          @click="exportQrList"
        >
          导出清单
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          plain
          icon="el-icon-printer"
          size="mini"
          :disabled="!workingRows.length"
          @click="printLabels"
        >
          打印标签
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button plain icon="el-icon-back" size="mini" @click="goBackToBottling">
          返回灌装信息
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="bottlingList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="批次号" align="center" prop="batchNo" min-width="150" />
      <el-table-column label="来源容器" align="center" prop="sourceContainerCode" min-width="140" />
      <el-table-column label="溯源码区间" align="center" min-width="220">
        <template slot-scope="scope">
          {{ formatTraceRange(scope.row) }}
        </template>
      </el-table-column>
      <el-table-column label="首瓶码" align="center" min-width="130">
        <template slot-scope="scope">
          {{ buildTraceCode(scope.row) || '--' }}
        </template>
      </el-table-column>
      <el-table-column label="瓶数" align="center" prop="bottleCount" width="90" />
      <el-table-column label="二维码状态" align="center" width="110">
        <template slot-scope="scope">
          <el-tag size="mini" :type="getQrStatusTagType(scope.row)">
            {{ getQrStatusText(scope.row) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="扫码跳转" align="center" min-width="260" show-overflow-tooltip>
        <template slot-scope="scope">
          {{ buildTraceQueryUrl(buildTraceCode(scope.row)) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="310">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-picture-outline" @click="handleGenerate(scope.row)" v-hasPermi="['trace:bottling:edit']">
            {{ getGenerateButtonText(scope.row) }}
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-view" @click="handlePreview(scope.row)">
            预览
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-document-copy" @click="copyTraceUrl(scope.row)">
            复制链接
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-search" @click="openTracePage(scope.row)">
            查看溯源页
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog title="二维码预览" :visible.sync="qrPreviewOpen" width="420px" append-to-body>
      <div class="qr-preview-dialog">
        <img v-if="qrPreviewData.imageUrl" :src="qrPreviewData.imageUrl" alt="qr code" class="qr-preview-dialog__image">
        <el-empty v-else :image-size="72" description="暂无二维码可预览" />
        <div v-if="qrPreviewData.traceCode" class="qr-preview-dialog__item">
          <span class="qr-preview-dialog__label">首瓶溯源码</span>
          <strong>{{ qrPreviewData.traceCode }}</strong>
        </div>
        <div v-if="qrPreviewData.targetUrl" class="qr-preview-dialog__item">
          <span class="qr-preview-dialog__label">扫码跳转</span>
          <span class="qr-preview-dialog__text">{{ qrPreviewData.targetUrl }}</span>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listBottling, updateBottling } from '@/api/trace/bottling'
import { listBatchOptions } from '@/api/trace/batch'

export default {
  name: 'TraceQrManager',
  data() {
    return {
      loading: false,
      showSearch: true,
      total: 0,
      batchOptions: [],
      bottlingList: [],
      selectedRows: [],
      qrPreviewOpen: false,
      qrPreviewData: {
        imageUrl: '',
        traceCode: '',
        targetUrl: ''
      },
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        batchId: undefined,
        traceCodePrefix: undefined
      }
    }
  },
  computed: {
    generatedCount() {
      return this.bottlingList.filter(item => this.isCurrentQrCode(item)).length
    },
    pendingCount() {
      return this.bottlingList.length - this.generatedCount
    },
    workingRows() {
      return this.selectedRows.length ? this.selectedRows : this.bottlingList
    }
  },
  created() {
    this.initQueryFromRoute()
    this.getBatchOptions()
    this.getList()
  },
  methods: {
    initQueryFromRoute() {
      const { batchId, traceCodePrefix } = this.$route.query
      this.queryParams.batchId = batchId ? Number(batchId) : undefined
      this.queryParams.traceCodePrefix = traceCodePrefix ? String(traceCodePrefix).trim() : undefined
    },
    getBatchOptions() {
      listBatchOptions().then(response => {
        this.batchOptions = response.data || []
      })
    },
    getList() {
      this.loading = true
      listBottling(this.queryParams).then(response => {
        this.bottlingList = response.rows || []
        this.total = response.total || 0
      }).finally(() => {
        this.loading = false
      })
    },
    buildTraceCode(row) {
      if (!row || !row.traceCodePrefix || row.traceCodeStart === undefined || row.traceCodeStart === null) {
        return ''
      }
      return String(row.traceCodePrefix).trim() + String(row.traceCodeStart).trim()
    },
    formatTraceRange(row) {
      if (!row || !row.traceCodePrefix || row.traceCodeStart === undefined || row.traceCodeEnd === undefined) {
        return '--'
      }
      return `${row.traceCodePrefix}${row.traceCodeStart} - ${row.traceCodePrefix}${row.traceCodeEnd}`
    },
    normalizeOrigin(origin) {
      return String(origin || '').trim().replace(/\/+$/, '')
    },
    isLocalOrPrivateHost(hostname) {
      if (!hostname) {
        return true
      }
      const host = String(hostname).trim().toLowerCase()
      if (host === 'localhost' || host === '127.0.0.1' || host === '0.0.0.0' || host === '::1') {
        return true
      }
      if (/^10\./.test(host) || /^192\.168\./.test(host) || /^169\.254\./.test(host)) {
        return true
      }
      const match = host.match(/^172\.(\d{1,3})\./)
      if (match) {
        const second = Number(match[1])
        return second >= 16 && second <= 31
      }
      return false
    },
    getPublicOrigin() {
      const currentOrigin = this.normalizeOrigin(window.location.origin)
      try {
        const currentUrl = new URL(currentOrigin)
        if (!this.isLocalOrPrivateHost(currentUrl.hostname)) {
          return currentOrigin
        }
      } catch (error) {
        // Fall back to configured origin when the current origin is unavailable.
      }
      const configuredOrigin = this.normalizeOrigin(process.env.VUE_APP_PUBLIC_BASE_URL)
      if (configuredOrigin) {
        return configuredOrigin
      }
      return currentOrigin
    },
    buildTraceQueryUrl(traceCode) {
      if (!traceCode) {
        return '--'
      }
      const routeData = this.$router.resolve({
        path: '/trace-query',
        query: { traceCode }
      })
      return new URL(routeData.href, this.getPublicOrigin()).toString()
    },
    buildQrServiceUrl(content) {
      return 'https://quickchart.io/qr?size=260&margin=1&text=' + encodeURIComponent(content)
    },
    resolvePreviewImageUrl(value) {
      if (!value) {
        return ''
      }
      if (/^https?:\/\//.test(value) || value.startsWith('data:image')) {
        return value
      }
      return process.env.VUE_APP_BASE_API + value
    },
    buildQrPayload(row) {
      const traceCode = this.buildTraceCode(row)
      const targetUrl = this.buildTraceQueryUrl(traceCode)
      return {
        traceCode,
        targetUrl,
        imageUrl: this.buildQrServiceUrl(targetUrl)
      }
    },
    isCurrentQrCode(row) {
      if (!row || !row.qrCodeUrl) {
        return false
      }
      return String(row.qrCodeUrl).trim() === this.buildQrPayload(row).imageUrl
    },
    getQrCodeState(row) {
      if (!row || !row.qrCodeUrl) {
        return 'pending'
      }
      return this.isCurrentQrCode(row) ? 'current' : 'outdated'
    },
    getQrStatusTagType(row) {
      const state = this.getQrCodeState(row)
      if (state === 'current') {
        return 'success'
      }
      if (state === 'outdated') {
        return 'warning'
      }
      return 'info'
    },
    getQrStatusText(row) {
      const state = this.getQrCodeState(row)
      if (state === 'current') {
        return '已更新'
      }
      if (state === 'outdated') {
        return '待更新'
      }
      return '待生成'
    },
    getGenerateButtonText(row) {
      const state = this.getQrCodeState(row)
      if (state === 'current') {
        return '重新生成'
      }
      if (state === 'outdated') {
        return '更新二维码'
      }
      return '生成二维码'
    },
    getPreferredQrImageUrl(row) {
      const payload = this.buildQrPayload(row)
      if (this.isCurrentQrCode(row)) {
        return this.resolvePreviewImageUrl(row.qrCodeUrl)
      }
      return payload.imageUrl
    },
    getAvailableRows(rows) {
      return (rows || []).filter(row => this.buildTraceCode(row))
    },
    updateQrCode(row) {
      const payload = this.buildQrPayload(row)
      return updateBottling({
        ...row,
        qrCodeUrl: payload.imageUrl
      }).then(() => {
        row.qrCodeUrl = payload.imageUrl
        return payload
      })
    },
    handleGenerate(row) {
      if (!this.buildTraceCode(row)) {
        this.$message.warning('当前记录缺少首瓶溯源码，无法生成二维码')
        return
      }
      this.updateQrCode(row).then(() => {
        this.$modal.msgSuccess('二维码已生成')
      })
    },
    handleGenerateSelected() {
      if (!this.selectedRows.length) {
        this.$message.warning('请先选择要生成二维码的记录')
        return
      }
      this.handleGenerateRows(this.selectedRows, `已生成 ${this.selectedRows.length} 条二维码`)
    },
    handleGenerateCurrentPage() {
      if (!this.bottlingList.length) {
        this.$message.warning('当前页暂无可生成的记录')
        return
      }
      this.handleGenerateRows(this.bottlingList, `当前页 ${this.bottlingList.length} 条记录已完成二维码生成`)
    },
    handleGenerateRows(rows, successMessage) {
      const availableRows = this.getAvailableRows(rows)
      if (!availableRows.length) {
        this.$message.warning('没有可生成二维码的有效记录')
        return
      }
      Promise.all(availableRows.map(row => this.updateQrCode(row))).then(() => {
        this.$modal.msgSuccess(successMessage)
        this.getList()
      })
    },
    handlePreview(row) {
      const payload = this.buildQrPayload(row)
      this.qrPreviewData = {
        imageUrl: this.getPreferredQrImageUrl(row),
        traceCode: payload.traceCode,
        targetUrl: payload.targetUrl
      }
      this.qrPreviewOpen = true
    },
    openTracePage(row) {
      const traceCode = this.buildTraceCode(row)
      if (!traceCode) {
        this.$message.warning('当前记录缺少首瓶溯源码，无法打开溯源页')
        return
      }
      this.$router.push({
        path: '/trace-query',
        query: { traceCode }
      }).catch(() => {})
    },
    copyTraceUrl(row) {
      const targetUrl = this.buildTraceQueryUrl(this.buildTraceCode(row))
      if (!targetUrl || targetUrl === '--') {
        this.$message.warning('当前记录缺少有效跳转链接')
        return
      }
      if (navigator.clipboard && navigator.clipboard.writeText) {
        navigator.clipboard.writeText(targetUrl).then(() => {
          this.$message.success('溯源链接已复制')
        }).catch(() => {
          this.$message.warning('复制失败，请手动复制')
        })
        return
      }
      const input = document.createElement('input')
      input.value = targetUrl
      document.body.appendChild(input)
      input.select()
      try {
        document.execCommand('copy')
        this.$message.success('溯源链接已复制')
      } catch (error) {
        this.$message.warning('复制失败，请手动复制')
      }
      document.body.removeChild(input)
    },
    toExportRows() {
      return this.getAvailableRows(this.workingRows)
    },
    escapeCsv(value) {
      const text = value === undefined || value === null ? '' : String(value)
      return `"${text.replace(/"/g, '""')}"`
    },
    exportQrList() {
      const rows = this.toExportRows()
      if (!rows.length) {
        this.$message.warning('没有可导出的二维码记录')
        return
      }
      const header = ['批次号', '来源容器', '首瓶溯源码', '溯源码区间', '扫码跳转', '二维码地址']
      const lines = rows.map(row => {
        const payload = this.buildQrPayload(row)
        return [
          row.batchNo || '',
          row.sourceContainerCode || '',
          payload.traceCode,
          this.formatTraceRange(row),
          payload.targetUrl,
          this.getPreferredQrImageUrl(row)
        ].map(value => this.escapeCsv(value)).join(',')
      })
      const csv = '\uFEFF' + header.join(',') + '\n' + lines.join('\n')
      const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' })
      const url = window.URL.createObjectURL(blob)
      const link = document.createElement('a')
      link.href = url
      link.download = `二维码清单_${new Date().getTime()}.csv`
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
      window.URL.revokeObjectURL(url)
      this.$message.success(`已导出 ${rows.length} 条二维码清单`)
    },
    printLabels() {
      const rows = this.toExportRows()
      if (!rows.length) {
        this.$message.warning('没有可打印的二维码记录')
        return
      }
      const html = this.buildPrintHtml(rows)
      const win = window.open('', '_blank')
      if (!win) {
        this.$message.warning('打印窗口被浏览器拦截，请允许弹窗后重试')
        return
      }
      win.document.open()
      win.document.write(html)
      win.document.close()
      win.onload = function() {
        setTimeout(function() {
          win.print()
        }, 300)
      }
    },
    buildPrintHtml(rows) {
      const cards = rows.map(row => {
        const payload = this.buildQrPayload(row)
        return `
          <div class="label-card">
            <div class="label-head">
              <div class="label-title">${row.batchNo || '未命名批次'}</div>
              <div class="label-sub">${row.sourceContainerCode || '未设置来源容器'}</div>
            </div>
            <div class="label-body">
              <img class="label-qr" src="${this.getPreferredQrImageUrl(row)}" alt="qr code" />
              <div class="label-meta">
                <div><span>首瓶码</span><strong>${payload.traceCode}</strong></div>
                <div><span>区间</span><strong>${this.formatTraceRange(row)}</strong></div>
                <div><span>瓶数</span><strong>${row.bottleCount || 0}</strong></div>
                <div><span>跳转</span><p>${payload.targetUrl}</p></div>
              </div>
            </div>
          </div>
        `
      }).join('')
      return `
        <!DOCTYPE html>
        <html lang="zh-CN">
        <head>
          <meta charset="UTF-8" />
          <title>二维码标签打印</title>
          <style>
            * { box-sizing: border-box; }
            body {
              margin: 0;
              padding: 24px;
              font-family: "Microsoft YaHei", sans-serif;
              background: #f6efe7;
              color: #2f221f;
            }
            .print-head {
              margin-bottom: 18px;
            }
            .print-head h1 {
              margin: 0 0 8px;
              font-size: 24px;
            }
            .print-head p {
              margin: 0;
              font-size: 13px;
              color: #7f695f;
            }
            .label-grid {
              display: grid;
              grid-template-columns: repeat(2, minmax(0, 1fr));
              gap: 16px;
            }
            .label-card {
              background: #fffdfb;
              border: 1px solid #eadfd5;
              border-radius: 18px;
              padding: 16px;
              break-inside: avoid;
            }
            .label-head {
              margin-bottom: 12px;
            }
            .label-title {
              font-size: 18px;
              font-weight: 700;
              color: #4c1d1d;
            }
            .label-sub {
              margin-top: 4px;
              font-size: 13px;
              color: #866d62;
            }
            .label-body {
              display: flex;
              gap: 16px;
              align-items: center;
            }
            .label-qr {
              width: 158px;
              height: 158px;
              object-fit: contain;
              border: 1px solid #efe5dc;
              border-radius: 12px;
              background: #fff;
              flex-shrink: 0;
            }
            .label-meta {
              min-width: 0;
              flex: 1;
            }
            .label-meta div {
              margin-bottom: 10px;
            }
            .label-meta span {
              display: block;
              margin-bottom: 4px;
              font-size: 12px;
              color: #8c756a;
            }
            .label-meta strong {
              font-size: 14px;
              color: #342420;
              word-break: break-all;
            }
            .label-meta p {
              margin: 0;
              font-size: 12px;
              line-height: 1.55;
              color: #5a443a;
              word-break: break-all;
            }
            @media print {
              body {
                padding: 12px;
                background: #fff;
              }
              .print-head {
                margin-bottom: 12px;
              }
            }
          </style>
        </head>
        <body>
          <div class="print-head">
            <h1>二维码标签打印页</h1>
            <p>共 ${rows.length} 条记录，页面加载完成后可直接打印。</p>
          </div>
          <div class="label-grid">${cards}</div>
        </body>
        </html>
      `
    },
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.resetForm('queryForm')
      this.queryParams.pageNum = 1
      this.getList()
    },
    goBackToBottling() {
      this.$router.push('/trace-quality/bottling').catch(() => {})
    }
  }
}
</script>

<style scoped>
.qr-manage-page {
  background:
    radial-gradient(circle at top right, rgba(124, 45, 45, 0.08), transparent 24%),
    linear-gradient(180deg, #fbf7f2 0%, #f6efe7 100%);
  min-height: calc(100vh - 84px);
}

.qr-hero {
  margin-bottom: 14px;
  border: none;
  border-radius: 24px;
  background: linear-gradient(135deg, #4c1d1d, #7c2d2d 55%, #b05e2d);
  box-shadow: 0 18px 36px rgba(92, 36, 36, 0.18);
}

.qr-hero /deep/ .el-card__body {
  padding: 22px 24px;
}

.qr-hero__content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 18px;
  color: #fff8f3;
}

.qr-hero__kicker {
  margin: 0 0 8px;
  font-size: 12px;
  letter-spacing: 0.16em;
  text-transform: uppercase;
  color: rgba(255, 248, 243, 0.72);
}

.qr-hero h2 {
  margin: 0 0 10px;
  font-size: 28px;
}

.qr-hero p {
  margin: 0;
  max-width: 720px;
  line-height: 1.75;
  color: rgba(255, 248, 243, 0.88);
}

.qr-hero__stats {
  display: grid;
  grid-template-columns: repeat(3, minmax(100px, 1fr));
  gap: 12px;
  min-width: 340px;
}

.qr-stat {
  padding: 14px 16px;
  border-radius: 16px;
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.12);
}

.qr-stat span {
  display: block;
  margin-bottom: 6px;
  font-size: 12px;
  color: rgba(255, 248, 243, 0.74);
}

.qr-stat strong {
  font-size: 24px;
  color: #fffdf9;
}

.query-form {
  margin-bottom: 8px;
}

.qr-alert {
  margin-bottom: 12px;
}

.qr-preview-dialog {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  text-align: center;
}

.qr-preview-dialog__image {
  width: 220px;
  height: 220px;
  object-fit: contain;
  border: 1px solid #ebe5df;
  border-radius: 12px;
  background: #fff;
}

.qr-preview-dialog__item {
  width: 100%;
}

.qr-preview-dialog__label {
  display: block;
  margin-bottom: 6px;
  font-size: 12px;
  color: #8d7466;
}

.qr-preview-dialog__text {
  font-size: 13px;
  line-height: 1.6;
  color: #4d372d;
  word-break: break-all;
}

@media (max-width: 992px) {
  .qr-hero__content {
    flex-direction: column;
    align-items: flex-start;
  }

  .qr-hero__stats {
    width: 100%;
    min-width: 0;
  }
}

@media (max-width: 768px) {
  .qr-hero /deep/ .el-card__body {
    padding: 18px;
  }

  .qr-hero h2 {
    font-size: 22px;
  }

  .qr-hero__stats {
    grid-template-columns: 1fr;
  }
}
</style>
