package com.dap.system.nh.service.impl;

import java.util.List;
import java.util.UUID;
import com.dap.common.core.utils.DateUtils;
import com.dap.common.core.utils.SecurityUtils;
import com.dap.system.nh.domain.vo.NhRoomVo;
import com.dap.system.nh.domain.vo.TreeNode;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dap.system.nh.mapper.NhRoomMapper;
import com.dap.system.nh.domain.NhRoom;
import com.dap.system.nh.service.INhRoomService;

/**
 * 房间Service业务层处理
 *
 * @author dap
 * @date 2021-12-03
 */
@Service
public class NhRoomServiceImpl implements INhRoomService
{
    @Autowired
    private NhRoomMapper nhRoomMapper;

    /**
     * 查询房间
     *
     * @param id 房间ID
     * @return 房间
     */
    @Override
    public NhRoom selectNhRoomById(Long id)
    {
        return nhRoomMapper.selectNhRoomById(id);
    }

    /**
     * 查询房间列表
     *
     * @param nhRoomVo 房间
     * @return 房间
     */
    @Override
    public List<NhRoomVo> selectNhRoomList(NhRoomVo nhRoomVo)
    {
        return nhRoomMapper.selectNhRoomList(nhRoomVo);
    }

    @Override
    public List<TreeNode> selectNhRoomTreeList() {
        return nhRoomMapper.selectNhBuildTreeList();
    }


    /**
     * 分页查询房间列表
     *
     * @param nhRoomVo 房间
     * @return 房间
     */
    @Override
    public List<NhRoomVo> selectNhRoomListPage(NhRoomVo nhRoomVo)
    {
        PageHelper.startPage(nhRoomVo.getPageNum(),nhRoomVo.getPageSize());
        List<NhRoomVo> list = nhRoomMapper.selectNhRoomList(nhRoomVo);
        PageInfo<NhRoomVo> tPageInfo = new PageInfo<>(list);
        return tPageInfo.getList();
    }
    /**
     * 新增房间
     *
     * @param nhRoom 房间
     * @return 结果
     */
    @Override
    public int insertNhRoom(NhRoom nhRoom)
    {
        nhRoom.setDelFlag("0");
        nhRoom.setCreateUser(SecurityUtils.getUsername());
        nhRoom.setCreateTime(DateUtils.getNowDate());
        return nhRoomMapper.insertNhRoom(nhRoom);
    }

    /**
     * 修改房间
     *
     * @param nhRoom 房间
     * @return 结果
     */
    @Override
    public int updateNhRoom(NhRoom nhRoom)
    {
        nhRoom.setUpdateUser(SecurityUtils.getLoginCustomizeId());
        nhRoom.setUpdateTime(DateUtils.getNowDate());
        return nhRoomMapper.updateNhRoom(nhRoom);
    }

    /**
     * 批量删除房间
     *
     * @param ids 需要删除的房间ID
     * @return 结果
     */
    @Override
    public int deleteNhRoomByIds(Long[] ids)
    {
        return nhRoomMapper.deleteNhRoomByIds(ids);
    }

    /**
     * 删除房间信息
     *
     * @param id 房间ID
     * @return 结果
     */
    @Override
    public int deleteNhRoomById(Long id)
    {
        return nhRoomMapper.deleteNhRoomById(id);
    }

    @Override
    public NhRoomVo existsRoomByFloor(NhRoom nhRoom) {
        return nhRoomMapper.existsRoomByFloor(nhRoom);
    }
}
