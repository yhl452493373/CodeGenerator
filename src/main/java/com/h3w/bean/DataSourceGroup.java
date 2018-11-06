package com.h3w.bean;

import java.util.List;

public class DataSourceGroup {
    private String configPackage;
    private DataSourceProperties dataSourceProperties;
    private List<DataSourceProperties> dataSourcePropertiesList;

    public String getConfigPackage() {
        return configPackage;
    }

    public void setConfigPackage(String configPackage) {
        this.configPackage = configPackage;
    }

    public DataSourceProperties getDataSourceProperties() {
        return dataSourceProperties;
    }

    public void setDataSourceProperties(DataSourceProperties dataSourceProperties) {
        this.dataSourceProperties = dataSourceProperties;
    }

    public List<DataSourceProperties> getDataSourcePropertiesList() {
        return dataSourcePropertiesList;
    }

    public void setDataSourcePropertiesList(List<DataSourceProperties> dataSourcePropertiesList) {
        this.dataSourcePropertiesList = dataSourcePropertiesList;
    }
}
