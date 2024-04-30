package com.dap.system.office.domain.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;

import java.util.List;

public class TStudentScoreInfoVo {
    // 分数id和,区分成绩记录
    private Integer scoreIds;
    @Excel(name="学号",needMerge = true)
    private String studentNum;
    // 学生id
    private Integer id;
    @Excel(name="学生姓名",needMerge = true)
    private String studentName;
    @Excel(name="班级名称",needMerge = true)
    private String className;
    @Excel(name="总分",needMerge = true,isImportField="true")
    private Double totalScore;
    @Excel(name="平均分",needMerge = true,isImportField="true")
    private Double avgScore;

    private Integer grade;

    private String updateBy;

    private String schoolYear;

    private Integer semId;
    @Excel(name="结束年份",needMerge = true)
    private String endYear;
    @Excel(name="开始年份",needMerge = true)
    private String startYear;
    private List<Integer> courseIds;

    @ExcelCollection(name="详细分数")
    private List<TCourseScoreVo> courseScoreList;

    public List<TCourseScoreVo> getCourseScoreList() {
        return courseScoreList;
    }

    public void setCourseScoreList(List<TCourseScoreVo> courseScoreList) {
        this.courseScoreList = courseScoreList;
    }

    public List<Integer> getCourseIds() {
        return courseIds;
    }

    public void setCourseIds(List<Integer> courseIds) {
        this.courseIds = courseIds;
    }

    public String getEndYear() {
        return endYear;
    }

    public void setEndYear(String endYear) {
        this.endYear = endYear;
    }

    public String getStartYear() {
        return startYear;
    }

    public void setStartYear(String startYear) {
        this.startYear = startYear;
    }

    public Integer getScoreIds() {
        return scoreIds;
    }

    public void setScoreIds(Integer scoreIds) {
        this.scoreIds = scoreIds;
    }

    public Integer getSemId() {
        return semId;
    }

    public void setSemId(Integer semId) {
        this.semId = semId;
    }

    public String getStudentNum() {
        return studentNum;
    }

    public void setStudentNum(String studentNum) {
        this.studentNum = studentNum;
    }

    public String getSchoolYear() {
        return schoolYear;
    }

    public void setSchoolYear(String schoolYear) {
        this.schoolYear = schoolYear;
    }


    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }



    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }



    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }



    public Double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Double totalScore) {
        this.totalScore = totalScore;
    }

    public Double getAvgScore() {
        return avgScore;
    }

    public void setAvgScore(Double avgScore) {
        this.avgScore = avgScore;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "TStudentScoreInfoVo{" +
                "scoreIds=" + scoreIds +
                ", studentNum='" + studentNum + '\'' +
                ", id=" + id +
                ", studentName='" + studentName + '\'' +
                ", className='" + className + '\'' +
                ", totalScore=" + totalScore +
                ", avgScore=" + avgScore +
                ", grade=" + grade +
                ", updateBy='" + updateBy + '\'' +
                ", schoolYear='" + schoolYear + '\'' +
                ", semId=" + semId +
                ", endYear='" + endYear + '\'' +
                ", startYear='" + startYear + '\'' +
                '}';
    }
}
