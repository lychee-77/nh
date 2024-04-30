package com.dap.system.nh.mapper;

import java.util.List;

import com.dap.system.nh.domain.NhBuild;
import com.dap.system.nh.domain.NhFloor;
import com.dap.system.nh.domain.vo.NhFloorVo;
import com.dap.system.nh.domain.vo.TreeNode;
import org.apache.ibatis.annotations.MapKey;

/**
 * 楼层Mapper接口
 * 
 * @author dap
 * @date 2021-12-03
 */
public interface NhFloorMapper 
{
    /**
     * 查询楼层
     * 
     * @param id 楼层ID
     * @return 楼层
     */
    public NhFloor selectNhFloorById(Long id);

    /**
     * 查询楼层列表
     * 
     * @param nhFloor 楼层
     * @return 楼层集合
     */
    public List<NhFloorVo> selectNhFloorList(NhFloorVo nhFloor);

    /**
     * 新增楼层
     * 
     * @param nhFloor 楼层
     * @return 结果
     */
    public int insertNhFloor(NhFloor nhFloor);

    /**
     * 修改楼层
     * 
     * @param nhFloor 楼层
     * @return 结果
     */
    public int updateNhFloor(NhFloor nhFloor);

    /**
     * 删除楼层
     * 
     * @param id 楼层ID
     * @return 结果
     */
    public int deleteNhFloorById(Long id);

    /**
     * 批量删除楼层
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteNhFloorByIds(Long[] ids);

    NhFloorVo existsFloorer(NhFloor nhFloorVo);

    Long existsFloorerIds(List<Long> ids);

    List<String> currentFloorerIsExistsRoom(List<Long> buildIds);


    List<TreeNode> selectNhFloorTreeList();

}
