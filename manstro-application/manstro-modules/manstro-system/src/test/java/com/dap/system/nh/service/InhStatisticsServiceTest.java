package com.dap.system.nh.service;

import com.dap.common.core.utils.DateUtils;
import com.dap.system.nh.domain.vo.NhStatisticsChartItem;
import com.dap.system.nh.domain.vo.NhStatisticsChartVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class InhStatisticsServiceTest {

    @Autowired
    InhStatisticsService inhStatisticsService;

    @Test
    void testSelectNhStatisRingRatioByType() throws ParseException {

        NhStatisticsChartVo nhStatisticsChartVo = inhStatisticsService.selectNhStatisRingRatioByType(3, DateUtils.parseDate("2016-03", "yyyy-MM"));
        NhStatisticsChartItem currentMonthItem = nhStatisticsChartVo.getCurrentMonthItem();
        NhStatisticsChartItem lastMonthItem = nhStatisticsChartVo.getLastMonthItem();
        NhStatisticsChartItem rete = nhStatisticsChartVo.getRete();
        System.out.println(currentMonthItem);
        System.out.println(lastMonthItem);
        System.out.println(rete);
    }
}