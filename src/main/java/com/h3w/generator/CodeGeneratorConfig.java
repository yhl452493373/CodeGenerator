package com.h3w.generator;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import java.io.File;

/**
 * Mybatis Plus代码生成器的配置封装.
 * 此封装仅针对mysql数据源.其他数据源请根据情况修改链接地址拼接方式或手动拼接
 * 这里配置是否启用二级缓存 enableCache 和是否启用 redis 作为二级缓存 enableRedis
 */
@SuppressWarnings("unused")
public class CodeGeneratorConfig {
    //数据源配置
    private String host = "localhost";
    private String port = "3306";
    private String database;
    //如果不是mysql，需要根据实际修改拼接字符串
    private String dataSourceUrl;
    //如果不是mysql，需要根据实际修改驱动
    private String dataSourceDriver = "com.mysql.jdbc.Driver";
    private String dataSourceUsername = "root";
    private String dataSourcePassword = "root";
    //是否使用mybatis二级缓存,如果要使用redis,这个必须为true
    private Boolean enableCache = false;
    //是否使用redis作为二级缓存
    private Boolean enableRedis = false;
    //代码中的author注释
    private String author = "User";
    //代码生成后是否打开输出文件夹
    private Boolean openGenerateDir = false;
    //文件存在时是否覆盖
    private Boolean fileOverride = false;
    //数据库中表的前缀，可以为null
    private String[] tablePrefix = null;
    //要生成代码的表名
    private String[] tableInclude = {};
    //不生成代码的表名
    private String[] tableExclude = {};
    //代码包名，模块的上一级
    private String packageParent = null;
    //模块名，会在其下生成到controller，mapper，service，entity等包
    private String packageModule = null;
    //redis相关文件所在包名,包括数据源,Cache.java
    private String packageRedis = "redis";
    //shiro相关文件所在包名.
    private String packageShiro = "shiro";
    //shiro的Redis配置文件所在包名
    private String packageShiroRedis = packageShiro + StringPool.DOT + "redis";
    //Config.java配置文件位置,启用redis时会把RedisConfig.java放到这里
    private String packageConfig = "config";
    //Mapper.java位置,多数据源时建议写成 mapper.数据源名
    private String packageMapper = "mapper";
    //Service.java位置,其实现方法在该包下的impl中,多数据源时建议写成 service.数据源名
    private String packageService = "service";
    //实体Bean位置,多数据源时建议写成 entity.数据源名
    private String packageEntity = "entity";
    //Controller.java位置,多数据源时酌情使用 controller.数据源名
    private String packageController = "controller";
    //mapper.xml是否放到resources目录下，否则放到Mapper.java（Dao层）所在目录下
    private Boolean mapperInResource = true;
    //mapper.xml包名,可为null。默认为mapper.xml,即mapper包下的xml包。如果要放到resources目录下，建议给个包名，否则为mapper/xml
    //多数据源时建议写成 mapper包.数据源名
    private String mapperPackage = "mapper";
    //mapper.xml中生成基本的resultMap(通用查询映射结果)，包含所有属性
    private Boolean mapperResultMap = false;
    //mapper.xml中生成基本的sql(通用查询结果列)，包含所有属性
    private Boolean mapperColumnList = false;
    //是否利用lombok以达到不写getter和setter的目的
    private Boolean lombokModel = false;
    //是否生成为rest风格的Controller,否则生成普通Controller
    private Boolean restControllerStyle = true;
    //用户定义模板后缀，.ftl或者.vm  目前只支持freemarker的ftl模板
    private String userTemplateType = "ftl";
    //用户定义模板在resources下的位置, 这些模板用于生成代码,发布后可删除这个目录下的所有文件
    private String userTemplateDir = "templates";
    //用户定义的模板，ftl或者vm。在resources目录下下
    private String userTemplateController = "controller.java";
    //用户定义的redisMapper.xml代码模板,仅启用二级缓存和redis后有效
    private String userTemplateMapperXml = "redisMapper.xml";
    //用户定义的redisMapper.java代码模板,仅启用二级缓存和redis后有效
    private String userTemplateMapperJava = "redisMapper.java";
    //用户定义的redisCache.java代码模板,仅启用二级缓存和redis后有效
    private String userTemplateRedisCache = "redisCache.java";
    //用户定义的redisConfiguration.java代码模板,仅启用二级缓存和redis后有效
    private String userTemplateRedisConfiguration = "redisConfiguration.java";
    //用户定义的redisConfig.java代码模板,,仅启用二级缓存和redis后有效
    private String userTemplateRedisConfig = "redisConfig.java";
    //用户定义的redisProperties.java(redis配置属性)代码模板,仅启用二级缓存和redis后有效
    private String userTemplateRedisProperties = "redisProperties.java";
    //用户定义的redisLettuceProperties.java(lettuce连接池属性)代码模板,仅启用二级缓存和redis后有效
    private String userTemplateRedisLettuceProperties = "redisLettuceProperties.java";
    //用户定义的redisLettucePoolProperties.java(lettuce连接池皮配置属性)代码模板,仅启用二级缓存和redis后有效
    private String userTemplateRedisLettucePoolProperties = "redisLettucePoolProperties.java";
    //用户定义的ShiroConfig.java相关代码模板
    private String userTemplateShiroConfig = "shiroConfig.java";
    //用户定义的ShiroRealm.java相关代码模板
    private String userTemplateShiroRealm = "shiroRealm.java";
    //用户定义的shiro使用redis缓存需要的相关代码模板
    private String userTemplateRedisShiroCache = "redisShiroCache.java";
    private String userTemplateRedisShiroCacheManager = "redisShiroCacheManager.java";
    private String userTemplateRedisShiroConfiguration = "redisShiroConfiguration.java";
    private String userTemplateRedisShiroSessionDAO = "redisShiroSessionDAO.java";
    //自定义的service注入类
    private String userTemplateServiceConfig = "serviceConfig.java";
    //目录分隔符，不需要修改
    private String fileSeparator = File.separator;

