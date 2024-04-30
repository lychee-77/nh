package com.dap.system.nh.service.impl;

import java.util.List;
import java.util.UUID;
import com.dap.common.core.utils.DateUtils;
import com.dap.common.core.utils.SecurityUtils;
import com.dap.system.nh.domain.vo.NhEquipmentVo;
import com.dap.system.nh.domain.vo.TreeNode;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dap.system.nh.mapper.NhEquipmentMapper;
import com.dap.system.nh.domain.NhEquipment;
import com.dap.system.nh.service.INhEquipmentService;

/**
 * 设备表Service业务层处理
 *
 * @author dap
 * @date 2021-12-04
 */
@Service
public class NhEquipmentServiceImpl implements INhEquipmentService
{
    @Autowired
    private NhEquipmentMapper nhEquipmentMapper;

    /**
     * 查询设备表
     *
     * @param id 设备表ID
     * @return 设备表
     */
    @Override
    public NhEquipment selectNhEquipmentById(Long id)
    {
        return nhEquipmentMapper.selectNhEquipmentById(id);
    }

    /**
     * 查询设备表列表
     *
     * @param nhEquipment 设备表
     * @return 设备表
     */
    @Override
    public List<NhEquipment> selectNhEquipmentList(NhEquipmentVo nhEquipment)
    {
        return nhEquipmentMapper.selectNhEquipmentList(nhEquipment);
    }

    /**
     * 分页查询设备表列表
     *
     * @param nhEquipment 设备表
     * @return 设备表
     */
    @Override
    public List<NhEquipment> selectNhEquipmentListPage(NhEquipmentVo nhEquipment)
    {
        PageHelper.startPage(nhEquipment.getPageNum(),nhEquipment.getPageSize());
        List<NhEquipment> list = nhEquipmentMapper.selectNhEquipmentList(nhEquipment);
        PageInfo<NhEquipment> tPageInfo = new PageInfo<>(list);
        List<NhEquipment> dataList = tPageInfo.getList();
        return dataList;
    }

    @Override
    public List<NhEquipmentVo> selectBatchNhEquipmentsBySpaceIds(List<Long> ids) {

        return nhEquipmentMapper.selectBatchNhEquipmentsBySpaceIds(ids);
    }

    /**
     * 新增设备表
     *
     * @param nhEquipment 设备表
     * @return 结果
     */
    @Override
    public int insertNhEquipment(NhEquipment nhEquipment)
    {
        nhEquipment.setDelFlag("0");
        nhEquipment.setCreateUser(SecurityUtils.getUsername());
        nhEquipment.setCreateTime(DateUtils.getNowDate());
        return nhEquipmentMapper.insertNhEquipment(nhEquipment);
    }

    /**
     * 修改设备表
     *
     * @param nhEquipment 设备表
     * @return 结果
     */
    @Override
    public int updateNhEquipment(NhEquipment nhEquipment)
    {
        nhEquipment.setUpdateUser(SecurityUtils.getUsername());
        nhEquipment.setUpdateTime(DateUtils.getNowDate());
        return nhEquipmentMapper.updateNhEquipment(nhEquipment);
    }

    /**
     * 批量删除设备表
     *
     * @param ids 需要删除的设备表ID
     * @return 结果
     */
    @Override
    public int deleteNhEquipmentByIds(Long[] ids)
    {
        return nhEquipmentMapper.deleteNhEquipmentByIds(ids);
    }

    /**
     * 删除设备表信息
     *
     * @param id 设备表ID
     * @return 结果
     */
    @Override
    public int deleteNhEquipmentById(Long id)
    {
        return nhEquipmentMapper.deleteNhEquipmentById(id);
    }

    @Override
    public List<TreeNode> selectNhEquipmentTreeList() {
        return nhEquipmentMapper.selectNhEquipmentTreeList();
    }

    @Override
    public List<NhEquipmentVo> existsNhType(NhEquipment nhEquipment) {
        return nhEquipmentMapper.existsNhType(nhEquipment);
    }
}
