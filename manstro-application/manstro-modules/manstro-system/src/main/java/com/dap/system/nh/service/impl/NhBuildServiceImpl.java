package com.dap.system.nh.service.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.dap.common.core.utils.DateUtils;
import com.dap.common.core.utils.SecurityUtils;
import com.dap.system.nh.domain.NhRoom;
import com.dap.system.nh.domain.vo.NhFloorVo;
import com.dap.system.nh.domain.vo.NhRoomVo;
import com.dap.system.nh.domain.vo.TreeNode;
import com.dap.system.nh.mapper.NhFloorMapper;
import com.dap.system.nh.mapper.NhRoomMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dap.system.nh.mapper.NhBuildMapper;
import com.dap.system.nh.domain.NhBuild;
import com.dap.system.nh.service.INhBuildService;

/**
 * 楼宇Service业务层处理
 *
 * @author dap
 * @date 2021-12-02
 */
@Service
public class NhBuildServiceImpl implements INhBuildService {
    @Autowired
    private NhBuildMapper nhBuildMapper;

    @Autowired
    private NhRoomMapper nhRoomMapper;

    @Autowired
    private NhFloorMapper nhFloorMapper;

    /**
     * 查询楼宇
     *
     * @param id 楼宇ID
     * @return 楼宇
     */
    @Override
    public NhBuild selectNhBuildById(Long id) {
        return nhBuildMapper.selectNhBuildById(id);
    }

    /**
     * 查询楼宇列表
     *
     * @param nhBuild 楼宇
     * @return 楼宇
     */
    @Override
    public List<NhBuild> selectNhBuildList(NhBuild nhBuild) {
        return nhBuildMapper.selectNhBuildList(nhBuild);
    }

    /**
     * 分页查询楼宇列表
     *
     * @param nhBuild 楼宇
     * @return 楼宇
     */
    @Override
    public List<NhBuild> selectNhBuildListPage(NhBuild nhBuild) {
        PageHelper.startPage(nhBuild.getPageNum(), nhBuild.getPageSize());
        List<NhBuild> list = nhBuildMapper.selectNhBuildList(nhBuild);
        PageInfo<NhBuild> tPageInfo = new PageInfo<>(list);
        return tPageInfo.getList();
    }

    /**
     * 新增楼宇
     *
     * @param nhBuild 楼宇
     * @return 结果
     */
    @Override
    public int insertNhBuild(NhBuild nhBuild) {
        nhBuild.setDelFlag("0");
        nhBuild.setCreateUser(SecurityUtils.getUsername());
        nhBuild.setCreateTime(DateUtils.getNowDate());
        return nhBuildMapper.insertNhBuild(nhBuild);
    }

    /**
     * 修改楼宇
     *
     * @param nhBuild 楼宇
     * @return 结果
     */
    @Override
    public int updateNhBuild(NhBuild nhBuild) {
        nhBuild.setUpdateUser(SecurityUtils.getLoginCustomizeId());
        nhBuild.setUpdateTime(DateUtils.getNowDate());
        return nhBuildMapper.updateNhBuild(nhBuild);
    }

    /**
     * 批量删除楼宇
     *
     * @param ids 需要删除的楼宇ID
     * @return 结果
     */
    @Override
    public int deleteNhBuildByIds(Long[] ids) {
        return nhBuildMapper.deleteNhBuildByIds(ids);
    }

    /**
     * 删除楼宇信息
     *
     * @param id 楼宇ID
     * @return 结果
     */
    @Override
    public int deleteNhBuildById(Long id) {
        return nhBuildMapper.deleteNhBuildById(id);
    }

    @Override
    public NhBuild existsBuilder(List<Long> ids) {
        return nhBuildMapper.existsBuilder(ids);
    }

    /**
     * 当前楼宇下是否存在楼层
     * @param builderIds 楼宇id
     * @return
     */
    @Override
    public List<String> currentBuilderIsExistsFloorer(List<Long> builderIds) {
        return nhBuildMapper.currentBuilderIsExistsFloorer(builderIds);
    }

    /**
     * 根据楼宇名称是否已经被占用
     * @param nyBuild
     * @return
     */
    @Override
    public Boolean existsBuilderByBuilderName(NhBuild nyBuild){
        Long row = nhBuildMapper.existsBuilderByBuildName(nyBuild);
        return  row != null && row > 0;
    }

    @Override
    public List<TreeNode> selectNhBuildTreeList() {
        return nhBuildMapper.selectNhBuildTreeList();
    }






}
