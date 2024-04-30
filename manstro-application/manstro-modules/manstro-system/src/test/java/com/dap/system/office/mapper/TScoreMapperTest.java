package com.dap.system.office.mapper;

import com.dap.common.core.utils.StringUtils;
import com.dap.system.office.domain.TScore;
import com.dap.system.office.domain.vo.TCourseScoreVo;
import com.dap.system.office.domain.vo.TCourseSelectorVo;
import com.dap.system.office.domain.vo.TStudentDetailScoreVo;
import com.dap.system.office.domain.vo.TStudentScoreInfoVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TScoreMapperTest {

    @Autowired
    TScoreMapper tScoreMapper;


    @Autowired
    TCourseMapper tCourseMapper;


    @Test
    void selectTScoreList() {


        // 获取所有的课程列表
        List<TCourseSelectorVo> tCourseSelectorVos = tCourseMapper.selectTCourseNameList();
        // 所有课程id
        List<Integer> courseIds = tCourseSelectorVos.stream()
                .map(TCourseSelectorVo::getId).collect(Collectors.toList());

        // 根据课程id列表获取分数概要信息
        List<TStudentScoreInfoVo> tStudentScoreInfoVos =
                tScoreMapper.selectStudentScoreInfo(null,courseIds);

        // 根据平均分计算等级,并将课程id传入,供下层调用
        List<TStudentScoreInfoVo> collect = tStudentScoreInfoVos.stream()
                .map(this::resultScoreGrade)
                .peek(tStudentScoreInfoVo -> {
                    tStudentScoreInfoVo.setCourseIds(courseIds);
                })
                .map(this::getCourseScore)
                .collect(Collectors.toList());


    }

    private TStudentScoreInfoVo resultScoreGrade(TStudentScoreInfoVo tStudentScoreInfoVo){
        Double avgScore = tStudentScoreInfoVo.getAvgScore();
        if(avgScore == null || avgScore < 0 || avgScore > 100 ){
            tStudentScoreInfoVo.setGrade(1);
            return tStudentScoreInfoVo;
        }

        if(avgScore >= 90){
            tStudentScoreInfoVo.setGrade(4);
            return tStudentScoreInfoVo;
        }

        if(avgScore >= 80 && avgScore < 90){
            tStudentScoreInfoVo.setGrade(3);
            return tStudentScoreInfoVo;
        }

        if(avgScore >= 60 && avgScore < 80){
            tStudentScoreInfoVo.setGrade(2);
            return tStudentScoreInfoVo;
        }

        tStudentScoreInfoVo.setGrade(1);
        return tStudentScoreInfoVo;


    }
    private TStudentScoreInfoVo getCourseScore(TStudentScoreInfoVo tStudentScoreInfoVo){

        TScore tScore = new TScore();
        tScore.setStuId(tStudentScoreInfoVo.getId());
        tScore.setSemId(tStudentScoreInfoVo.getSemId());
        List<TScore> tScores = tScoreMapper.selectTScoreList(tScore,tStudentScoreInfoVo.getCourseIds());

        List<TCourseScoreVo> tCourseScoreVos = tScores.stream()
                .map(TCourseScoreVo::new).collect(Collectors.toList());

        tStudentScoreInfoVo.setCourseScoreList(tCourseScoreVos);
        return tStudentScoreInfoVo;
    }
}