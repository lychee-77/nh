package com.dap.system.nh.service.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.dap.common.core.utils.DateUtils;
import com.dap.common.core.utils.SecurityUtils;
import com.dap.system.nh.domain.NhBuild;
import com.dap.system.nh.domain.vo.NhFloorVo;
import com.dap.system.nh.domain.vo.TreeNode;
import com.dap.system.nh.mapper.NhBuildMapper;
import com.dap.system.nh.service.INhBuildService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dap.system.nh.mapper.NhFloorMapper;
import com.dap.system.nh.domain.NhFloor;
import com.dap.system.nh.service.INhFloorService;

/**
 * 楼层Service业务层处理
 *
 * @author dap
 * @date 2021-12-03
 */
@Service
public class NhFloorServiceImpl implements INhFloorService
{
    @Autowired
    private NhFloorMapper nhFloorMapper;

    @Autowired
    private NhBuildMapper nhBuildMapper;

    @Autowired
    private INhBuildService nhBuildService;

    /**
     * 查询楼层
     *
     * @param id 楼层ID
     * @return 楼层
     */
    @Override
    public NhFloor selectNhFloorById(Long id)
    {
        return nhFloorMapper.selectNhFloorById(id);
    }

    /**
     * 查询楼层列表
     *
     * @param nhFloor 楼层
     * @return 楼层
     */
    @Override
    public List<NhFloorVo> selectNhFloorList(NhFloorVo  nhFloor)
    {
        return nhFloorMapper.selectNhFloorList(nhFloor);
    }

    /**
     * 分页查询楼层列表
     *
     * @param nhFloor 楼层
     * @return 楼层
     */
    @Override
    public List<NhFloorVo> selectNhFloorListPage(NhFloorVo nhFloor)
    {
        PageHelper.startPage(nhFloor.getPageNum(),nhFloor.getPageSize());
        List<NhFloorVo> list = nhFloorMapper.selectNhFloorList(nhFloor);
        PageInfo<NhFloorVo> tPageInfo = new PageInfo<>(list);
        List<NhFloorVo> dataList = tPageInfo.getList();
        return dataList;
    }
    /**
     * 新增楼层
     *
     * @param nhFloor 楼层
     * @return 结果
     */
    @Override
    public int insertNhFloor(NhFloor nhFloor)
    {
        nhFloor.setDelFlag("0");
        nhFloor.setCreateUser(SecurityUtils.getUsername());
        nhFloor.setCreateTime(DateUtils.getNowDate());
        return nhFloorMapper.insertNhFloor(nhFloor);
    }

    /**
     * 修改楼层
     *
     * @param nhFloor 楼层
     * @return 结果
     */
    @Override
    public int updateNhFloor(NhFloor nhFloor)
    {
        nhFloor.setUpdateUser(SecurityUtils.getLoginCustomizeId());
        nhFloor.setUpdateTime(DateUtils.getNowDate());
        return nhFloorMapper.updateNhFloor(nhFloor);
    }

    /**
     * 批量删除楼层
     *
     * @param ids 需要删除的楼层ID
     * @return 结果
     */
    @Override
    public int deleteNhFloorByIds(Long[] ids)
    {
        return nhFloorMapper.deleteNhFloorByIds(ids);
    }

    /**
     * 删除楼层信息
     *
     * @param id 楼层ID
     * @return 结果
     */
    @Override
    public int deleteNhFloorById(Long id)
    {
        return nhFloorMapper.deleteNhFloorById(id);
    }

    @Override
    public NhFloorVo existsFloorer(NhFloor nhFloor) {
        return nhFloorMapper.existsFloorer(nhFloor);
    }

    @Override
    public Long existsFloorerIds(List<Long> ids) {
        return nhFloorMapper.existsFloorerIds(ids);
    }

    @Override
    public NhFloor existsFloorByFloorName(NhFloor floor) {
        return nhFloorMapper.existsFloorer(floor);
    }

    @Override
    public List<String> currentFloorerIsExistsRoom(List<Long> buildIds) {
        return nhFloorMapper.currentFloorerIsExistsRoom(buildIds);
    }

    @Override
    public List<TreeNode> selectNhBuildTreeList() {
        // 获取楼宇列表
     return nhFloorMapper.selectNhFloorTreeList();

    }




}
