import request from '@/utils/request'

// 查询学期管理列表
export function listSemester(query) {
  return request({
    url: '/system/office/semester/list',
    method: 'get',
    params: query
  })
}

// 查询学期管理详细
export function getSemester(id) {
  return request({
    url: '/system/office/semester/' + id,
    method: 'get'
  })
}



// 新增学期管理
export function addSemester(data) {
  return request({
    url: '/system/office/semester',
    method: 'post',
    data: data
  })
}

// 修改学期管理
export function updateSemester(data) {
  return request({
    url: '/system/office/semester',
    method: 'put',
    data: data
  })
}

// 删除学期管理
export function delSemester(id) {
  return request({
    url: '/system/office/semester/' + id,
    method: 'delete'
  })
}

// 导入学期
export function importTemplate() {
  return request({
    url: '/system/office/semester/importTemplate',
    method: 'post'
  })
}


// 查询年级树列表
export function treeGrade(query) {
  return request({
    url: '/system/office/semester/treeList',
    method: 'get',
    params: query
  })
}


// 查询学期下拉框列表
export function allName() {
  return request({
    url: '/system/office/semester/allName',
    method: 'get',
  })
}
