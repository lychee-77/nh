package com.dap.system.office.domain.vo;

import java.util.List;

public class TSemesterCascaderVo {
    private String name;
    private List<TCascaderNode> children;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TCascaderNode> getChildren() {
        return children;
    }

    public void setChildren(List<TCascaderNode> children) {
        this.children = children;
    }
}
