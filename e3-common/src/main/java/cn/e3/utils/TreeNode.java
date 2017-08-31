package cn.e3.utils;

import java.io.Serializable;

public class TreeNode implements Serializable{

    /**
     * easyUI的Tree
     */
    private static final long serialVersionUID = -3896607880577591599L;
    
    //id
    private Integer id;
    //树形菜单节点名称
    private String text;
    //树形节点状态：是否有子节点
    private String state;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
}
