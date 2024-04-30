package com.dap.system.nh.mapper;

import java.util.List;

import com.dap.system.nh.domain.NhMeasure;
import com.dap.system.nh.domain.vo.NhMeasureVo;
import com.dap.system.nh.domain.vo.NhStatisticsVo;

/**
 * 能耗统计Mapper接口
 * 
 * @author dap
 * @date 2021-12-07
 */
public interface NhMeasureMapper
{
    /**
     * 查询能耗统计
     * 
     * @param id 能耗统计ID
     * @return 能耗统计
     */
    public NhMeasureVo selectNhMeasureById(Long id);

    /**
     * 查询能耗统计列表
     * 
     * @param nhMeasure 能耗统计
     * @return 能耗统计集合
     */
    public List<NhMeasureVo> selectNhMeasureList(NhMeasure nhMeasure);

    /**
     * 新增能耗统计
     * 
     * @param nhMeasure 能耗统计
     * @return 结果
     */
    public int insertNhMeasure(NhMeasure nhMeasure);

    /**
     * 修改能耗统计
     * 
     * @param nhMeasure 能耗统计
     * @return 结果
     */
    public int updateNhMeasure(NhMeasure nhMeasure);

    /**
     * 删除能耗统计
     * 
     * @param id 能耗统计ID
     * @return 结果
     */
    public int deleteNhMeasureById(Long id);

    /**
     * 批量删除能耗统计
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteNhMeasureByIds(Long[] ids);

    public List<NhMeasureVo> currentSpaceIdIsExistsMeasure(List<NhMeasureVo> nhMeasure);

    List<NhMeasureVo> selectTotalNumIsRange(NhMeasure nhMeasure);




}
