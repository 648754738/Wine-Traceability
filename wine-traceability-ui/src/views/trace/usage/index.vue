<template>
  <div class="app-container">
    <el-form ref="queryForm" :model="queryParams" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="批次" prop="batchId">
        <el-select v-model="queryParams.batchId" placeholder="请选择批次" clearable filterable>
          <el-option v-for="item in batchOptions" :key="item.batchId" :label="item.batchNo" :value="item.batchId" />
        </el-select>
      </el-form-item>
      <el-form-item label="容器" prop="containerId">
        <el-select v-model="queryParams.containerId" placeholder="请选择容器" clearable filterable>
          <el-option v-for="item in queryContainerOptions" :key="item.containerId" :label="item.containerCode" :value="item.containerId" />
        </el-select>
      </el-form-item>
      <el-form-item label="容器编码" prop="containerCode">
        <el-input v-model="queryParams.containerCode" placeholder="请输入容器编码" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['trace:usage:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['trace:usage:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['trace:usage:remove']">删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="usageList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="批次号" align="center" prop="batchNo" min-width="150" />
      <el-table-column label="容器编码" align="center" prop="containerCode" min-width="140" />
      <el-table-column label="容器类型" align="center" prop="containerType" width="120">
        <template slot-scope="scope">
          <span>{{ containerTypeLabel(scope.row.containerType) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="入桶/入罐时间" align="center" prop="startTime" width="170" />
      <el-table-column label="出桶/出罐时间" align="center" prop="endTime" width="170" />
      <el-table-column label="装入量(L)" align="center" prop="fillQuantityL" width="110" />
      <el-table-column label="备注" align="center" prop="remarks" min-width="180" show-overflow-tooltip />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="160">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['trace:usage:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['trace:usage:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="760px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="105px">
        <el-form-item label="生产批次" prop="batchId">
          <el-select v-model="form.batchId" placeholder="请选择生产批次" filterable style="width: 100%" @change="handleBatchFormChange">
            <el-option v-for="item in batchOptions" :key="item.batchId" :label="item.batchNo" :value="item.batchId" />
          </el-select>
        </el-form-item>
        <el-form-item label="容器" prop="containerId">
          <el-select v-model="form.containerId" placeholder="请选择容器" filterable style="width: 100%">
            <el-option v-for="item in formContainerOptions" :key="item.containerId" :label="item.containerCode" :value="item.containerId" />
          </el-select>
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="入桶/入罐时间" prop="startTime">
              <el-date-picker v-model="form.startTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择开始时间" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="出桶/出罐时间" prop="endTime">
              <el-date-picker v-model="form.endTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择结束时间" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="装入量(L)" prop="fillQuantityL">
          <el-input-number v-model="form.fillQuantityL" :min="0" :precision="2" :step="1" controls-position="right" style="width: 100%" />
        </el-form-item>
        <el-form-item label="备注" prop="remarks">
          <el-input v-model="form.remarks" type="textarea" :rows="3" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listUsage, getUsage, addUsage, updateUsage, delUsage } from '@/api/trace/usage'
import { listBatchOptions, getBatch } from '@/api/trace/batch'
import { listContainerOptions } from '@/api/trace/container'

export default {
  name: 'TraceContainerUsage',
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      usageList: [],
      batchOptions: [],
      queryContainerOptions: [],
      formContainerOptions: [],
      title: '',
      open: false,
      containerTypeOptions: [
        { label: '橡木桶', value: 'oak_barrel' },
        { label: '不锈钢罐', value: 'stainless_tank' },
        { label: '陶罐', value: 'ceramic_tank' }
      ],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        batchId: undefined,
        containerId: undefined,
        containerCode: undefined
      },
      form: {},
      rules: {
        batchId: [{ required: true, message: '生产批次不能为空', trigger: 'change' }],
        containerId: [{ required: true, message: '容器不能为空', trigger: 'change' }]
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
      listUsage(this.queryParams).then(response => {
        this.usageList = response.rows
        this.total = response.total
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
    loadFormContainerOptionsByBatch(batchId) {
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
    containerTypeLabel(value) {
      const item = this.containerTypeOptions.find(option => option.value === value)
      return item ? item.label : value
    },
    handleBatchFormChange(value) {
      this.form.containerId = undefined
      this.loadFormContainerOptionsByBatch(value)
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        usageId: undefined,
        batchId: undefined,
        containerId: undefined,
        startTime: undefined,
        endTime: undefined,
        fillQuantityL: undefined,
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
      this.ids = selection.map(item => item.usageId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '新增容器流转'
    },
    handleUpdate(row) {
      this.reset()
      const usageId = row.usageId || this.ids
      getUsage(usageId).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改容器流转'
        this.loadFormContainerOptionsByBatch(this.form.batchId)
      })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        const request = this.form.usageId ? updateUsage(this.form) : addUsage(this.form)
        request.then(() => {
          this.$modal.msgSuccess(this.form.usageId ? '修改成功' : '新增成功')
          this.open = false
          this.getList()
        })
      })
    },
    handleDelete(row) {
      const usageIds = row.usageId || this.ids
      this.$modal.confirm('是否确认删除选中的容器流转记录？').then(() => delUsage(usageIds)).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    }
  }
}
</script>
