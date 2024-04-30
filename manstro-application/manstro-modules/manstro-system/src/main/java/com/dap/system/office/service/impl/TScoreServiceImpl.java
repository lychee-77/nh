package com.dap.system.office.service.impl;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.dap.common.core.exception.CustomException;
import com.dap.common.core.utils.DateUtils;
import com.dap.common.core.utils.SecurityUtils;
import com.dap.system.office.domain.TCourse;
import com.dap.system.office.domain.TSemester;
import com.dap.system.office.domain.TStudent;
import com.dap.system.office.domain.vo.TCourseScoreVo;
import com.dap.system.office.domain.vo.TCourseSelectorVo;
import com.dap.system.office.domain.vo.TStudentDetailScoreVo;
import com.dap.system.office.domain.vo.TStudentScoreInfoVo;
import com.dap.system.office.mapper.TCourseMapper;
import com.dap.system.office.mapper.TSemesterMapper;
import com.dap.system.office.mapper.TStudentMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dap.system.office.mapper.TScoreMapper;
import com.dap.system.office.domain.TScore;
import com.dap.system.office.service.ITScoreService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 分数Service业务层处理
 *
 * @author dap
 * @date 2021-11-12
 */
@Service
public class TScoreServiceImpl implements ITScoreService {
    @Autowired
    private TScoreMapper tScoreMapper;

    @Autowired
    private TStudentMapper tStudentMapper;


    @Autowired
    private TSemesterMapper tSemesterMapper;

    @Autowired
    private TCourseMapper tCourseMapper;

    /**
     * 查询分数
     *
     * @param id 分数ID
     * @return 分数
     */
    @Override
    public TStudentDetailScoreVo selectTScoreById(Integer id, Integer semId) {
        TScore tScore = new TScore();
        tScore.setStuId(id);
        tScore.setSemId(semId);
        TStudent tStudent = tStudentMapper.selectTStudentById(id);
        TSemester semester = tSemesterMapper.selectTSemesterById(semId);

        List<TScore> tScores = tScoreMapper.selectTScoreList(tScore, null);
        TStudentDetailScoreVo tStudentDetailScoreVo = new TStudentDetailScoreVo(tStudent,semester);

        // 从分数表中构建课程分数对象
        List<TCourseScoreVo> tCourseScoreVos = tScores.stream()
                .map(TCourseScoreVo::new).collect(Collectors.toList());
        // 查询出来的有成绩的TCourseScoreVo课程id集合
        List<Integer> courseIds = tCourseScoreVos.stream()
                .map(TCourseScoreVo::getCourseId)
                .collect(Collectors.toList());

        // 获取课程表中所有课程
                 // tCourseSelectorVos中只保留不存在的课程id
        List<TCourseScoreVo> tCourseVos = tCourseMapper.selectTCourseNameList().stream()
                .filter(tCourseScoreVo ->
                        !courseIds.contains(tCourseScoreVo.getId()
                        ))
                .map(TCourseScoreVo::new)
                .collect(Collectors.toList());

        // 将从课程表中查询出来的TCourseScoreVo在分数表中不存在的TCourseScoreVo对象添加到tScoreVos中
        tCourseScoreVos.addAll(tCourseVos);

        tStudentDetailScoreVo.setCourseScoreList(tCourseScoreVos);

        return tStudentDetailScoreVo;
    }

    /**
     * 查询分数列表
     *
     * @param tScore 分数
     * @return 分数
     */
    @Override
    public List<TScore> selectTScoreList(TScore tScore) {
        return tScoreMapper.selectTScoreList(tScore, null);
    }

