package com.dap.system.nh.service.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import com.dap.common.core.utils.DateUtils;
import com.dap.common.core.utils.SecurityUtils;
import com.dap.system.nh.domain.vo.TreeNode;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dap.system.nh.mapper.NhTypeMapper;
import com.dap.system.nh.domain.NhType;
import com.dap.system.nh.service.INhTypeService;

/**
 * 能耗类型Service业务层处理
 *
 * @author dap
 * @date 2021-12-07
 */
@Service
public class NhTypeServiceImpl implements INhTypeService
{
    @Autowired
    private NhTypeMapper nhTypeMapper;

    /**
     * 查询能耗类型
     *
     * @param id 能耗类型ID
     * @return 能耗类型
     */
    @Override
    public NhType selectNhTypeById(Long id)
    {
        return nhTypeMapper.selectNhTypeById(id);
    }

    /**
     * 查询能耗类型列表
     *
     * @param nhType 能耗类型
     * @return 能耗类型
     */
    @Override
    public List<NhType> selectNhTypeList(NhType nhType)
    {
        return nhTypeMapper.selectNhTypeList(nhType);
    }

    /**
     * 分页查询能耗类型列表
     *
     * @param nhType 能耗类型
     * @return 能耗类型
     */
    @Override
    public List<NhType> selectNhTypeListPage(NhType nhType)
    {
        PageHelper.startPage(nhType.getPageNum(),nhType.getPageSize());
        List<NhType> list = nhTypeMapper.selectNhTypeList(nhType);
        PageInfo<NhType> tPageInfo = new PageInfo<>(list);
        List<NhType> dataList = tPageInfo.getList();
        return dataList;
    }

    @Override
    public List<String> currentNhTypeIsExistsMeasure(List<Long> ids) {
        return nhTypeMapper.currentNhTypeIsExistsMeasure(ids);
    }

    @Override
    public NhType existsNhType(NhType nhType) {
        return nhTypeMapper.existsNhType(nhType);
    }

    /**
     * 新增能耗类型
     *
     * @param nhType 能耗类型
     * @return 结果
     */
    @Override
    public int insertNhType(NhType nhType)
    {
        nhType.setDelFlag("0");
        nhType.setCreateUser(SecurityUtils.getLoginCustomizeId());
        nhType.setCreateTime(DateUtils.getNowDate());
        return nhTypeMapper.insertNhType(nhType);
    }

    /**
     * 修改能耗类型
     *
     * @param nhType 能耗类型
     * @return 结果
     */
    @Override
    public int updateNhType(NhType nhType)
    {
        nhType.setUpdateUser(SecurityUtils.getLoginCustomizeId());
        nhType.setUpdateTime(DateUtils.getNowDate());
        return nhTypeMapper.updateNhType(nhType);
    }

    /**
     * 批量删除能耗类型
     *
     * @param ids 需要删除的能耗类型ID
     * @return 结果
     */
    @Override
    public int deleteNhTypeByIds(Long[] ids)
    {
        return nhTypeMapper.deleteNhTypeByIds(ids);
    }

    /**
     * 删除能耗类型信息
     *
     * @param id 能耗类型ID
     * @return 结果
     */
    @Override
    public int deleteNhTypeById(Long id)
    {
        return nhTypeMapper.deleteNhTypeById(id);
    }

    @Override
    public List<TreeNode> selectNhTypeTreeList() {
        return nhTypeMapper.selectNhTypeTreeList();
    }
}
