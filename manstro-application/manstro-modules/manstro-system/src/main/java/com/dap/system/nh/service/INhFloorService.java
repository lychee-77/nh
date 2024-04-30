package com.dap.system.nh.service;

import java.util.List;

import com.dap.system.nh.domain.NhBuild;
import com.dap.system.nh.domain.NhEquipment;
import com.dap.system.nh.domain.NhFloor;
import com.dap.system.nh.domain.vo.NhFloorVo;
import com.dap.system.nh.domain.vo.TreeNode;

/**
 * 楼层Service接口
 *
 * @author dap
 * @date 2021-12-03
 */
public interface INhFloorService {
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
     * 分页查询楼层列表
     *
     * @param nhFloor 楼层
     * @return 楼层集合
     */
    public List<NhFloorVo> selectNhFloorListPage(NhFloorVo nhFloor);


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
     * 批量删除楼层
     *
     * @param ids 需要删除的楼层ID
     * @return 结果
     */
    public int deleteNhFloorByIds(Long[] ids);

    /**
     * 删除楼层信息
     *
     * @param id 楼层ID
     * @return 结果
     */
    public int deleteNhFloorById(Long id);

    NhFloorVo existsFloorer(NhFloor nhFloor);

    Long existsFloorerIds(List<Long> ids);

    NhFloor existsFloorByFloorName(NhFloor floor);

    /**
     * 当前楼层下是否存在房屋
     * @param buildIds
     * @return
     */
    List<String> currentFloorerIsExistsRoom(List<Long> buildIds);

    List<TreeNode> selectNhBuildTreeList();

}
