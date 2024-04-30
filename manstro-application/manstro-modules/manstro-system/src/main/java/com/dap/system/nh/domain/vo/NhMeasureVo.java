package com.dap.system.nh.domain.vo;

import com.dap.common.core.annotation.Excel;
import com.dap.system.nh.domain.NhMeasure;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class NhMeasureVo extends NhMeasure {

    /**
     * 区域id,实际上是房屋id的别名,在查询时,只需要携带该id即可
     */
    private Long spaceId;

    /**
     * 查询条件
     */
    private Long buildId;

    private Long floorId;
    @Excel(name = "楼宇名称",type = Excel.Type.IMPORT)
    private String buildName;
    @Excel(name = "楼层名称",type = Excel.Type.IMPORT)
    private String floorName;
    @Excel(name = "房间名称",type = Excel.Type.IMPORT)
    private String roomName;

    /**
     * 模糊匹配的统计时间,一个大于,一个小于
     */
    private List<String> likeCurrentDate;

    /**
     * 设备类别id
     */
    private Long equipmentTypeModelId;

    private String equipmentTypeModel;

    /**
     * 能耗类型
     */
    @Excel(name = "能耗类型",type = Excel.Type.IMPORT)
    private String nhTypeModel;

    /**
     * 设备型号
     */
    @Excel(name = "设备型号",type = Excel.Type.IMPORT)
    private String typeModel;

    /**
     * 环比增长率
     */
    private String ringRatio;





    /**
     * 同一spaceId
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NhMeasureVo that = (NhMeasureVo) o;

        if (!Objects.equals(roomName, that.roomName)) {
            return false;
        }
        if (!Objects.equals(buildName, that.buildName)) {
            return false;
        }
        if (!Objects.equals(floorName, that.floorName)) {
            return false;
        }
        if (!Objects.equals(nhTypeModel, that.nhTypeModel)) {
            return false;
        }
        return Objects.equals(typeModel, that.typeModel);
    }

    @Override
    public int hashCode() {
        int result = spaceId != null ? spaceId.hashCode() : 0;
        result = 31 * result + (buildId != null ? buildId.hashCode() : 0);
        result = 31 * result + (floorId != null ? floorId.hashCode() : 0);
        result = 31 * result + (nhTypeModel != null ? nhTypeModel.hashCode() : 0);
        result = 31 * result + (typeModel != null ? typeModel.hashCode() : 0);
        return result;
    }


    public String getRingRatio() {
        return ringRatio;
    }

    public void setRingRatio(String ringRatio) {
        this.ringRatio = ringRatio;
    }

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

    public String getTypeModel() {
        return typeModel;
    }

    public void setTypeModel(String typeModel) {
        this.typeModel = typeModel;
    }

    public String getNhTypeModel() {
        return nhTypeModel;
    }

    public void setNhTypeModel(String nhTypeModel) {
        this.nhTypeModel = nhTypeModel;
    }

    public Long getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(Long spaceId) {
        this.spaceId = spaceId;
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

    public List<String> getLikeCurrentDate() {
        return likeCurrentDate;
    }

    public void setLikeCurrentDate(List<String> likeCurrentDate) {
        this.likeCurrentDate = likeCurrentDate;
    }

    public Long getEquipmentTypeModelId() {
        return equipmentTypeModelId;
    }

    public void setEquipmentTypeModelId(Long equipmentTypeModelId) {
        this.equipmentTypeModelId = equipmentTypeModelId;
    }

    public String getEquipmentTypeModel() {
        return equipmentTypeModel;
    }

    public void setEquipmentTypeModel(String equipmentTypeModel) {
        this.equipmentTypeModel = equipmentTypeModel;
    }

    @Override
    public String toString() {
        return "NhMeasureVo{" +
                "spaceId=" + spaceId +
                ", buildId=" + buildId +
                ", floorId=" + floorId +
                ", buildName='" + buildName + '\'' +
                ", floorName='" + floorName + '\'' +
                ", roomName='" + roomName + '\'' +
                ", likeCurrentDate=" + likeCurrentDate +
                ", equipmentTypeModelId=" + equipmentTypeModelId +
                ", equipmentTypeModel='" + equipmentTypeModel + '\'' +
                ", nhTypeModel='" + nhTypeModel + '\'' +
                ", typeModel='" + typeModel + '\'' +
                ", ringRatio='" + ringRatio + '\'' +
                '}';
    }
}
