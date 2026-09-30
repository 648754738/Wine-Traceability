import request from '@/utils/request'

export function listVariety(query) {
  return request({
    url: '/trace/variety/list',
    method: 'get',
    params: query
  })
}

export function listVarietyOptions() {
  return request({
    url: '/trace/variety/options',
    method: 'get'
  })
}

export function getVariety(varietyId) {
  return request({
    url: '/trace/variety/' + varietyId,
    method: 'get'
  })
}

export function addVariety(data) {
  return request({
    url: '/trace/variety',
    method: 'post',
    data: data
  })
}

export function updateVariety(data) {
  return request({
    url: '/trace/variety',
    method: 'put',
    data: data
  })
}

export function delVariety(varietyId) {
  return request({
    url: '/trace/variety/' + varietyId,
    method: 'delete'
  })
}
