package com.dap.system.office.domain.vo;

import java.util.List;

public class TCascaderNode {
    private Integer id;
    private String name;
    private List<TCascaderNode> children;

    public List<TCascaderNode> getChildren() {
        return children;
    }

    public void setChildren(List<TCascaderNode> children) {
        this.children = children;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TCascaderNode{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
