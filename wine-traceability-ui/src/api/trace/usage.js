import request from '@/utils/request'

export function listUsage(query) {
  return request({
    url: '/trace/usage/list',
    method: 'get',
    params: query
  })
}

export function getUsage(usageId) {
  return request({
    url: '/trace/usage/' + usageId,
    method: 'get'
  })
}

export function addUsage(data) {
  return request({
    url: '/trace/usage',
    method: 'post',
    data: data
  })
}

export function updateUsage(data) {
  return request({
    url: '/trace/usage',
    method: 'put',
    data: data
  })
}

export function delUsage(usageId) {
  return request({
    url: '/trace/usage/' + usageId,
    method: 'delete'
  })
}
