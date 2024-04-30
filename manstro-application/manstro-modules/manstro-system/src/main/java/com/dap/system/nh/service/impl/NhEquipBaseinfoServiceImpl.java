package com.dap.system.nh.service.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.dap.common.core.utils.DateUtils;
import com.dap.common.core.utils.SecurityUtils;
import com.dap.system.nh.domain.vo.NhRoomVo;
import com.dap.system.nh.domain.vo.TreeNode;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dap.system.nh.mapper.NhEquipBaseinfoMapper;
import com.dap.system.nh.domain.NhEquipBaseinfo;
import com.dap.system.nh.service.INhEquipBaseinfoService;

/**
 * 设备基本信息Service业务层处理
 *
 * @author dap
 * @date 2021-12-04
 */
@Service
public class NhEquipBaseinfoServiceImpl implements INhEquipBaseinfoService
{
    @Autowired
    private NhEquipBaseinfoMapper nhEquipBaseinfoMapper;

    /**
     * 查询设备基本信息
     *
     * @param id 设备基本信息ID
     * @return 设备基本信息
     */
    @Override
    public NhEquipBaseinfo selectNhEquipBaseinfoById(Long id)
    {
        return nhEquipBaseinfoMapper.selectNhEquipBaseinfoById(id);
    }

    /**
     * 查询设备基本信息列表
     *
     * @param nhEquipBaseinfo 设备基本信息
     * @return 设备基本信息
     */
    @Override
    public List<NhEquipBaseinfo> selectNhEquipBaseinfoList(NhEquipBaseinfo nhEquipBaseinfo)
    {
        return nhEquipBaseinfoMapper.selectNhEquipBaseinfoList(nhEquipBaseinfo);
    }

    /**
     * 分页查询设备基本信息列表
     *
     * @param nhEquipBaseinfo 设备基本信息
     * @return 设备基本信息
     */
    @Override
    public List<NhEquipBaseinfo> selectNhEquipBaseinfoListPage(NhEquipBaseinfo nhEquipBaseinfo)
    {
        PageHelper.startPage(nhEquipBaseinfo.getPageNum(),nhEquipBaseinfo.getPageSize());
        List<NhEquipBaseinfo> list = nhEquipBaseinfoMapper.selectNhEquipBaseinfoList(nhEquipBaseinfo);
        PageInfo<NhEquipBaseinfo> tPageInfo = new PageInfo<>(list);
        List<NhEquipBaseinfo> dataList = tPageInfo.getList();
        return dataList;
    }
    /**
     * 新增设备基本信息
     *
     * @param nhEquipBaseinfo 设备基本信息
     * @return 结果
     */
    @Override
    public int insertNhEquipBaseinfo(NhEquipBaseinfo nhEquipBaseinfo)
    {
        nhEquipBaseinfo.setDelFlag("0");
        nhEquipBaseinfo.setCreateUser(SecurityUtils.getUsername());
        nhEquipBaseinfo.setCreateTime(DateUtils.getNowDate());
        return nhEquipBaseinfoMapper.insertNhEquipBaseinfo(nhEquipBaseinfo);
    }

    /**
     * 修改设备基本信息
     *
     * @param nhEquipBaseinfo 设备基本信息
     * @return 结果
     */
    @Override
    public int updateNhEquipBaseinfo(NhEquipBaseinfo nhEquipBaseinfo)
    {
        nhEquipBaseinfo.setUpdateUser(SecurityUtils.getUsername());
        nhEquipBaseinfo.setUpdateTime(DateUtils.getNowDate());
        return nhEquipBaseinfoMapper.updateNhEquipBaseinfo(nhEquipBaseinfo);
    }

    /**
     * 批量删除设备基本信息
     *
     * @param ids 需要删除的设备基本信息ID
     * @return 结果
     */
    @Override
    public int deleteNhEquipBaseinfoByIds(Long[] ids)
    {
        return nhEquipBaseinfoMapper.deleteNhEquipBaseinfoByIds(ids);
    }

    /**
     * 删除设备基本信息信息
     *
     * @param id 设备基本信息ID
     * @return 结果
     */
    @Override
    public int deleteNhEquipBaseinfoById(Long id)
    {
        return nhEquipBaseinfoMapper.deleteNhEquipBaseinfoById(id);
    }



    @Override
    public NhEquipBaseinfo existsEquipBaseInfo(NhEquipBaseinfo nhEquipBaseinfo){
        return nhEquipBaseinfoMapper.existsEquipBaseInfo(nhEquipBaseinfo);
    }

    @Override
    public List<String> currentBaseInfoIsExistsEquipment(List<Long> asList) {
        return nhEquipBaseinfoMapper.currentBaseInfoIsExistsEquipment(asList);
    }

    @Override
    public List<TreeNode> selectNhEquipBaseinfoTreeList() {
        List<NhEquipBaseinfo> nhEquipBaseinfos = nhEquipBaseinfoMapper.selectNhEquipBaseinfoList(new NhEquipBaseinfo());
        return nhEquipBaseinfos.stream().map(NhEquipBaseinfo::buildTreeNode).collect(Collectors.toList());
    }
}
