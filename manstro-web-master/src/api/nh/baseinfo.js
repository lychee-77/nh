import request from '@/utils/request'

// 查询设备基本信息列表
export function listBaseinfo(query) {
  return request({
    url: '/system/nh/baseInfo/list',
    method: 'get',
    params: query
  })
}

// 查询设备基本信息详细
export function getBaseinfo(id) {
  return request({
    url: '/system/nh/baseInfo/' + id,
    method: 'get'
  })
}

export function getBaseinfoTreeList(){
  return request({
    url: '/system/nh/baseInfo/treeList',
    method: 'get'
  })
}

// 新增设备基本信息
export function addBaseinfo(data) {
  return request({
    url: '/system/nh/baseInfo',
    method: 'post',
    data: data
  })
}

// 修改设备基本信息
export function updateBaseinfo(data) {
  return request({
    url: '/system/nh/baseInfo',
    method: 'put',
    data: data
  })
}

// 删除设备基本信息
export function delBaseinfo(id) {
  return request({
    url: '/system/nh/baseInfo/' + id,
    method: 'delete'
  })
}
