<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="78px">
      <el-form-item label="容器编码" prop="containerCode">
        <el-input v-model="queryParams.containerCode" placeholder="请输入容器编码" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="酒庄" prop="enterpriseId">
        <el-select v-model="queryParams.enterpriseId" placeholder="请选择酒庄" clearable filterable @change="handleEnterpriseFilterChange">
          <el-option v-for="item in enterpriseOptions" :key="item.enterpriseId" :label="item.enterpriseName" :value="item.enterpriseId" />
        </el-select>
      </el-form-item>
      <el-form-item label="车间" prop="workshopId">
        <el-select v-model="queryParams.workshopId" placeholder="请选择车间" clearable filterable>
          <el-option v-for="item in workshopOptions" :key="item.workshopId" :label="item.workshopName" :value="item.workshopId" />
        </el-select>
      </el-form-item>
      <el-form-item label="容器类型" prop="containerType">
        <el-select v-model="queryParams.containerType" placeholder="请选择类型" clearable>
          <el-option v-for="item in containerTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="容器状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option v-for="item in containerStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['trace:container:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['trace:container:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['trace:container:remove']">删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="containerList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="容器编码" align="center" prop="containerCode" min-width="130" />
      <el-table-column label="容器类型" align="center" prop="containerType" width="120">
        <template slot-scope="scope">
          <span>{{ containerTypeLabel(scope.row.containerType) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="所属酒庄" align="center" prop="enterpriseName" min-width="150" />
      <el-table-column label="所属车间" align="center" prop="workshopName" min-width="150" />
      <el-table-column label="容积(L)" align="center" prop="capacityL" width="100" />
      <el-table-column label="位置" align="center" prop="locationDetail" min-width="160" show-overflow-tooltip />
      <el-table-column label="状态" align="center" prop="status" width="110">
        <template slot-scope="scope">
          <span>{{ containerStatusLabel(scope.row.status) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="160">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['trace:container:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['trace:container:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="760px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="95px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="所属酒庄" prop="enterpriseId">
              <el-select v-model="form.enterpriseId" placeholder="请选择所属酒庄" filterable style="width: 100%" @change="handleEnterpriseFormChange">
                <el-option v-for="item in enterpriseOptions" :key="item.enterpriseId" :label="item.enterpriseName" :value="item.enterpriseId" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属车间" prop="workshopId">
              <el-select v-model="form.workshopId" placeholder="请选择所属车间" clearable filterable style="width: 100%">
                <el-option v-for="item in formWorkshopOptions" :key="item.workshopId" :label="item.workshopName" :value="item.workshopId" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="容器类型" prop="containerType">
              <el-select v-model="form.containerType" placeholder="请选择容器类型" style="width: 100%">
                <el-option v-for="item in containerTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="容器编码" prop="containerCode">
              <el-input v-model="form.containerCode" placeholder="请输入容器编码" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="容积(L)" prop="capacityL">
              <el-input-number v-model="form.capacityL" :min="0" :precision="2" :step="1" controls-position="right" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="容器状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择容器状态" style="width: 100%">
                <el-option v-for="item in containerStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="详细位置" prop="locationDetail">
          <el-input v-model="form.locationDetail" placeholder="请输入详细位置" />
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="购入日期" prop="purchaseDate">
              <el-date-picker v-model="form.purchaseDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择购入日期" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="最近烘烤" prop="lastToasted">
              <el-date-picker v-model="form.lastToasted" type="date" value-format="yyyy-MM-dd" placeholder="请选择最近烘烤日期" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
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
import { listContainer, getContainer, addContainer, updateContainer, delContainer } from '@/api/trace/container'
import { listEnterpriseOptions } from '@/api/trace/enterprise'
import { listWorkshopOptions } from '@/api/trace/workshop'

export default {
  name: 'TraceContainer',
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      containerList: [],
      enterpriseOptions: [],
      workshopOptions: [],
      formWorkshopOptions: [],
      title: '',
      open: false,
      containerTypeOptions: [
        { label: '橡木桶', value: 'oak_barrel' },
        { label: '不锈钢罐', value: 'stainless_tank' },
        { label: '陶罐', value: 'ceramic_tank' }
      ],
      containerStatusOptions: [
        { label: '空闲', value: 'idle' },
        { label: '使用中', value: 'using' },
        { label: '清洗中', value: 'cleaning' },
        { label: '报废', value: 'scrapped' }
      ],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        enterpriseId: undefined,
        workshopId: undefined,
        containerType: undefined,
        containerCode: undefined,
        status: undefined
      },
      form: {},
      rules: {
        enterpriseId: [{ required: true, message: '所属酒庄不能为空', trigger: 'change' }],
        containerType: [{ required: true, message: '容器类型不能为空', trigger: 'change' }],
        containerCode: [{ required: true, message: '容器编码不能为空', trigger: 'blur' }]
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
      listContainer(this.queryParams).then(response => {
        this.containerList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    getEnterpriseOptions() {
      listEnterpriseOptions().then(response => {
        this.enterpriseOptions = response.data || []
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
      this.queryParams.workshopId = undefined
      this.loadWorkshopOptions(value, 'workshopOptions')
    },
    handleEnterpriseFormChange(value) {
      this.form.workshopId = undefined
      this.loadWorkshopOptions(value, 'formWorkshopOptions')
    },
    containerTypeLabel(value) {
      const item = this.containerTypeOptions.find(option => option.value === value)
      return item ? item.label : value
    },
    containerStatusLabel(value) {
      const item = this.containerStatusOptions.find(option => option.value === value)
      return item ? item.label : value
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        containerId: undefined,
        enterpriseId: undefined,
        workshopId: undefined,
        containerType: undefined,
        containerCode: undefined,
        capacityL: undefined,
        locationDetail: undefined,
        status: 'idle',
        purchaseDate: undefined,
        lastToasted: undefined,
        remark: undefined
      }
      this.formWorkshopOptions = []
      this.resetForm('form')
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.resetForm('queryForm')
      this.workshopOptions = []
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.containerId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加容器'
    },
    handleUpdate(row) {
      this.reset()
      const containerId = row.containerId || this.ids
      getContainer(containerId).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改容器'
        this.loadWorkshopOptions(this.form.enterpriseId, 'formWorkshopOptions')
      })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        const request = this.form.containerId ? updateContainer(this.form) : addContainer(this.form)
        request.then(() => {
          this.$modal.msgSuccess(this.form.containerId ? '修改成功' : '新增成功')
          this.open = false
          this.getList()
        })
      })
    },
    handleDelete(row) {
      const containerIds = row.containerId || this.ids
      this.$modal.confirm('是否确认删除选中的容器数据？').then(() => delContainer(containerIds)).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    }
  }
}
</script>
