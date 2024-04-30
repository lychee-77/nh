package com.dap.system.office;

import com.dap.system.office.domain.TSemester;
import com.dap.system.office.mapper.TSemesterMapper;
import org.checkerframework.checker.units.qual.A;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
public class TSemesterMapperTest {

    @Autowired
    TSemesterMapper tSemesterMapper;


    List<TSemester> semesters =  new ArrayList<>();
    @BeforeEach
    void initsTemester(){
        TSemester tSemester = new TSemester();
        tSemester.setStartYear("2013");
        tSemester.setEndYear("2014");
        tSemester.setCreateTime(new Date());
        tSemester.setCreateBy("yxlzm");
        TSemester tSemester1 = new TSemester();
        tSemester1.setStartYear("2014");
        tSemester1.setEndYear("2015");
        tSemester1.setCreateTime(new Date());
        tSemester1.setCreateBy("yxlzm");
        TSemester tSemester2 = new TSemester();
        tSemester2.setStartYear("2015");
        tSemester2.setEndYear("2016");
        tSemester2.setCreateTime(new Date());
        tSemester2.setCreateBy("yxlzm");
        TSemester tSemester3 = new TSemester();
        tSemester3.setStartYear("2017");
        tSemester3.setEndYear("2018");
        tSemester3.setCreateTime(new Date());
        tSemester3.setCreateBy("yxlzm");
        semesters.add(tSemester);
        semesters.add(tSemester1);
        semesters.add(tSemester2);
        semesters.add(tSemester3);
    }
    @Test
    void testexistsTSemesters(){
        Integer integer = tSemesterMapper.existsTSemesters(semesters);
        System.out.println(integer);
    }

    @Test
    void testInsertTSemesters(){
        Integer integer = tSemesterMapper.insertBatchTSemester(semesters);
        System.out.println(integer);
    }
}
