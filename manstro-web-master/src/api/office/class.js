import request from '@/utils/request'

// 查询班级列表
export function listClass(query) {
  return request({
    url: '/system/office/class/list',
    method: 'get',
    params: query
  })
}

// 查询班级姓名列表
export function getClassAllName(query) {
  return request({
    url: '/system/office/class/allName',
    method: 'get',
    params: query
  })
}

// 查询班级姓名列表
export function getGradeAllName(query) {
  return request({
    url: '/system/office/class/allGradeName',
    method: 'get',
    params: query
  })
}

// 查询班级详细
export function getClass(id) {
  return request({
    url: '/system/office/class/' + id,
    method: 'get'
  })
}

// 新增班级
export function addClass(data) {
  return request({
    url: '/system//office/class',
    method: 'post',
    data: data
  })
}

// 修改班级
export function updateClass(data) {
  return request({
    url: '/system//office/class',
    method: 'put',
    data: data
  })
}

// 删除班级
export function delClass(id) {
  return request({
    url: '/system//office/class/' + id,
    method: 'delete'
  })
}
