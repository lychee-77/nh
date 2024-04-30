import request from '@/utils/request'

// 查询分数列表
export function listScore(query) {
  return request({
    url: '/system/office/score/list',
    method: 'get',
    params: query
  })
}
// 查询学生分数概览信息
export function studentScoreInfo(query) {
  return request({
    url: '/system/office/score/studentScoreInfo',
    method: 'get',
    params: query
  })
}


// 查询分数详细
export function getScore(id,semId) {
  return request({
    url: '/system/office/score/' + id+"/"+semId,
    method: 'get'
  })
}

// 新增分数
export function addScore(data) {
  return request({
    url: '/system/office/score',
    method: 'post',
    data: data
  })
}

// 修改分数
export function updateScore(data) {
  return request({
    url: '/system/office/score',
    method: 'put',
    data: data
  })
}

// 删除分数
export function delScore(id,semId) {
  return request({
    url: '/system/office/score/' + id+"/"+semId,
    method: 'delete'
  })
}
