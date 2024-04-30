package com.dap.system.office.domain;

import com.dap.common.core.annotation.Excel;
import com.dap.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.Min;
import java.util.Objects;

/**
 * 课程对象 t_course
 *
 * @author dap
 * @date 2021-11-10
 */
public class TCourse extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 课程名字
     */
    @Excel(name = "课程名字")
    private String couName;


    /**
     * 是否删除
     */
    private String delFlag;

    /**
     * 教师id,在插入更新课程时使用
     */
    @Min(value = 1, message = "所选教师不合法")
    private Integer teaId;

    @Excel(name="课程英文名字")
    private String note;


    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getTeaId() {
        return teaId;
    }

    public void setTeaId(Integer teaId) {
        this.teaId = teaId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setCouName(String couName) {
        this.couName = couName;
    }

    public String getCouName() {
        return couName;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getDelFlag() {
        return delFlag;
    }


}
