package com.dap.system.nh.domain.vo;

import org.apache.poi.ss.formula.functions.T;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 该实体类过时,NhStatisticsChartVo
 */

public class NhStatisticsVo {

    /**
     * 统计的数据
     */
    private List<NhStatisticsDataVo> datas;

    /**
     * y轴的列表的最大值与最小值
     */
    private BigDecimal minTotalNum ;

    private BigDecimal maxTotalNum ;


    public NhStatisticsVo() {
    }

    public NhStatisticsVo( Map<String, List<NhMeasureVo>> nhTypesData,Predicate<NhStatisticsVo>... predicates) {
        // 每种能耗对应的总值
        List<NhStatisticsDataVo> nhTypeTotalNumList = new ArrayList<>();
        // 遍历
        // y轴的列表的最大值与最小值
        BigDecimal minBigDecimal = BigDecimal.ZERO;
        BigDecimal maxBigDecimal = BigDecimal.ZERO;

        for (Map.Entry<String, List<NhMeasureVo>> stringListEntry : nhTypesData.entrySet()) {
            NhStatisticsDataVo nhStatisticsDataVo = new NhStatisticsDataVo();
            nhStatisticsDataVo.setxData(stringListEntry.getKey());
            // 计算每种能耗类型下的消耗值
            BigDecimal nhTypeTotalNum = stringListEntry.getValue().stream()
                    .map(NhMeasureVo::getConsumeNum)
                    .reduce(BigDecimal::add).orElseGet(() -> new BigDecimal(0));
            // 当前值小于最小值
            if(nhTypeTotalNum.compareTo(minBigDecimal) < 0){
                minBigDecimal = nhTypeTotalNum;
            }

            // 当前值大于最大值
            if(nhTypeTotalNum.compareTo(maxBigDecimal) > 0){
                maxBigDecimal = nhTypeTotalNum;
            }

            nhStatisticsDataVo.setyData(nhTypeTotalNum);
            nhTypeTotalNumList.add(nhStatisticsDataVo);
        }
        // 值列表
        this.setDatas(nhTypeTotalNumList);
        // 设备y轴的最大值与最小值
        this.setMaxTotalNum(maxBigDecimal);
        this.setMinTotalNum(minBigDecimal);
    }


    /**
     * 按照封装好得(条件,List<NhMeasureVo>)来返回统计封装好的NhStatisticsVo
     * @param nhTypesData
     * @param predicate
     */
    public NhStatisticsVo( Map<String, List<NhMeasureVo>> nhTypesData,Predicate<NhMeasureVo> predicate) {
        // 每种能耗对应的总值
        List<NhStatisticsDataVo> nhTypeTotalNumList = new ArrayList<>();
        // 遍历
        // y轴的列表的最大值与最小值
        BigDecimal minBigDecimal = BigDecimal.ZERO;
        BigDecimal maxBigDecimal = BigDecimal.ZERO;

        // 遍历当前条件下的 所有NhMeasureVo
        for (Map.Entry<String, List<NhMeasureVo>> stringListEntry : nhTypesData.entrySet()) {
            NhStatisticsDataVo nhStatisticsDataVo = new NhStatisticsDataVo();
            // 设置值
            nhStatisticsDataVo.setxData(stringListEntry.getKey());

            Stream<NhMeasureVo> conditionDataListStream = stringListEntry.getValue().stream();
            //传入的Predicate不为空,就作为过滤条件执行
            if(Objects.nonNull(predicate)){
                conditionDataListStream = conditionDataListStream.filter(predicate);
            }
            // 计算每种能耗类型下的消耗值
            BigDecimal nhTypeTotalNum = conditionDataListStream
                    .map(NhMeasureVo::getConsumeNum)
                    .reduce(BigDecimal::add).orElseGet(() -> new BigDecimal(0));
            // 当前值小于最小值
            if(nhTypeTotalNum.compareTo(minBigDecimal) < 0){
                minBigDecimal = nhTypeTotalNum;
            }

            // 当前值大于最大值
            if(nhTypeTotalNum.compareTo(maxBigDecimal) > 0){
                maxBigDecimal = nhTypeTotalNum;
            }

            nhStatisticsDataVo.setyData(nhTypeTotalNum);
            nhTypeTotalNumList.add(nhStatisticsDataVo);
        }
        // 值列表
        this.setDatas(nhTypeTotalNumList);
        // 设备y轴的最大值与最小值
        this.setMaxTotalNum(maxBigDecimal);
        this.setMinTotalNum(minBigDecimal);
    }


    public BigDecimal getMinTotalNum() {
        return minTotalNum;
    }

    public void setMinTotalNum(BigDecimal minTotalNum) {
        this.minTotalNum = minTotalNum;
    }

    public BigDecimal getMaxTotalNum() {
        return maxTotalNum;
    }

    public void setMaxTotalNum(BigDecimal maxTotalNum) {
        this.maxTotalNum = maxTotalNum;
    }



    public List<NhStatisticsDataVo> getDatas() {
        return datas;
    }

    public void setDatas(List<NhStatisticsDataVo> datas) {
        this.datas = datas;
    }



    @Override
    public String toString() {
        return "NhStatisticsVo{" +
                ", datas=" + datas +
                ", minTotalNum=" + minTotalNum +
                ", maxTotalNum=" + maxTotalNum +
                '}';
    }
}
