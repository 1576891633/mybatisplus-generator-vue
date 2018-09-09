package com.teemor.DAO;

import com.teemor.model.ColumnEntity;
import com.teemor.model.TableEntity;

import java.util.List;
import java.util.Map;

/**
 * @author: lujing
 * @description:
 * @create: 2018/9/6 下午4:46
 */

public interface TableDao {
    
    
    /**
     * 查询所有数据库表
     * @return
     */
    List<TableEntity> queryList();
    
    /**
     * 查询数据库表信息
     * @param tableName
     * @return
     */
    TableEntity queryTable(String tableName);
    
    
    /**
     * 查询数据库表字段
     * @param tableName
     * @return
     */
    List<ColumnEntity> queryColumns(String tableName);
    
    
}
