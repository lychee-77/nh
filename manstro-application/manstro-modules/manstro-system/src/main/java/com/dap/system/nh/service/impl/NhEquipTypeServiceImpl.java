package com.dap.system.nh.service.impl;

import java.util.List;
import java.util.UUID;
import com.dap.common.core.utils.DateUtils;
import com.dap.common.core.utils.SecurityUtils;
import com.dap.system.nh.domain.vo.TreeNode;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dap.system.nh.mapper.NhEquipTypeMapper;
import com.dap.system.nh.domain.NhEquipType;
import com.dap.system.nh.service.INhEquipTypeService;

/**
 * 设备类型Service业务层处理
 *
 * @author dap
 * @date 2021-12-04
 */
@Service
public class NhEquipTypeServiceImpl implements INhEquipTypeService
{
    @Autowired
    private NhEquipTypeMapper nhEquipTypeMapper;

    /**
     * 查询设备类型
     *
     * @param id 设备类型ID
     * @return 设备类型
     */
    @Override
    public NhEquipType selectNhEquipTypeById(Long id)
    {
        return nhEquipTypeMapper.selectNhEquipTypeById(id);
    }

    /**
     * 查询设备类型列表
     *
     * @param nhEquipType 设备类型
     * @return 设备类型
     */
    @Override
    public List<NhEquipType> selectNhEquipTypeList(NhEquipType nhEquipType)
    {
        return nhEquipTypeMapper.selectNhEquipTypeList(nhEquipType);
    }

    /**
     * 分页查询设备类型列表
     *
     * @param nhEquipType 设备类型
     * @return 设备类型
     */
    @Override
    public List<NhEquipType> selectNhEquipTypeListPage(NhEquipType nhEquipType)
    {
        PageHelper.startPage(nhEquipType.getPageNum(),nhEquipType.getPageSize());
        List<NhEquipType> list = nhEquipTypeMapper.selectNhEquipTypeList(nhEquipType);
        PageInfo<NhEquipType> tPageInfo = new PageInfo<>(list);
        return tPageInfo.getList();
    }
    /**
     * 新增设备类型
     *
     * @param nhEquipType 设备类型
     * @return 结果
     */
    @Override
    public int insertNhEquipType(NhEquipType nhEquipType)
    {
        nhEquipType.setDelFlag("0");
        nhEquipType.setCreateUser(SecurityUtils.getUsername());
        nhEquipType.setCreateTime(DateUtils.getNowDate());
        return nhEquipTypeMapper.insertNhEquipType(nhEquipType);
    }

    /**
     * 修改设备类型
     *
     * @param nhEquipType 设备类型
     * @return 结果
     */
    @Override
    public int updateNhEquipType(NhEquipType nhEquipType)
    {
        nhEquipType.setUpdateUser(SecurityUtils.getUsername());
        nhEquipType.setUpdateTime(DateUtils.getNowDate());
        return nhEquipTypeMapper.updateNhEquipType(nhEquipType);
    }

    /**
     * 批量删除设备类型
     *
     * @param ids 需要删除的设备类型ID
     * @return 结果
     */
    @Override
    public int deleteNhEquipTypeByIds(Long[] ids)
    {
        return nhEquipTypeMapper.deleteNhEquipTypeByIds(ids);
    }

    /**
     * 删除设备类型信息
     *
     * @param id 设备类型ID
     * @return 结果
     */
    @Override
    public int deleteNhEquipTypeById(Long id)
    {
        return nhEquipTypeMapper.deleteNhEquipTypeById(id);
    }

    @Override
    public NhEquipType existsEquipType(NhEquipType nhEquipType) {

        return nhEquipTypeMapper.existsEquipBaseType(nhEquipType);
    }

    @Override
    public List<String> currentEquipmentTypeIsExistsEquipment(List<Long> asList) {
        return nhEquipTypeMapper.currentEquipmentTypeIsExistsEquipment(asList);
    }

    @Override
    public List<TreeNode> selectNhEquipTypeTreeList() {
        return nhEquipTypeMapper.selectNhEquipTypeTreeList();
    }
}
