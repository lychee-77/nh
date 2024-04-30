package com.dap.system.office.mapper;

import java.util.List;
import com.dap.system.office.domain.TCourse;
import com.dap.system.office.domain.vo.TCourseSelectorVo;

/**
 * 课程Mapper接口
 * 
 * @author dap
 * @date 2021-11-10
 */
public interface TCourseMapper 
{
    /**
     * 查询课程
     * 
     * @param id 课程ID
     * @return 课程
     */
    public TCourse selectTCourseById(Long id);

    /**
     * 查询课程列表
     * 
     * @param tCourse 课程
     * @return 课程集合
     */
    public List<TCourse> selectTCourseList(TCourse tCourse);

    /**
     * 新增课程
     * 
     * @param tCourse 课程
     * @return 结果
     */
    public int insertTCourse(TCourse tCourse);

    /**
     * 修改课程
     * 
     * @param tCourse 课程
     * @return 结果
     */
    public int updateTCourse(TCourse tCourse);

    /**
     * 删除课程
     * 
     * @param id 课程ID
     * @return 结果
     */
    public int deleteTCourseById(Integer id);

    /**
     * 批量删除课程
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTCourseByIds(Integer[] ids);

    List<TCourseSelectorVo> selectTCourseNameList();

    Integer existsCourse(TCourse tCourse);

    Integer existsTClasses(List<TCourse> buildSemesterList);

    int insertBatchTCourse(List<TCourse> buildSemesterList);
}
