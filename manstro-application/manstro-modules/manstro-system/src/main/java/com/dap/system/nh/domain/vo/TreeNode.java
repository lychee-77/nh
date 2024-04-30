package com.dap.system.nh.domain.vo;

import java.util.List;

public class TreeNode {
    /**
     * 祖父节点等级
     */
    public static final Integer GRADE_FATHER_LEVEL = 1;
    /**
     * 父节点等级
     */
    public static final Integer FATHER_LEVEL = 2;
    /**
     * 子节点等级
     */
    public static final Integer CHILDREN_LEVEL = 3;

    private Long id;
    /**
     * 树空间当前显示的名称
     */
    private String label;
    /**
     *  当前处于哪一级
      */
    private Integer level;

    private List<TreeNode> children;

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }
}
