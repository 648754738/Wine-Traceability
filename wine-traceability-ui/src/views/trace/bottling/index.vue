<template>
  <div class="app-container">
    <el-form ref="queryForm" :model="queryParams" size="small" :inline="true" v-show="showSearch" label-width="90px">
      <el-form-item label="生产批次" prop="batchId">
        <el-select v-model="queryParams.batchId" placeholder="请选择批次" clearable filterable>
          <el-option v-for="item in batchOptions" :key="item.batchId" :label="item.batchNo" :value="item.batchId" />
        </el-select>
      </el-form-item>
      <el-form-item label="来源容器" prop="sourceContainerId">
        <el-select v-model="queryParams.sourceContainerId" placeholder="请选择容器" clearable filterable>
          <el-option v-for="item in queryContainerOptions" :key="item.containerId" :label="item.containerCode" :value="item.containerId" />
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

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['trace:bottling:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['trace:bottling:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['trace:bottling:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-picture-outline" size="mini" @click="goQrManager">二维码管理</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="bottlingList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="批次号" align="center" prop="batchNo" min-width="150" />
      <el-table-column label="来源容器" align="center" prop="sourceContainerCode" min-width="140" />
      <el-table-column label="溯源码前缀" align="center" prop="traceCodePrefix" width="110" />
      <el-table-column label="起始序号" align="center" prop="traceCodeStart" width="110" />
      <el-table-column label="结束序号" align="center" prop="traceCodeEnd" width="110" />
      <el-table-column label="瓶数" align="center" prop="bottleCount" width="90" />
      <el-table-column label="灌装日期" align="center" prop="bottlingDate" width="170" />
      <el-table-column label="瓶型规格" align="center" prop="bottleSpec" min-width="120" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="240">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['trace:bottling:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-picture-outline" @click="goQrManager(scope.row)">二维码</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['trace:bottling:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="920px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="生产批次" prop="batchId">
              <el-select v-model="form.batchId" placeholder="请选择生产批次" filterable style="width: 100%" @change="handleBatchChange">
                <el-option v-for="item in batchOptions" :key="item.batchId" :label="item.batchNo" :value="item.batchId" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="来源容器" prop="sourceContainerId">
              <el-select v-model="form.sourceContainerId" placeholder="请选择来源容器" clearable filterable style="width: 100%">
                <el-option v-for="item in formContainerOptions" :key="item.containerId" :label="item.containerCode" :value="item.containerId" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="溯源码前缀" prop="traceCodePrefix">
              <el-input v-model="form.traceCodePrefix" placeholder="例如 YSH" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="起始序号" prop="traceCodeStart">
              <el-input-number v-model="form.traceCodeStart" :min="1" :step="1" controls-position="right" style="width: 100%" @change="syncBottleCountFromRange" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="瓶数" prop="bottleCount">
              <el-input-number v-model="form.bottleCount" :min="1" :step="1" controls-position="right" style="width: 100%" @change="syncEndFromBottleCount" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="结束序号" prop="traceCodeEnd">
              <el-input-number v-model="form.traceCodeEnd" :min="1" :step="1" controls-position="right" style="width: 100%" @change="syncBottleCountFromRange" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="灌装日期" prop="bottlingDate">
              <el-date-picker v-model="form.bottlingDate" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择灌装日期" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="瓶型规格" prop="bottleSpec">
              <el-input v-model="form.bottleSpec" placeholder="例如 750ml" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="瓶盖码" prop="capCode">
              <el-input v-model="form.capCode" placeholder="请输入瓶盖码" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注" prop="remarks">
          <el-input v-model="form.remarks" type="textarea" :rows="3" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确定</el-button>
        <el-button @click="cancel">取消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listBottling, getBottling, addBottling, updateBottling, delBottling } from '@/api/trace/bottling'
import { listBatchOptions, getBatch } from '@/api/trace/batch'
import { listContainerOptions } from '@/api/trace/container'

export default {
  name: 'TraceBottlingInfo',
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      bottlingList: [],
      batchOptions: [],
      queryContainerOptions: [],
      formContainerOptions: [],
      title: '',
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        batchId: undefined,
        sourceContainerId: undefined,
        traceCodePrefix: undefined
      },
      form: {},
      rules: {
        batchId: [{ required: true, message: '生产批次不能为空', trigger: 'change' }],
        traceCodePrefix: [{ required: true, message: '溯源码前缀不能为空', trigger: 'blur' }],
        traceCodeStart: [{ required: true, message: '起始序号不能为空', trigger: 'change' }],
        traceCodeEnd: [{ required: true, message: '结束序号不能为空', trigger: 'change' }]
      }
    }
  },
  created() {
    this.getList()
    this.getBatchOptions()
    this.loadQueryContainerOptions()
  },
  methods: {
    getList() {
      this.loading = true
      listBottling(this.queryParams).then(response => {
        this.bottlingList = response.rows
        this.total = response.total
      }).finally(() => {
        this.loading = false
      })
    },
    getBatchOptions() {
      listBatchOptions().then(response => {
        this.batchOptions = response.data || []
      })
    },
    loadQueryContainerOptions() {
      listContainerOptions({}).then(response => {
        this.queryContainerOptions = response.data || []
      })
    },
    loadFormContainerOptions(batchId) {
      if (!batchId) {
        this.formContainerOptions = []
        return Promise.resolve()
      }
      return getBatch(batchId).then(response => {
        const batch = response.data || {}
        return listContainerOptions({ enterpriseId: batch.enterpriseId, workshopId: batch.workshopId }).then(res => {
          this.formContainerOptions = res.data || []
        })
      })
    },
    syncEndFromBottleCount() {
      if (this.form.traceCodeStart && this.form.bottleCount) {
        this.form.traceCodeEnd = Number(this.form.traceCodeStart) + Number(this.form.bottleCount) - 1
      }
    },
    syncBottleCountFromRange() {
      if (this.form.traceCodeStart && this.form.traceCodeEnd && this.form.traceCodeEnd >= this.form.traceCodeStart) {
        this.form.bottleCount = Number(this.form.traceCodeEnd) - Number(this.form.traceCodeStart) + 1
      }
    },
    handleBatchChange(value) {
      this.form.sourceContainerId = undefined
      this.loadFormContainerOptions(value)
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        bottlingId: undefined,
        batchId: undefined,
        sourceContainerId: undefined,
        traceCodePrefix: undefined,
        traceCodeStart: undefined,
        traceCodeEnd: undefined,
        bottleCount: undefined,
        bottlingDate: undefined,
        bottleSpec: undefined,
        capCode: undefined,
        qrCodeUrl: undefined,
        remarks: undefined
      }
      this.formContainerOptions = []
      this.resetForm('form')
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.bottlingId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '新增灌装信息'
    },
    handleUpdate(row) {
      this.reset()
      const bottlingId = row.bottlingId || this.ids
      getBottling(bottlingId).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改灌装信息'
        this.syncBottleCountFromRange()
        this.loadFormContainerOptions(this.form.batchId)
      })
    },
    goQrManager(row) {
      const query = {}
      if (row && row.batchId) {
        query.batchId = row.batchId
      }
      if (row && row.traceCodePrefix) {
        query.traceCodePrefix = row.traceCodePrefix
      }
      this.$router.push({
        path: '/trace-bottling/qrcode',
        query
      }).catch(() => {})
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        const request = this.form.bottlingId ? updateBottling(this.form) : addBottling(this.form)
        request.then(() => {
          this.$modal.msgSuccess(this.form.bottlingId ? '修改成功' : '新增成功')
          this.open = false
          this.getList()
        })
      })
    },
    handleDelete(row) {
      const bottlingIds = row.bottlingId || this.ids
      this.$modal.confirm('是否确认删除选中的灌装信息？').then(() => delBottling(bottlingIds)).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    }
  }
}
</script>

<style scoped>
</style>
