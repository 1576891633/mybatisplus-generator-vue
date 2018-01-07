package com.jerry;

public class FieldInfo {
	private String type;
	private String name;
	private String remarks;
	
	public FieldInfo(String type,String name,String remarks){
		this.type=type;
		this.name=name;
		this.remarks =remarks;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}
