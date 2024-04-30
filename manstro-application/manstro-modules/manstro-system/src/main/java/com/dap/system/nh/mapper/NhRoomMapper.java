package com.dap.system.nh.mapper;

import java.util.List;
import com.dap.system.nh.domain.NhRoom;
import com.dap.system.nh.domain.vo.NhEquipmentVo;
import com.dap.system.nh.domain.vo.NhRoomVo;
import com.dap.system.nh.domain.vo.TreeNode;

/**
 * 房间Mapper接口
 * 
 * @author dap
 * @date 2021-12-03
 */
public interface NhRoomMapper 
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
     * 构建树列表
     * @return
     */
    public List<TreeNode> selectNhBuildTreeList();
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
     * 删除房间
     * 
     * @param id 房间ID
     * @return 结果
     */
    public int deleteNhRoomById(Long id);

    /**
     * 批量删除房间
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteNhRoomByIds(Long[] ids);

    NhRoomVo existsRoomByFloor(NhRoom nhRoom);

//    NhEquipmentVo existsEquipmentByRoom(NhRoom nhRoom);
}
