package com.dap.system.nh.service;

import java.util.List;
import com.dap.system.nh.domain.NhEquipBaseinfo;
import com.dap.system.nh.domain.vo.NhRoomVo;
import com.dap.system.nh.domain.vo.TreeNode;

/**
 * 设备基本信息Service接口
 * 
 * @author dap
 * @date 2021-12-04
 */
public interface INhEquipBaseinfoService 
{
    /**
     * 查询设备基本信息
     * 
     * @param id 设备基本信息ID
     * @return 设备基本信息
     */
    public NhEquipBaseinfo selectNhEquipBaseinfoById(Long id);

    /**
     * 查询设备基本信息列表
     * 
     * @param nhEquipBaseinfo 设备基本信息
     * @return 设备基本信息集合
     */
    public List<NhEquipBaseinfo> selectNhEquipBaseinfoList(NhEquipBaseinfo nhEquipBaseinfo);

    /**
     * 分页查询设备基本信息列表
     *
     * @param nhEquipBaseinfo 设备基本信息
     * @return 设备基本信息集合
     */
    public List<NhEquipBaseinfo> selectNhEquipBaseinfoListPage(NhEquipBaseinfo nhEquipBaseinfo);


    /**
     * 新增设备基本信息
     * 
     * @param nhEquipBaseinfo 设备基本信息
     * @return 结果
     */
    public int insertNhEquipBaseinfo(NhEquipBaseinfo nhEquipBaseinfo);

    /**
     * 修改设备基本信息
     * 
     * @param nhEquipBaseinfo 设备基本信息
     * @return 结果
     */
    public int updateNhEquipBaseinfo(NhEquipBaseinfo nhEquipBaseinfo);

    /**
     * 批量删除设备基本信息
     * 
     * @param ids 需要删除的设备基本信息ID
     * @return 结果
     */
    public int deleteNhEquipBaseinfoByIds(Long[] ids);

    /**
     * 删除设备基本信息信息
     * 
     * @param id 设备基本信息ID
     * @return 结果
     */
    public int deleteNhEquipBaseinfoById(Long id);

    NhEquipBaseinfo existsEquipBaseInfo(NhEquipBaseinfo nhEquipBaseinfo);

    List<String> currentBaseInfoIsExistsEquipment(List<Long> asList);

    List<TreeNode> selectNhEquipBaseinfoTreeList();
}
