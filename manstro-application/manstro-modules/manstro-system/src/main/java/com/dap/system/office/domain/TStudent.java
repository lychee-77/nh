package com.dap.system.office.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.dap.common.core.annotation.Excel;
import com.dap.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 学生对象 t_student
 *
 * @author dap
 * @date 2021-11-11
 */
public class TStudent extends BaseEntity {
    private static final long serialVersionUID = 1L;

//    @Excel(name = "id")
    private Integer id;

    /**
     * 学号
     */
    @Excel(name = "学号")
    private String stuNum;

    /**
     * 学生姓名
     */
    @Excel(name = "学生姓名")
    private String stuName;

    /**
     * 班级id
     */
//    @Excel(name = "班级id")
    private Integer classId;

    /**
     * 出生日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "出生日期", width = 30,type = Excel.Type.EXPORT)
    private Date stuBirthday;

    /**
     * 性别
     */
    @Excel(name = "性别",readConverterExp = "0=女,1=男",type = Excel.Type.EXPORT)
    private String stuSex;

    /**
     * 身份证号
     */
    @Excel(name = "身份证号")
    private String stuIdNumber;

    /**
     * 民族
     */
    @Excel(name = "民族")
    private String nation;

    /**
     * 年龄
     */
    @Excel(name = "年龄",type = Excel.Type.EXPORT)
    private Integer age;

    /**
     * 照片
     */
//    @Excel(name = "照片地址")
    private String photo;

    /**
     * 备注
     */
    @Excel(name = "备注")
    private String note;

    /**
     * 业务删除
     */
    private String delFlag;

    /**
     * 学生所属班级
     */
    @Excel(name = "所在班级")
    private String className;

    /**
     * 所在学期
     */
    @Excel(name = "入学年份")
    private String startYear;
    @Excel(name = "结束年份")
    private String endYear;


    private Integer semId;

    // 该学生班级对应的学期id
    private Integer classSemId;



    private String startBirthdayMonnt;

    private String endBirthdayMonnt;


    public Integer getClassSemId() {
        return classSemId;
    }

    public void setClassSemId(Integer classSemId) {
        this.classSemId = classSemId;
    }

    public String getEndBirthdayMonnt() {
        return endBirthdayMonnt;
    }

    public void setEndBirthdayMonnt(String endBirthdayMonnt) {
        this.endBirthdayMonnt = endBirthdayMonnt;
    }

    public String getStartBirthdayMonnt() {
        return startBirthdayMonnt;
    }

    public void setStartBirthdayMonnt(String startBirthdayMonnt) {
        this.startBirthdayMonnt = startBirthdayMonnt;
    }

    public Integer getSemId() {
        return semId;
    }

    public void setSemId(Integer semId) {
        this.semId = semId;
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

    /**
     * 学生所属年级
     */



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

    public void setStuNum(String stuNum) {
        this.stuNum = stuNum;
    }

    public String getStuNum() {
        return stuNum;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public String getStuName() {
        return stuName;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setStuBirthday(Date stuBirthday) {
        this.stuBirthday = stuBirthday;
    }

    public Date getStuBirthday() {
        return stuBirthday;
    }

    public void setStuSex(String stuSex) {
        this.stuSex = stuSex;
    }

    public String getStuSex() {
        return stuSex;
    }

    public void setStuIdNumber(String stuIdNumber) {
        this.stuIdNumber = stuIdNumber;
    }

    public String getStuIdNumber() {
        return stuIdNumber;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getNation() {
        return nation;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getAge() {
        return age;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhoto() {
        return photo;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNote() {
        return note;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getDelFlag() {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("stuNum", getStuNum())
                .append("stuName", getStuName())
                .append("classId", getClassId())
                .append("stuBirthday", getStuBirthday())
                .append("stuSex", getStuSex())
                .append("stuIdNumber", getStuIdNumber())
                .append("nation", getNation())
                .append("age", getAge())
                .append("photo", getPhoto())
                .append("note", getNote())
                .append("createTime", getCreateTime())
                .append("createBy", getCreateBy())
                .append("updateTime", getUpdateTime())
                .append("updateBy", getUpdateBy())
                .append("delFlag", getDelFlag())
                .append("className", getClassName())
                .toString();
    }
}
