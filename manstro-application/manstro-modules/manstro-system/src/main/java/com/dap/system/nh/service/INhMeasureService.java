package com.dap.system.nh.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Predicate;

import com.dap.system.nh.domain.NhMeasure;
import com.dap.system.nh.domain.vo.NhMeasureVo;

/**
 * 能耗统计Service接口
 * 
 * @author dap
 * @date 2021-12-07
 */
public interface INhMeasureService
{
    /**
     * 查询能耗统计
     * 
     * @param id 能耗统计ID
     * @return 能耗统计
     */
    public NhMeasure selectNhMeasureById(Long id);

    /**
     * 分页查询能耗统计列表
     *
     * @param NhMeasureVo 能耗统计
     * @return 能耗统计
     */
    public List<NhMeasureVo> selectNhMeasureList(NhMeasureVo NhMeasureVo,boolean isStatistics);

    /**
     * 分页查询能耗统计列表
     *
     * @param nhMeasure 能耗统计
     * @return 能耗统计集合
     */
    public List<NhMeasureVo> selectNhMeasureListPage(NhMeasureVo nhMeasure,boolean isStatistics);

    public List<NhMeasureVo> currentSpaceIdIsExistsMeasure(List<NhMeasureVo> nhMeasure);

    /**
     * 同一日期.同一能耗类型,同一型号只能有一条记录
     * 因为在设备表中维护了空间信息,所以一个设备id就对应了空间信息,当设备id相同,空间必定相同
     * @param nhMeasure
     * @return
     */
    public List<NhMeasureVo> existsEquipmentByStatistic(NhMeasure nhMeasure);

    public List<NhMeasureVo> doCompareNhMeasureVo(List<NhMeasureVo> nhMeasure, Predicate<? super NhMeasureVo> predicate);


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
     * 批量删除能耗统计
     * 
     * @param ids 需要删除的能耗统计ID
     * @return 结果
     */
    public int deleteNhMeasureByIds(Long[] ids);

    /**
     * 删除能耗统计信息
     * 
     * @param id 能耗统计ID
     * @return 结果
     */
    public int deleteNhMeasureById(Long id);

    /**
     * 检查数据库中的数据是否重复没有问题返回null
     * @param nhMeasureVos
     * @return 错误的数据
     */
    public List<NhMeasureVo> checkExcelDataValidity(List<NhMeasureVo> dataBaseNhMeasureVos, List<NhMeasureVo> nhMeasureVos);

    /**
     * 根据vo对象解析出来实体类对象
     */
    public List<NhMeasure> nhMeasureByNhStatisticVos(List<NhMeasureVo> nhMeasureVos);

    /**
     * 返回当前设备,当前能耗下如果携带了统计日期,会查询低于该统计日期下的最大的能耗总值(会某一月份修改,只需要高于该月之前的月份,
     * 以List<Double>返回范围
     *
     * 防止新增或者修改的总值小于该值
     * @param nhMeasure
     * @return
     */
    public List<BigDecimal> selectTotalNumRange(NhMeasure nhMeasure);

}
