package com.dap.system.nh.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import com.dap.system.nh.domain.vo.NhMeasureVo;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.dap.common.core.annotation.Excel;
import com.dap.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 能耗统计对象 nh_statistics
 *
 * @author dap
 * @date 2021-12-07
 */
public class NhMeasure extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 设备id */

    private Long equipId;

    /** 统计日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "统计日期", width = 30, dateFormat = "yyyy-MM-dd",type = Excel.Type.IMPORT)
    private Date currentDate;

    /** 能耗类型id */

    private Long nhTypeId;

    /** 总表值 */
    @Excel(name = "总表值",type = Excel.Type.IMPORT)
    private BigDecimal totalNum;

    private BigDecimal consumeNum;

    /** $column.columnComment */
    private String delFlag;

    /** $column.columnComment */
    private String createUser;

    /** $column.columnComment */
    private String updateUser;

    public NhMeasure(NhMeasureVo nhMeasureVo){
        this.id = nhMeasureVo.getId();
        this.equipId = nhMeasureVo.getEquipId();
        this.currentDate = nhMeasureVo.getCurrentDate();
        this.nhTypeId = nhMeasureVo.getNhTypeId();
        this.totalNum = nhMeasureVo.getTotalNum();
        this.consumeNum = nhMeasureVo.getConsumeNum();
    }
    public NhMeasure(NhEquipment nhEquipment, NhType nhType, NhMeasureVo nhMeasureVo) {
        this.id = nhMeasureVo.getId();
        this.currentDate = nhMeasureVo.getCurrentDate();
        this.totalNum = nhMeasureVo.getTotalNum();
        this.consumeNum = nhMeasureVo.getConsumeNum();
        this.equipId = nhEquipment.getId();
        this.nhTypeId = nhType.getId();
    }
    public NhMeasure() {
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setEquipId(Long equipId)
    {
        this.equipId = equipId;
    }

    public Long getEquipId()
    {
        return equipId;
    }
    public void setCurrentDate(Date currentDate)
    {
        this.currentDate = currentDate;
    }

    public Date getCurrentDate()
    {
        return currentDate;
    }
    public void setNhTypeId(Long nhTypeId)
    {
        this.nhTypeId = nhTypeId;
    }

    public Long getNhTypeId()
    {
        return nhTypeId;
    }
    public void setTotalNum(BigDecimal totalNum)
    {
        this.totalNum = totalNum;
    }

    public BigDecimal getTotalNum()
    {
        return totalNum;
    }
    public void setConsumeNum(BigDecimal consumeNum)
    {
        this.consumeNum = consumeNum;
    }

    public BigDecimal getConsumeNum()
    {
        return consumeNum;
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NhMeasure that = (NhMeasure) o;

        // 设备id不相同
        if (!Objects.equals(equipId, that.equipId)) {
            return false;
        }
        // 统计日期不同
        if (!Objects.equals(currentDate, that.currentDate)) {
            return false;
        }

        // 能耗类型不同
        return Objects.equals(nhTypeId, that.nhTypeId);
    }

    @Override
    public int hashCode() {
        int result = equipId != null ? equipId.hashCode() : 0;
        result = 31 * result + (currentDate != null ? currentDate.hashCode() : 0);
        result = 31 * result + (nhTypeId != null ? nhTypeId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("equipId", getEquipId())
            .append("currentDate", getCurrentDate())
            .append("nhTypeId", getNhTypeId())
            .append("totalNum", getTotalNum())
            .append("consumeNum", getConsumeNum())
            .append("remark", getRemark())
            .append("delFlag", getDelFlag())
            .append("createUser", getCreateUser())
            .append("createTime", getCreateTime())
            .append("updateUser", getUpdateUser())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
