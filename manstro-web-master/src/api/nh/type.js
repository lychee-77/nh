import request from '@/utils/request'

// 查询能耗类型列表
export function listType(query) {
  return request({
    url: '/system/nh/type/list',
    method: 'get',
    params: query
  })
}

// 查询能耗类型详细
export function getType(id) {
  return request({
    url: '/system/nh/type/' + id,
    method: 'get'
  })
}

// 查询能耗类型列表
export function getTypeTreeList() {
  return request({
    url: '/system/nh/type/treeList',
    method: 'get',
  })
}

// 新增能耗类型
export function addType(data) {
  return request({
    url: '/system/nh/type',
    method: 'post',
    data: data
  })
}

// 修改能耗类型
export function updateType(data) {
  return request({
    url: '/system/nh/type',
    method: 'put',
    data: data
  })
}

// 删除能耗类型
export function delType(id) {
  return request({
    url: '/system/nh/type/' + id,
    method: 'delete'
  })
}
