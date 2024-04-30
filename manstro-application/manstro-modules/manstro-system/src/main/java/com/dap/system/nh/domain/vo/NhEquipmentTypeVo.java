package com.dap.system.nh.domain.vo;


import com.dap.system.nh.domain.NhEquipment;

public class NhEquipmentTypeVo extends NhEquipment {
    /**
     * 厂家信息
     */
    private String venderName;
    /**
     * 厂家地址
     */
    private String locationName;
    /**
     * 设备类型
     */
    private String equipmentTypeModel;


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

    public String getEquipmentTypeModel() {
        return equipmentTypeModel;
    }

    public void setEquipmentTypeModel(String equipmentTypeModel) {
        this.equipmentTypeModel = equipmentTypeModel;
    }
}
