import request from '@/utils/request'

// 查询设备表列表
export function listEquipment(query) {
  return request({
    url: '/system/nh/equipment/list',
    method: 'get',
    params: query
  })
}


// 查询设备表树列表
export function getEquipmentTreeList() {
  return request({
    url: '/system/nh/equipment/treeList',
    method: 'get'
  })
}

// 查询设备表详细
export function getEquipment(id) {
  return request({
    url: '/system/nh/equipment/' + id,
    method: 'get'
  })
}

// 新增设备表
export function addEquipment(data) {
  return request({
    url: '/system/nh/equipment',
    method: 'post',
    data: data
  })
}

// 修改设备表
export function updateEquipment(data) {
  return request({
    url: '/system/nh/equipment',
    method: 'put',
    data: data
  })
}

// 删除设备表
export function delEquipment(id) {
  return request({
    url: '/system/nh/equipment/' + id,
    method: 'delete'
  })
}
