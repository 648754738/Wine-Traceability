<template>
  <div class="app-container">
    <el-form ref="queryForm" :model="queryParams" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="批次" prop="batchId">
        <el-select v-model="queryParams.batchId" placeholder="请选择批次" clearable filterable>
          <el-option v-for="item in batchOptions" :key="item.batchId" :label="item.batchNo" :value="item.batchId" />
        </el-select>
      </el-form-item>
      <el-form-item label="工序类型" prop="processType">
        <el-input v-model="queryParams.processType" placeholder="请输入工序类型" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="操作人" prop="operatorName">
        <el-input v-model="queryParams.operatorName" placeholder="请输入操作人" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['trace:process:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['trace:process:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['trace:process:remove']">删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="processList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="批次号" align="center" prop="batchNo" min-width="150" />
      <el-table-column label="工序类型" align="center" prop="processType" min-width="140" />
      <el-table-column label="开始时间" align="center" prop="startTime" width="170" />
      <el-table-column label="结束时间" align="center" prop="endTime" width="170" />
      <el-table-column label="温控范围" align="center" prop="tempControl" min-width="120" />
      <el-table-column label="操作人" align="center" prop="operatorName" width="100" />
      <el-table-column label="工序说明" align="center" prop="remarks" min-width="180" show-overflow-tooltip />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="160">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['trace:process:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['trace:process:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="760px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="95px">
        <el-form-item label="生产批次" prop="batchId">
          <el-select v-model="form.batchId" placeholder="请选择生产批次" filterable style="width: 100%">
            <el-option v-for="item in batchOptions" :key="item.batchId" :label="item.batchNo" :value="item.batchId" />
          </el-select>
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="工序类型" prop="processType">
              <el-input v-model="form.processType" placeholder="如破碎、发酵、陈酿" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="操作人" prop="operatorName">
              <el-input v-model="form.operatorName" placeholder="请输入操作人" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="开始时间" prop="startTime">
              <el-date-picker v-model="form.startTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择开始时间" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束时间" prop="endTime">
              <el-date-picker v-model="form.endTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择结束时间" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="温控范围" prop="tempControl">
          <el-input v-model="form.tempControl" placeholder="例如 18-22℃" />
        </el-form-item>
        <el-form-item label="工序说明" prop="remarks">
          <el-input v-model="form.remarks" type="textarea" :rows="3" placeholder="请输入工序说明" />
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
import { listProcess, getProcess, addProcess, updateProcess, delProcess } from '@/api/trace/process'
import { listBatchOptions } from '@/api/trace/batch'

export default {
  name: 'TraceProcessRecord',
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      processList: [],
      batchOptions: [],
      title: '',
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        batchId: undefined,
        processType: undefined,
        operatorName: undefined
      },
      form: {},
      rules: {
        batchId: [{ required: true, message: '生产批次不能为空', trigger: 'change' }],
        processType: [{ required: true, message: '工序类型不能为空', trigger: 'blur' }]
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
      listProcess(this.queryParams).then(response => {
        this.processList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    getBatchOptions() {
      listBatchOptions().then(response => {
        this.batchOptions = response.data || []
      })
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        processId: undefined,
        batchId: undefined,
        processType: undefined,
        startTime: undefined,
        endTime: undefined,
        tempControl: undefined,
        operatorName: undefined,
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
      this.ids = selection.map(item => item.processId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '新增工序记录'
    },
    handleUpdate(row) {
      this.reset()
      const processId = row.processId || this.ids
      getProcess(processId).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改工序记录'
      })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        const request = this.form.processId ? updateProcess(this.form) : addProcess(this.form)
        request.then(() => {
          this.$modal.msgSuccess(this.form.processId ? '修改成功' : '新增成功')
          this.open = false
          this.getList()
        })
      })
    },
    handleDelete(row) {
      const processIds = row.processId || this.ids
      this.$modal.confirm('是否确认删除选中的工序记录？').then(() => delProcess(processIds)).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    }
  }
}
</script>
