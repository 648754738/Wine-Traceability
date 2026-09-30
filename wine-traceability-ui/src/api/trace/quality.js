import request from '@/utils/request'

export function listQuality(query) {
  return request({
    url: '/trace/quality/list',
    method: 'get',
    params: query
  })
}

export function getQuality(reportId) {
  return request({
    url: '/trace/quality/' + reportId,
    method: 'get'
  })
}

export function addQuality(data) {
  return request({
    url: '/trace/quality',
    method: 'post',
    data: data
  })
}

export function updateQuality(data) {
  return request({
    url: '/trace/quality',
    method: 'put',
    data: data
  })
}

export function delQuality(reportId) {
  return request({
    url: '/trace/quality/' + reportId,
    method: 'delete'
  })
}