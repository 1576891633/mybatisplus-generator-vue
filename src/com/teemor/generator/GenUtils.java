

package com.teemor.generator;


import com.teemor.model.ColumnEntity;
import com.teemor.model.TableEntity;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;


/**
 * 代码生成器   工具类
 *
 * @author lujing
 * @email
 * @date 2018年9月7日 11:40:24
 */
public class GenUtils {
    
    
    /**
     * 获取模板
     *
     * @return
     */
    public static List<String> getTemplates() {
        List<String> templates = new ArrayList<>();
        templates.add("template/Entity.java.vm");
        templates.add("template/Dao.java.vm");
        templates.add("template/Dao.xml.vm");
        templates.add("template/Service.java.vm");
        templates.add("template/ServiceImpl.java.vm");
        templates.add("template/Controller.java.vm");
        templates.add("template/query.java.vm");
        
        templates.add("template/index.vue.vm");
        templates.add("template/api.js.vm");
        templates.add("template/edit.vue.vm");
        
//        templates.add("template/test.vue.vm");
//        templates.add("template/list.html.vm");
//        templates.add("template/list.js.vm");
//        templates.add("template/menu.sql.vm");
        return templates;
    }
    
    
    /**
     * 代码生成核心方法
     *
     * @param tableEntity      数据库表详情
     * @param columnEntityList 表对应的字段详情
     * @param businessName     对应业务模块名称
     */
    public static void generatorCode(TableEntity tableEntity, List<ColumnEntity> columnEntityList, String businessName, String authorName) {
        //配置信息
        Configuration config = getConfig();
        
        boolean hasBigDecimal = buildTableInfo(tableEntity, columnEntityList, config);
        
        //设置velocity资源加载器
        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(prop);
        
        String mainPath = config.getString("mainPath");
        mainPath = StringUtils.isBlank(mainPath) ? "com.kingyon" : mainPath;
        
        //封装模板数据
        Map<String, Object> map = new HashMap<>();
        map.put("tableName", tableEntity.getTableName());
        map.put("routerName", tableEntity.getRouterName());
        
        map.put("comments", tableEntity.getComments());
        map.put("pk", tableEntity.getPk());
        map.put("className", tableEntity.getClassName());
        map.put("classname", tableEntity.getClassname());
        map.put("pathName", tableEntity.getClassname().toLowerCase());
        map.put("columns", tableEntity.getColumns());
        map.put("hasBigDecimal", hasBigDecimal);
        map.put("mainPath", mainPath);
        map.put("package", config.getString("package"));
        map.put("moduleName", businessName);
        map.put("author", authorName);
        map.put("email", config.getString("email"));
        map.put("datetime", DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
        
        VelocityContext context = new VelocityContext(map);
        //获取模板列表
        List<String> templates = getTemplates();
        for (String template : templates) {
            //渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, "UTF-8");
            tpl.merge(context, sw);
            
            String filePath = getFileName(template, tableEntity.getClassName(), config.getString("package"), businessName);
            
            
            File file = createFile(filePath);
            
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(sw.toString().getBytes());
                fileOutputStream.close();
                System.out.println("模板" + template + "解析成功 ，代码位置：" + filePath);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("模板 [ " + template + " ] 解析错误");
            }
        }
    }
    
    
    /**
     * 构建数据库表==>类名
     * 字段名==>属性名
     * 类型转换
     *
     * @param tableEntity
     * @param columnEntityList
     * @param config           配置
     * @return 表格中是否有decimal
     */
    private static boolean buildTableInfo(TableEntity tableEntity, List<ColumnEntity> columnEntityList, Configuration config) {
        boolean hasBigDecimal = false;
        //表名转换成Java类名
        String className = tableToJava(tableEntity.getTableName(), config);
        String routerName = tableToRouterName(tableEntity.getTableName());
        String excludeTableFiled = config.getString("excludeTableFiled");
        
        tableEntity.setClassName(className);
        tableEntity.setClassname(StringUtils.uncapitalize(className));
        tableEntity.setColumns(columnEntityList);
        tableEntity.setRouterName(routerName);
        
        Iterator<ColumnEntity> iterator = columnEntityList.iterator();
        while (iterator.hasNext()) {
            ColumnEntity columnEntity = iterator.next();
            String columnName = columnEntity.getColumnName();
            
            //列名转换成Java属性名
            String attrName = columnToJava(columnEntity.getColumnName());
            columnEntity.setAttrName(attrName);
            //属性名称 小写
            columnEntity.setAttrname(StringUtils.uncapitalize(attrName));
            //列的数据类型，转换成Java类型
            String attrType = config.getString(columnEntity.getDataType(), "unknowType");
            columnEntity.setAttrType(attrType);
            if (!hasBigDecimal && attrType.equals("BigDecimal")) {
                hasBigDecimal = true;
            }
            if ("PRI".equalsIgnoreCase(columnEntity.getColumnKey())
                    && tableEntity.getPk() == null) {
                tableEntity.setPk(columnEntity);
            }
            if (StringUtils.isNotBlank(columnName) && excludeTableFiled.contains(columnName)) {
                iterator.remove();
                continue;
            }
            
        }
        //没主键，则第一个字段为主键
        if (tableEntity.getPk() == null) {
            throw new RuntimeException("没有主键，解析失败");
        }
        return hasBigDecimal;
    }
    
