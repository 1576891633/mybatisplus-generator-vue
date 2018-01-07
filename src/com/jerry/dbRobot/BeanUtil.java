package com.jerry.dbRobot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class BeanUtil {


	public BeanUtil() {
		// TODO Auto-generated constructor stub
	}
	//创建JavaBean文件
	public String createBean(String tbName, List<Map<String, String>> collist,
			Map<String, String> infoMap) {
		
		StringBuilder fields = new StringBuilder();
		StringBuilder methods = new StringBuilder();

		StringBuilder classInfo = new StringBuilder("\t/**\r\n\t*");
		for (Map<String, String> colmap : collist) {
			String field = colmap.get("filed").toString();
			String type = typeTrans(colmap.get("type").toString());
			fields.append(getFieldStr(field, type));
			methods.append(getMethodStr(field, type));

		}
		classInfo.append("\t*@author"+new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
		classInfo.append("\r\n\t*/\r\n\r\n");
		classInfo.append("\tpublic class ");
		System.out.println(tbName);
		String[] s = tbName.split("_");
		StringBuffer fs=new StringBuffer();
		for(int i=0;i<s.length;i++){
			fs.append(captureName(s[i]));
		}
		classInfo.append(fs)
				.append("{\r\n");
		classInfo.append(fields);
		classInfo.append("\r\n");
		classInfo.append(methods);
		classInfo.append("\r\n");
		classInfo.append("}");
		File file = new File(infoMap.get("catName"), upperFirestChar(tbName) + ".java");
		try {
			FileWriter fw = new FileWriter(file);
			if (infoMap.get("packName") == null || infoMap.get("packName").toString().equals("")) {
				
			} else {
				String packageinfo =  "package " + infoMap.get("packName").toString() + ";\r\n\r\n";
				fw.write(packageinfo);
			}
			
			fw.write(classInfo.toString());
			fw.flush();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	//数据库字段类型与JAVA类型转换
	public String typeTrans(String type) {
		if (type.contains("tinyint")) {
			return "boolean";
		} else if (type.contains("int")) {
			return "int";
		} else if (type.contains("datetime")) {
			return "Date";
		} else if (type.contains("BIGINT")) {
			return "Long";
		} else if (type.contains("varchar") || type.contains("date")
				|| type.contains("time") || type.contains("timestamp")
				|| type.contains("text") || type.contains("enum")
				|| type.contains("set")) {
			return "String";
		} else if (type.contains("binary") || type.contains("blob")) {
			return "byte[]";
		} else {
			return "String";
		}
	}
	//获取方法字符串 
	private String getMethodStr(String field, String type) {
		StringBuilder get = new StringBuilder("\tpublic ");
		get.append(type).append(" ");
		if (type.equals("boolean")) {
			get.append(field);
		} else {
			get.append("get");
			get.append(upperFirestChar(field));
		}
		get.append("(){").append("\r\n\t\treturn this.").append(field)
				.append(";\r\n\t}\r\n");
		StringBuilder set = new StringBuilder("\tpublic void ");

		if (type.equals("boolean")) {
			set.append(field);
		} else {
			set.append("set");
			set.append(upperFirestChar(field));
		}
		set.append("(").append(type).append(" ").append(field)
				.append("){\r\n\t\tthis.").append(field).append("=")
				.append(field).append(";\r\n\t}\r\n");
		get.append(set);
		return get.toString();
	}

	//首字母大写
	public String upperFirestChar(String src) {
		return src.substring(0, 1).toUpperCase().concat(src.substring(1));
	}

	//获取字段
	private String getFieldStr(String field, String type) {
		StringBuilder sb = new StringBuilder();
		sb.append("\t").append("private ").append(type).append(" ");
		String[] s = field.split("_");
		StringBuffer fs=new StringBuffer();
		for(int i=0;i<s.length;i++){
			fs.append(captureName(s[i]));
		}
		sb.append(fs.toString()).append(";");
		sb.append("\r\n");
		return sb.toString();
	}
	 public static String captureName(String name) {
	        char[] cs=name.toCharArray();
	        cs[0]-=32;
	        return String.valueOf(cs);
	    }
}
