import request from '@/utils/request'

export function listBottling(query) {
  return request({
    url: '/trace/bottling/list',
    method: 'get',
    params: query
  })
}

export function listBottlingOptions(batchId) {
  return request({
    url: '/trace/bottling/options',
    method: 'get',
    params: { batchId }
  })
}

export function getBottling(bottlingId) {
  return request({
    url: '/trace/bottling/' + bottlingId,
    method: 'get'
  })
}

export function addBottling(data) {
  return request({
    url: '/trace/bottling',
    method: 'post',
    data: data
  })
}

export function updateBottling(data) {
  return request({
    url: '/trace/bottling',
    method: 'put',
    data: data
  })
}

export function delBottling(bottlingId) {
  return request({
    url: '/trace/bottling/' + bottlingId,
    method: 'delete'
  })
}