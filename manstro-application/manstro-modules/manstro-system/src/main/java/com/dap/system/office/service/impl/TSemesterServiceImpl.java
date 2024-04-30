package com.dap.system.office.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.dap.common.core.exception.CustomException;
import com.dap.common.core.utils.DateUtils;
import com.dap.common.core.utils.SecurityUtils;
import com.dap.common.core.utils.StringUtils;
import com.dap.common.core.web.domain.BaseEntity;
import com.dap.system.office.domain.TClass;
import com.dap.system.office.domain.dto.TClassDTO;
import com.dap.system.office.domain.vo.*;
import com.dap.system.office.mapper.TClassMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dap.system.office.mapper.TSemesterMapper;
import com.dap.system.office.domain.TSemester;
import com.dap.system.office.service.ITSemesterService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 学期管理Service业务层处理
 *
 * @author dap
 * @date 2021-11-11
 */
@Service
public class TSemesterServiceImpl implements ITSemesterService {
    @Autowired
    private TSemesterMapper tSemesterMapper;

    @Autowired
    private TClassMapper tClassMapper;

    /**
     * 查询学期管理
     *
     * @param id 学期管理ID
     * @return 学期管理
     */
    @Override
    public TSemester selectTSemesterById(Integer id) {
        return tSemesterMapper.selectTSemesterById(id);
    }

    /**
     * 查询学期管理列表
     *
     * @param tSemester 学期管理
     * @return 学期管理
     */
    @Override
    public List<TSemester> selectTSemesterList(TSemester tSemester) {
        return tSemesterMapper.selectTSemesterList(tSemester);
    }

    /**
     * 分页查询学期管理列表
     *
     * @param tSemester 学期管理
     * @return 学期管理
     */
    @Override
    public List<TSemester> selectTSemesterListPage(TSemester tSemester) {
        PageHelper.startPage(tSemester.getPageNum(), tSemester.getPageSize());
        List<TSemester> list = tSemesterMapper.selectTSemesterList(tSemester);
        PageInfo<TSemester> tPageInfo = new PageInfo<>(list);
        List<TSemester> dataList = tPageInfo.getList();

        return dataList;
    }

    /**
     * 新增学期管理
     *
     * @param tSemester 学期管理
     * @return 结果
     */
    @Override
    public int insertTSemester(TSemester tSemester) {

        List<TSemester> tSemesters = tSemesterMapper.selectTSemesterList(tSemester);

        // 数据库中没有才会进行插入,否则不插入
        if (tSemesters == null || tSemesters.size() == 0) {
            tSemester.setCreateTime(DateUtils.getNowDate());
            tSemester.setCreateBy(SecurityUtils.getUsername());
            return tSemesterMapper.insertTSemester(tSemester);
        }

        return 0;

    }

    /**
     * 修改学期管理
     *
     * @param tSemester 学期管理
     * @return 结果
     */
    @Override
    public int updateTSemester(TSemester tSemester) {

        TClass tClass = new TClass();
        tClass.setSemId(tSemester.getId());
        Integer isClassSemester = tClassMapper.existsClass(tClass);

        if (isClassSemester > 0) {
            throw new CustomException("当前学期下还有班级,请先删除该学期下的班级");
        }

        Integer row = tSemesterMapper.existsTSemesters(Arrays.asList(tSemester));

        if (row > 0) {
            throw new CustomException("修改的学期已经存在");
        }



        tSemester.setUpdateTime(DateUtils.getNowDate());
        tSemester.setUpdateBy(SecurityUtils.getUsername());
        return tSemesterMapper.updateTSemester(tSemester);
    }

    /**
     * 批量删除学期管理
     *
     * @param ids 需要删除的学期管理ID
     * @return 结果
     */
    @Override
    public int deleteTSemesterByIds(Integer[] ids) {
        List<TClassDTO> classes = new ArrayList<>();
        for (Integer id : ids) {
            TClassDTO tClass = new TClassDTO();
            tClass.setSemId(id);
            classes.add(tClass);
        }


        Integer rows = tClassMapper.existsTClasses(classes);
        if (rows == null || rows == 0) {
            return tSemesterMapper.deleteTSemesterByIds(ids);
        }
        return 0;
    }

