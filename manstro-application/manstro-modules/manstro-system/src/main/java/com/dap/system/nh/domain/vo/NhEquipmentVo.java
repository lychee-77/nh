package com.dap.system.nh.domain.vo;

import com.dap.common.core.annotation.Excel;
import com.dap.system.nh.domain.NhEquipment;

public class NhEquipmentVo extends NhEquipment {
    /**
     * 设备类型
     */
    private String equipmentTypeModel;

    /**
     * 公司名称
     */
    private String venderName;

    /**
     * 公司地址
     */

    private String locationName;

    @Excel(name = "楼宇名称")
    private String buildName;
    @Excel(name = "楼层名称")
    private String floorName;
    @Excel(name = "房间名称")
    private String roomName;

    private Long buildId;

    private Long floorId;

    public Long getBuildId() {
        return buildId;
    }

    public void setBuildId(Long buildId) {
        this.buildId = buildId;
    }

    public Long getFloorId() {
        return floorId;
    }

    public void setFloorId(Long floorId) {
        this.floorId = floorId;
    }

    public String getBuildName() {
        return buildName;
    }

    public void setBuildName(String buildName) {
        this.buildName = buildName;
    }

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getEquipmentTypeModel() {
        return equipmentTypeModel;
    }

    public void setEquipmentTypeModel(String equipmentTypeModel) {
        this.equipmentTypeModel = equipmentTypeModel;
    }

    public String getVenderName() {
        return venderName;
    }

    public void setVenderName(String venderName) {
        this.venderName = venderName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}
