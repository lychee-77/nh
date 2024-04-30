package com.dap.system.office.service;

import java.util.List;
import com.dap.system.office.domain.TClass;
import com.dap.system.office.domain.vo.TClassSelectorVo;
import com.dap.system.office.domain.vo.TSemesterCascaderVo;

/**
 * 班级Service接口
 * 
 * @author dap
 * @date 2021-11-11
 */
public interface ITClassService 
{
    /**
     * 查询班级
     * 
     * @param id 班级ID
     * @return 班级
     */
    public TClassSelectorVo selectTClassById(Long id);

    /**
     * 查询班级列表
     * 
     * @param tClass 班级
     * @return 班级集合
     */
    public List<TClass> selectTClassList(TClass tClass);

    /**
     * 分页查询班级列表
     *
     * @param tClass 班级
     * @return 班级集合
     */
    public List<TClass> selectTClassListPage(TClass tClass);


    /**
     * 新增班级
     * 
     * @param tClass 班级
     * @return 结果
     */
    public int insertTClass(TClass tClass);

    /**
     * 修改班级
     * 
     * @param tClass 班级
     * @return 结果
     */
    public int updateTClass(TClass tClass);

    /**
     * 批量删除班级
     * 
     * @param ids 需要删除的班级ID
     * @return 结果
     */
    public int deleteTClassByIds(Integer[] ids);

    /**
     * 删除班级信息
     * 
     * @param id 班级ID
     * @return 结果
     */
    public int deleteTClassById(Long id);

    List<TClassSelectorVo> selectTClassNameList(TClass tClass) ;


    Integer importData(List<TClass> list, Boolean isUpdateSupport);


}
