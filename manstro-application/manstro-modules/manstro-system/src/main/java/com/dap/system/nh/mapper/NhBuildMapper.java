package com.dap.system.nh.mapper;

import java.util.List;
import com.dap.system.nh.domain.NhBuild;
import com.dap.system.nh.domain.vo.NhFloorVo;
import com.dap.system.nh.domain.vo.TreeNode;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.SelectKey;

/**
 * 楼宇Mapper接口
 * 
 * @author dap
 * @date 2021-12-02
 */
public interface NhBuildMapper 
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
     * 删除楼宇
     * 
     * @param id 楼宇ID
     * @return 结果
     */
    public int deleteNhBuildById(Long id);

    /**
     * 批量删除楼宇
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteNhBuildByIds(Long[] ids);

    NhBuild existsBuilder(List<Long> ids);

    List<String> currentBuilderIsExistsFloorer(List<Long> builderIds);

    Long existsBuilderByBuildName(NhBuild nyBuild);


    List<TreeNode> selectNhBuildTreeList();
}
