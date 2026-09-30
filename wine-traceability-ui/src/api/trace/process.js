import request from '@/utils/request'

export function listProcess(query) {
  return request({
    url: '/trace/process/list',
    method: 'get',
    params: query
  })
}

export function getProcess(processId) {
  return request({
    url: '/trace/process/' + processId,
    method: 'get'
  })
}

export function addProcess(data) {
  return request({
    url: '/trace/process',
    method: 'post',
    data: data
  })
}

export function updateProcess(data) {
  return request({
    url: '/trace/process',
    method: 'put',
    data: data
  })
}

export function delProcess(processId) {
  return request({
    url: '/trace/process/' + processId,
    method: 'delete'
  })
}
