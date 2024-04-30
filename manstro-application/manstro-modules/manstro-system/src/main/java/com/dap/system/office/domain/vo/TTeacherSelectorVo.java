package com.dap.system.office.domain.vo;

import com.dap.common.core.web.domain.BaseEntity;

public class TTeacherSelectorVo extends BaseEntity {
    private Integer id;

    private String teaName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTeaName() {
        return teaName;
    }

    public void setTeaName(String teaName) {
        this.teaName = teaName;
    }
}
