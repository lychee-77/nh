package com.dap.system.nh.service;

import java.util.List;

import com.dap.common.core.web.domain.AjaxResult;
import com.dap.common.log.annotation.Log;
import com.dap.common.log.enums.BusinessType;
import com.dap.common.security.annotation.PreAuthorize;
import com.dap.system.nh.domain.NhBuild;
import com.dap.system.nh.domain.vo.NhFloorVo;
import com.dap.system.nh.domain.vo.TreeNode;
import org.springframework.web.bind.annotation.PutMapping;

/**
 * 楼宇Service接口
 * 
 * @author dap
 * @date 2021-12-02
 */
public interface INhBuildService 
{
    /**
     * 查询楼宇
     * 
     * @param id 楼宇ID
     * @return 楼宇
     */
    public NhBuild selectNhBuildById(Long id);

    /**
     * 查询楼宇列表
     * 
     * @param nhBuild 楼宇
     * @return 楼宇集合
     */
    public List<NhBuild> selectNhBuildList(NhBuild nhBuild);

    /**
     * 分页查询楼宇列表
     *
     * @param nhBuild 楼宇
     * @return 楼宇集合
     */
    public List<NhBuild> selectNhBuildListPage(NhBuild nhBuild);


    /**
     * 新增楼宇
     * 
     * @param nhBuild 楼宇
     * @return 结果
     */
    public int insertNhBuild(NhBuild nhBuild);

    /**
     * 修改楼宇
     * 
     * @param nhBuild 楼宇
     * @return 结果
     */
    public int updateNhBuild(NhBuild nhBuild);

    /**
     * 批量删除楼宇
     * 
     * @param ids 需要删除的楼宇ID
     * @return 结果
     */
    public int deleteNhBuildByIds(Long[] ids);

    /**
     * 删除楼宇信息
     * 
     * @param id 楼宇ID
     * @return 结果
     */
    public int deleteNhBuildById(Long id);

    public NhBuild existsBuilder(List<Long> ids);

    List<String> currentBuilderIsExistsFloorer(List<Long> asList);

    Boolean existsBuilderByBuilderName(NhBuild nyBuild);

    List<TreeNode> selectNhBuildTreeList();

}
