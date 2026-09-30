import request from '@/utils/request'

export function listLogistics(query) {
  return request({
    url: '/trace/logistics/list',
    method: 'get',
    params: query
  })
}

export function getLogistics(logisticsId) {
  return request({
    url: '/trace/logistics/' + logisticsId,
    method: 'get'
  })
}

export function addLogistics(data) {
  return request({
    url: '/trace/logistics',
    method: 'post',
    data: data
  })
}

export function updateLogistics(data) {
  return request({
    url: '/trace/logistics',
    method: 'put',
    data: data
  })
}

export function delLogistics(logisticsId) {
  return request({
    url: '/trace/logistics/' + logisticsId,
    method: 'delete'
  })
}