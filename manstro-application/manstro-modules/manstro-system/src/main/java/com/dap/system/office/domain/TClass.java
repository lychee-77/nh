package com.dap.system.office.domain;

import com.dap.common.core.annotation.Excel;
import com.dap.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

/**
 * 班级对象 t_class
 *
 * @author dap
 * @date 2021-11-11
 */
public class TClass extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    private Integer id;

    /**
     * 所属班级
     */
    @Excel(name = "所属班级")
    private String className;



    /**
     * 所属学年
     */
    @NotEmpty(message = "所属学期不能为空")
    @Min(value = 1,message = "所选学期不合法")
    private Integer semId;

    private String schoolYear;

    public String getSchoolYear() {
        return schoolYear;
    }

    public void setSchoolYear(String schoolYear) {
        this.schoolYear = schoolYear;
    }

    /**
     * 所属学期名称
     */
    @NotEmpty()
    @Excel(name = "入学时间")
    private String startYear;
    @NotEmpty
    @Excel(name = "结束时间")
    private String endYear;



    /**
     * 业务删除
     */
    private String delFlag;



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



    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }


    public void setSemId(Integer semId) {
        this.semId = semId;
    }

    public Integer getSemId() {
        return semId;
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
                .append("className", getClassName())
                .append("semId", getSemId())
                .append("startYear", getStartYear())
                .append("endYear", getEndYear())
                .append("createTime", getCreateTime())
                .append("createBy", getCreateBy())
                .append("updateTime", getUpdateTime())
                .append("updateBy", getUpdateBy())
                .append("delFlag", getDelFlag())
                .toString();
    }
}
