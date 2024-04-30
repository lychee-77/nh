package com.dap.system.nh;

import com.dap.system.nh.domain.vo.NhMeasureVo;
import com.dap.system.nh.domain.vo.NhStatisticsDataVo;
import com.dap.system.nh.domain.vo.NhStatisticsVo;
import com.dap.system.nh.mapper.NhMeasureMapper;
import com.dap.system.nh.mapper.NhStatisticsMapper;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@SpringBootTest
public class StatisticsMapperTest {

    @Autowired
    NhStatisticsMapper nhStatisticsMapper;

    @Autowired
    NhMeasureMapper nhMeasureMapper;

    @Test
    void testStatics(){
        List<NhMeasureVo> nhMeasureVos = nhMeasureMapper.selectNhMeasureList(new NhMeasureVo());
        Map<String, List<NhMeasureVo>> collect = nhMeasureVos.stream()
                .collect(Collectors.groupingBy(NhMeasureVo::getNhTypeModel));
        NhStatisticsVo nhStatisticsVo = new NhStatisticsVo();
        List<NhStatisticsDataVo> nhStatisticsVoids = new ArrayList<NhStatisticsDataVo>();
        for (Map.Entry<String, List<NhMeasureVo>> entry : collect.entrySet()) {
            String k = entry.getKey();
            List<NhMeasureVo> v = entry.getValue();
            NhStatisticsDataVo nhStatisticsDataVo = new NhStatisticsDataVo();
            nhStatisticsDataVo.setxData(k);
            BigDecimal bigDecimal = v.stream().map(NhMeasureVo::getTotalNum)
                    .reduce(BigDecimal::add
            ).orElseGet(() -> new BigDecimal(0));

            nhStatisticsDataVo.setyData(bigDecimal);
            nhStatisticsVoids.add(nhStatisticsDataVo);
        }
        nhStatisticsVo.setDatas(nhStatisticsVoids);
        System.out.println(nhStatisticsVo);
    }
}
