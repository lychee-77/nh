package com.dap.system.office.domain.vo;

import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import com.dap.common.core.annotation.Excel;
import com.dap.common.core.annotation.Excels;
import com.dap.system.office.domain.TCourse;
import com.dap.system.office.domain.TSemester;
import com.dap.system.office.domain.TStudent;

import java.util.Date;
import java.util.List;

public class TStudentDetailScoreVo {
    @Excel(name="姓名")
    private String studentName;

    private Integer semId;

    private Integer classSemId;
    @Excel(name="结束年份",type= Excel.Type.IMPORT)
    private String startYear;
    @Excel(name="结束年份",type= Excel.Type.IMPORT)
    private String endYear;

    private String schoolYear;
    private Integer stuId;
    private Integer classId;

    @ExcelCollection(name="详细分数")
    private List<TCourseScoreVo> courseScoreList;

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public TStudentDetailScoreVo() {
    }

    public TStudentDetailScoreVo(TStudent tStudent, TSemester tSemester) {
        this.studentName = tStudent.getStuName();
        this.semId = tSemester.getId();
        this.classSemId = tStudent.getSemId();
        this.classId = tStudent.getClassId();
        this.startYear = tSemester.getStartYear();
        this.endYear = tSemester.getEndYear();
        this.schoolYear = this.getStartYear() + "-" +this.getEndYear();
        this.stuId = this.getStuId();
    }


    public Integer getClassSemId() {
        return classSemId;
    }

    public void setClassSemId(Integer classSemId) {
        this.classSemId = classSemId;
    }

    public List<TCourseScoreVo> getCourseScoreList() {
        return courseScoreList;
    }

    public void setCourseScoreList(List<TCourseScoreVo> courseScoreList) {
        this.courseScoreList = courseScoreList;
    }

    public String getSchoolYear() {
        return schoolYear;
    }

    public void setSchoolYear(String schoolYear) {
        this.schoolYear = schoolYear;
    }

    public String getStartYear() {
        return startYear;
    }

    public void setStartYear(String startYear) {
        this.startYear = startYear;
    }

    public String getEndYear() {
        return endYear;
    }

    public void setEndYear(String endYear) {
        this.endYear = endYear;
    }

    public Integer getSemId() {
        return semId;
    }

    public void setSemId(Integer semId) {
        this.semId = semId;
    }

    public Integer getStuId() {
        return stuId;
    }

    public void setStuId(Integer stuId) {
        this.stuId = stuId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }



}
