package com.dap.system.nh.service.impl;

import com.dap.system.nh.domain.NhMeasure;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.dap.common.core.utils.DateUtils;
import com.dap.common.core.utils.SecurityUtils;
import com.dap.system.nh.domain.NhEquipment;
import com.dap.system.nh.domain.NhType;
import com.dap.system.nh.domain.vo.NhEquipmentVo;
import com.dap.system.nh.domain.vo.NhMeasureVo;
import com.dap.system.nh.mapper.NhEquipmentMapper;
import com.dap.system.nh.mapper.NhTypeMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dap.system.nh.mapper.NhMeasureMapper;
import com.dap.system.nh.service.INhMeasureService;

/**
 * 能耗统计Service业务层处理
 *
 * @author dap
 * @date 2021-12-07
 */
@Service
public class NhMeasureServiceImpl implements INhMeasureService {
    @Autowired
    private NhMeasureMapper nhMeasureMapper;

    @Autowired
    private NhEquipmentMapper nhEquipmentMapper;

    @Autowired
    private NhTypeMapper nhTypeMapper;

    /**
     * 查询能耗统计
     *
     * @param id 能耗统计ID
     * @return 能耗统计
     */
    @Override
    public NhMeasure selectNhMeasureById(Long id) {
        return nhMeasureMapper.selectNhMeasureById(id);
    }

    /**
     * 分页查询能耗统计列表
     *
     * @param NhMeasureVo 能耗统计
     * @return 能耗统计
     */
    @Override
    public List<NhMeasureVo> selectNhMeasureList(NhMeasureVo NhMeasureVo,boolean isStatistics) {
        // 返回给前端的数据
        List<NhMeasureVo> list = nhMeasureMapper.selectNhMeasureList(new NhMeasureVo());
        // 计算消耗值
        return setNhMeasureConsumeNum(list,isStatistics);

    }


    /**
     * 分页查询能耗统计列表
     *
     * @param qeuryParamsMeasureVo 能耗统计
     * @return 能耗统计
     */
    @Override
    public List<NhMeasureVo> selectNhMeasureListPage(NhMeasureVo qeuryParamsMeasureVo,boolean isStatistics) {
        PageHelper.startPage(qeuryParamsMeasureVo.getPageNum(), qeuryParamsMeasureVo.getPageSize());
        List<NhMeasureVo> nhMeasureVos = nhMeasureMapper.selectNhMeasureList(qeuryParamsMeasureVo);
        // 设置完消耗值的列表
        List<NhMeasureVo> consureMeasure = setNhMeasureConsumeNum(nhMeasureVos,false);
        PageInfo<NhMeasureVo> tPageInfo = new PageInfo<>(consureMeasure);
        return tPageInfo.getList();
    }

    /**
     * 计算取消耗值
     *
     * @param nhMeasureVos
     * @param isStatistics 设备为false
     * @return
     */
    private List<NhMeasureVo> setNhMeasureConsumeNum(List<NhMeasureVo> nhMeasureVos,boolean isStatistics) {
        // 数据库中所有的数据,计算差值不应该只从当前页中进行计算而是从全表中找距离最近的时间
        List<NhMeasureVo> allList = nhMeasureMapper.selectNhMeasureList(new NhMeasureVo());
        for (NhMeasureVo nhMeasureVo : nhMeasureVos) {
            // 获取统计日期
            Date currentDate = nhMeasureVo.getCurrentDate();

            // 获取相同设备,相同能耗类型的列表按照封装为Map<统计时间的毫秒值,当前对象>
            Stream<NhMeasureVo> stream = allList.stream();
            Map<Long, NhMeasureVo> equDataMap ;
            // 断言不为空,按照断言过滤
            if(!isStatistics){
                stream = stream.filter((item) ->
                        Objects.equals(item.getEquipId(), nhMeasureVo.getEquipId())
                                && Objects.equals(item.getNhTypeId(), nhMeasureVo.getNhTypeId()));
            }
            // 如果 isStatistics为true即获取的是统计值,因为不会过滤,所以会有重复的key 统计日期下必定会有重复的设备,能耗类型
            // 这里将重复key进行转换
            equDataMap = stream.collect(
                    Collectors.toMap((item) ->
                    item.getCurrentDate().getTime(), (item) -> item,
                            (NhMeasureVo beforeNhMeasureVo,NhMeasureVo afterNhMeasureVo) -> {
                        // 当key重复时,将前一个NhMeasureVo对象的totalNum和后一个相加,然后返回前一个
                                BigDecimal beforeNhMeasureVoTotalNum = beforeNhMeasureVo.getTotalNum();
                                afterNhMeasureVo.setTotalNum(beforeNhMeasureVoTotalNum.add(afterNhMeasureVo.getTotalNum()));
                                return beforeNhMeasureVo;
                            }));

            // 从时间列表中按照当前时间 减去时间得到一个差值
            // 获取大于0且最小的值
            // 没有则返回-1
            long latestTime = equDataMap.values().stream().map(NhMeasure::getCurrentDate)
                    .map((item) -> currentDate.getTime() - item.getTime())
                    .filter((item) -> item > 0).min(Long::compareTo).orElseGet(() -> -1L);

            // 最近一次的NhMeasureVo对象(当前值 - 差值) = 原来值
            NhMeasureVo latesNhMeasureVo = equDataMap.get(currentDate.getTime() - latestTime);

            // 数据库中不存在比当前时间大的时间
            if (-1L == latestTime) {
                // 消耗值为当前自己
                nhMeasureVo.setConsumeNum(nhMeasureVo.getTotalNum());
                continue;
            }
            // 消耗值
            BigDecimal consumeNum = nhMeasureVo.getTotalNum().subtract(latesNhMeasureVo.getTotalNum());
            nhMeasureVo.setConsumeNum(consumeNum);
        }
        return nhMeasureVos;
    }



