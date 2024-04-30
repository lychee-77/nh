package com.dap.system.office.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.dap.common.core.exception.CustomException;
import com.dap.common.core.utils.DateUtils;
import com.dap.common.core.utils.SecurityUtils;
import com.dap.common.core.utils.StringUtils;
import com.dap.system.office.domain.TSemester;
import com.dap.system.office.domain.TStudent;
import com.dap.system.office.domain.dto.TClassDTO;
import com.dap.system.office.domain.vo.TClassSelectorVo;
import com.dap.system.office.domain.vo.TSemesterSelectorVo;
import com.dap.system.office.mapper.TSemesterMapper;
import com.dap.system.office.mapper.TStudentMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dap.system.office.mapper.TClassMapper;
import com.dap.system.office.domain.TClass;
import com.dap.system.office.service.ITClassService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 班级Service业务层处理
 *
 * @author dap
 * @date 2021-11-11
 */
@Service
public class TClassServiceImpl implements ITClassService
{
    @Autowired
    private TClassMapper tClassMapper;

    @Autowired
    private TStudentMapper tStudentMapper;

    @Autowired
    private TSemesterMapper tSemesterMapper;

    /**
     * 查询班级
     *
     * @param id 班级ID
     * @return 班级
     */
    @Override
    public TClassSelectorVo selectTClassById(Long id)
    {
        TClass tClass = tClassMapper.selectTClassById(id);
        return buildTClassSelectorVo(tClass);
    }

    private TClassSelectorVo buildTClassSelectorVo(TClass tClass){
        TClassSelectorVo tClassSelectorVo = new TClassSelectorVo();
        tClassSelectorVo.setClassName(tClass.getClassName());
        TSemesterSelectorVo tSemesterSelectorVo = new TSemesterSelectorVo();
        tSemesterSelectorVo.setStudayYear(tClass.getStartYear() + "-" + tClass.getEndYear());
        tSemesterSelectorVo.setId(tClass.getSemId());
        tClassSelectorVo.setId(tClass.getId());
        tClassSelectorVo.setSemesterSelectorVo(tSemesterSelectorVo);
        return tClassSelectorVo;
    }
    /**
     * 查询班级列表
     *
     * @param tClass 班级
     * @return 班级
     */
    @Override
    public List<TClass> selectTClassList(TClass tClass)
    {
        //        tClasses.stream().peek(this::)
        return tClassMapper.selectTClassList(tClass);
    }

    /**
     * 分页查询班级列表
     *
     * @param tClass 班级
     * @return 班级
     */
    @Override
    public List<TClass> selectTClassListPage(TClass tClass)
    {
        PageHelper.startPage(tClass.getPageNum(),tClass.getPageSize());
        List<TClass> list = tClassMapper.selectTClassList(tClass);
        PageInfo<TClass> tPageInfo = new PageInfo<>(list);
        List<TClass> dataList = tPageInfo.getList();
        return dataList;
    }
    /**
     * 新增班级
     *
     * @param tClass 班级
     * @return 结果
     */
    @Override
    public int insertTClass(TClass tClass)
    {
        Integer row = tClassMapper.existsClass(tClass);

        if(row == null || row == 0){
            tClass.setCreateTime(DateUtils.getNowDate());
            tClass.setDelFlag("0");
            tClass.setCreateBy(SecurityUtils.getUsername());

            return tClassMapper.insertTClass(tClass);
        }

        return 0;
    }



    /**
     * 修改班级
     *
     * @param tClass 班级
     * @return 结果
     */
    @Override
    public int updateTClass(TClass tClass)
    {
        Integer row = tClassMapper.existsClass(tClass);
        if(row > 0 ){
            throw new CustomException("课程名字已存在!");
        }
        tClass.setUpdateBy(SecurityUtils.getUsername());
        tClass.setUpdateTime(DateUtils.getNowDate());
        tClass.setDelFlag("0");
        return tClassMapper.updateTClass(tClass);
    }

    /**
     * 批量删除班级
     *
     * @param ids 需要删除的班级ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteTClassByIds(Integer[] ids)
    {
        Integer studentNumAsClassIds = tStudentMapper.existsStudentAsClassId(Arrays.asList(ids));

        // 班级中至少有一个学生
        if(studentNumAsClassIds > 0 ){
            return 0;
        }

        return tClassMapper.deleteTClassByIds(ids);
    }

    /**
     * 删除班级信息
     *
     * @param id 班级ID
     * @return 结果
     */
    @Override
    public int deleteTClassById(Long id)
    {
        return tClassMapper.deleteTClassById(id);
    }

    @Override
    public List<TClassSelectorVo> selectTClassNameList(TClass tClass) {
        return tClassMapper.selectTClassNameList(tClass);
    }



    @Override
    public Integer importData(List<TClass> tClassSelectors , Boolean isUpdateSupport) {
        if (StringUtils.isNull(tClassSelectors) || tClassSelectors.size() == 0) {
            throw new CustomException("导入数据不能为空！");
        }

        // 构建创建人和创建时间
        List<TClassDTO> buildSemesterList = tClassSelectors.stream()
                .map(this::buildTClassDTO)
                .peek((tClass) -> {
                    tClass.setCreateBy(SecurityUtils.getUsername());
                    tClass.setCreateTime(DateUtils.getNowDate());
                }).collect(Collectors.toList());

        // 数据库中是否已经有记录
        Integer existsRow = tClassMapper.existsTClasses(buildSemesterList);

        // 不更新数据且数据库中有重复数据
        if ( existsRow > 0) {
            throw new CustomException("导入的数据在数据库中已经存在!");
        }

        return tClassMapper.insertBatchTSemester(buildSemesterList);
    }

    private TClassDTO buildTClassDTO(TClass tClass){
        TSemester tSemester = new TSemester();
        tSemester.setStartYear(tClass.getStartYear());
        tSemester.setEndYear(tClass.getEndYear());
        TClassDTO tClassDTO = new TClassDTO();
        List<TSemester> semesters = tSemesterMapper.selectTSemesterList(tSemester);
        if(semesters.size() == 0){
            throw new CustomException("所填的学年不存在");
        }
        tClassDTO.setSemId(semesters.get(0).getId());
        tClassDTO.setClassName(tClass.getClassName());
        return tClassDTO;

    }
}
