import request from '@/utils/request'

export function listWarehouse(query) {
  return request({
    url: '/trace/warehouse/list',
    method: 'get',
    params: query
  })
}

export function listWarehouseOptions(billType) {
  return request({
    url: '/trace/warehouse/options',
    method: 'get',
    params: { billType }
  })
}

export function getWarehouse(billId) {
  return request({
    url: '/trace/warehouse/' + billId,
    method: 'get'
  })
}

export function addWarehouse(data) {
  return request({
    url: '/trace/warehouse',
    method: 'post',
    data: data
  })
}

export function updateWarehouse(data) {
  return request({
    url: '/trace/warehouse',
    method: 'put',
    data: data
  })
}

export function delWarehouse(billId) {
  return request({
    url: '/trace/warehouse/' + billId,
    method: 'delete'
  })
}