package com.jerry;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.*;
import java.util.List;
import java.util.Map;

public class ScaffoldGen {

	private static final String NULLABLE = "NULLABLE";
	private static final String DECIMAL_DIGITS = "DECIMAL_DIGITS";
	private static final String COLUMN_SIZE = "COLUMN_SIZE";
	private static final String TYPE_NAME = "TYPE_NAME";
	private static final String SQLSERVER = "sqlserver";
	private static final String COLUMN_NAME = "COLUMN_NAME";
	private  final String JDBC_PASSWORD;
	private  final String JDBC_USER;
	private  final String JDBC_URL;
	private  final String JDBC_DRIVER;
	private static final String JDBC_SCHEMA = "";
	private static final String CONFIG_PROPERTIES = "jdbc.properties";
	private final Log log = LogFactory.getLog(getClass());
	private Connection conn;
	private String schema;
	private DatabaseMetaData metaData;
	private final String pkgName;
	private final String clzName;
	private final String tblName;
	private final String mpthName;
	public ScaffoldGen(String pkgName, String clzName, String tblName,String mpthName, Map<String, String> dbInfo) {
		this.pkgName = pkgName;
		this.clzName = StringUtils.capitalize(clzName);
		this.tblName = tblName;
		this.mpthName = mpthName;
		JDBC_PASSWORD = dbInfo.get("userpwd");
		JDBC_USER = dbInfo.get("userName");
		JDBC_URL = dbInfo.get("jdbc");
		JDBC_DRIVER = dbInfo.get("driver");
	}

	public void execute() throws SQLException {
		execute(false);
	}

	public void execute(boolean debug) throws SQLException {
		if (!initConnection()) {
			return;
		}
		TableInfo tableInfo = parseDbTable(this.tblName);
		if (tableInfo == null) {
			return;
		}
		ScaffoldBuilder sf = new ScaffoldBuilder(this.pkgName, this.clzName, tableInfo,this.mpthName);
		List<FileGenerator> list = sf.buildGenerators();
		for (FileGenerator gen : list) {
			gen.execute(debug);
		}
	}
	
	private boolean initConnection() {
		Configuration config;
		try {
			if (StringUtil.isNotBlank(JDBC_SCHEMA)) {
				this.schema = JDBC_SCHEMA;
			}
			if (JDBC_DRIVER.toLowerCase().contains(SQLSERVER)) {
				this.schema = "dbo";
			}
			if (StringUtil.isBlank(JDBC_SCHEMA)) {
				this.schema = JDBC_USER.toUpperCase();
			}
			Class.forName(JDBC_DRIVER);
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
			log.fatal("Jdbc driver not found - " + JDBC_DRIVER);
			return false;
		}

		try {
			conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
			if (conn == null) {
				log.fatal("Database connection is null");
				return false;
			}
			metaData = conn.getMetaData();
			if (metaData == null) {
				log.fatal("Database MetaData is null");
				return false;
			}
		} catch (SQLException e) {
			log.fatal("Database connect failed");
			e.printStackTrace();
		}
		return true;
	}
	/**
	 * @return
	 * @throws SQLException 
	 */
	private TableInfo parseDbTable(String tableName) throws SQLException {
		TableInfo tableInfo = new TableInfo(tableName);
		ResultSet rs = null;
		log.trace("parseDbTable begin");
		try {
			rs = metaData.getPrimaryKeys(null, schema, tableName.toUpperCase());
			if (rs.next()) {
				tableInfo.setPrimaryKey(rs.getString(COLUMN_NAME));
				String keyName = rs.getString(COLUMN_NAME);
				ColumnInfo keyInfo = new ColumnInfo(keyName, "VARCHAR2", 64, 20, 20,"主键");
				tableInfo.setParserKey(keyInfo.parseFieldName());
			}

			if (rs.next()) {
				return null;
			}
		} catch (SQLException e) {
			log.error("Table " + tableName + " parse error.");
			e.printStackTrace();
			return null;
		}
		log.info("PrimaryKey : " + tableInfo.getPrimaryKey());

		try {
			
			rs = metaData.getColumns(conn.getCatalog(), schema, tableName, null);
			if (!rs.next()) {
				log.fatal("Table " + schema + "." + tableName + " not found.");
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			while (rs.next()) {
				String columnName = rs.getString(COLUMN_NAME);
				String columnType = rs.getString(TYPE_NAME);
				String remarks = rs.getString("REMARKS");
				int datasize = rs.getInt(COLUMN_SIZE);	
				int digits = rs.getInt(DECIMAL_DIGITS);
				int nullable = rs.getInt(NULLABLE);
				
				ColumnInfo colInfo = new ColumnInfo(columnName, columnType, datasize, digits, nullable,remarks);
				log.info("DB column : " + colInfo);
				log.info("Java field : " + colInfo.parseFieldName() + " / " + colInfo.parseJavaType());
				tableInfo.addColumn(colInfo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("Table " + tableName + " parse error.");
		}
		log.trace("parseDbTable end");
		return tableInfo;
	}

}
