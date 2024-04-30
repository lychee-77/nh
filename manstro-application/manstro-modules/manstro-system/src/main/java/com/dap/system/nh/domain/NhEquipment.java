package com.dap.system.nh.domain;

import com.dap.common.core.annotation.Excel;
import com.dap.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 设备表对象 nh_equipment
 *
 * @author dap
 * @date 2021-12-04
 */
public class NhEquipment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 型号 */
    @Excel(name = "型号")
    private String typeModel;

    /** 基本信息id */
//    @Excel(name = "基本信息id",type = Excel.Type.IMPORT)
    private Long baseinfoId;

    /** 类型id */
//    @Excel(name = "类型id")
    private Long typeId;

    /** 空间id */
//    @Excel(name = "空间id")
    private Long spaceId;

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
    public void setTypeModel(String typeModel)
    {
        this.typeModel = typeModel;
    }

    public String getTypeModel()
    {
        return typeModel;
    }
    public void setBaseinfoId(Long baseinfoId)
    {
        this.baseinfoId = baseinfoId;
    }

    public Long getBaseinfoId()
    {
        return baseinfoId;
    }
    public void setTypeId(Long typeId)
    {
        this.typeId = typeId;
    }

    public Long getTypeId()
    {
        return typeId;
    }
    public void setSpaceId(Long spaceId)
    {
        this.spaceId = spaceId;
    }

    public Long getSpaceId()
    {
        return spaceId;
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
            .append("typeModel", getTypeModel())
            .append("baseinfoId", getBaseinfoId())
            .append("typeId", getTypeId())
            .append("spaceId", getSpaceId())
            .append("remark", getRemark())
            .append("delFlag", getDelFlag())
            .append("createUser", getCreateUser())
            .append("createTime", getCreateTime())
            .append("updateUser", getUpdateUser())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
