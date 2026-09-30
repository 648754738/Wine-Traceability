<template>
  <div class="app-container">
    <el-form ref="queryForm" :model="queryParams" size="small" :inline="true" v-show="showSearch" label-width="90px">
      <el-form-item label="溯源码" prop="traceCode">
        <el-input v-model="queryParams.traceCode" placeholder="请输入溯源码" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="处理状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option label="待处理" value="0" />
          <el-option label="已处理" value="1" />
        </el-select>
      </el-form-item>
      <el-form-item label="可疑反馈" prop="isSuspicious">
        <el-select v-model="queryParams.isSuspicious" placeholder="请选择" clearable>
          <el-option label="否" value="0" />
          <el-option label="是" value="1" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['trace:feedback:edit']">处理</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['trace:feedback:remove']">删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="feedbackList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="溯源码" prop="traceCode" min-width="140" />
      <el-table-column label="酒庄 / 产品" min-width="220">
        <template slot-scope="scope">
          <span>{{ scope.row.enterpriseName || '-' }} / {{ scope.row.productName || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="评分" prop="rating" width="90" />
      <el-table-column label="可疑" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.isSuspicious === '1' ? 'danger' : 'success'" size="mini">
            {{ scope.row.isSuspicious === '1' ? '是' : '否' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === '1' ? 'success' : 'warning'" size="mini">
            {{ scope.row.status === '1' ? '已处理' : '待处理' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="反馈内容" prop="content" min-width="240" show-overflow-tooltip />
      <el-table-column label="反馈时间" prop="createTime" width="170" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="150">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['trace:feedback:edit']">处理</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['trace:feedback:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="760px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-descriptions :column="2" border style="margin-bottom: 16px">
          <el-descriptions-item label="溯源码">{{ form.traceCode || '-' }}</el-descriptions-item>
          <el-descriptions-item label="生产批次">{{ form.batchNo || '-' }}</el-descriptions-item>
          <el-descriptions-item label="酒庄">{{ form.enterpriseName || '-' }}</el-descriptions-item>
          <el-descriptions-item label="产品">{{ form.productName || '-' }}</el-descriptions-item>
        </el-descriptions>
        <el-form-item label="评分">
          <el-rate v-model="form.rating" disabled show-score text-color="#ff9900" />
        </el-form-item>
        <el-form-item label="反馈内容">
          <el-input v-model="form.content" type="textarea" :rows="3" disabled />
        </el-form-item>
        <el-form-item label="是否可疑" prop="isSuspicious">
          <el-radio-group v-model="form.isSuspicious">
            <el-radio label="0">否</el-radio>
            <el-radio label="1">是</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="处理状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio label="0">待处理</el-radio>
            <el-radio label="1">已处理</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="回复内容" prop="replyContent">
          <el-input v-model="form.replyContent" type="textarea" :rows="3" placeholder="请输入回复内容" />
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
import { listFeedback, getFeedback, updateFeedback, delFeedback } from '@/api/trace/feedback'

export default {
  name: 'TraceFeedback',
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      feedbackList: [],
      open: false,
      title: '',
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        traceCode: undefined,
        status: undefined,
        isSuspicious: undefined
      },
      form: {},
      rules: {
        status: [{ required: true, message: '处理状态不能为空', trigger: 'change' }]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listFeedback(this.queryParams).then(response => {
        this.feedbackList = response.rows || []
        this.total = response.total || 0
      }).finally(() => {
        this.loading = false
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
      this.ids = selection.map(item => item.feedbackId)
      this.single = selection.length !== 1
      this.multiple = selection.length === 0
    },
    reset() {
      this.form = {
        feedbackId: undefined,
        traceCode: undefined,
        batchNo: undefined,
        enterpriseName: undefined,
        productName: undefined,
        rating: 0,
        content: undefined,
        isSuspicious: '0',
        status: '0',
        replyContent: undefined
      }
      this.resetForm('form')
    },
    cancel() {
      this.open = false
      this.reset()
    },
    handleUpdate(row) {
      this.reset()
      const feedbackId = row && row.feedbackId ? row.feedbackId : this.ids[0]
      getFeedback(feedbackId).then(response => {
        this.form = response.data || {}
        if (!this.form.isSuspicious) {
          this.form.isSuspicious = '0'
        }
        if (!this.form.status) {
          this.form.status = '0'
        }
        this.open = true
        this.title = '处理消费者反馈'
      })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (!valid) {
          return
        }
        updateFeedback(this.form).then(() => {
          this.$modal.msgSuccess('处理成功')
          this.open = false
          this.getList()
        })
      })
    },
    handleDelete(row) {
      const feedbackIds = row && row.feedbackId ? row.feedbackId : this.ids
      this.$modal.confirm('确认删除选中的反馈记录吗？').then(() => {
        return delFeedback(feedbackIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    }
  }
}
</script>