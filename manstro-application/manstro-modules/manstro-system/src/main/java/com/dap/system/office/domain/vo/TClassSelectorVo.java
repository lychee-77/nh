package com.dap.system.office.domain.vo;

import com.dap.common.core.annotation.Excel;
import com.dap.common.core.annotation.Excels;

public class TClassSelectorVo {


    private Integer id;

    @Excel(name="班级名称")
    private String className;

    @Excels({
            @Excel(name = "学年名称",targetAttr = "studayYear")
    })
    private TSemesterSelectorVo semesterSelectorVo;




    public TSemesterSelectorVo getSemesterSelectorVo() {
        return semesterSelectorVo;
    }

    public void setSemesterSelectorVo(TSemesterSelectorVo semesterSelectorVo) {
        this.semesterSelectorVo = semesterSelectorVo;
    }




    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
