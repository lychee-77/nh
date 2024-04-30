package com.dap.system.office.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.dap.common.core.annotation.Excel;
import com.dap.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

/**
 * 分数对象 t_score
 *
 * @author dap
 * @date 2021-11-12
 */
public class TScore extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    private Integer id;


    /**
     * 课程id
     */
    private Integer courseId;

    /**
     * 是否通过
     */
    private Integer isPass;

    /**
     * 学生id
     */
    private Integer stuId;

    /**
     * 班级 id
     */
    private Integer classId;

    /**
     * 分数
     */
    @Excel(name = "分数")
    @Min(value = 1,message = "选择教师不合法")
    private Double score;

    /**
     * 业务删除
     */
    private String delFlag;

    /**
     * 学期id
     */
    private Integer semId;

    @Excel(name = "课程名称")
    private String courseName;

    @Excel(name = "学生名称")
    private String studentName;

    @Excel(name = "所在班级")
    private String className;


    /**
     * 所在学期
     */
    @Excel(name = "入学年份")
    private String startYear;
    @Excel(name = "结束年份")
    private String endYear;

    /**
     * 学期名称
     */
    @Excel(name = "所在学期")
    private String schoolYear;

    private String note;

    /**
     * 查询成绩表中某一学期的所有学期
     */
    private Integer scoreSemId;

    public Integer getScoreSemId() {
        return scoreSemId;
    }

    public void setScoreSemId(Integer scoreSemId) {
        this.scoreSemId = scoreSemId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public String getSchoolYear() {
        return schoolYear;
    }

    public void setSchoolYear(String schoolYear) {
        this.schoolYear = schoolYear;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }




    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }



    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setIsPass(Integer isPass) {
        this.isPass = isPass;
    }

    public Integer getIsPass() {
        return isPass;
    }

    public void setStuId(Integer stuId) {
        this.stuId = stuId;
    }

    public Integer getStuId() {
        return stuId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Double getScore() {
        return score;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setSemId(Integer semId) {
        this.semId = semId;
    }

    public Integer getSemId() {
        return semId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("courseId", getCourseId())
                .append("stuId", getStuId())
                .append("classId", getClassId())
                .append("score", getScore())
                .append("createTime", getCreateTime())
                .append("createBy", getCreateBy())
                .append("updateTime", getUpdateTime())
                .append("updateBy", getUpdateBy())
                .append("delFlag", getDelFlag())
                .append("semId", getSemId())
                .append("courseName", getCourseName())
                .append("studentName", getStudentName())
                .append("className", getClassName())
                .toString();
    }
}
