import request from '@/utils/request'

// 查询设备类型列表
export function listType(query) {
  return request({
    url: '/system/nh/equipType/list',
    method: 'get',
    params: query
  })
}

// 查询设备类型列表
export function getEquipTypeTreeList() {
  return request({
    url: '/system/nh/equipType/treeList',
    method: 'get',
  })
}

// 查询设备类型详细
export function getType(id) {
  return request({
    url: '/system/nh/equipType/' + id,
    method: 'get'
  })
}

// 新增设备类型
export function addType(data) {
  return request({
    url: '/system/nh/equipType',
    method: 'post',
    data: data
  })
}

// 修改设备类型
export function updateType(data) {
  return request({
    url: '/system/nh/equipType',
    method: 'put',
    data: data
  })
}

// 删除设备类型
export function delType(id) {
  return request({
    url: '/system/nh/equipType/' + id,
    method: 'delete'
  })
}
