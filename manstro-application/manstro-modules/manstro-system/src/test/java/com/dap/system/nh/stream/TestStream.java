package com.dap.system.nh.stream;

import com.dap.system.nh.domain.vo.NhMeasureVo;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestStream {

    @Test
    void testStream(){
        // 快速生成集合,generate方法参数为一个供给者接口
        List<NhMeasureVo> collect = Stream.generate(NhMeasureVo::new).limit(5).collect(Collectors.toList());
        System.out.println(collect);
        // 数组快速转换为Stream
        Arrays.stream(new Integer[]{1,2,3,4,5}).forEach(System.out::println);

        // 迭代,为一个final UnaryOperator<T> f,类型.入参和返回值是同一类型
        Stream<Integer> stream2 = Stream.iterate(0, (x) -> x + 3).limit(4);
        stream2.forEach(System.out::println);

    }
}