    @Override
    public List<NhMeasureVo> currentSpaceIdIsExistsMeasure(List<NhMeasureVo> nhMeasure) {
        // 获取记录表中的数据
        List<NhMeasureVo> nhMeasureVos = nhMeasureMapper.selectNhMeasureList(new NhMeasureVo());

        // 返回的集合
        List<NhMeasureVo> resultNhStatists = new ArrayList<>();

        // 遍历传递的参数
        for (NhMeasureVo nhStatistic : nhMeasure) {
            resultNhStatists.addAll(doCompareNhMeasureVo(nhMeasureVos, nhStatistic));
        }

        return resultNhStatists;
    }


    @Override
    public List<NhMeasureVo> existsEquipmentByStatistic(NhMeasure nhMeasure) {
        // 获取所有的记录
        List<NhMeasureVo> nhMeasureVos = nhMeasureMapper.selectNhMeasureList(new NhMeasureVo());
        List<NhMeasureVo> resultNhMeasure = new ArrayList<>();
        // 查看当前记录是否在记录表中
        List<NhMeasureVo> MeasureVos = nhMeasureIsExists(nhMeasureVos, nhMeasure);
        if (MeasureVos.size() > 0) {
            // 剩下的记录都认为是相同的数据
            resultNhMeasure.addAll(MeasureVos);
        }
        return resultNhMeasure;
    }

    /**
     * 判断当前NhMeasureVo是否与nhMeasure重复
     * 设备id,日期,能耗都相同视为相同
     *
     * @param nhMeasure
     * @param nhStatistic
     * @return
     */
    private List<NhMeasureVo> nhMeasureIsExists(List<NhMeasureVo> nhMeasure, NhMeasure nhStatistic) {
        // 如果携带了id就排除自己
        if (Objects.nonNull(nhStatistic.getId())) {
            nhMeasure = nhMeasure.stream()
                    .filter(item -> !item.getId().equals(nhStatistic.getId())).collect(Collectors.toList());
        }
        Calendar instance = Calendar.getInstance();
        instance.setTime(nhStatistic.getCurrentDate());
        int year = instance.get(Calendar.YEAR);
        // 1月是0
        int month = instance.get(Calendar.MONTH);

        // 设备id,日期,能耗都相同视为相同
        // 日期在当前月下的
        return nhMeasure.stream()
                .filter((item) -> {
                    Calendar itemCalendar = Calendar.getInstance();
                    itemCalendar.setTime(item.getCurrentDate());
                    return itemCalendar.get(Calendar.YEAR) == year && itemCalendar.get(Calendar.MONTH) == month;
                })
                .filter((item) ->
                Objects.equals(item.getEquipId(), nhStatistic.getEquipId())
                && Objects.equals(item.getNhTypeId(), nhStatistic.getNhTypeId())).collect(Collectors.toList());
    }

