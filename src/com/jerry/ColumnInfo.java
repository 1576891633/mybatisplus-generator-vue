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
			case "BLOB":result = "java.lang.Byte[]";break;
			case "TEXT":result = "String";break;
			case "INTEGER":result = "java.lang.Long";break;
			case "TINYINT":result = "java.lang.Integer";break;
			case "SMALLINT":result = "java.lang.Integer";break;
			case "MEDIUMINT":result = "java.lang.Integer";break;
			case "BIT":result = "java.lang.Boolean";break;
			case "BIGINT":result = "java.math.BigInteger";break;
			case "FLOAT":result = "java.lang.Float";break;
			case "DOUBLE":result = "java.lang.Double";break;
			case "DECIMAL":result = "java.math.BigDecimal";break;
			case "ID":result = "java.lang.Long";break;
			case "DATE":result = "java.util.Date";break;
			case "TIME":result = "java.util.Date";break;
			case "DATETIME":result = "java.util.Date";break;
			case "TIMESTAMP":result = "java.util.Date";break;
			case "YEAR":result = "java.util.Date";break;
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
		if (javaType.endsWith("Date")) {
			result = "DATE";
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
