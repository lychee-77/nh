package com.dap.system.office.service;

import java.util.List;
import com.dap.system.office.domain.TStudent;
import com.dap.system.office.domain.vo.TCascaderNode;
import com.dap.system.office.domain.vo.TStudentSelectorVo;

/**
 * 学生Service接口
 * 
 * @author dap
 * @date 2021-11-11
 */
public interface ITStudentService 
{
    /**
     * 查询学生
     * 
     * @param id 学生ID
     * @return 学生
     */
    public TStudent selectTStudentById(Integer id);

    /**
     * 查询学生列表
     * 
     * @param tStudent 学生
     * @return 学生集合
     */
    public List<TStudent> selectTStudentList(TStudent tStudent);

    /**
     * 分页查询学生列表
     *
     * @param tStudent 学生
     * @return 学生集合
     */
    public List<TStudent> selectTStudentListPage(TStudent tStudent);


    /**
     * 新增学生
     * 
     * @param tStudent 学生
     * @return 结果
     */
    public int insertTStudent(TStudent tStudent);

    /**
     * 修改学生
     * 
     * @param tStudent 学生
     * @return 结果
     */
    public int updateTStudent(TStudent tStudent);

    /**
     * 批量删除学生
     * 
     * @param ids 需要删除的学生ID
     * @return 结果
     */
    public int deleteTStudentByIds(Integer[] ids);

    /**
     * 删除学生信息
     * 
     * @param id 学生ID
     * @return 结果
     */
    public int deleteTStudentById(Integer id);

    List<TCascaderNode> selectTStudentNameList(TStudent tStudent);

    Integer importData(List<TStudent> semesterList, boolean updateSupport);
}
