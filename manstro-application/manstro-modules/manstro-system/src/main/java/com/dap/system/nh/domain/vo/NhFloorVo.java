package com.dap.system.nh.domain.vo;

import com.dap.system.nh.domain.NhFloor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class NhFloorVo extends NhFloor {
    private String buildName;

    public String getBuildName() {
        return buildName;
    }

    public void setBuildName(String buildName) {
        this.buildName = buildName;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("buildId", getBuildId())
                .append("buildName", getBuildName())
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
