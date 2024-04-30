package com.dap.system.nh.domain;

import com.dap.common.core.annotation.Excel;
import com.dap.common.core.web.domain.BaseEntity;
import com.dap.system.nh.domain.vo.TreeNode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 设备基本信息对象 nh_equip_baseinfo
 *
 * @author dap
 * @date 2021-12-04
 */
public class NhEquipBaseinfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 业务id */
    private Long id;

    /** 厂家信息 */
    @Excel(name = "厂家信息")
    private String venderName;

    /** 厂家地址 */
    @Excel(name = "厂家地址")
    private String locationName;

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
    public void setVenderName(String venderName)
    {
        this.venderName = venderName;
    }

    public String getVenderName()
    {
        return venderName;
    }
    public void setLocationName(String locationName)
    {
        this.locationName = locationName;
    }

    public String getLocationName()
    {
        return locationName;
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

    public static TreeNode buildTreeNode(NhEquipBaseinfo nhEquipBaseinfo) {
        TreeNode treeNode = new TreeNode();
        treeNode.setId(nhEquipBaseinfo.id);
        treeNode.setLabel(nhEquipBaseinfo.venderName);
        treeNode.setLevel(TreeNode.CHILDREN_LEVEL);
        return treeNode;
    }
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("venderName", getVenderName())
            .append("locationName", getLocationName())
            .append("remark", getRemark())
            .append("delFlag", getDelFlag())
            .append("createUser", getCreateUser())
            .append("createTime", getCreateTime())
            .append("updateUser", getUpdateUser())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
