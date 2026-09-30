<template>
  <div class="app-container">
    <el-form ref="queryForm" :model="queryParams" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="单据类型" prop="billType">
        <el-select v-model="queryParams.billType" placeholder="请选择类型" clearable>
          <el-option v-for="item in billTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="单据编号" prop="billNo">
        <el-input v-model="queryParams.billNo" placeholder="请输入单据编号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['trace:warehouse:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['trace:warehouse:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['trace:warehouse:remove']">删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="warehouseList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="单据编号" prop="billNo" min-width="150" />
      <el-table-column label="单据类型" width="100">
        <template slot-scope="scope">
          <el-tag size="mini">{{ billTypeLabel(scope.row.billType) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="明细数" prop="detailCount" width="90" />
      <el-table-column label="总数量(瓶)" prop="totalQuantity" width="110" />
      <el-table-column label="操作时间" prop="operateTime" width="170" />
      <el-table-column label="备注" prop="remark" min-width="180" show-overflow-tooltip />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="160">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['trace:warehouse:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['trace:warehouse:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="1080px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="90px">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="单据类型" prop="billType">
              <el-select v-model="form.billType" placeholder="请选择类型" style="width: 100%">
                <el-option v-for="item in billTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="单据编号" prop="billNo">
              <el-input v-model="form.billNo" placeholder="请输入单据编号" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="操作时间" prop="operateTime">
              <el-date-picker v-model="form.operateTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择操作时间" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>

        <el-form-item label="单据明细">
          <el-button size="mini" type="primary" plain icon="el-icon-plus" @click="handleAddDetail">添加明细</el-button>
          <el-table :data="form.detailList" border size="mini" style="margin-top: 10px">
            <el-table-column label="灌装批次" min-width="250">
              <template slot-scope="scope">
                <el-select v-model="scope.row.bottlingId" placeholder="请选择灌装记录" filterable style="width: 100%" @change="handleBottlingChange(scope.row)">
                  <el-option v-for="item in bottlingOptions" :key="item.bottlingId" :label="bottlingLabel(item)" :value="item.bottlingId" />
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="生产批次" prop="batchNo" min-width="140" />
            <el-table-column label="码段" min-width="180">
              <template slot-scope="scope">
                <span>{{ traceRangeLabel(scope.row) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="数量(瓶)" width="130">
              <template slot-scope="scope">
                <el-input-number v-model="scope.row.quantity" :min="1" :max="scope.row.bottleCount || 999999" controls-position="right" style="width: 100%" />
              </template>
            </el-table-column>
            <el-table-column label="操作" width="90" align="center">
              <template slot-scope="scope">
                <el-button size="mini" type="text" icon="el-icon-delete" @click="handleRemoveDetail(scope.$index)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
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
import { listWarehouse, getWarehouse, addWarehouse, updateWarehouse, delWarehouse } from '@/api/trace/warehouse'
import { listBottlingOptions } from '@/api/trace/bottling'

export default {
  name: 'TraceWarehouseBill',
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      open: false,
      title: '',
      warehouseList: [],
      bottlingOptions: [],
      billTypeOptions: [
        { label: '入库', value: 'IN' },
        { label: '出库', value: 'OUT' }
      ],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        billType: undefined,
        billNo: undefined
      },
      form: {},
      rules: {
        billType: [{ required: true, message: '单据类型不能为空', trigger: 'change' }],
        billNo: [{ required: true, message: '单据编号不能为空', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
    this.loadBottlingOptions()
  },
  methods: {
    billTypeLabel(value) {
      const matched = this.billTypeOptions.find(item => item.value === value)
      return matched ? matched.label : value || '-'
    },
    bottlingLabel(item) {
      return `${item.batchNo || '-'} / ${item.traceCodePrefix || ''}${item.traceCodeStart || ''}-${item.traceCodePrefix || ''}${item.traceCodeEnd || ''}`
    },
    traceRangeLabel(row) {
      if (!row.traceCodePrefix || row.traceCodeStart === undefined || row.traceCodeEnd === undefined) {
        return '-'
      }
      return `${row.traceCodePrefix}${row.traceCodeStart} - ${row.traceCodePrefix}${row.traceCodeEnd}`
    },
    getList() {
      this.loading = true
      listWarehouse(this.queryParams).then(response => {
        this.warehouseList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    loadBottlingOptions() {
      listBottlingOptions().then(response => {
        this.bottlingOptions = response.data || []
      })
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
      this.ids = selection.map(item => item.billId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    reset() {
      this.form = {
        billId: undefined,
        billType: 'IN',
        billNo: undefined,
        operateTime: undefined,
        remark: undefined,
        detailList: []
      }
      this.resetForm('form')
    },
    cancel() {
      this.open = false
      this.reset()
    },
    handleAdd() {
      this.reset()
      this.handleAddDetail()
      this.open = true
      this.title = '新增仓储单据'
    },
    handleUpdate(row) {
      this.reset()
      const billId = row.billId || this.ids
      getWarehouse(billId).then(response => {
        this.form = response.data
        if (!this.form.detailList) {
          this.form.detailList = []
        }
        this.open = true
        this.title = '修改仓储单据'
      })
    },
    handleAddDetail() {
      this.form.detailList.push({
        bottlingId: undefined,
        batchNo: undefined,
        traceCodePrefix: undefined,
        traceCodeStart: undefined,
        traceCodeEnd: undefined,
        bottleCount: undefined,
        quantity: 1,
        startTraceCode: undefined,
        endTraceCode: undefined
      })
    },
    handleRemoveDetail(index) {
      this.form.detailList.splice(index, 1)
    },
    handleBottlingChange(row) {
      const target = this.bottlingOptions.find(item => item.bottlingId === row.bottlingId)
      if (!target) return
      row.batchNo = target.batchNo
      row.traceCodePrefix = target.traceCodePrefix
      row.traceCodeStart = target.traceCodeStart
      row.traceCodeEnd = target.traceCodeEnd
      row.bottleCount = target.bottleCount
      row.quantity = target.bottleCount || 1
      row.startTraceCode = `${target.traceCodePrefix || ''}${target.traceCodeStart || ''}`
      row.endTraceCode = `${target.traceCodePrefix || ''}${target.traceCodeEnd || ''}`
    },
    validateDetailList() {
      if (!this.form.detailList || !this.form.detailList.length) {
        this.$modal.msgError('请至少维护一条单据明细')
        return false
      }
      for (const item of this.form.detailList) {
        if (!item.bottlingId) {
          this.$modal.msgError('存在未选择灌装记录的明细')
          return false
        }
        if (!item.quantity || item.quantity <= 0) {
          this.$modal.msgError('明细数量必须大于 0')
          return false
        }
      }
      return true
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (!valid || !this.validateDetailList()) return
        const request = this.form.billId ? updateWarehouse(this.form) : addWarehouse(this.form)
        request.then(() => {
          this.$modal.msgSuccess(this.form.billId ? '修改成功' : '新增成功')
          this.open = false
          this.getList()
        })
      })
    },
    handleDelete(row) {
      const billIds = row.billId || this.ids
      this.$modal.confirm('是否确认删除选中的仓储单据？').then(() => delWarehouse(billIds)).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    }
  }
}
</script>