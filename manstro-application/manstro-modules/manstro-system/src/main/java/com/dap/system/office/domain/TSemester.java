package com.dap.system.office.domain;

import com.dap.common.core.annotation.Excel;
import com.dap.common.core.web.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 学期管理对象 t_semester
 *
 * @author dap
 * @date 2021-11-11
 */
public class TSemester extends BaseEntity
{
    private static final long serialVersionUID = 1L;


    private Integer id;

    /** 业务三岔湖 */
    private String delFlag;

    @Excel(name="入学年份",type = Excel.Type.ALL)
    @JsonFormat(pattern = "yyyy")
    private String startYear;
    @Excel(name="结束年份",type = Excel.Type.ALL)
    @JsonFormat(pattern = "yyyy")
    private String endYear;

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

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getId()
    {
        return id;
    }

    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
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
