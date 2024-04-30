package com.dap.system.nh;

import com.dap.common.core.utils.SecurityUtils;
import com.dap.system.nh.domain.vo.NhMeasureVo;
import com.dap.system.nh.domain.vo.NhStatisticsVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.function.Predicate;

@SpringBootTest
public class TimeTest {

    @Test
    void testDate(){
        SecurityUtils.encryptPassword("admin123");
    }

    public static void main(String[] args) {
        String admin123 = SecurityUtils.encryptPassword("admin123");
        System.out.println(admin123);
    }

}
