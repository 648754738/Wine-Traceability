import request from '@/utils/request'

export function listPlot(query) {
  return request({
    url: '/trace/plot/list',
    method: 'get',
    params: query
  })
}

export function listPlotOptions(enterpriseId) {
  return request({
    url: '/trace/plot/options',
    method: 'get',
    params: { enterpriseId }
  })
}

export function getPlot(plotId) {
  return request({
    url: '/trace/plot/' + plotId,
    method: 'get'
  })
}

export function addPlot(data) {
  return request({
    url: '/trace/plot',
    method: 'post',
    data: data
  })
}

export function updatePlot(data) {
  return request({
    url: '/trace/plot',
    method: 'put',
    data: data
  })
}

export function delPlot(plotId) {
  return request({
    url: '/trace/plot/' + plotId,
    method: 'delete'
  })
}
