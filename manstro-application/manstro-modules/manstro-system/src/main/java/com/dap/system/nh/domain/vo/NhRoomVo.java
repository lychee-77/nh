package com.dap.system.nh.domain.vo;

import cn.hutool.core.lang.tree.Tree;
import com.dap.system.nh.domain.NhRoom;

public class NhRoomVo extends NhRoom {
    private String floorName;

    private String buildName;

    private Long buildId;



    public String getBuildName() {
        return buildName;
    }

    public void setBuildName(String buildName) {
        this.buildName = buildName;
    }

    public Long getBuildId() {
        return buildId;
    }

    public void setBuildId(Long buildId) {
        this.buildId = buildId;
    }

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }
}
