package com.dap.system.nh.domain.vo;


import java.math.BigDecimal;
import java.util.List;

/**
 * 统计图项实体,
 * 每一个该对象代表了统计图中的一类数据集
 */
public class NhStatisticsChartItem {
    /**
     * 数据集,具体的x轴和y轴数据
     */
    private List<NhStatisticsDataVo> datas;

    /**
     * 当前数据属于哪一类信息,在统计图中有多项数据集时做区分条件
     */
    private String typeInfo;

    /**
     * 当前数据集的最大值,动态改变统计图的纵坐标最大值
     */
    private BigDecimal maxTotalNum;

    /**
     * 当前数据集的最大值,动态改变统计图的纵坐标最小值
     */
    private BigDecimal minTotalNum;

    public NhStatisticsChartItem() {
    }

    public NhStatisticsChartItem(NhStatisticsVo nhStatisticsVo) {
        this.datas = nhStatisticsVo.getDatas();
        this.maxTotalNum = nhStatisticsVo.getMaxTotalNum();
        this.minTotalNum = nhStatisticsVo.getMinTotalNum();
    }


    public List<NhStatisticsDataVo> getDatas() {
        return datas;
    }

    public void setDatas(List<NhStatisticsDataVo> datas) {
        this.datas = datas;
    }

    public String getTypeInfo() {
        return typeInfo;
    }

    public void setTypeInfo(String typeInfo) {
        this.typeInfo = typeInfo;
    }

    public BigDecimal getMaxTotalNum() {
        return maxTotalNum;
    }

    public void setMaxTotalNum(BigDecimal maxTotalNum) {
        this.maxTotalNum = maxTotalNum;
    }

    public BigDecimal getMinTotalNum() {
        return minTotalNum;
    }

    public void setMinTotalNum(BigDecimal minTotalNum) {
        this.minTotalNum = minTotalNum;
    }

    @Override
    public String toString() {
        return "NhStatisticsChartItem{" +
                "datas=" + datas +
                ", typeInfo='" + typeInfo + '\'' +
                ", maxTotalNum=" + maxTotalNum +
                ", minTotalNum=" + minTotalNum +
                '}';
    }
}