    /**
     * 根据参数返回断言
     *
     * @param nhMeasureVo
     * @return
     */
    private Predicate<NhMeasureVo> buildQueryNhMeasureQueryCondition(NhMeasureVo nhMeasureVo) {
        // 楼宇 id不为null,按照 楼宇 id进行过滤
        if (Objects.nonNull(nhMeasureVo.getBuildId())) {
            return (item) -> Objects.equals(item.getBuildId(), nhMeasureVo.getBuildId());
        }

        // 楼层 id不为null,按照 楼层 id进行过滤
        if (Objects.nonNull(nhMeasureVo.getFloorId())) {
            return (item) -> Objects.equals(item.getFloorId(), nhMeasureVo.getFloorId());
        }

        // 房屋 id不为null,按照 房屋 id进行过滤
        if (Objects.nonNull(nhMeasureVo.getSpaceId())) {
            return (item) -> Objects.equals(item.getSpaceId(), nhMeasureVo.getSpaceId());
        }

        // 设备 id不为null,按照 设备 id进行过滤
        if (Objects.nonNull(nhMeasureVo.getEquipId())) {
            return (item) -> Objects.equals(item.getEquipId(), nhMeasureVo.getEquipId());
        }

        // 能耗类型 id不为null,按照 能耗类型 id进行过滤
        if (Objects.nonNull(nhMeasureVo.getNhTypeId())) {
            return (item) -> Objects.equals(item.getNhTypeId(), nhMeasureVo.getNhTypeId());
        }

        // 其余情况视为不匹配
        return (item) -> false;

    }

    /**
     * 按照参数判断,指定集合中是否有该类型
     *
     * @param nhMeasureVo
     * @return
     */
    protected List<NhMeasureVo> doCompareNhMeasureVo(List<NhMeasureVo> nhMeasure, NhMeasureVo nhMeasureVo) {
        // 从nhMeasure对象从解析出Predicate对象类型
        return this.doCompareNhMeasureVo(nhMeasure, buildQueryNhMeasureQueryCondition(nhMeasureVo));
    }

    /**
     * 重载的方法,按照自定义的predicate进行过滤
     *
     * @return
     */
    @Override
    public List<NhMeasureVo> doCompareNhMeasureVo(List<NhMeasureVo> nhMeasure, Predicate<? super NhMeasureVo> predicate) {
        // 从nhMeasure中过滤
        return nhMeasure.stream().filter(predicate).collect(Collectors.toList());
    }

    /**
     * 根据vo对象解析出来实体类对象
     */
    @Override
    public List<NhMeasure> nhMeasureByNhStatisticVos(List<NhMeasureVo> nhMeasureVos) {

        List<NhMeasure> resultExistsNhMeasureVos = new ArrayList<>();
        for (NhMeasureVo nhMeasureVo : nhMeasureVos) {
            // 排除自身的同时其他都相同,即表格中有重复数据
            // 条件构造
            NhEquipmentVo nhEquipmentVo = new NhEquipmentVo();
            nhEquipmentVo.setBuildName(nhMeasureVo.getBuildName());
            nhEquipmentVo.setFloorName(nhMeasureVo.getFloorName());
            nhEquipmentVo.setRoomName(nhMeasureVo.getRoomName());
            nhEquipmentVo.setTypeModel(nhMeasureVo.getTypeModel());
            // 区域,型号唯一确定一个设备
            List<NhEquipment> nhEquipments = nhEquipmentMapper.selectNhEquipmentGetEquipId(nhEquipmentVo);
            NhType nhType = new NhType();
            nhType.setName(nhMeasureVo.getNhTypeModel());
            List<NhType> nhTypes = nhTypeMapper.selectNhTypeList(nhType);
            // 设备或者能耗类型只要有一个无法获得id就返回空列表
            if (!(nhEquipments.isEmpty() || nhTypes.isEmpty())) {
                resultExistsNhMeasureVos.add(new NhMeasure(nhEquipments.get(0), nhTypes.get(0), nhMeasureVo));
            }

        }

        // 不存在相同,返回null
        return resultExistsNhMeasureVos;
    }

    /**
     * 得到当前日期下允许的最大总值
     *
     * @param nhMeasure
     * @return
     */
    @Override
    public List<BigDecimal> selectTotalNumRange(NhMeasure nhMeasure) {
        NhMeasureVo queryObj = new NhMeasureVo();
        queryObj.setEquipId(nhMeasure.getEquipId());
        queryObj.setNhTypeId(nhMeasure.getNhTypeId());
        List<NhMeasureVo> nhMeasureVos = nhMeasureMapper.selectNhMeasureList(queryObj);
        // 最大值是大于当前时间的最小值
        BigDecimal maxRange = nhMeasureVos.stream()
                .filter((item) -> item.getCurrentDate().after(nhMeasure.getCurrentDate()))
                .map(NhMeasure::getTotalNum)
                .min(BigDecimal::compareTo).orElse(new BigDecimal(0));
        // 最小值是小于当前时间的最大值
        BigDecimal minRange = nhMeasureVos.stream()
                .filter((item) -> item.getCurrentDate().before(nhMeasure.getCurrentDate()))
                .map(NhMeasure::getTotalNum)
                .max(BigDecimal::compareTo).orElse(new BigDecimal(0));
        List<BigDecimal> totalNumRnage = new ArrayList<>();

        totalNumRnage.add(minRange);
        if (maxRange.compareTo(BigDecimal.ZERO) == 0) {
            totalNumRnage.add(BigDecimal.valueOf(Integer.MAX_VALUE));
            return totalNumRnage;
        }
        totalNumRnage.add(maxRange);
        return totalNumRnage;
    }

