package com.teemor.model;

import java.util.List;

/**
 * 表数据
 *
 * @author lujing
 * @date
 */
public class TableEntity {
    /**
     * 选中状态
     */
    private Boolean checked;
    //表的名称
    private String tableName;
    //表的备注
    private String comments;
    //表的主键
    private ColumnEntity pk;
    //表的列名(不包含主键)
    private List<ColumnEntity> columns;
    
    //类名(第一个字母大写)，如：sys_user => SysUser
    private String className;
    //类名(第一个字母小写)，如：sys_user => sysUser
    private String classname;
    
    /**
     * 路由名称
     */
    private String routerName;
    
    
    
    public String getTableName() {
        return tableName;
    }
    
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    
    public String getComments() {
        return comments;
    }
    
    public void setComments(String comments) {
        this.comments = comments;
    }
    
    public ColumnEntity getPk() {
        return pk;
    }
    
    public void setPk(ColumnEntity pk) {
        this.pk = pk;
    }
    
    public List<ColumnEntity> getColumns() {
        return columns;
    }
    
    public void setColumns(List<ColumnEntity> columns) {
        this.columns = columns;
    }
    
    public String getClassName() {
        return className;
    }
    
    public void setClassName(String className) {
        this.className = className;
    }
    
    public String getClassname() {
        return classname;
    }
    
    public void setClassname(String classname) {
        this.classname = classname;
    }
    
    public Boolean isChecked() {
        return checked;
    }
    
    public void setChecked(boolean checked) {
        this.checked = checked;
    }
    
    public String getRouterName() {
        return routerName;
    }
    
    public void setRouterName(String routerName) {
        this.routerName = routerName;
    }
}