    /**
     * 分页查询分数列表
     *
     * @param tScore 分数
     * @return 分数
     */
    @Override
    public List<TScore> selectTScoreListPage(TScore tScore) {
        PageHelper.startPage(tScore.getPageNum(), tScore.getPageSize());
        List<TScore> list = tScoreMapper.selectTScoreList(tScore, null);
        PageInfo<TScore> tPageInfo = new PageInfo<>(list);
        List<TScore> dataList = tPageInfo.getList();
        return dataList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertTScore(TStudentDetailScoreVo tScoreInfo) {
        // 构建查询对象
        TScore tScoreParamObj = new TScore();
        tScoreParamObj.setStuId(tScoreInfo.getStuId());
        tScoreParamObj.setSemId(tScoreInfo.getSemId());
        List<TScore> tScores = tScoreMapper.selectTScoreList(tScoreParamObj, null);
        // 数据库中已经有学生的成绩记录后禁止插入
        if (tScores.size() > 0) {
            return 0;
        }

        int row = 0;
        // 获取待插入的TScore对象
        List<TScore> tStudentScoreInfoVo = getTStudentScoreInfoVo(tScoreInfo);
        for (TScore tScore : tStudentScoreInfoVo) {
            int i = tScoreMapper.insertTScore(tScore);
            // 插入失败,触发回滚
            if (i == 0) {
                throw new CustomException("数据发生冲突,请检查");
            }
            row += i;
        }
        return row;
    }

    private List<TScore> getTStudentScoreInfoVo(TStudentDetailScoreVo tStudentDetailScoreVo) {
        ArrayList<TScore> tscores = new ArrayList<>();

       // 获取前端传入的待插入的课程列表
        List<TCourseScoreVo> courseScoreList = tStudentDetailScoreVo.getCourseScoreList();
        // 遍历
        for (TCourseScoreVo tCourseScoreVo : courseScoreList) {
            // 初始化TScore,学生id,学期id
            TScore tScore = initTScore(tStudentDetailScoreVo);
            // 设置课程id及对应分数
            tScore.setCourseId(tCourseScoreVo.getCourseId());
            tScore.setScore(tCourseScoreVo.getScore());
            tscores.add(tScore);
        }

        return tscores;

    }

    private TScore initTScore(TStudentDetailScoreVo tStudentDetailScoreVo) {
        TScore tScore = new TScore();
        tScore.setStuId(tStudentDetailScoreVo.getStuId());
        tScore.setSemId(tStudentDetailScoreVo.getSemId());
        tScore.setCreateTime(DateUtils.getNowDate());
        tScore.setUpdateTime(DateUtils.getNowDate());
        tScore.setUpdateBy(SecurityUtils.getUsername());
        tScore.setDelFlag("0");
        tScore.setIsPass(0);
        tScore.setCreateBy(SecurityUtils.getUsername());
        return tScore;
    }


    /**
     * 修改分数
     *
     * @param tStudentDetailScoreVo
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateTScore(TStudentDetailScoreVo tStudentDetailScoreVo) {
        // 构建查询对象
        TScore tScoreParamObj = new TScore();
        tScoreParamObj.setStuId(tStudentDetailScoreVo.getStuId());
        tScoreParamObj.setSemId(tStudentDetailScoreVo.getSemId());
        List<TScore> tScores = tScoreMapper.selectTScoreList(tScoreParamObj, null);
        // 数据库中必须有该学生,该学期才可以进行修改
        if (tScores.size() == 0) {
            throw new CustomException("该学生在该学期还未有成绩,请先进行录入");
        }

        int row = 0;
        List<TScore> tscores = getTStudentScoreInfoVo(tStudentDetailScoreVo);
        for (TScore tscore : tscores) {

            int i = tScoreMapper.updateTScore(tscore);
            if(i == 0){
                tScoreMapper.insertTScore(tscore);
//                throw new CustomException("出现不存在的课程成绩已为你自动插入!");
            }

            row += i;
        }
        return row;
    }


    /**
     * 批量删除分数
     *
     * @param id 需要删除的分数ID
     * @return 结果
     */
    @Override
    public int deleteTScoreByIds(Integer id, Integer semId) {
        TScore tScore = new TScore();
        tScore.setStuId(id);
        tScore.setSemId(semId);
        return tScoreMapper.deleteTScoreByStuId(tScore);
    }

    /**
     * 删除分数信息
     *
     * @param id 分数ID
     * @return 结果
     */
    @Override
    public int deleteTScoreById(Integer id) {
        return tScoreMapper.deleteTScoreById(id);
    }

    @Override
    public List<TStudentScoreInfoVo> selectStudentScoreInfo(TScore tScore) {


        PageHelper.clearPage();
        PageHelper.startPage(tScore.getPageNum(), tScore.getPageSize());
        List<TStudentScoreInfoVo> tStudentScoreInfoVos = tScoreMapper.selectStudentScoreInfo(tScore, null);
        // 根据平均分计算等级
        List<TStudentScoreInfoVo> tStudentScoreInfoVoList = tStudentScoreInfoVos.stream()
                .map(this::resultScoreGrade)
                .map(this::getCourseScore)
                .collect(Collectors.toList());
        PageInfo<TStudentScoreInfoVo> tPageInfo = new PageInfo<TStudentScoreInfoVo>(tStudentScoreInfoVos);
        List<TStudentScoreInfoVo> dataList = tPageInfo.getList();
        return dataList;
    }

    @Override
    public List<TStudentDetailScoreVo> selectStudentScoreDetails() {
//        List<TStudentScoreInfoVo> tStudentScoreInfoVos = tScoreMapper.selectStudentScoreInfo(new TScore());
//        // 根据平均分计算等级
//        return tStudentScoreInfoVos.stream()
//                .map(this::resultScoreGrade)
//                .map(this::getStudentDetailScore)
//                .map(TStudentScoreInfoVo::gettStudentDetailVo)
//                .collect(Collectors.toList());
        return null;

    }

    /**
     * 根据tStudentScoreInfoVo 封装信息成绩信息
     *
     * @param tStudentScoreInfoVo
     * @return
     */
    private TStudentScoreInfoVo getCourseScore(TStudentScoreInfoVo tStudentScoreInfoVo) {

        TScore tScore = new TScore();
        tScore.setStuId(tStudentScoreInfoVo.getId());
        tScore.setSemId(tStudentScoreInfoVo.getSemId());
        List<TScore> tScores = tScoreMapper.selectTScoreList(tScore, tStudentScoreInfoVo.getCourseIds());

        // 从分数表中获取到有成绩的TCourseScoreVo
        List<TCourseScoreVo> tScoreVos = tScores.stream()
                .map(TCourseScoreVo::new).collect(Collectors.toList());


        tStudentScoreInfoVo.setCourseScoreList(tScoreVos);
        return tStudentScoreInfoVo;
    }

    private TStudentScoreInfoVo resultScoreGrade(TStudentScoreInfoVo tStudentScoreInfoVo) {
        Double avgScore = tStudentScoreInfoVo.getAvgScore();
        if (avgScore == null || avgScore < 0 || avgScore > 100) {
            tStudentScoreInfoVo.setGrade(1);
            return tStudentScoreInfoVo;
        }

        if (avgScore >= 90) {
            tStudentScoreInfoVo.setGrade(4);
            return tStudentScoreInfoVo;
        }

        if (avgScore >= 80 && avgScore < 90) {
            tStudentScoreInfoVo.setGrade(3);
            return tStudentScoreInfoVo;
        }

        if (avgScore >= 60 && avgScore < 80) {
            tStudentScoreInfoVo.setGrade(2);
            return tStudentScoreInfoVo;
        }

        tStudentScoreInfoVo.setGrade(1);
        return tStudentScoreInfoVo;


    }


}