    private Predicate<NhMeasureVo> getPredicateByNhMeasureVo(List<NhMeasureVo> databaseNhMeasureVos, NhMeasureVo nhMeasureVo) {
        // 统计时间为空,所有记录都是重复记录
        if(nhMeasureVo.getCurrentDate() == null){
            return (item) -> false;
        }
        Calendar beforeMonth = Calendar.getInstance();
        // 获取打入数据的统计日期
        beforeMonth.setTime(nhMeasureVo.getCurrentDate());
        // 本月第一天
        beforeMonth.set(Calendar.DAY_OF_MONTH,0);
        // 下月第0天,即本月最后一天
        Calendar lastMonth = Calendar.getInstance();
        lastMonth.setTime(nhMeasureVo.getCurrentDate());
        lastMonth.add(Calendar.MONTH,1);
        lastMonth.set(Calendar.DAY_OF_MONTH, 1);

        return (item) ->
                Objects.equals(item.getBuildName(), nhMeasureVo.getBuildName())
                        &&
                        Objects.equals(item.getFloorName(), nhMeasureVo.getFloorName())
                        &&
                        Objects.equals(item.getRoomName(), nhMeasureVo.getRoomName())
                        &&
                        Objects.equals(item.getTypeModel(), nhMeasureVo.getTypeModel())
                        &&
                        item.getCurrentDate().after(beforeMonth.getTime()) && item.getCurrentDate().before(lastMonth.getTime())
                        &&
                        Objects.equals(item.getNhTypeModel(), nhMeasureVo.getNhTypeModel());
    }


    /**
     * 新增能耗统计
     *
     * @param nhMeasure 能耗统计
     * @return 结果
     */
    @Override
    public int insertNhMeasure(NhMeasure nhMeasure) {
        nhMeasure.setDelFlag("0");
        nhMeasure.setCreateUser(SecurityUtils.getUsername());
        nhMeasure.setCreateTime(DateUtils.getNowDate());
        return nhMeasureMapper.insertNhMeasure(nhMeasure);
    }

    /**
     * 修改能耗统计
     *
     * @param nhMeasure 能耗统计
     * @return 结果
     */
    @Override
    public int updateNhMeasure(NhMeasure nhMeasure) {
        nhMeasure.setUpdateUser(SecurityUtils.getUsername());
        nhMeasure.setUpdateTime(DateUtils.getNowDate());
        return nhMeasureMapper.updateNhMeasure(nhMeasure);
    }

    /**
     * 批量删除能耗统计
     *
     * @param ids 需要删除的能耗统计ID
     * @return 结果
     */
    @Override
    public int deleteNhMeasureByIds(Long[] ids) {
        return nhMeasureMapper.deleteNhMeasureByIds(ids);
    }

    /**
     * 删除能耗统计信息
     *
     * @param id 能耗统计ID
     * @return 结果
     */
    @Override
    public int deleteNhMeasureById(Long id) {
        return nhMeasureMapper.deleteNhMeasureById(id);
    }


    /**
     * 检查表格中的数据是否存在问题,没有问题返回null
     *
     * @param nhMeasureVos 待检测的数据
     * @return 错误的数据
     */
    @Override
    public List<NhMeasureVo> checkExcelDataValidity(List<NhMeasureVo> dataBaseNhMeasureVos, List<NhMeasureVo> nhMeasureVos) {
        List<NhMeasureVo> resultExistsNhMeasureVos = new ArrayList<>();
        for (NhMeasureVo nhMeasureVo : nhMeasureVos) {
            // 排除自身的同时其他都相同,即表格中有重复数据
            Predicate<NhMeasureVo> predicate = getPredicateByNhMeasureVo(dataBaseNhMeasureVos, nhMeasureVo);
            List<NhMeasureVo> existsNhMeasureVo = doCompareNhMeasureVo(dataBaseNhMeasureVos, predicate);
            // 存在相同的信息,
            if (existsNhMeasureVo.size() > 0) {
                NhMeasureVo nhMeasureVo1 = existsNhMeasureVo.get(0);
                nhMeasureVo1.setTotalNum(nhMeasureVo.getTotalNum());
                nhMeasureVo1.setConsumeNum(nhMeasureVo.getConsumeNum());
                resultExistsNhMeasureVos.add(nhMeasureVo1);
            }
        }
        // 不存在相同,返回null
        return resultExistsNhMeasureVos;
    }
}
