import request from '@/utils/request'

export function listContainer(query) {
  return request({
    url: '/trace/container/list',
    method: 'get',
    params: query
  })
}

export function listContainerOptions(query) {
  return request({
    url: '/trace/container/options',
    method: 'get',
    params: query
  })
}

export function getContainer(containerId) {
  return request({
    url: '/trace/container/' + containerId,
    method: 'get'
  })
}

export function addContainer(data) {
  return request({
    url: '/trace/container',
    method: 'post',
    data: data
  })
}

export function updateContainer(data) {
  return request({
    url: '/trace/container',
    method: 'put',
    data: data
  })
}

export function delContainer(containerId) {
  return request({
    url: '/trace/container/' + containerId,
    method: 'delete'
  })
}