    private static String tableToRouterName(String tableName) {
        int firstLineIndex = tableName.indexOf("_");
        return tableName.substring(firstLineIndex + 1);
    }
    
    /**
     * 列名转换成Java属性名
     */
    public static String columnToJava(String columnName) {
        return WordUtils.capitalizeFully(columnName, new char[]{'_'}).replace("_", "");
    }
    
    /**
     * 表名转换成Java类名
     */
    public static String tableToJava(String tableName, String tablePrefix) {
        
        if (StringUtils.isNotBlank(tablePrefix)) {
            tableName = tableName.replace(tablePrefix, "");
        }
        return columnToJava(tableName);
    }
    
    public static String tableToJava(String tableName, Configuration config) {
        boolean removeProfix = config.getBoolean("removePrefix");
        String tablePrefix = config.getString("tablePrefix");
        
        if (removeProfix) {
            int indexFirstLine = tableName.indexOf("_");
            tableName = tableName.substring(indexFirstLine);
        }
        
        if (StringUtils.isNotBlank(tablePrefix)) {
            tableName = tableName.replace(tablePrefix, "");
        }
        
        return columnToJava(tableName);
    }
    
    /**
     * 获取配置信息
     */
    public static Configuration getConfig() {
        try {
            return new PropertiesConfiguration("generator.properties");
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
    /**
     * 获取输出文件的路径
     *
     * @param template    模板名称
     * @param className   类名
     * @param packageName 包名
     * @param moduleName  业务木块名称
     * @return
     */
    public static String getFileName(String template, String className, String packageName, String moduleName) {
        
        String packagePath = "";
        String outPath = getConfig().getString("outPath");
        
        String classname = StringUtils.uncapitalize(className);
        boolean xmlToResource = false;
        if (StringUtils.isNotBlank(outPath)) {
            packagePath = outPath + File.separator;
        } else {
            packagePath = "main" + File.separator + "java" + File.separator;
        }
        
        if (StringUtils.isNotBlank(packageName)) {
            packagePath += packageName.replace(".", File.separator) + File.separator;
        }
        
        if (template.contains("Entity.java.vm")) {
            return packagePath + "entity" + File.separator + moduleName + File.separator + className + ".java";
        }
        
        if (template.contains("query.java.vm")) {
            return packagePath + "request" + File.separator + moduleName + File.separator + className + "Request.java";
        }
        
        if (template.contains("Dao.java.vm")) {
            return packagePath + "dao" + File.separator + moduleName + File.separator + className + "Mapper.java";
        }
        
        if (template.contains("Service.java.vm")) {
            return packagePath + "service" + File.separator + moduleName + File.separator + className + "Service.java";
        }
        
        if (template.contains("ServiceImpl.java.vm")) {
            return packagePath + "service" + File.separator + moduleName + File.separator + "impl" + File.separator + className + "ServiceImpl.java";
        }
        
        if (template.contains("Controller.java.vm")) {
            return packagePath + "controller" + File.separator + moduleName + File.separator + className + "Controller.java";
        }
        
        if (template.contains("Dao.xml.vm")) {
            if (xmlToResource) {
                return "main" + File.separator + "resources" + File.separator + "mapper" + File.separator + moduleName + File.separator + className + "Dao.xml";
            } else {
                return packagePath + "dao" + File.separator + moduleName + File.separator + className + "Mapper.xml";
            }
        }
        
        /*--页面  start--*/
        
        if (template.contains("index.vue.vm")) {
            
            return packagePath + "views" + File.separator + moduleName + File.separator + classname + File.separator + "index.vue";
            
        }
        
        if (template.contains("api.js.vm")) {
            
            return packagePath + "api" + File.separator + moduleName + File.separator + classname + File.separator + "api.js";
            
        }
        
        if (template.contains("edit.vue.vm")) {
            
            return packagePath + "views" + File.separator + moduleName + File.separator + classname + File.separator + "edit.vue";
            
        }
        
        
        /*----页面 end -----  */
        
        
        if (template.contains("list.html.vm")) {
            return "main" + File.separator + "resources" + File.separator + "templates" + File.separator
                    + "modules" + File.separator + moduleName + File.separator + className.toLowerCase() + ".html";
        }
        
        if (template.contains("list.js.vm")) {
            return "main" + File.separator + "resources" + File.separator + "statics" + File.separator + "js" + File.separator
                    + "modules" + File.separator + moduleName + File.separator + className.toLowerCase() + ".js";
        }
        
        if (template.contains("menu.sql.vm")) {
            return className.toLowerCase() + "_menu.sql";
        }
        
        return null;
    }
    
    
    /**
     * 创建多级目录文件
     *
     * @param path 文件路径
     * @throws IOException
     */
    private static File createFile(String path) {
        if (StringUtils.isNotEmpty(path)) {
            File file = new File(path);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            return file;
        }
        
        return null;
    }
    
    
}