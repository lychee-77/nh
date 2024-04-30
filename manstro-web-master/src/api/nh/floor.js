import request from '@/utils/request'

// 查询楼层列表
export function listFloor(query) {
  return request({
    url: '/system/nh/floor/list',
    method: 'get',
    params: query
  })
}

// 查询楼层详细
export function getFloor(id) {
  return request({
    url: '/system/nh/floor/' + id,
    method: 'get'
  })
}

// 新增楼层
export function addFloor(data) {
  return request({
    url: '/system/nh/floor',
    method: 'post',
    data: data
  })
}

// 修改楼层
export function updateFloor(data) {
  return request({
    url: '/system/nh/floor',
    method: 'put',
    data: data
  })
}

// 删除楼层
export function delFloor(id) {
  return request({
    url: '/system/nh/floor/' + id,
    method: 'delete'
  })
}


// 楼层下拉框
export function getFloroTreeList(id) {
  return request({
    url: '/system/nh/floor/treeList',
    method: 'get'
  })
}

