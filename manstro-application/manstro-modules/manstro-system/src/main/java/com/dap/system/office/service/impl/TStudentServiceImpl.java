package com.dap.system.office.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.dap.common.core.exception.CustomException;
import com.dap.common.core.utils.DateUtils;
import com.dap.common.core.utils.SecurityUtils;
import com.dap.common.core.utils.StringUtils;
import com.dap.system.office.domain.TClass;
import com.dap.system.office.domain.TScore;
import com.dap.system.office.domain.TSemester;
import com.dap.system.office.domain.dto.TClassDTO;
import com.dap.system.office.domain.vo.TCascaderNode;
import com.dap.system.office.domain.vo.TClassSelectorVo;
import com.dap.system.office.domain.vo.TStudentDetailScoreVo;
import com.dap.system.office.domain.vo.TStudentSelectorVo;
import com.dap.system.office.mapper.TClassMapper;
import com.dap.system.office.mapper.TScoreMapper;
import com.dap.system.office.mapper.TSemesterMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dap.system.office.mapper.TStudentMapper;
import com.dap.system.office.domain.TStudent;
import com.dap.system.office.service.ITStudentService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 学生Service业务层处理
 *
 * @author dap
 * @date 2021-11-11
 */
@Service
public class TStudentServiceImpl implements ITStudentService {
    @Autowired
    private TStudentMapper tStudentMapper;

    @Autowired
    private TClassMapper tClassMapper;

    @Autowired
    private TSemesterMapper tSemeMapper;

    @Autowired
    private TScoreMapper tScoreMapper;

    private static final String REGEX_ID_NUMBER = "^(([1][1-5])|([2][1-3])|([3][1-7])|([4][1-6])|([5][0-4])|([6][1-5])|([7][1])|([8][1-2]))\\d{4}(([1][9]\\d{2})|([2]\\d{3}))(([0][1-9])|([1][0-2]))(([0][1-9])|([1-2][0-9])|([3][0-1]))\\d{3}[0-9xX]$";

    private static final String DATE_FORMAT = "yyyyMMdd";

    private static final String YEAR_FORMAT = "yyyy";

    /**
     * 查询学生
     *
     * @param id 学生ID
     * @return 学生
     */
    @Override
    public TStudent selectTStudentById(Integer id) {
        return tStudentMapper.selectTStudentById(id);
    }

    /**
     * 查询学生列表
     *
     * @param tStudent 学生
     * @return 学生
     */
    @Override
    public List<TStudent> selectTStudentList(TStudent tStudent) {
        return tStudentMapper.selectTStudentList(tStudent);
    }

    /**
     * 分页查询学生列表
     *
     * @param tStudent 学生
     * @return 学生
     */
    @Override
    public List<TStudent> selectTStudentListPage(TStudent tStudent) {
        PageHelper.startPage(tStudent.getPageNum(), tStudent.getPageSize());
        List<TStudent> list = tStudentMapper.selectTStudentList(tStudent);
        PageInfo<TStudent> tPageInfo = new PageInfo<>(list);
        return tPageInfo.getList();
    }

    /**
     * 新增学生
     *
     * @param tStudent 学生
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertTStudent(TStudent tStudent) {
        Integer row = tStudentMapper.existsIdNumberAndStudentNum(tStudent);
        if (row == null || row == 0) {
            tStudent.setCreateTime(DateUtils.getNowDate());
            tStudent.setDelFlag("0");
            tStudent.setCreateBy(SecurityUtils.getUsername());
            return tStudentMapper.insertTStudent(tStudent);
        }
        return 0;
    }

    /**
     * 修改学生
     *
     * @param tStudent 学生
     * @return 结果
     */
    @Override
    public int updateTStudent(TStudent tStudent) {
        Integer row = tStudentMapper.existsIdNumberAndStudentNum(tStudent);
        if (row == null || row == 0) {
            tStudent.setUpdateBy(SecurityUtils.getUsername());
            tStudent.setUpdateTime(DateUtils.getNowDate());
            return tStudentMapper.updateTStudent(tStudent);
        }
        return 0;

    }

