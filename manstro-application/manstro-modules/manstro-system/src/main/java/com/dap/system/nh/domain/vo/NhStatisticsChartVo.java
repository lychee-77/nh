package com.dap.system.nh.domain.vo;

import java.util.List;

/**
 * 增长率统计图实体,一个对象就是一个统计图实体
 * 环比/同比
 */
public class NhStatisticsChartVo {

    /**
     * 本次数据项
     */
   private NhStatisticsChartItem currentMonthItem;

    /**
     * 上一次数据项
     */
    private NhStatisticsChartItem lastMonthItem;

    /**
     * 增长率
     */
    private NhStatisticsChartItem rete;

    public NhStatisticsChartVo(NhStatisticsChartItem currentMonthItem, NhStatisticsChartItem lastMonthItem, NhStatisticsChartItem rete) {
        this.currentMonthItem = currentMonthItem;
        this.lastMonthItem = lastMonthItem;
        this.rete = rete;
    }

    public NhStatisticsChartVo() {
    }

    public NhStatisticsChartItem getCurrentMonthItem() {
        return currentMonthItem;
    }

    public void setCurrentMonthItem(NhStatisticsChartItem currentMonthItem) {
        this.currentMonthItem = currentMonthItem;
    }

    public NhStatisticsChartItem getLastMonthItem() {
        return lastMonthItem;
    }

    public void setLastMonthItem(NhStatisticsChartItem lastMonthItem) {
        this.lastMonthItem = lastMonthItem;
    }

    public NhStatisticsChartItem getRete() {
        return rete;
    }

    public void setRete(NhStatisticsChartItem rete) {
        this.rete = rete;
    }

    @Override
    public String toString() {
        return "NhStatisticsChartVo{" +
                "currentMonthItem=" + currentMonthItem +
                ", lastMonthItem=" + lastMonthItem +
                ", rete=" + rete +
                '}';
    }
}
