import request from '@/utils/request'

export function listBatch(query) {
  return request({
    url: '/trace/batch/list',
    method: 'get',
    params: query
  })
}

export function listBatchOptions(enterpriseId) {
  return request({
    url: '/trace/batch/options',
    method: 'get',
    params: { enterpriseId }
  })
}

export function getBatch(batchId) {
  return request({
    url: '/trace/batch/' + batchId,
    method: 'get'
  })
}

export function addBatch(data) {
  return request({
    url: '/trace/batch',
    method: 'post',
    data: data
  })
}

export function updateBatch(data) {
  return request({
    url: '/trace/batch',
    method: 'put',
    data: data
  })
}

export function delBatch(batchId) {
  return request({
    url: '/trace/batch/' + batchId,
    method: 'delete'
  })
}