    /**
     * 批量删除学生
     *
     * @param ids 需要删除的学生ID
     * @return 结果
     */
    @Override
    public int deleteTStudentByIds(Integer[] ids) {
        Integer rows = tScoreMapper.existsStudentIds(ids);
        // 该学生没有考试
        if (rows == null || rows == 0) {
            return tStudentMapper.deleteTStudentByIds(ids);

        }
        return 0;
    }

    /**
     * 删除学生信息
     *
     * @param id 学生ID
     * @return 结果
     */
    @Override
    public int deleteTStudentById(Integer id) {
        return tStudentMapper.deleteTStudentById(id);
    }

    @Override
    public List<TCascaderNode> selectTStudentNameList(TStudent tStudent) {
        List<TSemester> semesters = tSemeMapper.selectAllTSemesterNameList();
        // 根据学期封装底层的班级和学生

        return semesters.stream()
                .map(this::buildTClassCascaderNode).collect(Collectors.toList());
    }

    private TCascaderNode buildTClassCascaderNode(TSemester tSemester) {
        TCascaderNode tCascaderNode = new TCascaderNode();
        TClass tClass = new TClass();
        tClass.setSemId(tSemester.getId());
        // 为学期赋值名字
        tCascaderNode.setId(tSemester.getId());
        tCascaderNode.setName(tSemester.getStartYear() + '-' + tSemester.getEndYear());
        // 查询该学期下的所有班级
        List<TClass> tClasses = tClassMapper.selectTClassList(tClass);

        // 遍历班级对象封装该班级下的所有学生到CascaderNode对象
        List<TCascaderNode> tStudentsCascaderNode = tClasses.stream().
                map(this::buildTStudentCascaderNode).collect(Collectors.toList());
        tCascaderNode.setChildren(tStudentsCascaderNode);
        return tCascaderNode;
    }

    private TCascaderNode buildTStudentCascaderNode(TClass tClass) {
        // 待返回的班级CascaderNode对
        TCascaderNode tCascaderNode = new TCascaderNode();
        tCascaderNode.setId(tClass.getId());
        tCascaderNode.setName(tClass.getClassName());

        // 根据班级查询学生
        TStudent tStudentQueryParam = new TStudent();
        tStudentQueryParam.setClassId(tClass.getId());
        List<TStudentSelectorVo> tStudentSelectorVos = tStudentMapper.selectAllTStudentNameList(tStudentQueryParam);

        // 构建学生CasaderNode对象
        List<TCascaderNode> tStudentCascaderNode = tStudentSelectorVos.stream().map((tStudent) -> {
            TCascaderNode tCascaderNode1 = new TCascaderNode();
            tCascaderNode1.setId(tStudent.getId());
            tCascaderNode1.setName(tStudent.getStuName());
            return tCascaderNode1;
        }).collect(Collectors.toList());

        tCascaderNode.setChildren(tStudentCascaderNode);
        return tCascaderNode;
    }

    @Override
    public Integer importData(List<TStudent> studentList, boolean updateSupport) {
        // 不为空
        if (StringUtils.isNull(studentList) || studentList.size() == 0) {
            throw new CustomException("导入数据不能为空！");
        }

        // Excel中数据重复
        if (checkStudent(studentList)) {
            throw new CustomException("导入的数据自身身份证和学号存在问题!");
        }


        // 与数据库中数据重复
        if (checkDataBaseStudent(studentList)) {
            throw new CustomException("导入的数据在数据库中已经存在!");
        }
        // 解析出学生的出生年月,身份证号和年龄信息
        studentList = resolverStudentInfo(studentList);

        // 构建待插入的TStudent对象
        studentList = studentList.stream()
                .map(this::buildTStudent)
                .collect(Collectors.toList());

        return tStudentMapper.insertBatchTStudent(studentList);
    }

