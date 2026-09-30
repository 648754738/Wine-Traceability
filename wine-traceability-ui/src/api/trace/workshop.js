import request from '@/utils/request'

export function listWorkshop(query) {
  return request({
    url: '/trace/workshop/list',
    method: 'get',
    params: query
  })
}

export function listWorkshopOptions(enterpriseId) {
  return request({
    url: '/trace/workshop/options',
    method: 'get',
    params: { enterpriseId }
  })
}

export function getWorkshop(workshopId) {
  return request({
    url: '/trace/workshop/' + workshopId,
    method: 'get'
  })
}

export function addWorkshop(data) {
  return request({
    url: '/trace/workshop',
    method: 'post',
    data: data
  })
}

export function updateWorkshop(data) {
  return request({
    url: '/trace/workshop',
    method: 'put',
    data: data
  })
}

export function delWorkshop(workshopId) {
  return request({
    url: '/trace/workshop/' + workshopId,
    method: 'delete'
  })
}
