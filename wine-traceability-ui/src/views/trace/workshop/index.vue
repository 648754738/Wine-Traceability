<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="车间名称" prop="workshopName">
        <el-input v-model="queryParams.workshopName" placeholder="请输入车间名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="酒庄" prop="enterpriseId">
        <el-select v-model="queryParams.enterpriseId" placeholder="请选择酒庄" clearable filterable>
          <el-option v-for="item in enterpriseOptions" :key="item.enterpriseId" :label="item.enterpriseName" :value="item.enterpriseId" />
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
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['trace:workshop:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['trace:workshop:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['trace:workshop:remove']">删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="workshopList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="车间名称" align="center" prop="workshopName" min-width="160" />
      <el-table-column label="所属酒庄" align="center" prop="enterpriseName" min-width="160" />
      <el-table-column label="位置" align="center" prop="location" min-width="180" show-overflow-tooltip />
      <el-table-column label="负责人" align="center" prop="supervisor" width="120" />
      <el-table-column label="状态" align="center" prop="status" width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="160">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['trace:workshop:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['trace:workshop:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="620px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="所属酒庄" prop="enterpriseId">
          <el-select v-model="form.enterpriseId" placeholder="请选择所属酒庄" filterable style="width: 100%">
            <el-option v-for="item in enterpriseOptions" :key="item.enterpriseId" :label="item.enterpriseName" :value="item.enterpriseId" />
          </el-select>
        </el-form-item>
        <el-form-item label="车间名称" prop="workshopName">
          <el-input v-model="form.workshopName" placeholder="请输入车间名称" />
        </el-form-item>
        <el-form-item label="位置" prop="location">
          <el-input v-model="form.location" placeholder="请输入位置描述" />
        </el-form-item>
        <el-form-item label="负责人" prop="supervisor">
          <el-input v-model="form.supervisor" placeholder="请输入负责人" />
        </el-form-item>
        <el-form-item label="状态">
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
import { listWorkshop, getWorkshop, addWorkshop, updateWorkshop, delWorkshop } from '@/api/trace/workshop'
import { listEnterpriseOptions } from '@/api/trace/enterprise'

export default {
  name: 'TraceWorkshop',
  dicts: ['sys_normal_disable'],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      workshopList: [],
      enterpriseOptions: [],
      title: '',
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        workshopName: undefined,
        enterpriseId: undefined,
        status: undefined
      },
      form: {},
      rules: {
        enterpriseId: [{ required: true, message: '所属酒庄不能为空', trigger: 'change' }],
        workshopName: [{ required: true, message: '车间名称不能为空', trigger: 'blur' }]
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
      listWorkshop(this.queryParams).then(response => {
        this.workshopList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    getEnterpriseOptions() {
      listEnterpriseOptions().then(response => {
        this.enterpriseOptions = response.data || []
      })
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        workshopId: undefined,
        enterpriseId: undefined,
        workshopName: undefined,
        location: undefined,
        supervisor: undefined,
        status: '0',
        remark: undefined
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
      this.ids = selection.map(item => item.workshopId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加车间'
    },
    handleUpdate(row) {
      this.reset()
      const workshopId = row.workshopId || this.ids
      getWorkshop(workshopId).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改车间'
      })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        const request = this.form.workshopId ? updateWorkshop(this.form) : addWorkshop(this.form)
        request.then(() => {
          this.$modal.msgSuccess(this.form.workshopId ? '修改成功' : '新增成功')
          this.open = false
          this.getList()
        })
      })
    },
    handleDelete(row) {
      const workshopIds = row.workshopId || this.ids
      this.$modal.confirm('是否确认删除选中的车间数据？').then(() => delWorkshop(workshopIds)).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    }
  }
}
</script>
