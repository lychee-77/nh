package com.dap.system.office.mapper;

import java.util.List;

import com.dap.system.office.domain.TClass;
import com.dap.system.office.domain.TStudent;
import com.dap.system.office.domain.vo.TStudentSelectorVo;

/**
 * 学生Mapper接口
 * 
 * @author dap
 * @date 2021-11-11
 */
public interface TStudentMapper 
{
    /**
     * 查询学生
     * 
     * @param id 学生ID
     * @return 学生
     */
    public TStudent selectTStudentById(Integer id);

    public TStudent selectTStudentByName(String name);

    Integer existsStudentAsClassId(List<Integer> ids);

    Integer existsIdNumberAndStudentNum(TStudent tStudent);
    /**
     * 查询学生列表
     * 
     * @param tStudent 学生
     * @return 学生集合
     */
    public List<TStudent> selectTStudentList(TStudent tStudent);

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
     * 删除学生
     * 
     * @param id 学生ID
     * @return 结果
     */
    public int deleteTStudentById(Integer id);

    /**
     * 批量删除学生
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTStudentByIds(Integer[] ids);

    List<TStudentSelectorVo> selectAllTStudentNameList(TStudent tStudent);

    Integer insertBatchTStudent(List<TStudent> studentList);
}
