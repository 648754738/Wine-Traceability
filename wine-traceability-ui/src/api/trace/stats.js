import request from '@/utils/request'

export function getStatsDashboard() {
  return request({
    url: '/trace/stats/dashboard',
    method: 'get'
  })
}