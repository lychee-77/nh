package com.dap.system.office.service.impl;

import com.dap.system.office.domain.TClass;
import com.dap.system.office.mapper.TClassMapper;
import com.dap.system.office.mapper.TStudentMapper;
import com.dap.system.office.service.ITClassService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TClassServiceImplTest {

    @Autowired
    ITClassService itClassService;

    @Autowired
    TClassMapper tClassMapper;

    @Autowired
    TStudentMapper tStudentMapper;

    @Test
    void deleteTClassByIds() {
        Integer integer = tStudentMapper.existsStudentAsClassId(Arrays.asList(1));
        System.out.println(integer);
    }
}