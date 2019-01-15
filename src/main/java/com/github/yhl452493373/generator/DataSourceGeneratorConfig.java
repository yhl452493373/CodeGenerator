package com.github.yhl452493373.generator;

import java.io.File;

/**
 * 多数据源配置类
 * 这里面配置是否启用一级缓存 cacheEnabled
 */
@SuppressWarnings("unused")
public class DataSourceGeneratorConfig {
    //是否多数据源
    private Boolean multiple = false;
    //是否全局地开启或关闭配置文件中的所有映射器已经配置的任何缓存
    private Boolean cacheEnabled = false;
    //单数据源配置文件
    private String singleYmlFile = "application-single-datasource.yml";
    //多数据源配置文件
    private String multipleYmlFile = "application-multiple-datasource.yml";
    //生成数据源配置的模板名称
    private String templateDatasource = "templates/datasource.java.ftl";
    //文件存在时是否覆盖
    private Boolean fileOverride = false;
    //路径分隔符,不用管
    private String fileSeparator = File.separator;

    public Boolean getMultiple() {
        return multiple;
    }

    public DataSourceGeneratorConfig setMultiple(Boolean multiple) {
        this.multiple = multiple;
        return this;
    }

    public Boolean getCacheEnabled() {
        return cacheEnabled;
    }

    public DataSourceGeneratorConfig setCacheEnabled(Boolean cacheEnabled) {
        this.cacheEnabled = cacheEnabled;
        return this;
    }

    public String getSingleYmlFile() {
        return singleYmlFile;
    }

    public DataSourceGeneratorConfig setSingleYmlFile(String singleYmlFile) {
        this.singleYmlFile = singleYmlFile;
        return this;
    }

    public String getMultipleYmlFile() {
        return multipleYmlFile;
    }

    public DataSourceGeneratorConfig setMultipleYmlFile(String multipleYmlFile) {
        this.multipleYmlFile = multipleYmlFile;
        return this;
    }

    public String getTemplateDatasource() {
        return templateDatasource;
    }

    public DataSourceGeneratorConfig setTemplateDatasource(String templateDatasource) {
        this.templateDatasource = templateDatasource;
        return this;
    }

    public Boolean getFileOverride() {
        return fileOverride;
    }

    public DataSourceGeneratorConfig setFileOverride(Boolean fileOverride) {
        this.fileOverride = fileOverride;
        return this;
    }

    public String getFileSeparator() {
        return fileSeparator;
    }

    public DataSourceGeneratorConfig setFileSeparator(String fileSeparator) {
        this.fileSeparator = fileSeparator;
        return this;
    }
}
