package com.github.yhl452493373.generator;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Mybatis Plus代码生成器的配置封装.
 * 此封装仅针对mysql数据源.其他数据源请根据情况修改链接地址拼接方式或手动拼接
 * 这里配置是否启用二级缓存 enableCache 和是否启用 redis 作为二级缓存 enableRedis
 */
@SuppressWarnings({"unused", "SpellCheckingInspection"})
public class CodeGeneratorConfig {
    //数据源配置
    private String host = "localhost";
    private String port = "3306";
    private String database;
    //时区设置,mysql6以上使用
    private String serverTimezone;
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
    //用户定义的redisServiceImpl.java代码模板,仅启用二级缓存和redis后有效
    private String userTemplateServiceImplJava = "redisServiceImpl.java";
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
    //实体基类上的字段
    private String[] superEntityColumns;
    //实体基类
    private String superEntityClass;
    //controller基类
    private String superControllerClass;
    //mapper基类,会覆盖自动生成的mapper上的BaseMapper
    private String superMapperClass = "com.baomidou.mybatisplus.core.mapper.BaseMapper";
    //service基类,会覆盖自动生成的service上的IService
    private String superServiceClass = "com.baomidou.mybatisplus.extension.service.IService";
    //serviceImpl基类,会覆盖自动生成的SeriviceImpl上的ServiceImpl
    private String superServiceImplClass = "com.baomidou.mybatisplus.extension.service.impl.ServiceImpl";

    public CodeGeneratorConfig() {
    }

    public CodeGeneratorConfig(String host, String port, String database, String dataSourceUsername, String dataSourcePassword) {
        this(host, port, database, dataSourceUsername, dataSourcePassword, null, null, null);
    }

    public CodeGeneratorConfig(String host, String port, String database, String dataSourceUsername, String dataSourcePassword, String serverTimezone) {
        this(host, port, database, dataSourceUsername, dataSourcePassword, serverTimezone, null, null);
    }

    public CodeGeneratorConfig(String database, String packageParent, String[] tableInclude) {
        this(null, null, database, null, packageParent, tableInclude);
    }

    public CodeGeneratorConfig(String host, String port, String database, String packageParent, String[] tableInclude) {
        this(host, port, database, null, packageParent, tableInclude);
    }

    public CodeGeneratorConfig(String host, String port, String database, String serverTimezone, String packageParent, String[] tableInclude) {
        this(host, port, database, null, null, serverTimezone, packageParent, tableInclude);
    }

    public CodeGeneratorConfig(String host, String port, String database, String dataSourceUsername, String dataSourcePassword, String serverTimezone, String packageParent, String[] tableInclude) {
        if (StringUtils.isNotEmpty(host))
            this.host = host;
        if (StringUtils.isNotEmpty(port))
            this.port = port;
        if (StringUtils.isNotEmpty(database))
            this.database = database;
        if (StringUtils.isNotEmpty(dataSourceUsername))
            this.dataSourceUsername = dataSourceUsername;
        if (StringUtils.isNotEmpty(dataSourcePassword))
            this.dataSourcePassword = dataSourcePassword;
        if (StringUtils.isNotEmpty(serverTimezone))
            this.serverTimezone = serverTimezone;
        if (StringUtils.isNotEmpty(packageParent))
            this.packageParent = packageParent;
        if (tableInclude != null && tableInclude.length > 0)
            this.tableInclude = tableInclude;
    }

    public String getHost() {
        return host;
    }

    public CodeGeneratorConfig setHost(String host) {
        this.host = host;
        return this;
    }

    public String getPort() {
        return port;
    }

    public CodeGeneratorConfig setPort(String port) {
        this.port = port;
        return this;
    }

    public String getDatabase() {
        if (StringUtils.isEmpty(this.database))
            throw new NullPointerException("请设置数据库!(database)");
        return database;
    }

    public CodeGeneratorConfig setDatabase(String database) {
        this.database = database;
        return this;
    }

    public String getServerTimezone() {
        return serverTimezone;
    }

    public CodeGeneratorConfig setServerTimezone(String serverTimezone) {
        this.serverTimezone = serverTimezone;
        return this;
    }

