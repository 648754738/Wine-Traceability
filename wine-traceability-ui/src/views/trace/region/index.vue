<template>
  <div class="app-container">
    <el-form ref="queryForm" :model="queryParams" size="small" :inline="true" v-show="showSearch">
      <el-form-item label="产区名称" prop="regionName">
        <el-select
          v-model="queryParams.regionName"
          placeholder="请选择产区名称"
          clearable
          filterable
          style="width: 260px"
          @change="handleQuery"
        >
          <el-option
            v-for="item in regionNameOptions"
            :key="item"
            :label="item"
            :value="item"
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
          v-hasPermi="['trace:region:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-sort"
          size="mini"
          @click="toggleExpandAll"
        >展开/折叠</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table
      v-if="refreshTable"
      v-loading="loading"
      :data="regionList"
      row-key="regionId"
      :default-expand-all="isExpandAll"
      :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
    >
      <el-table-column prop="regionName" label="产区名称" min-width="220" />
      <el-table-column prop="regionLevel" label="层级" width="90" />
      <el-table-column prop="orderNum" label="排序" width="90" />
      <el-table-column prop="status" label="状态" width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="描述" prop="description" min-width="220" show-overflow-tooltip />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="240">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['trace:region:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-plus"
            @click="handleAdd(scope.row)"
            v-hasPermi="['trace:region:add']"
          >新增下级</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['trace:region:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="title" :visible.sync="open" width="680px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="上级产区" prop="parentId">
          <treeselect
            v-model="form.parentId"
            :options="regionOptions"
            :normalizer="normalizer"
            placeholder="请选择上级产区"
          />
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="产区名称" prop="regionName">
              <el-input v-model="form.regionName" placeholder="请输入产区名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="显示排序" prop="orderNum">
              <el-input-number v-model="form.orderNum" controls-position="right" :min="0" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in dict.type.sys_normal_disable"
              :key="dict.value"
              :label="dict.value"
            >{{ dict.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入产区描述" />
        </el-form-item>
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
import { listRegion, listRegionExcludeChild, getRegion, addRegion, updateRegion, delRegion } from '@/api/trace/region'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'

export default {
  name: 'TraceRegion',
  dicts: ['sys_normal_disable'],
  components: { Treeselect },
  data() {
    return {
      loading: true,
      showSearch: true,
      regionList: [],
      allRegionTree: [],
      regionOptions: [],
      title: '',
      open: false,
      isExpandAll: true,
      refreshTable: true,
      queryParams: {
        regionName: undefined,
        status: undefined
      },
      form: {},
      rules: {
        parentId: [
          { required: true, message: '上级产区不能为空', trigger: 'change' }
        ],
        regionName: [
          { required: true, message: '产区名称不能为空', trigger: 'blur' }
        ],
        orderNum: [
          { required: true, message: '显示排序不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    regionNameOptions() {
      return this.flattenRegionNames(this.allRegionTree)
    }
  },
  created() {
    this.getList()
    this.loadAllRegionTree()
  },
  methods: {
    getList() {
      this.loading = true
      listRegion(this.queryParams).then(response => {
        this.regionList = response.data || []
        this.loading = false
      })
    },
    loadAllRegionTree() {
      listRegion().then(response => {
        this.allRegionTree = response.data || []
      })
    },
    flattenRegionNames(nodes, result = []) {
      ;(nodes || []).forEach(node => {
        if (node.regionName && !result.includes(node.regionName)) {
          result.push(node.regionName)
        }
        if (node.children && node.children.length) {
          this.flattenRegionNames(node.children, result)
        }
      })
      return result
    },
    getRegionOptions(excludeId) {
      const request = excludeId ? listRegionExcludeChild(excludeId) : listRegion()
      request.then(response => {
        this.regionOptions = [{
          regionId: 0,
          regionName: '顶级产区',
          children: response.data || []
        }]
      })
    },
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children
      }
      return {
        id: node.regionId,
        label: node.regionName,
        children: node.children
      }
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        regionId: undefined,
        parentId: 0,
        regionName: undefined,
        orderNum: 0,
        status: '0',
        description: undefined,
        remark: undefined
      }
      this.resetForm('form')
    },
    handleQuery() {
      this.getList()
    },
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    handleAdd(row) {
      this.reset()
      if (row) {
        this.form.parentId = row.regionId
      }
      this.getRegionOptions()
      this.open = true
      this.title = '添加产区'
    },
    handleUpdate(row) {
      this.reset()
      getRegion(row.regionId).then(response => {
        this.form = response.data
        this.getRegionOptions(row.regionId)
        this.open = true
        this.title = '修改产区'
      })
    },
    toggleExpandAll() {
      this.refreshTable = false
      this.isExpandAll = !this.isExpandAll
      this.$nextTick(() => {
        this.refreshTable = true
      })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (!valid) {
          return
        }
        const request = this.form.regionId ? updateRegion(this.form) : addRegion(this.form)
        request.then(() => {
          this.$modal.msgSuccess(this.form.regionId ? '修改成功' : '新增成功')
          this.open = false
          this.getList()
          this.loadAllRegionTree()
        })
      })
    },
    handleDelete(row) {
      this.$modal.confirm('是否确认删除产区“' + row.regionName + '”？').then(() => {
        return delRegion(row.regionId)
      }).then(() => {
        this.getList()
        this.loadAllRegionTree()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    }
  }
}
</script>
