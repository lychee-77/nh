package com.dap.system.office.mapper;

import java.util.List;
import com.dap.system.office.domain.TClass;
import com.dap.system.office.domain.TSemester;
import com.dap.system.office.domain.TStudent;
import com.dap.system.office.domain.dto.TClassDTO;
import com.dap.system.office.domain.vo.GradeClassTreeVo;
import com.dap.system.office.domain.vo.TClassSelectorVo;

/**
 * 班级Mapper接口
 * 
 * @author dap
 * @date 2021-11-11
 */
public interface TClassMapper 
{
    /**
     * 查询班级
     * 
     * @param id 班级ID
     * @return 班级
     */
    public TClass selectTClassById(Long id);

    public TClassDTO selectTClassAndSemester(TStudent tstudent);

    /**
     * 查询班级列表
     * 
     * @param tClass 班级
     * @return 班级集合
     */
    public List<TClass> selectTClassList(TClass tClass);

    /**
     * 新增班级
     * 
     * @param tClass 班级
     * @return 结果
     */
    public int insertTClass(TClass tClass);

    public Integer existsClass(TClass tClass);
    /**
     * 修改班级
     * 
     * @param tClass 班级
     * @return 结果
     */
    public int updateTClass(TClass tClass);

    /**
     * 删除班级
     * 
     * @param id 班级ID
     * @return 结果
     */
    public int deleteTClassById(Long id);

    /**
     * 批量删除班级
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTClassByIds(Integer[] ids);

    List<TClassSelectorVo> selectTClassNameList(TClass tClass);

    Integer existsTClasses(List<TClassDTO> semesterList);

    Integer insertBatchTSemester(List<TClassDTO> semesterList);
}
