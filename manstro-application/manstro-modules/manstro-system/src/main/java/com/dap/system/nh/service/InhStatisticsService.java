package com.dap.system.nh.service;

import com.dap.system.nh.domain.vo.NhMeasureVo;
import com.dap.system.nh.domain.vo.NhStatisticsChartVo;
import com.dap.system.nh.domain.vo.NhStatisticsVo;

import java.util.Date;
import java.util.List;

public interface InhStatisticsService {
    /**
     * 根据条件计算统计值
     */
    public NhStatisticsVo selectNhStatisRingRatio(Integer type) ;

    /**
     * 按照计算本月,上月,环比值
     * todo 根据前端传入的条件进行筛选
     */
    public NhStatisticsChartVo selectNhStatisRingRatioByType(Integer type, Date date);

    NhStatisticsChartVo selectNhStatisYearRatioByType(Integer type, Date statisticsDate);

}
