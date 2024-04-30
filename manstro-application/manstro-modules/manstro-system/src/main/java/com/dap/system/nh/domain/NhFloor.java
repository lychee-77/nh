package com.dap.system.nh.domain;

import com.dap.common.core.annotation.Excel;
import com.dap.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 楼层对象 nh_floor
 *
 * @author dap
 * @date 2021-12-03
 */
public class NhFloor extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 楼宇id */
    @Excel(name = "楼宇id")
    private Long buildId;

    /** 楼层名称 */
    @Excel(name = "楼层名称")
    private String floorName;

    /** $column.columnComment */
    private String delFlag;

    /** $column.columnComment */
    private String createUser;

    /** $column.columnComment */
    private String updateUser;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setBuildId(Long buildId)
    {
        this.buildId = buildId;
    }

    public Long getBuildId()
    {
        return buildId;
    }
    public void setFloorName(String floorName)
    {
        this.floorName = floorName;
    }

    public String getFloorName()
    {
        return floorName;
    }
    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag()
    {
        return delFlag;
    }
    public void setCreateUser(String createUser)
    {
        this.createUser = createUser;
    }

    public String getCreateUser()
    {
        return createUser;
    }
    public void setUpdateUser(String updateUser)
    {
        this.updateUser = updateUser;
    }

    public String getUpdateUser()
    {
        return updateUser;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("buildId", getBuildId())
            .append("floorName", getFloorName())
            .append("remark", getRemark())
            .append("delFlag", getDelFlag())
            .append("createUser", getCreateUser())
            .append("createTime", getCreateTime())
            .append("updateUser", getUpdateUser())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
