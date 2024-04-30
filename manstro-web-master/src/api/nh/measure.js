import request from '@/utils/request'

// 查询能耗统计列表
export function listMeasure(query) {
  return request({
    url: '/system/nh/measure/list',
    method: 'get',
    params: query
  })
}

// 查询能耗统计详细
export function getMeasure(id) {
  return request({
    url: '/system/nh/measure/' + id,
    method: 'get'
  })
}

// 新增能耗统计
export function addMeasure(data) {
  return request({
    url: '/system/nh/measure',
    method: 'post',
    data: data
  })
}

// 修改能耗统计
export function updateMeasure(data) {
  return request({
    url: '/system/nh/measure',
    method: 'put',
    data: data
  })
}

// 删除能耗统计
export function delMeasure(id) {
  return request({
    url: '/system/nh/measure/' + id,
    method: 'delete'
  })
}
