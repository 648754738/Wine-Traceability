import request from '@/utils/request'

export function listFeedback(query) {
  return request({
    url: '/trace/feedback/list',
    method: 'get',
    params: query
  })
}

export function getFeedback(feedbackId) {
  return request({
    url: '/trace/feedback/' + feedbackId,
    method: 'get'
  })
}

export function addPublicFeedback(data) {
  return request({
    url: '/trace/feedback/public',
    method: 'post',
    data: data
  })
}

export function updateFeedback(data) {
  return request({
    url: '/trace/feedback',
    method: 'put',
    data: data
  })
}

export function delFeedback(feedbackId) {
  return request({
    url: '/trace/feedback/' + feedbackId,
    method: 'delete'
  })
}