    /**
     * 删除学期管理信息
     *
     * @param id 学期管理ID
     * @return 结果
     */
    @Override
    public int deleteTSemesterById(Integer id) {
        return tSemesterMapper.deleteTSemesterById(id);
    }


    @Override
    public List<TSemesterSelectorVo> selectTSemesterNameListPage() {
        List<TSemester> semesters = tSemesterMapper.selectAllTSemesterNameList();
        return semesters.stream()
                .map(this::buildTSemesterSelectorVo)
                .collect(Collectors.toList());
    }

    private TSemesterSelectorVo buildTSemesterSelectorVo(TSemester tSemester) {
        String studayYear = tSemester.getStartYear() + "-" + tSemester.getEndYear();
        TSemesterSelectorVo tSemesterSelectorVo = new TSemesterSelectorVo();
        tSemesterSelectorVo.setId(tSemester.getId());
        tSemesterSelectorVo.setStudayYear(studayYear);
        return tSemesterSelectorVo;
    }

    @Override
    @Transactional
    public Integer importSemester(List<TSemester> semesterList, Boolean isUpdateSupport) {

        if (StringUtils.isNull(semesterList) || semesterList.size() == 0) {
            throw new CustomException("导入数据不能为空！");
        }
        // 数据库中是否已经有记录
        Integer existsRow = tSemesterMapper.existsTSemesters(semesterList);

        // 不更新数据且数据库中有重复数据
        if (!isUpdateSupport && existsRow > 0) {
            throw new CustomException("导入的数据在数据库中已经存在!");
        }

        // 构建创建人和创建时间
        List<TSemester> buildSemesterList = semesterList.stream()
                .peek((semester) -> {
                    semester.setCreateBy(SecurityUtils.getUsername());
                    semester.setCreateTime(DateUtils.getNowDate());
                }).collect(Collectors.toList());

        return tSemesterMapper.insertBatchTSemester(buildSemesterList);

    }

    @Override
    public List<TSemesterCascaderVo> selectTSemesterCascaderVo() {
        List<TSemester> semesters = tSemesterMapper.selectAllTSemesterNameList();

        return semesters.stream()
                .map(this::buildSemesterSelectorVo)
                .collect(Collectors.toList());
    }

    private TSemesterCascaderVo buildSemesterSelectorVo(TSemester tSemester) {

        TSemesterCascaderVo tSemesterCascaderVo = new TSemesterCascaderVo();
        tSemesterCascaderVo.setName(tSemester.getStartYear() + "-" + tSemester.getEndYear());

        TClass tClass = new TClass();
        tClass.setSemId(tSemester.getId());
        List<TCascaderNode> tCascaderNodeList = new ArrayList<>();
        List<TClass> tClasses = tClassMapper.selectTClassList(tClass);
        for (TClass aClass : tClasses) {
            TCascaderNode tCascaderNode = new TCascaderNode();
            tCascaderNode.setId(aClass.getId());
            tCascaderNode.setName(aClass.getClassName());
            tCascaderNodeList.add(tCascaderNode);
        }
        tSemesterCascaderVo.setChildren(tCascaderNodeList);
        return tSemesterCascaderVo;
    }

    private TSemesterCascaderVo buildSemesterSelectorVo(List<TClassSelectorVo> classSelectorVoList) {
        // 创建出GradeClassTreeVo待返回对象
        TSemesterCascaderVo tSemesterSelectorVo = new TSemesterCascaderVo();
        // 设置计量科第一级名称为当前对象的学年名
        tSemesterSelectorVo.setName(
                classSelectorVoList.get(0).getSemesterSelectorVo()
                        .getStudayYear());

        // 构建一个List
        List<TCascaderNode> tCascaderNodes = new ArrayList<>();
        //
        for (TClassSelectorVo tClassSelectorVo : classSelectorVoList) {
            TCascaderNode tCascaderNode = new TCascaderNode();
            tCascaderNode.setId(tClassSelectorVo.getId());
            tCascaderNode.setName(tClassSelectorVo.getClassName());
            tCascaderNodes.add(tCascaderNode);
        }

        tSemesterSelectorVo.setChildren(tCascaderNodes);
        // 将构建出来的List放在TSemesterCascaderVo对象中
        return tSemesterSelectorVo;

    }
}
