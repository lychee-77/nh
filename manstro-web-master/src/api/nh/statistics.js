import request from '@/utils/request'

// 查询能耗统计列表
export function listStatistics(type) {
  return request({
    url: '/system/nh/statistics/list/'+type,
    method: 'get',

  })
}
// 查询能耗统计环比
export function listRingRatio(type,query) {
  return request({
    url: '/system/nh/statistics/ringRatio/'+type,
    method: 'get',
    params: query
  })
}
// 查询能耗统计同比
export function listYearRatio(type,query) {
  return request({
    url: '/system/nh/statistics/yearRatio/'+type,
    method: 'get',
    params: query
  })
}
