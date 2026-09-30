<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="产品名称" prop="productName">
        <el-input v-model="queryParams.productName" placeholder="请输入产品名称" clearable @keyup.enter.native="handleQuery" />
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
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['trace:product:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['trace:product:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['trace:product:remove']">删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="productList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="产品名称" align="center" prop="productName" min-width="180" />
      <el-table-column label="所属酒庄" align="center" prop="enterpriseName" min-width="160" />
      <el-table-column label="产品类别" align="center" prop="category" width="120" />
      <el-table-column label="葡萄品种" align="center" prop="varietyNames" min-width="200" show-overflow-tooltip />
      <el-table-column label="酒精度" align="center" prop="alcoholContent" width="100" />
      <el-table-column label="净含量" align="center" prop="netContent" width="100" />
      <el-table-column label="状态" align="center" prop="status" width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="160">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['trace:product:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['trace:product:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="920px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="95px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="所属酒庄" prop="enterpriseId">
              <el-select v-model="form.enterpriseId" placeholder="请选择所属酒庄" filterable style="width: 100%">
                <el-option v-for="item in enterpriseOptions" :key="item.enterpriseId" :label="item.enterpriseName" :value="item.enterpriseId" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="产品名称" prop="productName">
              <el-input v-model="form.productName" placeholder="请输入产品名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="产品类别" prop="category">
              <el-input v-model="form.category" placeholder="如干红、半干" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="酒精度" prop="alcoholContent">
              <el-input-number v-model="form.alcoholContent" :min="0" :precision="2" :step="0.1" controls-position="right" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="净含量" prop="netContent">
              <el-input v-model="form.netContent" placeholder="如750ml" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="保质期(月)" prop="shelfLife">
              <el-input-number v-model="form.shelfLife" :min="0" controls-position="right" />
            </el-form-item>
          </el-col>
          <el-col :span="16">
            <el-form-item label="状态">
              <el-radio-group v-model="form.status">
                <el-radio v-for="dict in dict.type.sys_normal_disable" :key="dict.value" :label="dict.value">{{ dict.label }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="品种配比">
          <el-button size="mini" type="primary" plain icon="el-icon-plus" @click="handleAddVarietyRow">添加品种</el-button>
          <el-table :data="form.varietyList" border size="mini" style="margin-top: 10px">
            <el-table-column label="葡萄品种" min-width="220">
              <template slot-scope="scope">
                <el-select v-model="scope.row.varietyId" placeholder="请选择葡萄品种" filterable style="width: 100%">
                  <el-option v-for="item in varietyOptions" :key="item.varietyId" :label="item.varietyName" :value="item.varietyId" />
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="占比(%)" width="150">
              <template slot-scope="scope">
                <el-input-number v-model="scope.row.percentage" :min="0" :max="100" :precision="2" :step="1" controls-position="right" />
              </template>
            </el-table-column>
            <el-table-column label="操作" width="90" align="center">
              <template slot-scope="scope">
                <el-button size="mini" type="text" icon="el-icon-delete" @click="handleRemoveVarietyRow(scope.$index)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
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
import { listProduct, getProduct, addProduct, updateProduct, delProduct } from '@/api/trace/product'
import { listEnterpriseOptions } from '@/api/trace/enterprise'
import { listVarietyOptions } from '@/api/trace/variety'

export default {
  name: 'TraceProduct',
  dicts: ['sys_normal_disable'],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      productList: [],
      enterpriseOptions: [],
      varietyOptions: [],
      title: '',
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        productName: undefined,
        enterpriseId: undefined,
        status: undefined
      },
      form: {},
      rules: {
        enterpriseId: [{ required: true, message: '所属酒庄不能为空', trigger: 'change' }],
        productName: [{ required: true, message: '产品名称不能为空', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
    this.getEnterpriseOptions()
    this.getVarietyOptions()
  },
  methods: {
    getList() {
      this.loading = true
      listProduct(this.queryParams).then(response => {
        this.productList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    getEnterpriseOptions() {
      listEnterpriseOptions().then(response => {
        this.enterpriseOptions = response.data || []
      })
    },
    getVarietyOptions() {
      listVarietyOptions().then(response => {
        this.varietyOptions = response.data || []
      })
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        productId: undefined,
        enterpriseId: undefined,
        productName: undefined,
        category: undefined,
        alcoholContent: undefined,
        netContent: undefined,
        shelfLife: undefined,
        status: '0',
        varietyList: [],
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
      this.ids = selection.map(item => item.productId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset()
      this.handleAddVarietyRow()
      this.open = true
      this.title = '添加产品'
    },
    handleUpdate(row) {
      this.reset()
      const productId = row.productId || this.ids
      getProduct(productId).then(response => {
        this.form = response.data
        if (!this.form.varietyList) {
          this.form.varietyList = []
        }
        this.open = true
        this.title = '修改产品'
      })
    },
    handleAddVarietyRow() {
      this.form.varietyList.push({
        varietyId: undefined,
        percentage: undefined
      })
    },
    handleRemoveVarietyRow(index) {
      this.form.varietyList.splice(index, 1)
    },
    validateVarietyList() {
      if (!this.form.varietyList || !this.form.varietyList.length) {
        this.$modal.msgError('请至少维护一条品种配比')
        return false
      }
      const ids = []
      for (const item of this.form.varietyList) {
        if (!item.varietyId) {
          this.$modal.msgError('品种配比中存在未选择的葡萄品种')
          return false
        }
        if (ids.includes(item.varietyId)) {
          this.$modal.msgError('品种配比中存在重复的葡萄品种')
          return false
        }
        ids.push(item.varietyId)
      }
      return true
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (!valid || !this.validateVarietyList()) return
        const request = this.form.productId ? updateProduct(this.form) : addProduct(this.form)
        request.then(() => {
          this.$modal.msgSuccess(this.form.productId ? '修改成功' : '新增成功')
          this.open = false
          this.getList()
        })
      })
    },
    handleDelete(row) {
      const productIds = row.productId || this.ids
      this.$modal.confirm('是否确认删除选中的产品数据？').then(() => delProduct(productIds)).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    }
  }
}
</script>
