package com.dap.system.nh.controller;

import com.dap.common.core.web.controller.BaseController;
import com.dap.common.core.web.domain.AjaxResult;
import com.dap.common.core.web.page.TableDataInfo;
import com.dap.common.security.annotation.PreAuthorize;
import com.dap.system.nh.domain.NhMeasure;
import com.dap.system.nh.domain.vo.NhMeasureVo;
import com.dap.system.nh.domain.vo.NhStatisticsChartVo;
import com.dap.system.nh.domain.vo.NhStatisticsVo;
import com.dap.system.nh.service.INhMeasureService;
import com.dap.system.nh.service.InhStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/nh/statistics")
public class NhStatisticsController extends BaseController {


    @Autowired
    InhStatisticsService inhStatisticsService;

    @Autowired
    INhMeasureService inMeasureService;

    /**
     * 查询能耗统计列表
     */
    @PreAuthorize(hasPermi = "nh:statistics:list")
    @GetMapping("/list/{type}")
    public AjaxResult list(@PathVariable Integer type) {
        NhStatisticsVo nhStatisticsVo = inhStatisticsService.selectNhStatisRingRatio(type);
        return AjaxResult.success(nhStatisticsVo);
    }

    /**
     * 查询能耗统计环比列表
     */
    @PreAuthorize(hasPermi = "nh:statistics:list")
    @GetMapping("/ringRatio/{type}")
    public AjaxResult ringRatio(@PathVariable Integer type, Date statisticsDate) {
        NhStatisticsChartVo nhStatisticsChartVo = inhStatisticsService.selectNhStatisRingRatioByType(type, statisticsDate);
        return AjaxResult.success(nhStatisticsChartVo);
    }

    /**
     * 查询能耗统计同比列表
     */
    @PreAuthorize(hasPermi = "nh:statistics:list")
    @GetMapping("/yearRatio/{type}")
        public AjaxResult yearRatio(@PathVariable Integer type, Date statisticsDate) {
        NhStatisticsChartVo nhStatisticsChartVo = inhStatisticsService.selectNhStatisYearRatioByType(type, statisticsDate);
        return AjaxResult.success(nhStatisticsChartVo);
    }
}
