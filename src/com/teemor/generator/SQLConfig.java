package com.teemor.generator;

/**
 * @author: lujing
 * @description: 数据库连接参数 ip/库名/用户名/密码
 * @create: 2018/9/6 下午5:18
 */

public class SQLConfig {
    
    private String IP_PREFIX = "jdbc:mysql://";
    private String IP_PORT = ":3306/";
    private String IP_SUFFIX = "?characterEncoding=UTF-8";
    
    /**
     *
     */
    private String ip;
    
    private String dataBase;
    
    private String user;
    
    private String pwd;
    
    /**
     * 包名-业务模块名称
     */
    private String businessName;
    
    public String getURL() {
        
        return IP_PREFIX + ip + IP_PORT + this.dataBase + IP_SUFFIX;
    }
    
    public String getIp() {
        return IP_PREFIX + ip + IP_PORT + getDataBase() + IP_SUFFIX;
    }
    
    public void setIp(String ip) {
        this.ip = ip;
    }
    
    public String getDataBase() {
        return dataBase;
    }
    
    public void setDataBase(String dataBase) {
        this.dataBase = dataBase;
    }
    
    public String getUser() {
        return user;
    }
    
    public void setUser(String user) {
        this.user = user;
    }
    
    public String getPwd() {
        return pwd;
    }
    
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
    
    public String getBusinessName() {
        return businessName;
    }
    
    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }
}
