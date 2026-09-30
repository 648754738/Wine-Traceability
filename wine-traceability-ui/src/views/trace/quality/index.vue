<template>
  <div class="app-container">
    <el-form ref="queryForm" :model="queryParams" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="批次" prop="batchId">
        <el-select v-model="queryParams.batchId" placeholder="请选择批次" clearable filterable>
          <el-option v-for="item in batchOptions" :key="item.batchId" :label="item.batchNo" :value="item.batchId" />
        </el-select>
      </el-form-item>
      <el-form-item label="检测机构" prop="inspector">
        <el-input v-model="queryParams.inspector" placeholder="请输入检测机构" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="是否合格" prop="isQualified">
        <el-select v-model="queryParams.isQualified" placeholder="请选择" clearable>
          <el-option v-for="item in qualifiedOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['trace:quality:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['trace:quality:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['trace:quality:remove']">删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="qualityList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="批次号" align="center" prop="batchNo" min-width="150" />
      <el-table-column label="检测日期" align="center" prop="inspectionDate" width="120" />
      <el-table-column label="检测机构" align="center" prop="inspector" min-width="140" />
      <el-table-column label="酒精度" align="center" prop="alcohol" width="90" />
      <el-table-column label="总糖" align="center" prop="totalSugar" width="90" />
      <el-table-column label="挥发酸" align="center" prop="volatileAcid" width="90" />
      <el-table-column label="二氧化硫" align="center" prop="sulfite" width="100" />
      <el-table-column label="结论" align="center" prop="isQualified" width="90">
        <template slot-scope="scope">
          <span>{{ qualifiedLabel(scope.row.isQualified) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="160">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['trace:quality:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['trace:quality:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="860px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="95px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="生产批次" prop="batchId">
              <el-select v-model="form.batchId" placeholder="请选择生产批次" filterable style="width: 100%">
                <el-option v-for="item in batchOptions" :key="item.batchId" :label="item.batchNo" :value="item.batchId" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="检测日期" prop="inspectionDate">
              <el-date-picker v-model="form.inspectionDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择检测日期" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="检测机构" prop="inspector">
              <el-input v-model="form.inspector" placeholder="请输入检测机构或检测人" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="检测结论" prop="isQualified">
              <el-select v-model="form.isQualified" placeholder="请选择是否合格" style="width: 100%">
                <el-option v-for="item in qualifiedOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="6">
            <el-form-item label="酒精度" prop="alcohol">
              <el-input-number v-model="form.alcohol" :min="0" :precision="2" :step="0.1" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="总糖" prop="totalSugar">
              <el-input-number v-model="form.totalSugar" :min="0" :precision="2" :step="0.1" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="挥发酸" prop="volatileAcid">
              <el-input-number v-model="form.volatileAcid" :min="0" :precision="2" :step="0.1" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="二氧化硫" prop="sulfite">
              <el-input-number v-model="form.sulfite" :min="0" :precision="2" :step="0.1" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="报告地址" prop="reportUrl">
          <el-input v-model="form.reportUrl" placeholder="请输入报告附件地址" />
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
import { listQuality, getQuality, addQuality, updateQuality, delQuality } from '@/api/trace/quality'
import { listBatchOptions } from '@/api/trace/batch'

export default {
  name: 'TraceQualityReport',
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      qualityList: [],
      batchOptions: [],
      title: '',
      open: false,
      qualifiedOptions: [
        { label: '合格', value: '1' },
        { label: '不合格', value: '0' }
      ],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        batchId: undefined,
        inspector: undefined,
        isQualified: undefined
      },
      form: {},
      rules: {
        batchId: [{ required: true, message: '生产批次不能为空', trigger: 'change' }]
      }
    }
  },
  created() {
    this.getList()
    this.getBatchOptions()
  },
  methods: {
    getList() {
      this.loading = true
      listQuality(this.queryParams).then(response => {
        this.qualityList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    getBatchOptions() {
      listBatchOptions().then(response => {
        this.batchOptions = response.data || []
      })
    },
    qualifiedLabel(value) {
      const item = this.qualifiedOptions.find(option => option.value === value)
      return item ? item.label : value
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        reportId: undefined,
        batchId: undefined,
        inspectionDate: undefined,
        inspector: undefined,
        alcohol: undefined,
        totalSugar: undefined,
        volatileAcid: undefined,
        sulfite: undefined,
        isQualified: '1',
        reportUrl: undefined,
        remarks: undefined
      }
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
      this.ids = selection.map(item => item.reportId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '新增质检报告'
    },
    handleUpdate(row) {
      this.reset()
      const reportId = row.reportId || this.ids
      getQuality(reportId).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改质检报告'
      })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        const request = this.form.reportId ? updateQuality(this.form) : addQuality(this.form)
        request.then(() => {
          this.$modal.msgSuccess(this.form.reportId ? '修改成功' : '新增成功')
          this.open = false
          this.getList()
        })
      })
    },
    handleDelete(row) {
      const reportIds = row.reportId || this.ids
      this.$modal.confirm('是否确认删除选中的质检报告？').then(() => delQuality(reportIds)).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    }
  }
}
</script>