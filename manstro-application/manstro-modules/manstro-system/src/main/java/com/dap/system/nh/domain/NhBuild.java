package com.dap.system.nh.domain;

import com.dap.common.core.annotation.Excel;
import com.dap.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 楼宇对象 nh_build
 *
 * @author dap
 * @date 2021-12-02
 */
public class NhBuild extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 楼宇名称 */
    @Excel(name = "楼宇名称")
    private String buildName;

    /** 业务删除 */
    private String delFlag;

    /** 创建人 */
    private String createUser;

    /** 更新人 */
    private String updateUser;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setBuildName(String buildName)
    {
        this.buildName = buildName;
    }

    public String getBuildName()
    {
        return buildName;
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
            .append("buildName", getBuildName())
            .append("remark", getRemark())
            .append("delFlag", getDelFlag())
            .append("createUser", getCreateUser())
            .append("createTime", getCreateTime())
            .append("updateUser", getUpdateUser())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
