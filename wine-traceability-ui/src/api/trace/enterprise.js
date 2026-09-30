import request from '@/utils/request'

export function listEnterprise(query) {
  return request({
    url: '/trace/enterprise/list',
    method: 'get',
    params: query
  })
}

export function listEnterpriseOptions() {
  return request({
    url: '/trace/enterprise/options',
    method: 'get'
  })
}

export function getEnterprise(enterpriseId) {
  return request({
    url: '/trace/enterprise/' + enterpriseId,
    method: 'get'
  })
}

export function addEnterprise(data) {
  return request({
    url: '/trace/enterprise',
    method: 'post',
    data: data
  })
}

export function updateEnterprise(data) {
  return request({
    url: '/trace/enterprise',
    method: 'put',
    data: data
  })
}

export function delEnterprise(enterpriseId) {
  return request({
    url: '/trace/enterprise/' + enterpriseId,
    method: 'delete'
  })
}
