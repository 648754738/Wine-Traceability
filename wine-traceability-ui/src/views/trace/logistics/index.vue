<template>
  <div class="app-container">
    <el-form ref="queryForm" :model="queryParams" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="出库单号" prop="outBillNo">
        <el-input v-model="queryParams.outBillNo" placeholder="请输入出库单号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="承运商" prop="carrier">
        <el-input v-model="queryParams.carrier" placeholder="请输入承运商" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="运单号" prop="trackingNo">
        <el-input v-model="queryParams.trackingNo" placeholder="请输入运单号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['trace:logistics:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['trace:logistics:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['trace:logistics:remove']">删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="logisticsList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="出库单号" prop="outBillNo" min-width="150" />
      <el-table-column label="承运商" prop="carrier" min-width="120" />
      <el-table-column label="运单号" prop="trackingNo" min-width="150" />
      <el-table-column label="收货方" prop="consignee" min-width="120" />
      <el-table-column label="目的地" prop="destination" min-width="160" show-overflow-tooltip />
      <el-table-column label="发货日期" prop="shipDate" width="170" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="160">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['trace:logistics:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['trace:logistics:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="760px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="90px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="出库单号" prop="outBillId">
              <el-select v-model="form.outBillId" placeholder="请选择出库单" filterable style="width: 100%">
                <el-option v-for="item in outBillOptions" :key="item.billId" :label="item.billNo" :value="item.billId" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="承运商" prop="carrier">
              <el-input v-model="form.carrier" placeholder="请输入承运商" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="运单号" prop="trackingNo">
              <el-input v-model="form.trackingNo" placeholder="请输入运单号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="收货方" prop="consignee">
              <el-input v-model="form.consignee" placeholder="请输入收货方" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="目的地" prop="destination">
              <el-input v-model="form.destination" placeholder="请输入目的地" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="发货日期" prop="shipDate">
              <el-date-picker v-model="form.shipDate" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择发货日期" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注" />
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
import { listLogistics, getLogistics, addLogistics, updateLogistics, delLogistics } from '@/api/trace/logistics'
import { listWarehouseOptions } from '@/api/trace/warehouse'

export default {
  name: 'TraceLogistics',
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
      logisticsList: [],
      outBillOptions: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        outBillNo: undefined,
        carrier: undefined,
        trackingNo: undefined
      },
      form: {},
      rules: {
        outBillId: [{ required: true, message: '出库单不能为空', trigger: 'change' }]
      }
    }
  },
  created() {
    this.getList()
    this.loadOutBillOptions()
  },
  methods: {
    getList() {
      this.loading = true
      listLogistics(this.queryParams).then(response => {
        this.logisticsList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    loadOutBillOptions() {
      listWarehouseOptions('OUT').then(response => {
        this.outBillOptions = response.data || []
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
      this.ids = selection.map(item => item.logisticsId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    reset() {
      this.form = {
        logisticsId: undefined,
        outBillId: undefined,
        carrier: undefined,
        trackingNo: undefined,
        consignee: undefined,
        destination: undefined,
        shipDate: undefined,
        remark: undefined
      }
      this.resetForm('form')
    },
    cancel() {
      this.open = false
      this.reset()
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '新增物流信息'
    },
    handleUpdate(row) {
      this.reset()
      const logisticsId = row.logisticsId || this.ids
      getLogistics(logisticsId).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改物流信息'
      })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        const request = this.form.logisticsId ? updateLogistics(this.form) : addLogistics(this.form)
        request.then(() => {
          this.$modal.msgSuccess(this.form.logisticsId ? '修改成功' : '新增成功')
          this.open = false
          this.getList()
        })
      })
    },
    handleDelete(row) {
      const logisticsIds = row.logisticsId || this.ids
      this.$modal.confirm('是否确认删除选中的物流信息？').then(() => delLogistics(logisticsIds)).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    }
  }
}
</script>