    /**
     * 校验参数中的数据是否与数据库中的数据重复
     *
     * @param tStudents
     * @return true 有重复
     * @return false 无重复
     */
    private boolean checkDataBaseStudent(List<TStudent> tStudents) {
        // 数据库中是否已经有记录
        Integer existsRow = 0;
        for (TStudent tStudent : tStudents) {
            existsRow += tStudentMapper.existsIdNumberAndStudentNum(tStudent);
        }

        return existsRow > 0;

    }

    /**
     * 解析出的用户信息
     *
     * @param beforeStudents 解析前的用户信息
     * @return 封装好的用户信息
     */
    private List<TStudent> resolverStudentInfo(List<TStudent> beforeStudents) {
        return beforeStudents.stream()
                .map(this::getBasicStudentByIdNumber).collect(Collectors.toList());
    }

    /**
     * 从身份证中解析出用户信息
     *
     * @param tStudent 拥有身份证号的用户对象
     * @return tStudent 返回拥有基础信息的用户对象
     */
    private TStudent getBasicStudentByIdNumber(TStudent tStudent) {
        String stuIdNumber = tStudent.getStuIdNumber();

        // 格式校验
        if (!Pattern.matches(REGEX_ID_NUMBER, stuIdNumber)) {
            throw new CustomException("请输入正确的身份证号码");
        }

        // 获取出生年日
        String birthdayYear = stuIdNumber.substring(6, 10);
        String birthdayMonth = stuIdNumber.substring(10, 12);
        String birthdayDay = stuIdNumber.substring(12, 14);
        Date birthday = null;
        try {
            birthday = new SimpleDateFormat(DATE_FORMAT)
                    .parse(
                            String.format("%s%s%s",
                                    birthdayYear, birthdayMonth, birthdayDay));
        } catch (ParseException e) {
            throw new CustomException("身份证获取日期失败");
        }
        tStudent.setStuBirthday(birthday);

        // 获取年龄
        int age = (LocalDate.now().getYear() - Integer.parseInt(birthdayYear) - 1);
        tStudent.setAge(age);

        // 获取性别
        String sex = stuIdNumber.substring(16, 17);
        int stuSex = Integer.parseInt(sex) % 2;
        tStudent.setStuSex(Integer.toString(stuSex));
        return tStudent;
    }

    /**
     * 批量校验导入的学生列表是否自身数据重复
     *
     * @param tStudents 待校验的学生列表
     * @return true 数据重复
     * @return false 数据未重复
     */
    private boolean checkStudent(List<TStudent> tStudents) {
        // 获取学生列表个数
        int stuSize = tStudents.size();
        // 获取学生列表身份证集合并去重
        List<String> stuIdNumbers = tStudents.stream()
                .map(TStudent::getStuIdNumber).distinct().collect(Collectors.toList());

        // 获取学生列表学号集合并去重
        List<String> stuNums = tStudents.stream().map(TStudent::getStuNum).distinct().collect(Collectors.toList());

        // 去重后的身份证号和学号与学生列表个数一致即没有重复数据
        return stuIdNumbers.size() != stuSize || stuNums.size() != stuSize;
    }

    /**
     * 根据前端传入的班级名称构建出班级id便于插入
     *
     * @param tStudent 拥有班级名称
     * @return tStudent 构建后的班级id
     */
    private TStudent buildTStudent(TStudent tStudent) {
        TClass tClass = new TClass();
        tClass.setClassName(tStudent.getClassName());

        tClass.setStartYear(tStudent.getStartYear());
        tClass.setEndYear(tStudent.getEndYear());
        List<TClass> tClasses = tClassMapper.selectTClassList(tClass);
        if ( tClasses.size() >= 1) {
            tStudent.setClassId(tClasses.get(0).getId());
            tStudent.setCreateTime(DateUtils.getNowDate());
            tStudent.setCreateBy(SecurityUtils.getUsername());
            return tStudent;
        }
        throw new CustomException("导入的班级不存在!");
    }
}
