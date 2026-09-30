import request from '@/utils/request'

export function queryTraceCode(traceCode) {
  return request({
    url: '/trace/query/code/' + traceCode,
    method: 'get'
  })
}