package com.dap.system.office.service;

import java.util.List;

import com.dap.common.core.web.domain.BaseEntity;
import com.dap.system.office.domain.TClass;
import com.dap.system.office.domain.TSemester;
import com.dap.system.office.domain.vo.TSemesterCascaderVo;
import com.dap.system.office.domain.vo.TSemesterSelectorVo;
import com.dap.system.office.domain.vo.TTeacherSelectorVo;

/**
 * 学期管理Service接口
 * 
 * @author dap
 * @date 2021-11-11
 */
public interface ITSemesterService 
{
    /**
     * 查询学期管理
     * 
     * @param id 学期管理ID
     * @return 学期管理
     */
    public TSemester selectTSemesterById(Integer id);

    /**
     * 查询学期管理列表
     * 
     * @param tSemester 学期管理
     * @return 学期管理集合
     */
    public List<TSemester> selectTSemesterList(TSemester tSemester);

    /**
     * 分页查询学期管理列表
     *
     * @param tSemester 学期管理
     * @return 学期管理集合
     */
    public List<TSemester> selectTSemesterListPage(TSemester tSemester);


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
     * 批量删除学期管理
     * 
     * @param ids 需要删除的学期管理ID
     * @return 结果
     */
    public int deleteTSemesterByIds(Integer[] ids);

    /**
     * 删除学期管理信息
     * 
     * @param id 学期管理ID
     * @return 结果
     */
    public int deleteTSemesterById(Integer id);

    List<TSemesterSelectorVo> selectTSemesterNameListPage();


    public Integer importSemester
    (List<TSemester> semesterList, Boolean isUpdateSupport);

    public List<TSemesterCascaderVo> selectTSemesterCascaderVo();

}
