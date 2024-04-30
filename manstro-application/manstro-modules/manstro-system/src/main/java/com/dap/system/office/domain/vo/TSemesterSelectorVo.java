package com.dap.system.office.domain.vo;

import com.dap.common.core.annotation.Excel;

public class TSemesterSelectorVo {
    @Excel(name="学年id")
    private Integer id;
    @Excel(name="学年名称")
    private String studayYear;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStudayYear() {
        return studayYear;
    }

    public void setStudayYear(String studayYear) {
        this.studayYear = studayYear;
    }
}
