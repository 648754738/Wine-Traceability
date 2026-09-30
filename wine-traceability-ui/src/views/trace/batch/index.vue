<template>
  <div class="app-container">
    <el-form ref="queryForm" :model="queryParams" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="批次号" prop="batchNo">
        <el-input v-model="queryParams.batchNo" placeholder="请输入批次号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="酒庄" prop="enterpriseId">
        <el-select v-model="queryParams.enterpriseId" placeholder="请选择酒庄" clearable filterable @change="handleEnterpriseFilterChange">
          <el-option v-for="item in enterpriseOptions" :key="item.enterpriseId" :label="item.enterpriseName" :value="item.enterpriseId" />
        </el-select>
      </el-form-item>
      <el-form-item label="产品" prop="productId">
        <el-select v-model="queryParams.productId" placeholder="请选择产品" clearable filterable>
          <el-option v-for="item in queryProductOptions" :key="item.productId" :label="item.productName" :value="item.productId" />
        </el-select>
      </el-form-item>
      <el-form-item label="地块" prop="plotId">
        <el-select v-model="queryParams.plotId" placeholder="请选择地块" clearable filterable>
          <el-option v-for="item in queryPlotOptions" :key="item.plotId" :label="item.plotName" :value="item.plotId" />
        </el-select>
      </el-form-item>
      <el-form-item label="车间" prop="workshopId">
        <el-select v-model="queryParams.workshopId" placeholder="请选择车间" clearable filterable>
          <el-option v-for="item in queryWorkshopOptions" :key="item.workshopId" :label="item.workshopName" :value="item.workshopId" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option v-for="dict in dict.type.sys_normal_disable" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['trace:batch:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['trace:batch:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['trace:batch:remove']">删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="batchList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="批次号" align="center" prop="batchNo" min-width="150" />
      <el-table-column label="酒庄" align="center" prop="enterpriseName" min-width="140" />
      <el-table-column label="产品" align="center" prop="productName" min-width="160" />
      <el-table-column label="地块" align="center" prop="plotName" min-width="140" />
      <el-table-column label="车间" align="center" prop="workshopName" min-width="140" />
      <el-table-column label="入车间时间" align="center" prop="entryWorkshopTime" width="170" />
      <el-table-column label="采收日期" align="center" prop="harvestDate" width="120" />
      <el-table-column label="原酒产量(L)" align="center" prop="totalYieldL" width="110" />
      <el-table-column label="状态" align="center" prop="status" width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="160">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['trace:batch:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['trace:batch:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="900px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="95px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="批次号" prop="batchNo">
              <el-input v-model="form.batchNo" placeholder="请输入批次号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="酒庄" prop="enterpriseId">
              <el-select v-model="form.enterpriseId" placeholder="请选择酒庄" filterable style="width: 100%" @change="handleEnterpriseFormChange">
                <el-option v-for="item in enterpriseOptions" :key="item.enterpriseId" :label="item.enterpriseName" :value="item.enterpriseId" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="产品" prop="productId">
              <el-select v-model="form.productId" placeholder="请选择产品" filterable style="width: 100%">
                <el-option v-for="item in formProductOptions" :key="item.productId" :label="item.productName" :value="item.productId" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="地块" prop="plotId">
              <el-select v-model="form.plotId" placeholder="请选择地块" filterable style="width: 100%">
                <el-option v-for="item in formPlotOptions" :key="item.plotId" :label="item.plotName" :value="item.plotId" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="车间" prop="workshopId">
              <el-select v-model="form.workshopId" placeholder="请选择车间" filterable style="width: 100%">
                <el-option v-for="item in formWorkshopOptions" :key="item.workshopId" :label="item.workshopName" :value="item.workshopId" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="入车间时间" prop="entryWorkshopTime">
              <el-date-picker v-model="form.entryWorkshopTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择入车间时间" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="采收日期" prop="harvestDate">
              <el-date-picker v-model="form.harvestDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择采收日期" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="采收地描述" prop="harvestLocation">
              <el-input v-model="form.harvestLocation" placeholder="请输入采收地描述" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="原酒产量(L)" prop="totalYieldL">
              <el-input-number v-model="form.totalYieldL" :min="0" :precision="2" :step="1" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio v-for="dict in dict.type.sys_normal_disable" :key="dict.value" :label="dict.value">{{ dict.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入备注" />
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
import { listBatch, getBatch, addBatch, updateBatch, delBatch } from '@/api/trace/batch'
import { listEnterpriseOptions } from '@/api/trace/enterprise'
import { listProductOptions } from '@/api/trace/product'
import { listPlotOptions } from '@/api/trace/plot'
import { listWorkshopOptions } from '@/api/trace/workshop'

export default {
  name: 'TraceBatch',
  dicts: ['sys_normal_disable'],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      batchList: [],
      enterpriseOptions: [],
      queryProductOptions: [],
      queryPlotOptions: [],
      queryWorkshopOptions: [],
      formProductOptions: [],
      formPlotOptions: [],
      formWorkshopOptions: [],
      title: '',
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        batchNo: undefined,
        enterpriseId: undefined,
        productId: undefined,
        plotId: undefined,
        workshopId: undefined,
        status: undefined
      },
      form: {},
      rules: {
        batchNo: [{ required: true, message: '批次号不能为空', trigger: 'blur' }],
        enterpriseId: [{ required: true, message: '酒庄不能为空', trigger: 'change' }],
        productId: [{ required: true, message: '产品不能为空', trigger: 'change' }],
        plotId: [{ required: true, message: '地块不能为空', trigger: 'change' }],
        workshopId: [{ required: true, message: '车间不能为空', trigger: 'change' }]
      }
    }
  },
  created() {
    this.getList()
    this.getEnterpriseOptions()
  },
  methods: {
    getList() {
      this.loading = true
      listBatch(this.queryParams).then(response => {
        this.batchList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    getEnterpriseOptions() {
      listEnterpriseOptions().then(response => {
        this.enterpriseOptions = response.data || []
      })
    },
    loadProductOptions(enterpriseId, target) {
      if (!enterpriseId) {
        this[target] = []
        return
      }
      listProductOptions(enterpriseId).then(response => {
        this[target] = response.data || []
      })
    },
    loadPlotOptions(enterpriseId, target) {
      if (!enterpriseId) {
        this[target] = []
        return
      }
      listPlotOptions(enterpriseId).then(response => {
        this[target] = response.data || []
      })
    },
    loadWorkshopOptions(enterpriseId, target) {
      if (!enterpriseId) {
        this[target] = []
        return
      }
      listWorkshopOptions(enterpriseId).then(response => {
        this[target] = response.data || []
      })
    },
    handleEnterpriseFilterChange(value) {
      this.queryParams.productId = undefined
      this.queryParams.plotId = undefined
      this.queryParams.workshopId = undefined
      this.loadProductOptions(value, 'queryProductOptions')
      this.loadPlotOptions(value, 'queryPlotOptions')
      this.loadWorkshopOptions(value, 'queryWorkshopOptions')
    },
    handleEnterpriseFormChange(value) {
      this.form.productId = undefined
      this.form.plotId = undefined
      this.form.workshopId = undefined
      this.loadProductOptions(value, 'formProductOptions')
      this.loadPlotOptions(value, 'formPlotOptions')
      this.loadWorkshopOptions(value, 'formWorkshopOptions')
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        batchId: undefined,
        batchNo: undefined,
        enterpriseId: undefined,
        productId: undefined,
        plotId: undefined,
        workshopId: undefined,
        entryWorkshopTime: undefined,
        harvestDate: undefined,
        harvestLocation: undefined,
        totalYieldL: undefined,
        status: '0',
        remark: undefined
      }
      this.formProductOptions = []
      this.formPlotOptions = []
      this.formWorkshopOptions = []
      this.resetForm('form')
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.resetForm('queryForm')
      this.queryProductOptions = []
      this.queryPlotOptions = []
      this.queryWorkshopOptions = []
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.batchId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '新增生产批次'
    },
    handleUpdate(row) {
      this.reset()
      const batchId = row.batchId || this.ids
      getBatch(batchId).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改生产批次'
        this.loadProductOptions(this.form.enterpriseId, 'formProductOptions')
        this.loadPlotOptions(this.form.enterpriseId, 'formPlotOptions')
        this.loadWorkshopOptions(this.form.enterpriseId, 'formWorkshopOptions')
      })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        const request = this.form.batchId ? updateBatch(this.form) : addBatch(this.form)
        request.then(() => {
          this.$modal.msgSuccess(this.form.batchId ? '修改成功' : '新增成功')
          this.open = false
          this.getList()
        })
      })
    },
    handleDelete(row) {
      const batchIds = row.batchId || this.ids
      this.$modal.confirm('是否确认删除选中的生产批次数据？').then(() => delBatch(batchIds)).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    }
  }
}
</script>
