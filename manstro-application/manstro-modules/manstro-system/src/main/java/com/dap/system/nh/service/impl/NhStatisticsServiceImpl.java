package com.dap.system.nh.service.impl;

import com.dap.common.core.utils.DateUtils;
import com.dap.system.nh.domain.vo.*;
import com.dap.system.nh.mapper.NhMeasureMapper;
import com.dap.system.nh.service.INhMeasureService;
import com.dap.system.nh.service.InhStatisticsService;
import org.apache.poi.ss.formula.functions.T;
import org.checkerframework.checker.units.qual.K;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class NhStatisticsServiceImpl implements InhStatisticsService {

    @Resource
    NhMeasureMapper nhMeasureMapper;

    @Autowired
    INhMeasureService nhMeasureService;



    @Override
    public NhStatisticsVo selectNhStatisRingRatio(Integer type) {
        // 没有过滤条件
        return groupStatisticsByCondition(type,null);
    }

    /**
     * 根据参数类型去按照条件分组进行统计
     * @param type
     * @return
     */
    private NhStatisticsVo groupStatisticsByCondition(Integer type,Predicate<NhMeasureVo> predicate){
        switch (type){
            case 1:
                return selectNhStatisRingRatioByType(NhMeasureVo::getBuildName,predicate);
            case 2:
                return selectNhStatisRingRatioByType(NhMeasureVo::getEquipmentTypeModel,predicate);
            case 3:
                return selectNhStatisRingRatioByType(NhMeasureVo::getNhTypeModel,predicate);
            case 4:
                return selectNhStatisRingRatioByType((item) -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(item.getCurrentDate());
                    return calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1);
                },predicate);
            default:
                // 不支持的类型
                throw new IllegalStateException("Unexpected value: " + type);
        }
    }

    /**
     * 按照类型统计
     */
    public NhStatisticsVo selectNhStatisRingRatioByType(Function<NhMeasureVo, String> classifier,Predicate<NhMeasureVo> predicate) {

        List<NhMeasureVo> nhMeasureVos = nhMeasureService.selectNhMeasureList(new NhMeasureVo(),false);
        // 根据条件进行分类
        Map<String, List<NhMeasureVo>> nhEquipmentTypeModelData
                = nhMeasureVos.stream().collect(
                        Collectors.groupingBy(classifier));
        return new NhStatisticsVo(nhEquipmentTypeModelData,predicate);
    }


    /**
     * 按照计算本月,上月,环比值
     * todo 根据前端传入的条件进行筛选
     */
    @Override
    public NhStatisticsChartVo selectNhStatisRingRatioByType(Integer type,Date date) {
        // 当前月统计记录
        NhStatisticsVo nhCurrentStatisticsVo = requestStatisticsByMonthCondition(type, date);
        // 上一个月
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // month从0开始
        int lastMonth = calendar.get(Calendar.MONTH) ;
        calendar.add(Calendar.MONTH,-1);
        Date lastMonthDate = calendar.getTime();
        // 根据传入的date类型来获取上月月的统计数据
        NhStatisticsVo nhLastStatisticsVo = requestStatisticsByMonthCondition(type, lastMonthDate);


        // 如果上月值为0
        // 封装结果
        NhStatisticsChartVo nhStatisticsChartVo = statisticsChartVoResultHandler(nhCurrentStatisticsVo, nhLastStatisticsVo);
        nhStatisticsChartVo.getCurrentMonthItem().setTypeInfo(calendar.get(Calendar.YEAR)+"年"+(lastMonth + 1 )+"月");
        nhStatisticsChartVo.getLastMonthItem().setTypeInfo(calendar.get(Calendar.YEAR)+"年"+lastMonth+"月");
        nhStatisticsChartVo.getRete().setTypeInfo("环比值");
        return nhStatisticsChartVo;
    }

    @Override
    public NhStatisticsChartVo selectNhStatisYearRatioByType(Integer type, Date statisticsDate) {
        // 当年统计记录
        NhStatisticsVo nhCurrentStatisticsVo = requestStatisticsByYearCondition(type, statisticsDate);

        // 上年
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(statisticsDate);
        // month从0开始
        int lastYear = calendar.get(Calendar.YEAR) ;
        calendar.add(Calendar.YEAR,-1);
        Date lastYearDate = calendar.getTime();
        // 根据传入的date类型来获取去年的统计数据
        NhStatisticsVo nhLastStatisticsVo = requestStatisticsByYearCondition(type, lastYearDate);

        int month = calendar.get(Calendar.MONTH) + 1;
        // 如果上月值为0
        // 封装结果
        NhStatisticsChartVo nhStatisticsChartVo = statisticsChartVoResultHandler(nhCurrentStatisticsVo, nhLastStatisticsVo);
        nhStatisticsChartVo.getCurrentMonthItem().setTypeInfo(lastYear+"年"+month+"月");
        nhStatisticsChartVo.getLastMonthItem().setTypeInfo((lastYear - 1)+"年"+month+"月");
        nhStatisticsChartVo.getRete().setTypeInfo("同比值");
        return nhStatisticsChartVo;
    }

    /**
     * 根据传入的date类型来获取当月的统计数据
     * @param type
     * @param date
     * @return
     */
    public NhStatisticsVo requestStatisticsByYearCondition(Integer type,Date date){
        // 当前月统计记录
        Predicate<NhMeasureVo> predicate = (item) -> {
            Calendar currentYear = Calendar.getInstance();
            currentYear.setTime(date);
            // 设置为当前年当前月的第一天
            currentYear.set(Calendar.DAY_OF_MONTH, 1);
            Calendar lastYear = Calendar.getInstance();
            //去年
            lastYear.setTime(date);
            // 设置为当前年当前月最后一天
            lastYear.add(Calendar.MONTH, 1);
            lastYear.set(Calendar.DAY_OF_MONTH, 0);
            Date itemCurrentDate = item.getCurrentDate();
            System.out.println(currentYear);
            System.out.println();
            return itemCurrentDate.after(currentYear.getTime())
                    &&
                    itemCurrentDate.before(lastYear.getTime());
        };
        // 统计去年今年
        return groupStatisticsByCondition(type,predicate);
    };


    /**
     * 根据传入的date类型来获取当月的统计数据
     * @param type
     * @param date
     * @return
     */
    public NhStatisticsVo requestStatisticsByMonthCondition(Integer type,Date date){
        // 当前月统计记录
        Predicate<NhMeasureVo> predicate = (item) -> {
            Calendar firstDayByCurrentMonth  = Calendar.getInstance();
            // 设置为当前年当前月
            firstDayByCurrentMonth.setTime(date);
            firstDayByCurrentMonth.set(Calendar.DAY_OF_MONTH,0);
            Calendar lastDayByCurrentMonth = Calendar.getInstance();
            // 当前年下月的第0天
            lastDayByCurrentMonth.setTime(date);
            lastDayByCurrentMonth.add(Calendar.MONTH,1);
            lastDayByCurrentMonth.add(Calendar.DAY_OF_MONTH,0);
            Date itemCurrentDate = item.getCurrentDate();
            return itemCurrentDate.after(firstDayByCurrentMonth.getTime())
                    &&
                    itemCurrentDate.before(lastDayByCurrentMonth.getTime());
        };
        // 只保留当前年,当前月的统计信息
        return groupStatisticsByCondition(type,predicate);
    };
    /**
     * 结果集处理
     */
    public NhStatisticsChartVo statisticsChartVoResultHandler(NhStatisticsVo nhCurrentStatisticsVo,NhStatisticsVo nhLastStatisticsVo) {
        NhStatisticsChartVo resultStatisticsChartVoid = new NhStatisticsChartVo();
        // 当前
        NhStatisticsChartItem currentStatisticsChartItem = new NhStatisticsChartItem(nhCurrentStatisticsVo);
        resultStatisticsChartVoid.setCurrentMonthItem(currentStatisticsChartItem);
        // 对比上一个
        NhStatisticsChartItem lastStatisticsChartItem = new NhStatisticsChartItem(nhLastStatisticsVo);
        resultStatisticsChartVoid.setLastMonthItem(lastStatisticsChartItem);

        // 封装增长率
        // 按照x轴值和y轴
        Map<String, BigDecimal> lastStatisticsChartMap = lastStatisticsChartItem.getDatas().stream().collect(
                Collectors.toMap(NhStatisticsDataVo::getxData, NhStatisticsDataVo::getyData)
        );

        // 最大值
        BigDecimal maxBigDecimal = BigDecimal.ZERO;
        // 最大值
        BigDecimal minBigDecimal = BigDecimal.ZERO;
       // 当前的数据项
        List<NhStatisticsDataVo> rateStatisticsList = currentStatisticsChartItem.getDatas();
        // 增长率List
        List<NhStatisticsDataVo> rateLists = new ArrayList<>();
        for (NhStatisticsDataVo nhStatisticsDataVo : rateStatisticsList) {
            NhStatisticsDataVo newNhStatisticsDataVo = new NhStatisticsDataVo();
            // 当前遍历到的x轴数据
            String currentXdata = nhStatisticsDataVo.getxData();
            // 上次数据
            BigDecimal lastData = lastStatisticsChartMap.get(currentXdata);
            // 本次 - 上次 / 上次
            // BigDecimal的方法优于运算符的优先级
            // 默认保留两位小数 四舍五入
            BigDecimal rateOfRange;
            if(lastData.compareTo(BigDecimal.ZERO) > 0){
                 rateOfRange = nhStatisticsDataVo.getyData().subtract(lastData).divide(lastData, 4, RoundingMode.UP);
            }else{
                // 上月是0,环比值
                rateOfRange = BigDecimal.ZERO;
            }


            // 当前值小于最小值
            if(rateOfRange.compareTo(minBigDecimal) < 0){
                minBigDecimal = rateOfRange;
            }

            // 当前值大于最大值
            if(rateOfRange.compareTo(maxBigDecimal) > 0){
                maxBigDecimal = rateOfRange;
            }

            // 设置y值,x轴
            newNhStatisticsDataVo.setyData(rateOfRange);
            newNhStatisticsDataVo.setxData(currentXdata);
            rateLists.add(newNhStatisticsDataVo);
        }
        //封装增长率
        NhStatisticsChartItem rateStatisticsChartItem = new NhStatisticsChartItem();
        rateStatisticsChartItem.setDatas(rateLists);
        rateStatisticsChartItem.setMaxTotalNum(maxBigDecimal);
        rateStatisticsChartItem.setMinTotalNum(minBigDecimal);
        resultStatisticsChartVoid.setRete(rateStatisticsChartItem);
        return resultStatisticsChartVoid;
    }




}
