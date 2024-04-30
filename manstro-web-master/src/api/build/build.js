import request from '@/utils/request'

// 查询楼宇列表
export function listBuild(query) {
  return request({
    url: '/system/nh/build/list',
    method: 'get',
    params: query
  })
}

// 查询楼宇详细
export function getBuild(id) {
  return request({
    url: '/system/nh/build/' + id,
    method: 'get'
  })
}

// 新增楼宇
export function addBuild(data) {
  return request({
    url: '/system/nh/build/',
    method: 'post',
    data: data
  })
}

// 修改楼宇
export function updateBuild(data) {
  return request({
    url: '/system/build/nh',
    method: 'put',
    data: data
  })
}

// 删除楼宇
export function delBuild(id) {
  return request({
    url: '/system/nh/build/' + id,
    method: 'delete'
  })
}
