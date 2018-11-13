package com.github.yhl452493373.bean;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

public class DataSourceProperties extends DruidDataSource {
    //自定义的属性:数据源名称
    private String name;
    //自定义属性:是否主数据源.默认为false
    private Boolean primary = false;
    //自定义属性：是否多数据源，默认为false。为true时生成代码时会根据name组合
    private Boolean multiple = false;
    //mybatis plus属性:是否使用一级缓存.生成代码时配置
    private Boolean cacheEnabled = false;
    //自定义属性:实体位置
    private String typeAliasesPackage;
    //自定义属性:Config.java位置
    private String configPackage;
    //自定义属性:Mapper.java位置
    private String mapperPackage;
    //自定义属性:Mapper.xml位置
    private String mapperLocations;
    //以下为druid数据源属性
    private String url;
    private String username;
    private String password;
    private String driverClassName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getPrimary() {
        return primary;
    }

    public void setPrimary(Boolean primary) {
        this.primary = primary;
    }

    public Boolean getMultiple() {
        return multiple;
    }

    public void setMultiple(Boolean multiple) {
        this.multiple = multiple;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
        super.jdbcUrl = this.url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getTypeAliasesPackage() {
        return typeAliasesPackage;
    }

    public void setTypeAliasesPackage(String typeAliasesPackage) {
        this.typeAliasesPackage = typeAliasesPackage;
    }

    public String getMapperLocations() {
        return mapperLocations;
    }

    public void setMapperLocations(String mapperLocations) {
        this.mapperLocations = mapperLocations;
    }

    public String getMapperPackage() {
        return mapperPackage;
    }

    public void setMapperPackage(String mapperPackage) {
        this.mapperPackage = mapperPackage;
    }

    public String getConfigPackage() {
        return configPackage;
    }

    public void setConfigPackage(String configPackage) {
        this.configPackage = configPackage;
    }

    public Boolean getCacheEnabled() {
        return cacheEnabled;
    }

    public void setCacheEnabled(Boolean cacheEnabled) {
        this.cacheEnabled = cacheEnabled;
    }

    public static String firstCharToUpper(String string) {
        if (StringUtils.isEmpty(string))
            return null;
        if (Character.isUpperCase(string.charAt(0)))
            return string;
        else
            return Character.toUpperCase(string.charAt(0)) + string.substring(1);
    }
}
