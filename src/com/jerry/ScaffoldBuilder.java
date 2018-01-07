package com.jerry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class ScaffoldBuilder {
	protected final Log logger = LogFactory.getLog(getClass());

	protected final static String PKG_PREFIX = "com.bison.";
	protected final static String PKG_PREFIX_WEB = "com.bison.";
	protected final static String PKG_PREFIX_BUSINESS = "";
	protected final static String PKG_SUFFIX_MODEL = "model.";
	protected final static String PKG_SUFFIX_DAO = "repositories.";
	protected final static String PKG_SUFFIX_MANAGER = "service.";
	protected final static String PKG_IMPL = "impl";
	protected final static String PKG_SUFFIX_ACTION = ".action";
	protected final static String PKG_SUFFIX_WEB = ".web.";
	protected final static String PKG_SUFFIX_MAPPER = "mapper.";
	protected final static String PKG_SUFFIX_DOMAIN = "domain.";
	
	protected String pkgName;
	protected String pkgNameWeb;
	protected String businessName;
	protected String clzName;
	protected TableInfo tableInfo;
	protected String mpthName;

	private final Map<String, String> mapping;

	public ScaffoldBuilder(String pkgName, String clzName, TableInfo tableInfo,String mpthName) {
		this.businessName = pkgName;
		this.pkgName = PKG_PREFIX;
		this.pkgNameWeb = PKG_PREFIX_WEB;
		this.clzName = clzName;
		this.mpthName = mpthName;
		this.tableInfo = tableInfo;

		mapping = new HashMap<String, String>();

		mapping.put("clzName", clzName);
		mapping.put("clzNameLC", StringUtils.uncapitalize(clzName));
		mapping.put("tblName", tableInfo.getName());
		mapping.put("modelPath", getModelPath());
		mapping.put("columns", tableInfo.getColumnNames());
		
		mapping.put("conditionPath", getModelPath()+"SearchCondition");
		mapping.put("responsePath", getModelPath() +"Response");
		mapping.put("formPath", getModelPath() +"Form");
		mapping.put("formName", clzName+"Form");
		mapping.put("responseName", clzName+"Response");
		
		mapping.put("daoPath", getDaoPath());
		mapping.put("mapperPath", getMapperPath());
		mapping.put("servicePath", getManagerPath());
		mapping.put("serviceImplPath", getManagerImplPath());
		// for Model java
		mapping.put("fieldsDeclareInfo", tableInfo.getFieldsDeclareInfo());

		//getter setter

		mapping.put("fieldsGetterSertter",tableInfo.getGetterSetter());


		// for model sqlmapping
		mapping.put("resultMap", tableInfo.getResultMap());
		mapping.put("otherCondition", tableInfo.getOtherCondition());		

		DevLog.debug(tableInfo.getPrimaryKey());
		mapping.put("primaryKey", tableInfo.getPrimaryKey());
		
		DevLog.debug(tableInfo.getParserKey());
		mapping.put("parserKey", tableInfo.getParserKey());
		mapping.put("parserKeyType",tableInfo.getPrimaryKeyType());

		DevLog.debug(tableInfo.getFindByLike());
		mapping.put("findLikeBy", tableInfo.getFindByLike());
		DevLog.debug(tableInfo.getSelectStatement());
		mapping.put("selectStatement", tableInfo.getSelectStatement());
		DevLog.debug(tableInfo.getInsertStatement());
		mapping.put("insertStatement", tableInfo.getInsertStatement());
		DevLog.debug(tableInfo.getUpdateStatement());
		mapping.put("updateStatement", tableInfo.getUpdateStatement());

		mapping.put("updateMapModel", tableInfo.getUpdateMapModel());
	}
	
	public String getModelPath() {
		return PKG_PREFIX  + PKG_SUFFIX_MODEL + mpthName + "." +clzName;
	}
	public String getConditionPath() {
		return pkgName + PKG_SUFFIX_DOMAIN;
	}

	public String getDomainPath() {
		return pkgName + PKG_SUFFIX_DOMAIN ;
	}
	
	public String getSqlMapFile() {
		return pkgName + PKG_SUFFIX_WEB +PKG_SUFFIX_MODEL + clzName + ".xml";
	}

	public String getDaoPath() {
		return pkgName  + PKG_SUFFIX_DAO + businessName + "." + clzName + "Mapper";
	}
	public String getMapperPath() {
		return pkgName + PKG_SUFFIX_DAO + PKG_SUFFIX_MAPPER + clzName + "Mapper";
	}

	public String getManagerPath() {
		return pkgName + PKG_SUFFIX_MANAGER + businessName + "." + clzName + "Service";
	}

	public String getManagerImplPath() {
		return pkgName + PKG_SUFFIX_MANAGER + PKG_IMPL + StringUtil.DOT + clzName + "Service"
				+ StringUtils.capitalize(PKG_IMPL);
	}

	public List<FileGenerator> buildGenerators() {
		List<FileGenerator> list = new ArrayList<FileGenerator>();
		//dao mapper
		list.add(new FileGenerator(pkgName + PKG_SUFFIX_DAO+businessName, clzName + "Mapper", "DAO.txt", mapping));
		list.add(new FileGenerator(pkgName + PKG_SUFFIX_DAO + businessName, clzName+"Mapper", "SqlMap.txt", mapping, "xml"));
		//service
//		list.add(new FileGenerator(pkgName + "service."+businessName, clzName + "Service", "Service.txt", mapping));
//		list.add(new FileGenerator(pkgName + "service."+businessName+".impl", clzName + "ServiceImpl", "ServiceImpl.txt", mapping));
		//controller
//		list.add(new FileGenerator(pkgNameWeb + "controller."+businessName, clzName + "Controller", "Controller.txt", mapping));
		//model
//		list.add(new FileGenerator(pkgName + "model."+ mpthName, clzName, "Model.txt", mapping));
//		System.out.println("包路径："+pkgName + "model."+mpthName);
//		list.add(new FileGenerator(pkgName + "model."+mpthName, clzName+"Response", "Response.txt", mapping));
//		list.add(new FileGenerator(pkgName + "model."+mpthName, clzName+"Form", "Form.txt", mapping));
//		list.add(new FileGenerator(pkgName + "model."+mpthName, clzName+"SearchCondition", "SearchCondition.txt", mapping));

		//		list.add(new FileGenerator(PKG_PREFIX + "model."+ mpthName, clzName, "Model.txt", mapping));
//		list.add(new FileGenerator(pkgName + "model."+ businessName, clzName, "Model.txt", mapping));
//		list.add(new FileGenerator(pkgName +"."+ PKG_SUFFIX_MAPPER, clzName+"Mapper", "SqlMap.txt", mapping, "xml"));
		return list;
	}

}
