package com.jerry;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


public class Create  {
	/**
	 * 生成代码  T_BUSINESS_ACTIVITY_RANGE_RELATION
	 * 
	 */
	public static void main(String arg[]) throws SQLException {
		HashMap<String,String> map =new HashMap<>();
		map.put("userpwd","root");
		map.put("userName","root");
		map.put("jdbc","jdbc:mysql://localhost:3306/changepile");
		map.put("driver","com.mysql.jdbc.Driver");
						//business包名		//类名	   //具体的表名				    //MODEL包名
		new ScaffoldGen("system", "SysUser", "sys_user","user",map).execute();
	}
	public static void create(String business, String className, String tableName, String modelName, Map<String, String> dbInfo){
		try {
			new ScaffoldGen(business, className, tableName,modelName,dbInfo).execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}