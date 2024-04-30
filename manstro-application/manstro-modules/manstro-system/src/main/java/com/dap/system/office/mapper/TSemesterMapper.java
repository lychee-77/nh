package com.dap.system.office.mapper;

import java.util.List;
import com.dap.system.office.domain.TSemester;
import com.dap.system.office.domain.vo.TSemesterSelectorVo;

/**
 * 学期管理Mapper接口
 * 
 * @author dap
 * @date 2021-11-11
 */
public interface TSemesterMapper 
{
    /**
     * 查询学期管理
     * 
     * @param id 学期管理ID
     * @return 学期管理
     */
    public TSemester selectTSemesterById(Integer id);

    TSemester selectTSemester(TSemester tsemester);

    /**
     * 查询学期管理列表
     * 
     * @param tSemester 学期管理
     * @return 学期管理集合
     */
    public List<TSemester> selectTSemesterList(TSemester tSemester);

    /**
     * 新增学期管理
     * 
     * @param tSemester 学期管理
     * @return 结果
     */
    public int insertTSemester(TSemester tSemester);

    /**
     * 修改学期管理
     * 
     * @param tSemester 学期管理
     * @return 结果
     */
    public int updateTSemester(TSemester tSemester);

    /**
     * 删除学期管理
     * 
     * @param id 学期管理ID
     * @return 结果
     */
    public int deleteTSemesterById(Integer id);

    /**
     * 批量删除学期管理
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTSemesterByIds(Integer[] ids);

    List<TSemester> selectAllTSemesterNameList();

    Integer existsTSemesters(List<TSemester> semesterList);

    Integer insertBatchTSemester(List<TSemester> semesterList);


}
