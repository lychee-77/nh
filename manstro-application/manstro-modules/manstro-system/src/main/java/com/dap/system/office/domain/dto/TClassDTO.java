package com.dap.system.office.domain.dto;

import com.dap.common.core.web.domain.BaseEntity;

public class TClassDTO extends BaseEntity {
    private String className;
    private Integer semId;
    private Integer classId;

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getSemId() {
        return semId;
    }

    public void setSemId(Integer semId) {
        this.semId = semId;
    }
}
