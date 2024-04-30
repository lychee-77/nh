package com.dap.system.nh.domain.vo;

import java.math.BigDecimal;

public class NhStatisticsDataVo {
    // x轴数据
    private String xData;

    // 对应的y轴数据
    private BigDecimal yData;


    public String getxData() {
        return xData;
    }

    public void setxData(String xData) {
        this.xData = xData;
    }

    public BigDecimal getyData() {
        return yData;
    }

    public void setyData(BigDecimal yData) {
        this.yData = yData;
    }



    @Override
    public String toString() {
        return "NhStatisticsDataVo{" +
                "xData='" + xData + '\'' +
                ", yData=" + yData +
                '}';
    }
}
