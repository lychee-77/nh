package com.dap.system.nh.service;

import java.util.List;
import com.dap.system.nh.domain.NhEquipType;
import com.dap.system.nh.domain.vo.TreeNode;

/**
 * 设备类型Service接口
 * 
 * @author dap
 * @date 2021-12-04
 */
public interface INhEquipTypeService 
{
    /**
     * 查询设备类型
     * 
     * @param id 设备类型ID
     * @return 设备类型
     */
    public NhEquipType selectNhEquipTypeById(Long id);

    /**
     * 查询设备类型列表
     * 
     * @param nhEquipType 设备类型
     * @return 设备类型集合
     */
    public List<NhEquipType> selectNhEquipTypeList(NhEquipType nhEquipType);

    /**
     * 分页查询设备类型列表
     *
     * @param nhEquipType 设备类型
     * @return 设备类型集合
     */
    public List<NhEquipType> selectNhEquipTypeListPage(NhEquipType nhEquipType);


    /**
     * 新增设备类型
     * 
     * @param nhEquipType 设备类型
     * @return 结果
     */
    public int insertNhEquipType(NhEquipType nhEquipType);

    /**
     * 修改设备类型
     * 
     * @param nhEquipType 设备类型
     * @return 结果
     */
    public int updateNhEquipType(NhEquipType nhEquipType);

    /**
     * 批量删除设备类型
     * 
     * @param ids 需要删除的设备类型ID
     * @return 结果
     */
    public int deleteNhEquipTypeByIds(Long[] ids);

    /**
     * 删除设备类型信息
     * 
     * @param id 设备类型ID
     * @return 结果
     */
    public int deleteNhEquipTypeById(Long id);

    NhEquipType existsEquipType(NhEquipType nhEquipType);

    List<String> currentEquipmentTypeIsExistsEquipment(List<Long> asList);

    List<TreeNode> selectNhEquipTypeTreeList();
}
