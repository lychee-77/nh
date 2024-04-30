import request from '@/utils/request'

// 查询房间列表
export function listRoom(query) {
  return request({
    url: '/system/nh/room/list',
    method: 'get',
    params: query
  })
}

// 删除房间
export function roomTreeList() {
  return request({
    url: '/system/nh/room/treeList',
    method: 'get'
  })
}

// 查询房间详细
export function getRoom(id) {
  return request({
    url: '/system/nh/room/' + id,
  })
}

// 新增房间
export function addRoom(data) {
  return request({
    url: '/system/nh/room',
    method: 'post',
    data: data
  })
}

// 修改房间
export function updateRoom(data) {
  return request({
    url: '/system/nh/room',
    method: 'put',
    data: data
  })
}

// 删除房间
export function delRoom(id) {
  return request({
    url: '/system/nh/room/' + id,
    method: 'delete'
  })
}