    public CodeGeneratorConfig() {
    }

    public CodeGeneratorConfig(String database, String[] tableInclude, String packageParent) {
        this.database = database;
        this.tableInclude = tableInclude;
        this.packageParent = packageParent;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDatabase() {
        if (StringUtils.isEmpty(this.database))
            throw new NullPointerException("请设置数据库!(database)");
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getDataSourceUrl() {
        if (StringUtils.isEmpty(this.dataSourceUrl))
            this.dataSourceUrl = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useUnicode=true&useSSL=false&characterEncoding=utf8";
        return dataSourceUrl;
    }

    public void setDataSourceUrl(String dataSourceUrl) {
        this.dataSourceUrl = dataSourceUrl;
    }

    public String getDataSourceDriver() {
        return dataSourceDriver;
    }

    public void setDataSourceDriver(String dataSourceDriver) {
        this.dataSourceDriver = dataSourceDriver;
    }

    public String getDataSourceUsername() {
        return dataSourceUsername;
    }

    public void setDataSourceUsername(String dataSourceUsername) {
        this.dataSourceUsername = dataSourceUsername;
    }

    public String getDataSourcePassword() {
        return dataSourcePassword;
    }

    public void setDataSourcePassword(String dataSourcePassword) {
        this.dataSourcePassword = dataSourcePassword;
    }

    public Boolean getEnableCache() {
        return enableCache;
    }

    public void setEnableCache(Boolean enableCache) {
//        if (enableCache)
//            System.out.println("请添加mybatis的二级缓存依赖包,如果已经添加,请忽略:\n" +
//                    "           <dependency>\n" +
//                    "               <groupId>org.mybatis.caches</groupId>\n" +
//                    "               <artifactId>mybatis-ehcache</artifactId>\n" +
//                    "               <version>1.1.0</version>\n" +
//                    "           </dependency>");
        this.enableCache = enableCache;
    }

    public Boolean getEnableRedis() {
        return enableRedis;
    }

    public void setEnableRedis(Boolean enableRedis) {
//        if (enableRedis) {
//            System.out.println("请添加springboot的redis依赖包,如果已经添加,请忽略:\n" +
//                    "           <dependency>\n" +
//                    "               <groupId>org.springframework.boot</groupId>\n" +
//                    "               <artifactId>spring-boot-starter-data-redis</artifactId>\n" +
//                    "           </dependency>\n" +
//                    "           <dependency>\n" +
//                    "               <groupId>redis.clients</groupId>\n" +
//                    "               <artifactId>jedis</artifactId>\n" +
//                    "               <version>2.9.0</version>\n" +
//                    "               <type>jar</type>\n" +
//                    "               <scope>compile</scope>\n" +
//                    "           </dependency>");
//        }
        this.enableRedis = enableRedis;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Boolean getOpenGenerateDir() {
        return openGenerateDir;
    }

    public void setOpenGenerateDir(Boolean openGenerateDir) {
        this.openGenerateDir = openGenerateDir;
    }

    public Boolean getFileOverride() {
        return fileOverride;
    }

    public void setFileOverride(Boolean fileOverride) {
        this.fileOverride = fileOverride;
    }

    public String[] getTablePrefix() {
        return tablePrefix;
    }

    public void setTablePrefix(String[] tablePrefix) {
        this.tablePrefix = tablePrefix;
    }

    public String[] getTableInclude() {
        if (tableInclude.length == 0 && tableExclude.length == 0)
            throw new NullPointerException("请设置要生成实体的表名!(tableInclude[])");
        return tableInclude;
    }

    public void setTableInclude(String[] tableInclude) {
        this.tableInclude = tableInclude;
    }

    public String[] getTableExclude() {
        if (tableInclude.length == 0 && tableExclude.length == 0)
            throw new NullPointerException("请设置不生成实体的表名!(tableExclude[])");
        return tableExclude;
    }

    public void setTableExclude(String[] tableExclude) {
        this.tableExclude = tableExclude;
    }

    public String getPackageParent() {
        if (StringUtils.isEmpty(this.packageParent)) {
            throw new NullPointerException("请设置包名建议设置到模块层级,例如com.apache.tomcat.controller中的com.apache.tomcat!(packageParent)");
        }
        return packageParent;
    }

    public void setPackageParent(String packageParent) {
        this.packageParent = packageParent;
    }

    public String getPackageModule() {
        return packageModule;
    }

    public void setPackageModule(String packageModule) {
        this.packageModule = packageModule;
    }

    public String getPackageRedis() {
        return packageRedis;
    }

    public void setPackageRedis(String packageRedis) {
        this.packageRedis = packageRedis;
    }

    public String getPackageConfig() {
        return packageConfig;
    }

    public void setPackageConfig(String packageConfig) {
        this.packageConfig = packageConfig;
    }

    public String getPackageMapper() {
        return packageMapper;
    }

    public void setPackageMapper(String packageMapper) {
        this.packageMapper = packageMapper;
    }

    public String getPackageService() {
        return packageService;
    }

    public void setPackageService(String packageService) {
        this.packageService = packageService;
    }

    public String getPackageEntity() {
        return packageEntity;
    }

    public void setPackageEntity(String packageEntity) {
        this.packageEntity = packageEntity;
    }

    public String getPackageController() {
        return packageController;
    }

    public void setPackageController(String packageController) {
        this.packageController = packageController;
    }

    public Boolean getMapperInResource() {
        return mapperInResource;
    }

    public void setMapperInResource(Boolean mapperInResource) {
        this.mapperInResource = mapperInResource;
    }

    public String getMapperPackage() {
        return mapperPackage;
    }

    public void setMapperPackage(String mapperPackage) {
        this.mapperPackage = mapperPackage;
    }

    public Boolean getMapperResultMap() {
        return mapperResultMap;
    }

    public void setMapperResultMap(Boolean mapperResultMap) {
        this.mapperResultMap = mapperResultMap;
    }

    public Boolean getMapperColumnList() {
        return mapperColumnList;
    }

    public void setMapperColumnList(Boolean mapperColumnList) {
        this.mapperColumnList = mapperColumnList;
    }

    public Boolean getLombokModel() {
        return lombokModel;
    }

    public void setLombokModel(Boolean lombokModel) {
        this.lombokModel = lombokModel;
    }

    public Boolean getRestControllerStyle() {
        return restControllerStyle;
    }

    public void setRestControllerStyle(Boolean restControllerStyle) {
        this.restControllerStyle = restControllerStyle;
    }

    public String getUserTemplateType() {
        return userTemplateType;
    }

    public void setUserTemplateType(String userTemplateType) {
        this.userTemplateType = userTemplateType;
        this.userTemplateType = "ftl";
    }

    public String getUserTemplateDir() {
        return userTemplateDir;
    }

    public void setUserTemplateDir(String userTemplateDir) {
        this.userTemplateDir = userTemplateDir;
    }

    public String getUserTemplateController() {
        return userTemplateController;
    }

    public void setUserTemplateController(String userTemplateController) {
        this.userTemplateController = userTemplateController;
    }

    public String getUserTemplateMapperXml() {
        return userTemplateMapperXml;
    }

    public void setUserTemplateMapperXml(String userTemplateMapperXml) {
        this.userTemplateMapperXml = userTemplateMapperXml;
    }

    public String getUserTemplateMapperJava() {
        return userTemplateMapperJava;
    }

    public void setUserTemplateMapperJava(String userTemplateMapperJava) {
        this.userTemplateMapperJava = userTemplateMapperJava;
    }

    public String getUserTemplateRedisCache() {
        return userTemplateRedisCache;
    }

    public void setUserTemplateRedisCache(String userTemplateRedisCache) {
        this.userTemplateRedisCache = userTemplateRedisCache;
    }

    public String getUserTemplateRedisConfiguration() {
        return userTemplateRedisConfiguration;
    }

    public void setUserTemplateRedisConfiguration(String userTemplateRedisConfiguration) {
        this.userTemplateRedisConfiguration = userTemplateRedisConfiguration;
    }

    public String getUserTemplateRedisConfig() {
        return userTemplateRedisConfig;
    }

    public void setUserTemplateRedisConfig(String userTemplateRedisConfig) {
        this.userTemplateRedisConfig = userTemplateRedisConfig;
    }

    public String getUserTemplateRedisProperties() {
        return userTemplateRedisProperties;
    }

    public void setUserTemplateRedisProperties(String userTemplateRedisProperties) {
        this.userTemplateRedisProperties = userTemplateRedisProperties;
    }

    public String getUserTemplateRedisLettuceProperties() {
        return userTemplateRedisLettuceProperties;
    }

    public void setUserTemplateRedisLettuceProperties(String userTemplateRedisLettuceProperties) {
        this.userTemplateRedisLettuceProperties = userTemplateRedisLettuceProperties;
    }

    public String getUserTemplateRedisLettucePoolProperties() {
        return userTemplateRedisLettucePoolProperties;
    }

    public void setUserTemplateRedisLettucePoolProperties(String userTemplateRedisLettucePoolProperties) {
        this.userTemplateRedisLettucePoolProperties = userTemplateRedisLettucePoolProperties;
    }

    public String getFileSeparator() {
        return fileSeparator;
    }

    public void setFileSeparator(String fileSeparator) {
        this.fileSeparator = fileSeparator;
    }

    public String getPackageShiro() {
        return packageShiro;
    }

    public void setPackageShiro(String packageShiro) {
        this.packageShiro = packageShiro;
    }

    public String getUserTemplateRedisShiroCache() {
        return userTemplateRedisShiroCache;
    }

    public void setUserTemplateRedisShiroCache(String userTemplateRedisShiroCache) {
        this.userTemplateRedisShiroCache = userTemplateRedisShiroCache;
    }

    public String getUserTemplateRedisShiroCacheManager() {
        return userTemplateRedisShiroCacheManager;
    }

    public void setUserTemplateRedisShiroCacheManager(String userTemplateRedisShiroCacheManager) {
        this.userTemplateRedisShiroCacheManager = userTemplateRedisShiroCacheManager;
    }

    public String getUserTemplateRedisShiroConfiguration() {
        return userTemplateRedisShiroConfiguration;
    }

    public void setUserTemplateRedisShiroConfiguration(String userTemplateRedisShiroConfiguration) {
        this.userTemplateRedisShiroConfiguration = userTemplateRedisShiroConfiguration;
    }

    public String getUserTemplateRedisShiroSessionDAO() {
        return userTemplateRedisShiroSessionDAO;
    }

    public void setUserTemplateRedisShiroSessionDAO(String userTemplateRedisShiroSessionDAO) {
        this.userTemplateRedisShiroSessionDAO = userTemplateRedisShiroSessionDAO;
    }

    public String getPackageShiroRedis() {
        return packageShiroRedis;
    }

    public void setPackageShiroRedis(String packageShiroRedis) {
        this.packageShiroRedis = packageShiroRedis;
    }

    public String getUserTemplateShiroConfig() {
        return userTemplateShiroConfig;
    }

    public void setUserTemplateShiroConfig(String userTemplateShiroConfig) {
        this.userTemplateShiroConfig = userTemplateShiroConfig;
    }

    public String getUserTemplateShiroRealm() {
        return userTemplateShiroRealm;
    }

    public void setUserTemplateShiroRealm(String userTemplateShiroRealm) {
        this.userTemplateShiroRealm = userTemplateShiroRealm;
    }

    public String getUserTemplateServiceConfig() {
        return userTemplateServiceConfig;
    }

    public void setUserTemplateServiceConfig(String userTemplateServiceConfig) {
        this.userTemplateServiceConfig = userTemplateServiceConfig;
    }
}
