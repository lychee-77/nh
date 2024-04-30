package com.dap.system.nh.mapper;

import java.util.List;
import com.dap.system.nh.domain.NhEquipment;
import com.dap.system.nh.domain.vo.NhEquipmentVo;
import com.dap.system.nh.domain.vo.TreeNode;

/**
 * 设备表Mapper接口
 * 
 * @author dap
 * @date 2021-12-04
 */
public interface NhEquipmentMapper 
{
    /**
     * 查询设备表
     * 
     * @param id 设备表ID
     * @return 设备表
     */
    public NhEquipment selectNhEquipmentById(Long id);

    /**
     * 查询设备表列表
     * 
     * @param nhEquipment 设备表
     * @return 设备表集合
     */
    public List<NhEquipment> selectNhEquipmentList(NhEquipmentVo nhEquipment);

    /**
     * 新增设备表
     * 
     * @param nhEquipment 设备表
     * @return 结果
     */
    public int insertNhEquipment(NhEquipment nhEquipment);

    /**
     * 修改设备表
     * 
     * @param nhEquipment 设备表
     * @return 结果
     */
    public int updateNhEquipment(NhEquipment nhEquipment);

    /**
     * 删除设备表
     * 
     * @param id 设备表ID
     * @return 结果
     */
    public int deleteNhEquipmentById(Long id);

    /**
     * 批量删除设备表
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteNhEquipmentByIds(Long[] ids);

    List<NhEquipmentVo> selectBatchNhEquipmentsBySpaceIds(List<Long> ids);

    List<TreeNode> selectNhEquipmentTreeList();

    List<NhEquipmentVo> existsNhType(NhEquipment nhEquipment);

    List<NhEquipment> selectNhEquipmentGetEquipId(NhEquipmentVo nhEquipment);
}
