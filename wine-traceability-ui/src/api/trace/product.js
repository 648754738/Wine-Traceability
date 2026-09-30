import request from '@/utils/request'

export function listProduct(query) {
  return request({
    url: '/trace/product/list',
    method: 'get',
    params: query
  })
}

export function listProductOptions(enterpriseId) {
  return request({
    url: '/trace/product/options',
    method: 'get',
    params: { enterpriseId }
  })
}

export function getProduct(productId) {
  return request({
    url: '/trace/product/' + productId,
    method: 'get'
  })
}

export function addProduct(data) {
  return request({
    url: '/trace/product',
    method: 'post',
    data: data
  })
}

export function updateProduct(data) {
  return request({
    url: '/trace/product',
    method: 'put',
    data: data
  })
}

export function delProduct(productId) {
  return request({
    url: '/trace/product/' + productId,
    method: 'delete'
  })
}
