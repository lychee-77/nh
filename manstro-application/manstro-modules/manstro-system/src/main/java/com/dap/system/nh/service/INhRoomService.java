package com.dap.system.nh.service;

import java.util.List;
import com.dap.system.nh.domain.NhRoom;
import com.dap.system.nh.domain.vo.NhRoomVo;
import com.dap.system.nh.domain.vo.TreeNode;

/**
 * 房间Service接口
 * 
 * @author dap
 * @date 2021-12-03
 */
public interface INhRoomService 
{
    /**
     * 查询房间
     * 
     * @param id 房间ID
     * @return 房间
     */
    public NhRoom selectNhRoomById(Long id);

    /**
     * 查询房间列表
     * 
     * @param nhRoomVo 房间
     * @return 房间集合
     */
    public List<NhRoomVo> selectNhRoomList(NhRoomVo nhRoomVo);


    /**
     * 查询房间列表
     *
     *
     * @return 房间集合
     */
    public List<TreeNode> selectNhRoomTreeList();


    /**
     * 分页查询房间列表
     *
     * @param nhRoomVo 房间
     * @return 房间集合
     */
    public List<NhRoomVo> selectNhRoomListPage(NhRoomVo nhRoomVo);


    /**
     * 新增房间
     * 
     * @param nhRoom 房间
     * @return 结果
     */
    public int insertNhRoom(NhRoom nhRoom);

    /**
     * 修改房间
     * 
     * @param nhRoom 房间
     * @return 结果
     */
    public int updateNhRoom(NhRoom nhRoom);

    /**
     * 批量删除房间
     * 
     * @param ids 需要删除的房间ID
     * @return 结果
     */
    public int deleteNhRoomByIds(Long[] ids);

    /**
     * 删除房间信息
     * 
     * @param id 房间ID
     * @return 结果
     */
    public int deleteNhRoomById(Long id);

    NhRoomVo existsRoomByFloor(NhRoom nhRoom);
}