    public String getDataSourceUrl() {
        if (StringUtils.isEmpty(this.dataSourceUrl))
            this.dataSourceUrl = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useUnicode=true&useSSL=false&characterEncoding=utf8";
        if (!StringUtils.isEmpty(this.serverTimezone) && !this.dataSourceUrl.contains("serverTimezone=")) {
            try {
                this.dataSourceUrl += "serverTimezone=" + URLEncoder.encode(this.serverTimezone, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return dataSourceUrl;
    }

    public CodeGeneratorConfig setDataSourceUrl(String dataSourceUrl) {
        this.dataSourceUrl = dataSourceUrl;
        return this;
    }

    public String getDataSourceDriver() {
        return dataSourceDriver;
    }

    public CodeGeneratorConfig setDataSourceDriver(String dataSourceDriver) {
        this.dataSourceDriver = dataSourceDriver;
        return this;
    }

    public String getDataSourceUsername() {
        return dataSourceUsername;
    }

    public CodeGeneratorConfig setDataSourceUsername(String dataSourceUsername) {
        this.dataSourceUsername = dataSourceUsername;
        return this;
    }

    public String getDataSourcePassword() {
        return dataSourcePassword;
    }

    public CodeGeneratorConfig setDataSourcePassword(String dataSourcePassword) {
        this.dataSourcePassword = dataSourcePassword;
        return this;
    }

    public Boolean getEnableCache() {
        return enableCache;
    }

    public CodeGeneratorConfig setEnableCache(Boolean enableCache) {
//        if (enableCache)
//            System.out.println("请添加mybatis的二级缓存依赖包,如果已经添加,请忽略:\n" +
//                    "           <dependency>\n" +
//                    "               <groupId>org.mybatis.caches</groupId>\n" +
//                    "               <artifactId>mybatis-ehcache</artifactId>\n" +
//                    "               <version>1.1.0</version>\n" +
//                    "           </dependency>");
        this.enableCache = enableCache;
        return this;
    }

    public Boolean getEnableRedis() {
        return enableRedis;
    }

    public CodeGeneratorConfig setEnableRedis(Boolean enableRedis) {
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
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public CodeGeneratorConfig setAuthor(String author) {
        this.author = author;
        return this;
    }

    public Boolean getOpenGenerateDir() {
        return openGenerateDir;
    }

    public CodeGeneratorConfig setOpenGenerateDir(Boolean openGenerateDir) {
        this.openGenerateDir = openGenerateDir;
        return this;
    }

    public Boolean getFileOverride() {
        return fileOverride;
    }

    public CodeGeneratorConfig setFileOverride(Boolean fileOverride) {
        this.fileOverride = fileOverride;
        return this;
    }

    public String[] getTablePrefix() {
        return tablePrefix;
    }

    public CodeGeneratorConfig setTablePrefix(String[] tablePrefix) {
        this.tablePrefix = tablePrefix;
        return this;
    }

    public String[] getTableInclude() {
        if (tableInclude.length == 0 && tableExclude.length == 0)
            throw new NullPointerException("请设置要生成实体的表名!(tableInclude[])");
        return tableInclude;
    }

    public CodeGeneratorConfig setTableInclude(String[] tableInclude) {
        this.tableInclude = tableInclude;
        return this;
    }

    public String[] getTableExclude() {
        if (tableInclude.length == 0 && tableExclude.length == 0)
            throw new NullPointerException("请设置不生成实体的表名!(tableExclude[])");
        return tableExclude;
    }

    public CodeGeneratorConfig setTableExclude(String[] tableExclude) {
        this.tableExclude = tableExclude;
        return this;
    }

    public String getPackageParent() {
        if (StringUtils.isEmpty(this.packageParent)) {
            throw new NullPointerException("请设置包名建议设置到模块层级,例如com.apache.tomcat.controller中的com.apache.tomcat!(packageParent)");
        }
        return packageParent;
    }

    public CodeGeneratorConfig setPackageParent(String packageParent) {
        this.packageParent = packageParent;
        return this;
    }

    public String getPackageModule() {
        return packageModule;
    }

    public CodeGeneratorConfig setPackageModule(String packageModule) {
        this.packageModule = packageModule;
        return this;
    }

    public String getPackageRedis() {
        return packageRedis;
    }

    public CodeGeneratorConfig setPackageRedis(String packageRedis) {
        this.packageRedis = packageRedis;
        return this;
    }

    public String getPackageConfig() {
        return packageConfig;
    }

    public CodeGeneratorConfig setPackageConfig(String packageConfig) {
        this.packageConfig = packageConfig;
        return this;
    }

    public String getPackageMapper() {
        return packageMapper;
    }

    public CodeGeneratorConfig setPackageMapper(String packageMapper) {
        this.packageMapper = packageMapper;
        return this;
    }

    public String getPackageService() {
        return packageService;
    }

    public CodeGeneratorConfig setPackageService(String packageService) {
        this.packageService = packageService;
        return this;
    }

    public String getPackageEntity() {
        return packageEntity;
    }

    public CodeGeneratorConfig setPackageEntity(String packageEntity) {
        this.packageEntity = packageEntity;
        return this;
    }

    public String getPackageController() {
        return packageController;
    }

    public CodeGeneratorConfig setPackageController(String packageController) {
        this.packageController = packageController;
        return this;
    }

    public Boolean getMapperInResource() {
        return mapperInResource;
    }

    public CodeGeneratorConfig setMapperInResource(Boolean mapperInResource) {
        this.mapperInResource = mapperInResource;
        return this;
    }

    public String getMapperPackage() {
        return mapperPackage;
    }

    public CodeGeneratorConfig setMapperPackage(String mapperPackage) {
        this.mapperPackage = mapperPackage;
        return this;
    }

    public Boolean getMapperResultMap() {
        return mapperResultMap;
    }

    public CodeGeneratorConfig setMapperResultMap(Boolean mapperResultMap) {
        this.mapperResultMap = mapperResultMap;
        return this;
    }

    public Boolean getMapperColumnList() {
        return mapperColumnList;
    }

    public CodeGeneratorConfig setMapperColumnList(Boolean mapperColumnList) {
        this.mapperColumnList = mapperColumnList;
        return this;
    }

    public Boolean getLombokModel() {
        return lombokModel;
    }

    public CodeGeneratorConfig setLombokModel(Boolean lombokModel) {
        this.lombokModel = lombokModel;
        return this;
    }

    public Boolean getRestControllerStyle() {
        return restControllerStyle;
    }

    public CodeGeneratorConfig setRestControllerStyle(Boolean restControllerStyle) {
        this.restControllerStyle = restControllerStyle;
        return this;
    }

    public String getUserTemplateType() {
        return userTemplateType;
    }

    public CodeGeneratorConfig setUserTemplateType(String userTemplateType) {
        this.userTemplateType = userTemplateType;
        this.userTemplateType = "ftl";
        return this;
    }

    public String getUserTemplateDir() {
        return userTemplateDir;
    }

    public CodeGeneratorConfig setUserTemplateDir(String userTemplateDir) {
        this.userTemplateDir = userTemplateDir;
        return this;
    }

    public String getUserTemplateController() {
        return userTemplateController;
    }

    public CodeGeneratorConfig setUserTemplateController(String userTemplateController) {
        this.userTemplateController = userTemplateController;
        return this;
    }

    public String getUserTemplateMapperXml() {
        return userTemplateMapperXml;
    }

    public CodeGeneratorConfig setUserTemplateMapperXml(String userTemplateMapperXml) {
        this.userTemplateMapperXml = userTemplateMapperXml;
        return this;
    }

    public String getUserTemplateServiceImplJava() {
        return userTemplateServiceImplJava;
    }

    public CodeGeneratorConfig setUserTemplateServiceImplJava(String userTemplateServiceImplJava) {
        this.userTemplateServiceImplJava = userTemplateServiceImplJava;
        return this;
    }

    public String getUserTemplateRedisConfig() {
        return userTemplateRedisConfig;
    }

    public CodeGeneratorConfig setUserTemplateRedisConfig(String userTemplateRedisConfig) {
        this.userTemplateRedisConfig = userTemplateRedisConfig;
        return this;
    }

    public String getUserTemplateRedisProperties() {
        return userTemplateRedisProperties;
    }

    public CodeGeneratorConfig setUserTemplateRedisProperties(String userTemplateRedisProperties) {
        this.userTemplateRedisProperties = userTemplateRedisProperties;
        return this;
    }

    public String getUserTemplateRedisLettuceProperties() {
        return userTemplateRedisLettuceProperties;
    }

    public CodeGeneratorConfig setUserTemplateRedisLettuceProperties(String userTemplateRedisLettuceProperties) {
        this.userTemplateRedisLettuceProperties = userTemplateRedisLettuceProperties;
        return this;
    }

    public String getUserTemplateRedisLettucePoolProperties() {
        return userTemplateRedisLettucePoolProperties;
    }

    public CodeGeneratorConfig setUserTemplateRedisLettucePoolProperties(String userTemplateRedisLettucePoolProperties) {
        this.userTemplateRedisLettucePoolProperties = userTemplateRedisLettucePoolProperties;
        return this;
    }

    public String getFileSeparator() {
        return fileSeparator;
    }

    public CodeGeneratorConfig setFileSeparator(String fileSeparator) {
        this.fileSeparator = fileSeparator;
        return this;
    }

    public String getPackageShiro() {
        return packageShiro;
    }

    public CodeGeneratorConfig setPackageShiro(String packageShiro) {
        this.packageShiro = packageShiro;
        return this;
    }

    public String getUserTemplateRedisShiroCache() {
        return userTemplateRedisShiroCache;
    }

    public CodeGeneratorConfig setUserTemplateRedisShiroCache(String userTemplateRedisShiroCache) {
        this.userTemplateRedisShiroCache = userTemplateRedisShiroCache;
        return this;
    }

    public String getUserTemplateRedisShiroCacheManager() {
        return userTemplateRedisShiroCacheManager;
    }

    public CodeGeneratorConfig setUserTemplateRedisShiroCacheManager(String userTemplateRedisShiroCacheManager) {
        this.userTemplateRedisShiroCacheManager = userTemplateRedisShiroCacheManager;
        return this;
    }

    public String getUserTemplateRedisShiroConfiguration() {
        return userTemplateRedisShiroConfiguration;
    }

    public CodeGeneratorConfig setUserTemplateRedisShiroConfiguration(String userTemplateRedisShiroConfiguration) {
        this.userTemplateRedisShiroConfiguration = userTemplateRedisShiroConfiguration;
        return this;
    }

    public String getUserTemplateRedisShiroSessionDAO() {
        return userTemplateRedisShiroSessionDAO;
    }

    public CodeGeneratorConfig setUserTemplateRedisShiroSessionDAO(String userTemplateRedisShiroSessionDAO) {
        this.userTemplateRedisShiroSessionDAO = userTemplateRedisShiroSessionDAO;
        return this;
    }

    public String getPackageShiroRedis() {
        return packageShiroRedis;
    }

    public CodeGeneratorConfig setPackageShiroRedis(String packageShiroRedis) {
        this.packageShiroRedis = packageShiroRedis;
        return this;
    }

    public String getUserTemplateShiroConfig() {
        return userTemplateShiroConfig;
    }

    public CodeGeneratorConfig setUserTemplateShiroConfig(String userTemplateShiroConfig) {
        this.userTemplateShiroConfig = userTemplateShiroConfig;
        return this;
    }

    public String getUserTemplateShiroRealm() {
        return userTemplateShiroRealm;
    }

    public CodeGeneratorConfig setUserTemplateShiroRealm(String userTemplateShiroRealm) {
        this.userTemplateShiroRealm = userTemplateShiroRealm;
        return this;
    }

    public String getUserTemplateServiceConfig() {
        return userTemplateServiceConfig;
    }

    public CodeGeneratorConfig setUserTemplateServiceConfig(String userTemplateServiceConfig) {
        this.userTemplateServiceConfig = userTemplateServiceConfig;
        return this;
    }

    public String[] getSuperEntityColumns() {
        return superEntityColumns;
    }

    public CodeGeneratorConfig setSuperEntityColumns(String... superEntityColumns) {
        this.superEntityColumns = superEntityColumns;
        return this;
    }

    public String getSuperEntityClass() {
        return superEntityClass;
    }

    public CodeGeneratorConfig setSuperEntityClass(String superEntityClass) {
        this.superEntityClass = superEntityClass;
        return this;
    }

    public String getSuperControllerClass() {
        return superControllerClass;
    }

    public CodeGeneratorConfig setSuperControllerClass(String superControllerClass) {
        this.superControllerClass = superControllerClass;
        return this;
    }

    public String getSuperMapperClass() {
        return superMapperClass;
    }

    public CodeGeneratorConfig setSuperMapperClass(String superMapperClass) {
        this.superMapperClass = superMapperClass;
        return this;
    }

    public String getSuperServiceClass() {
        return superServiceClass;
    }

    public CodeGeneratorConfig setSuperServiceClass(String superServiceClass) {
        this.superServiceClass = superServiceClass;
        return this;
    }

    public String getSuperServiceImplClass() {
        return superServiceImplClass;
    }

    public CodeGeneratorConfig setSuperServiceImplClass(String superServiceImplClass) {
        this.superServiceImplClass = superServiceImplClass;
        return this;
    }
}
