package com.jerry;

import org.apache.commons.lang.StringUtils;


public class ColumnInfo {
	private String name;
	private String type;
	private int size;
	private int digits;
	private boolean nullable;
	private final WordsParser wordsParser;
	private String remarks;

	public ColumnInfo(String name, String type, int size, int decimalDigits,
			int nullable,String remarks) {
		this.name = name;
		this.type = type;
		this.size = size;
		this.digits = decimalDigits;
		this.remarks = remarks;
		if (nullable == 1)
			this.nullable = true;
		if (StringUtil.isUpperCase(name)
				|| name.contains(StringUtil.UNDER_LINE)) {
			this.wordsParser = new UnderlineSplitWordsParser();
		} else {
			this.wordsParser = new UncapitalizeWordsParser();
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getDigits() {
		return digits;
	}

	public void setDigits(int digits) {
		this.digits = digits;
	}

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	public String parseJavaType() {
		String jdbcType = StringUtils.upperCase(getType());
		// String result = "int";
		String result = "Integer";
		switch (jdbcType){
			case "VARCHAR":result = "String";break;
			case "VARCHAR2":result = "String";break;
			case "CHAR":result = "String";break;
			case "CLOB":result = "String";break;
			case "BLOB":result = "Byte[]";break;
			case "TEXT":result = "String";break;
			case "INTEGER":result = "Long";break;
			case "TINYINT":result = "Integer";break;
			case "SMALLINT":result = "Integer";break;
			case "MEDIUMINT":result = "Integer";break;
			case "BIT":result = "Boolean";break;
			case "BIGINT":result = "Long";break;
			case "FLOAT":result = "Float";break;
			case "DOUBLE":result = "Double";break;
			case "DECIMAL":result = "java.math.BigDecimal";break;
			case "ID":result = "Long";break;
			case "DATE":result = "java.sql.Date";break;
			case "TIME":result = "java.sql.Time";break;
			case "DATETIME":result = "java.sql.Timestamp";break;
			case "TIMESTAMP":result = "java.sql.Timestamp";break;
			case "YEAR":result = "java.sql.Date";break;
		}
		return result;
	}

	public String parseJdbcType() {
		String javaType = parseJavaType();
		String result = "NUMERIC";
		if ("String".equals(javaType)) {
			result = "VARCHAR";
		}
		if (javaType.equalsIgnoreCase("int")) {
			result = "INTEGER";
		}
		if (javaType.endsWith("Timestamp") || javaType.endsWith("Time")|| javaType.endsWith("Date")) {
			result = "TIMESTAMP";
		}

		return result;
	}

	public String parseFieldName() {
		return wordsParser.parseWords(name);
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Override
	public String toString() {
		return name + " " + type + " " + size + " " + digits + " " + nullable;
	}
}
