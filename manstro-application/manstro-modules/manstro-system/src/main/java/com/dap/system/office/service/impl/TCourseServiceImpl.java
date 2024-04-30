package com.dap.system.office.service.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.dap.common.core.exception.CustomException;
import com.dap.common.core.utils.DateUtils;
import com.dap.common.core.utils.SecurityUtils;
import com.dap.common.core.utils.StringUtils;
import com.dap.system.office.domain.TClass;
import com.dap.system.office.domain.TScore;
import com.dap.system.office.domain.TSemester;
import com.dap.system.office.domain.dto.TClassDTO;
import com.dap.system.office.domain.vo.TCourseSelectorVo;
import com.dap.system.office.mapper.TScoreMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dap.system.office.mapper.TCourseMapper;
import com.dap.system.office.domain.TCourse;
import com.dap.system.office.service.ITCourseService;

/**
 * 课程Service业务层处理
 *
 * @author dap
 * @date 2021-11-10
 */
@Service
public class TCourseServiceImpl implements ITCourseService
{
    @Autowired
    private TCourseMapper tCourseMapper;

    @Autowired
    private TScoreMapper tScoreMapper;

    /**
     * 查询课程
     *
     * @param id 课程ID
     * @return 课程
     */
    @Override
    public TCourse selectTCourseById(Long id)
    {
        return tCourseMapper.selectTCourseById(id);
    }

    /**
     * 查询课程列表
     *
     * @param tCourse 课程
     * @return 课程
     */
    @Override
    public List<TCourse> selectTCourseList(TCourse tCourse)
    {
        return tCourseMapper.selectTCourseList(tCourse);
    }

    /**
     * 分页查询课程列表
     *
     * @param tCourse 课程
     * @return 课程
     */
    @Override
    public List<TCourse> selectTCourseListPage(TCourse tCourse)
    {
        PageHelper.clearPage();
        Page<Object> objects = PageHelper.startPage(tCourse.getPageNum(), tCourse.getPageSize());
        objects.getTotal();
        PageHelper.clearPage();
        List<TCourse> list = tCourseMapper.selectTCourseList(tCourse);
        PageInfo<TCourse> tPageInfo = new PageInfo<>(list);
        List<TCourse> dataList = tPageInfo.getList();
        return dataList;
    }
    /**
     * 新增课程
     *
     * @param tCourse 课程
     * @return 结果
     */
    @Override
    public int insertTCourse(TCourse tCourse)
    {
        Integer row = tCourseMapper.existsCourse(tCourse);

        if(row == null || row == 0){
            tCourse.setCreateBy(SecurityUtils.getUsername());
            tCourse.setCreateTime(DateUtils.getNowDate());
            tCourse.setDelFlag("0");

            return tCourseMapper.insertTCourse(tCourse);
        }

        return 0;
    }

    /**
     * 修改课程
     *
     * @param tCourse 课程
     * @return 结果
     */
    @Override
    public int updateTCourse(TCourse tCourse)
    {
        // 是否存在课程
        Integer row = tCourseMapper.existsCourse(tCourse);
        if(row > 0 ){
            throw new CustomException("课程名字已存在!");
        }

        tCourse.setUpdateTime(DateUtils.getNowDate());
        tCourse.setUpdateBy(SecurityUtils.getUsername());
        tCourse.setDelFlag("0");
        return tCourseMapper.updateTCourse(tCourse);
    }

    /**
     * 批量删除课程
     *
     * @param ids 需要删除的课程ID
     * @return 结果
     */
    @Override
    public int deleteTCourseByIds(Integer[] ids)
    {

        // 避免多次查询数据库
        TScore tScore = new TScore();
        for (int i = 0; i < ids.length; i++) {
            tScore.setCourseId(ids[i]);
            List<TScore> tScores = tScoreMapper.selectTScoreList(tScore,null);
            if(tScores != null && tScores.size() > 0){
                throw new CustomException("该课程下还有成绩,无法直接删除");
            }
        }

        return tCourseMapper.deleteTCourseByIds(ids);
    }

    /**
     * 删除课程信息
     *
     * @param id 课程ID
     * @return 结果
     */
    @Override
    public int deleteTCourseById(Integer id)
    {
        TScore tScore = new TScore();
        tScore.setCourseId(id);
        Integer row = tScoreMapper.existsCourseScore(tScore);
        if(row > 0){
            throw new CustomException("你删除的课程已被选择");
        }
        return tCourseMapper.deleteTCourseById(id);
    }

    @Override
    public List<TCourseSelectorVo> selectTCourseNameList() {
        return tCourseMapper.selectTCourseNameList();
    }

    @Override
    public int importData(List<TCourse> courses, boolean updateSupport) {
        if (StringUtils.isNull(courses) || courses.size() == 0) {
            throw new CustomException("导入数据不能为空！");
        }


        // 数据库中是否已经有记录
        Integer existsRow = tCourseMapper.existsTClasses(courses);

        // 不更新数据且数据库中有重复数据
        if ( existsRow > 0) {
            throw new CustomException("导入的数据在数据库中已经存在!");
        }

        // 构建创建人和创建时间
        List<TCourse> buildSemesterList = courses.stream()
                .peek((tCourse) -> {
                    tCourse.setCreateBy(SecurityUtils.getUsername());
                    tCourse.setCreateTime(DateUtils.getNowDate());
                }).collect(Collectors.toList());


        return tCourseMapper.insertBatchTCourse(buildSemesterList);
    }


}
