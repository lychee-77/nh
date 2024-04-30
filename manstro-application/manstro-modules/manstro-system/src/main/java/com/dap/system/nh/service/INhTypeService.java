package com.dap.system.nh.service;

import java.util.List;
import com.dap.system.nh.domain.NhType;
import com.dap.system.nh.domain.vo.NhEquipmentTypeVo;
import com.dap.system.nh.domain.vo.TreeNode;

/**
 * 能耗类型Service接口
 * 
 * @author dap
 * @date 2021-12-07
 */
public interface INhTypeService 
{
    /**
     * 查询能耗类型
     * 
     * @param id 能耗类型ID
     * @return 能耗类型
     */
    public NhType selectNhTypeById(Long id);

    /**
     * 查询能耗类型列表
     * 
     * @param nhType 能耗类型
     * @return 能耗类型集合
     */
    public List<NhType> selectNhTypeList(NhType nhType);

    /**
     * 分页查询能耗类型列表
     *
     * @param nhType 能耗类型
     * @return 能耗类型集合
     */
    public List<NhType> selectNhTypeListPage(NhType nhType);

    /**
     * 当前能耗类型是否已经在统计表中存在
     * @param ids
     * @return
     */
    List<String> currentNhTypeIsExistsMeasure(List<Long> ids);

    /**
     * 当前表中是否已经存在相同的名称的类型
     * @param nhType
     * @return
     */
    NhType existsNhType(NhType nhType);
    /**
     * 新增能耗类型
     * 
     * @param nhType 能耗类型
     * @return 结果
     */
    public int insertNhType(NhType nhType);

    /**
     * 修改能耗类型
     * 
     * @param nhType 能耗类型
     * @return 结果
     */
    public int updateNhType(NhType nhType);

    /**
     * 批量删除能耗类型
     * 
     * @param ids 需要删除的能耗类型ID
     * @return 结果
     */
    public int deleteNhTypeByIds(Long[] ids);

    /**
     * 删除能耗类型信息
     * 
     * @param id 能耗类型ID
     * @return 结果
     */
    public int deleteNhTypeById(Long id);

    List<TreeNode> selectNhTypeTreeList();
}
