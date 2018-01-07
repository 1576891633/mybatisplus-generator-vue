package com.jerry;

import java.util.ArrayList;
import java.util.List;

public class TableInfo {
	private static final String ID = "id";
	private final String ENDL = "\n";
	private final String SPACE =" ";
	private final String TAB = "\t";
	private final String TAB2 = TAB + TAB;
	private final String TAB3 = TAB2 + TAB;
	private final String TAB4 = TAB2 + TAB2;
	private String name;
	private String primaryKey;
	private String parserKey;
	private String primaryKeyType;

	private List<ColumnInfo> columns;
	private List<FieldInfo> fields;

	public TableInfo(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	public List<ColumnInfo> getColumns() {
		return columns;
	}

	public void setColumns(List<ColumnInfo> columns) {
		this.columns = columns;
	}

	public String getParserKey() {
		return parserKey;
	}

	public void setParserKey(String parserKey) {
		this.parserKey = parserKey;
	}

	public String getPrimaryKeyType() {
		return primaryKeyType;
	}

	public void setPrimaryKeyType(String primaryKeyType) {
		this.primaryKeyType = primaryKeyType;
	}

	public void addColumn(ColumnInfo column) {
		if (columns == null)
			columns = new ArrayList<ColumnInfo>();
		if (!columns.contains(column)) {
			columns.add(column);
			String type = column.parseJavaType();
			String name = column.parseFieldName();
			String remarks = column.getRemarks();
			addFiled(new FieldInfo(type, name,remarks));
		}
	}

	public void addFiled(FieldInfo field) {
		if (fields == null)
			fields = new ArrayList<FieldInfo>();
		if (field.getName().equalsIgnoreCase(ID))
			return;
		if (!fields.contains(field)) {
			fields.add(field);
		}

	}

	public List<FieldInfo> getFields() {
		return fields;
	}

	public String getFieldsDeclareInfo() {
		StringBuffer sb = new StringBuffer();
		List<FieldInfo> cop =new ArrayList();
		cop.add(new FieldInfo(primaryKeyType,primaryKey,"主键"));
		cop.addAll(fields);
		for (FieldInfo field : cop) {
//			if (field.getName().equalsIgnoreCase(ID))
//				continue;// id property is in the BaseModel
			sb.append(TAB).append("/**").append(ENDL);
			sb.append(TAB).append(" * ").append(field.getRemarks()).append(ENDL);
			sb.append(TAB).append(" */").append(ENDL);
			sb.append(TAB);
			sb.append("private ");
			sb.append(field.getType());
			sb.append(" ");
			sb.append(field.getName());
			sb.append(";");
			sb.append(ENDL).append(ENDL);
		}
		return sb.toString();
	}


	public String getGetterSetter(){
		StringBuffer sb = new StringBuffer();
		List<FieldInfo> cop =new ArrayList();
		cop.add(new FieldInfo(primaryKeyType,primaryKey,"主键"));
		cop.addAll(fields);
		sb.append("//*************************** Getter and Setter ***************************//").append(ENDL).append(ENDL);
		for (FieldInfo field : cop) {
			String name = field.getName();
			String aCase = upperCase(name);
			sb.append(TAB).append("public void set").append(aCase);
			sb.append("(");
			sb.append(field.getType()).append(SPACE);
			sb.append(name).append("){this.");
			sb.append(name);
			sb.append("=").append(name).append(";}");
			sb.append(ENDL).append(ENDL);
			sb.append(TAB);
			sb.append("public ");
			sb.append(field.getType());
			sb.append(" get");
			sb.append(aCase).append("(){return this.");;
			sb.append(name).append(";}");
			sb.append(ENDL).append(ENDL);
		}
		return sb.toString();
	}

	/**
	 * 首字母大写
	 * @param str
	 * @return
	 */
	public String upperCase(String str) {
		char[] ch = str.toCharArray();
		if (ch[0] >= 'a' && ch[0] <= 'z') {
			ch[0] = (char) (ch[0] - 32);
		}
		return new String(ch);
	}


	public String getSelectStatement() {
		StringBuffer sb = new StringBuffer();
		sb.append(getSelectColumnNames());
		return sb.toString();
	}

	public String getInsertStatement() {
		StringBuffer sb = new StringBuffer();
		sb.append(" values (");
		sb.append(ENDL);
		ColumnInfo col = null;
		sb.append(TAB3);
		sb.append("#{" + parserKey + ",jdbcType=VARCHAR},");
		sb.append(ENDL);
		for (int i = 0; i < columns.size(); i++) {
			col = columns.get(i);
			sb.append(TAB3);
			sb.append("#{" + col.parseFieldName() + ",jdbcType=" + col.parseJdbcType() + "}");
			if (i + 1 != columns.size()) {
				sb.append(",");
			}
			sb.append(ENDL);
		}
		sb.append(TAB3);
		sb.append(" )");
		return sb.toString();
	}

	public String getUpdateStatement() {
		StringBuffer sb = new StringBuffer();
		sb.append("<trim prefix=\"set\" suffixOverrides=\",\">");
		sb.append(ENDL);
		ColumnInfo col = null;
		for (int i = 0; i < columns.size(); i++) {
			col = columns.get(i);
			sb.append(TAB3);
			sb.append("<if test=\"" + col.parseFieldName() + " != null and " + col.parseFieldName() + " != '' \"> ");
			sb.append(col.getName() + "=#{" + col.parseFieldName() + ",jdbcType=" + col.parseJdbcType() + "}");
			if (i + 1 != columns.size()) {
				sb.append(",");
			}
			sb.append("</if>");
			sb.append(ENDL);
		}
		sb.append(TAB3);
		sb.append("</trim>");
		sb.append(ENDL);
		sb.append(TAB3);
		sb.append("<where>");
		sb.append(ENDL);
		sb.append(TAB3);
		sb.append("<if test=\"" + parserKey + "!=null and " + parserKey + " != ''\">");
		sb.append(ENDL);
		sb.append(TAB3);
		sb.append("<include refid=\"conditionOnlyId\" />");
		sb.append(ENDL);
		sb.append(TAB3);
		sb.append("</if>");
		sb.append(ENDL);
		sb.append(TAB3);
		sb.append("</where>");		
		sb.append(ENDL);
		return sb.toString();
	}

	public String getResultMap() {
		StringBuffer sb = new StringBuffer();
		sb.append(TAB3);
		sb.append("<result property=\""+parserKey+"\"");
		sb.append(" column=\"" + primaryKey + "\"");
		sb.append(" jdbcType=\"VARCHAR\" />");
		sb.append(ENDL);
		for (ColumnInfo col : columns) {
			sb.append(TAB3);
			sb.append("<result property=\"" + col.parseFieldName() + "\" column=\"" + col.getName()
					+ "\" jdbcType=\"" + col.parseJdbcType() + "\" />");
			sb.append(ENDL);
		}
		return sb.toString();
	}

	public String getOtherCondition() {
		StringBuffer sb = new StringBuffer();
		sb.append("<where>");
		sb.append(ENDL);
		for (ColumnInfo col : columns) {
			sb.append(TAB3);
			sb.append("<if test=\"" + col.parseFieldName() + " != null and " + col.parseFieldName() + " != '' \"> ");
			sb.append(" and "+ col.getName() +" =#{"+ col.parseFieldName()+"}");
			sb.append("</if>");
			sb.append(ENDL);
		}
		sb.append(TAB3);
		sb.append("</where>");
		return sb.toString();
	}
	

	public String getFindByLike() {
		StringBuffer sb = new StringBuffer();
		for (ColumnInfo col : columns) {
			if (col.getType().contains("CHAR")) {
				sb.append(TAB4);
				sb.append("<if test=\"" + col.parseFieldName() + " != null and " + col.parseFieldName() + " != '' \"> ");
				sb.append(" and "+ col.getName() +" LIKE '%'||#{"+ col.parseFieldName()+",javaType=String,jdbcType=VARCHAR}||'%'");
				sb.append("</if>");
				sb.append(ENDL);
			}
		}
		return sb.toString();
	}

	public String getColumnNames() {
		StringBuffer sb = new StringBuffer();
		sb.append(primaryKey + ",");
		ColumnInfo column = null;
		for (int i = 0; i < columns.size(); i++) {
			column = columns.get(i);
			sb.append(column.getName());
			if (i + 1 != columns.size()) {
				sb.append(",");
			}
		}
		return sb.toString();
	}
	private String getSelectColumnNames() {
		StringBuffer sb = new StringBuffer();
		sb.append(primaryKey + " " +parserKey+ ",");
		ColumnInfo column = null;
		for (int i = 0; i < columns.size(); i++) {
			column = columns.get(i);
			sb.append(column.getName());
			sb.append(" "+column.parseFieldName());
			if (i + 1 != columns.size()) {
				sb.append(",");
			}
		}
		return sb.toString();
	}

	public String getUpdateMapModel() {
		StringBuffer sb = new StringBuffer();
		sb.append("update " + name.toUpperCase() + " set "+primaryKey + "=#id:VARCHAR#"+ENDL);
		sb.append(TAB+"<dynamic>"+ENDL);
		ColumnInfo col = null;
		for (int i = 0; i < columns.size(); i++) {
			col = columns.get(i);
			sb.append(TAB2+"<isPropertyAvailable property=\""+col.parseFieldName()+"\" prepend=\",\">"+ENDL);
			sb.append(TAB3+"<isNotNull property=\""+col.parseFieldName()+"\">"+ENDL);
			sb.append(TAB4+col.getName() + "=#" + col.parseFieldName() + ":" + col.parseJdbcType() + "#"+ENDL);
			sb.append(TAB3+"</isNotNull>"+ENDL);
			
			sb.append(TAB3+"<isNull property=\""+col.parseFieldName()+"\">"+ENDL);
			sb.append(TAB4+col.getName() + "=null "+ENDL);
			sb.append(TAB3+"</isNull>"+ENDL);
			sb.append(TAB2+"</isPropertyAvailable>"+ENDL);
		}
		sb.append(TAB+"</dynamic>"+ENDL);
		sb.append(TAB2+" where " + primaryKey + "=#id:VARCHAR#");
		return sb.toString();	
	}


}
