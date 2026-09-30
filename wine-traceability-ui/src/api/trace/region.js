import request from '@/utils/request'

export function listRegion(query) {
  return request({
    url: '/trace/region/list',
    method: 'get',
    params: query
  })
}

export function listRegionExcludeChild(regionId) {
  return request({
    url: '/trace/region/list/exclude/' + regionId,
    method: 'get'
  })
}

export function getRegion(regionId) {
  return request({
    url: '/trace/region/' + regionId,
    method: 'get'
  })
}

export function addRegion(data) {
  return request({
    url: '/trace/region',
    method: 'post',
    data: data
  })
}

export function updateRegion(data) {
  return request({
    url: '/trace/region',
    method: 'put',
    data: data
  })
}

export function delRegion(regionId) {
  return request({
    url: '/trace/region/' + regionId,
    method: 'delete'
  })
}
