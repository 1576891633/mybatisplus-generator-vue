package com.teemor.DAO;

import com.teemor.generator.SQLConfig;
import com.teemor.model.ColumnEntity;
import com.teemor.model.TableEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: lujing
 * @description:
 * @create: 2018/9/6 下午4:49
 */

public class TableDaoImpl implements TableDao {
    
    private Connection connection;
    private SQLConfig sqlConfig;
    
    public TableDaoImpl() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public TableDaoImpl(SQLConfig sqlConfig) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        this.sqlConfig = sqlConfig;
    }
    
    private Statement getStatement() throws SQLException {
        Connection connection =
                DriverManager.getConnection(sqlConfig.getIp(), sqlConfig.getUser(),
                        sqlConfig.getPwd());
        return connection.createStatement();
        
    }
    
    private PreparedStatement getPreparedStatement(String sql) throws SQLException {
        //只建立一次连接
        if (this.connection == null) {
            this.connection =
                    DriverManager.getConnection(sqlConfig.getURL(), sqlConfig.getUser(),
                            sqlConfig.getPwd());
        }
        
        
        return connection.prepareStatement(sql);
    }
    
    
    /**
     * 查询所有数据库表
     *
     * @return
     */
    @Override
    public List<TableEntity> queryList() {
        
        List<TableEntity> tableEntityList = new ArrayList();
        
        String sql = "select table_name tableName, table_comment tableComment, create_time createTime  from information_schema.tables where table_schema = (select database()) and engine = 'InnoDB'";
        
        try (PreparedStatement ps = getPreparedStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                TableEntity tableEntity = new TableEntity();
                String tableName = rs.getString(1);
                String tableComment = rs.getString(2);
                tableEntity.setTableName(tableName);
                tableEntity.setComments(tableComment);
                tableEntity.setChecked(false);
                tableEntityList.add(tableEntity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tableEntityList;
    }
    
    /**
     * 查询数据库表信息
     *
     * @param tableName
     * @return
     */
    @Override
    public TableEntity queryTable(String tableName) {
        TableEntity tableEntity = new TableEntity();
        
        String sql = "select table_name tableName, engine, table_comment tableComment, create_time createTime from information_schema.tables where table_schema = (select database()) and table_name = '" + tableName + "'";
        
        try (PreparedStatement ps = getPreparedStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                
                String tableNameGet = rs.getString(1);
                String tableComment = rs.getString(3);
                tableEntity.setTableName(tableNameGet);
                tableEntity.setComments(tableComment);
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return tableEntity;
    }
    
    /**
     * 查询数据库表字段
     *
     * @param tableName
     * @return
     */
    @Override
    public List<ColumnEntity> queryColumns(String tableName) {
        
        String sql = "select column_name columnName, data_type dataType, column_comment columnComment, column_key columnKey, extra from information_schema.columns where table_name = '" + tableName + "'  and table_schema = (select database()) order by ordinal_position";
        
        List<ColumnEntity> columnEntityList = new ArrayList<>();
        
        try (PreparedStatement ps = getPreparedStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                ColumnEntity columnEntity = new ColumnEntity();
                String columnName = rs.getString(1);
                String dataType = rs.getString(2);
                String columnComment = rs.getString(3);
                String columnKey = rs.getString(4);
                String extra = rs.getString(5);
                columnEntity.setColumnName(columnName);
                columnEntity.setDataType(dataType);
                columnEntity.setComments(columnComment);
                columnEntity.setExtra(extra);
                columnEntityList.add(columnEntity);
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return columnEntityList;
    }
}
