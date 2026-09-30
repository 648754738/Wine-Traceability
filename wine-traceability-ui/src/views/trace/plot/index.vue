<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="地块名称" prop="plotName">
        <el-input
          v-model="queryParams.plotName"
          placeholder="请输入地块名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="地块编码" prop="plotCode">
        <el-input
          v-model="queryParams.plotCode"
          placeholder="请输入地块编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="酒庄" prop="enterpriseId">
        <el-select v-model="queryParams.enterpriseId" placeholder="请选择酒庄" clearable filterable>
          <el-option
            v-for="item in enterpriseOptions"
            :key="item.enterpriseId"
            :label="item.enterpriseName"
            :value="item.enterpriseId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option
            v-for="dict in dict.type.sys_normal_disable"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['trace:plot:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['trace:plot:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['trace:plot:remove']"
        >删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="plotList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="地块名称" align="center" prop="plotName" min-width="150" />
      <el-table-column label="地块编码" align="center" prop="plotCode" min-width="140" />
      <el-table-column label="所属酒庄" align="center" prop="enterpriseName" min-width="150" />
      <el-table-column label="所属产区" align="center" prop="regionName" min-width="150" />
      <el-table-column label="面积(公顷)" align="center" prop="areaHa" width="110" />
      <el-table-column label="主要品种" align="center" prop="plantedVariety" min-width="120" />
      <el-table-column label="状态" align="center" prop="status" width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="160">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['trace:plot:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['trace:plot:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <el-dialog :title="title" :visible.sync="open" width="760px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="95px">
        <el-form-item label="所属酒庄" prop="enterpriseId">
          <el-select v-model="form.enterpriseId" placeholder="请选择所属酒庄" filterable style="width: 100%">
            <el-option
              v-for="item in enterpriseOptions"
              :key="item.enterpriseId"
              :label="item.enterpriseName"
              :value="item.enterpriseId"
            />
          </el-select>
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="地块名称" prop="plotName">
              <el-input v-model="form.plotName" placeholder="请输入地块名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="地块编码" prop="plotCode">
              <el-input v-model="form.plotCode" placeholder="请输入地块编码" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="面积(公顷)" prop="areaHa">
              <el-input-number v-model="form.areaHa" :min="0" :precision="2" :step="0.1" controls-position="right" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="海拔(米)" prop="elevationM">
              <el-input-number v-model="form.elevationM" :min="0" :precision="2" :step="1" controls-position="right" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="土壤类型" prop="soilType">
              <el-input v-model="form.soilType" placeholder="请输入土壤类型" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="主要品种" prop="plantedVariety">
              <el-input v-model="form.plantedVariety" placeholder="请输入主要品种" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="平均树龄" prop="vineAge">
              <el-input-number v-model="form.vineAge" :min="0" controls-position="right" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态">
              <el-radio-group v-model="form.status">
                <el-radio
                  v-for="dict in dict.type.sys_normal_disable"
                  :key="dict.value"
                  :label="dict.value"
                >{{ dict.label }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="GPS坐标" prop="gpsCoordinates">
          <el-input v-model="form.gpsCoordinates" placeholder="请输入GPS坐标" />
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
import { listPlot, getPlot, addPlot, updatePlot, delPlot } from '@/api/trace/plot'
import { listEnterpriseOptions } from '@/api/trace/enterprise'

export default {
  name: 'TracePlot',
  dicts: ['sys_normal_disable'],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      plotList: [],
      enterpriseOptions: [],
      title: '',
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        plotName: undefined,
        plotCode: undefined,
        enterpriseId: undefined,
        status: undefined
      },
      form: {},
      rules: {
        enterpriseId: [
          { required: true, message: '所属酒庄不能为空', trigger: 'change' }
        ],
        plotName: [
          { required: true, message: '地块名称不能为空', trigger: 'blur' }
        ],
        plotCode: [
          { required: true, message: '地块编码不能为空', trigger: 'blur' }
        ]
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
      listPlot(this.queryParams).then(response => {
        this.plotList = response.rows
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
        plotId: undefined,
        enterpriseId: undefined,
        plotName: undefined,
        plotCode: undefined,
        areaHa: undefined,
        elevationM: undefined,
        soilType: undefined,
        gpsCoordinates: undefined,
        plantedVariety: undefined,
        vineAge: undefined,
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
      this.ids = selection.map(item => item.plotId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加地块'
    },
    handleUpdate(row) {
      this.reset()
      const plotId = row.plotId || this.ids
      getPlot(plotId).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改地块'
      })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (!valid) {
          return
        }
        if (this.form.plotId) {
          updatePlot(this.form).then(() => {
            this.$modal.msgSuccess('修改成功')
            this.open = false
            this.getList()
          })
        } else {
          addPlot(this.form).then(() => {
            this.$modal.msgSuccess('新增成功')
            this.open = false
            this.getList()
          })
        }
      })
    },
    handleDelete(row) {
      const plotIds = row.plotId || this.ids
      this.$modal.confirm('是否确认删除选中的地块数据？').then(() => {
        return delPlot(plotIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    }
  }
}
</script>
