# code-generator
maven引用,如果下载失败,可能是maven库还没同步,需要耐心等待同步后在使用.在此之前可先使用1.1.11
```xml
    <dependency>
        <groupId>com.github.yhl452493373</groupId>
        <artifactId>code-generator</artifactId>
        <version>1.2.0</version>
    </dependency>
```
### 如果要使用mybatis-plus的枚举转换功能，请在生成的DataSourceConfig.java中的sqlSessionFactory方法新增一行sessionFactory.setTypeEnumsPackage("枚举所在包");
---
使用代码:
## 注意:从1.1.11开始,DataSourceGeneratorConfig和CodeGeneratorConfig的set方法均支持链式写法(兼容原来的写法),并且CodeGeneratorConfig增加了时区设置,增加了几个构造方法.CodeGeneratorConfig构造方法中,交换了packageParent和tableInclude的位置.具体增加的构造方法,请参考[CodeGeneratorConfig源码](src/main/java/com/github/yhl452493373/generator/CodeGeneratorConfig.java)
## 注意:从1.2.0开始,redis更改redis缓存为Mybatis Plus官方推荐方式:@Cacheable,@CacheEvict等自行管理.具体用法参考:[https://blog.csdn.net/dreamhai/article/details/80642010](https://blog.csdn.net/dreamhai/article/details/80642010)
### 从1.1.11升级到1.2.0,如果采用了redis,则需要删除RedisConfiguration.java, RedisCache.java文件。
### RedisConfig.java中,删除以下两行：
```java
    RedisCache.setRedisTemplate(redisTemplate);
    RedisCache.setRedisConfiguration(new RedisConfiguration());
```
### 同时删除以前生成的Mapper.xml中
```xml
    <cache-ref namespace="xxxxMapper" />
```
### 删除Mapper.java中
```java
    @CacheNamespace(implementation= RedisCache.class,eviction= RedisCache.class)
```
### 同时在ServiceImpl中增加
```java
    @CacheConfig(cacheNames = "缓存命名空间(只能为英文)")
```
### 之后,在ServiceImpl中需要缓存的方法上添加 
```java
    @Cacheable(key = "'id_'+#id")//缓存数据
    @CacheEvict(condition = "#id!=null", allEntries = true)//清除缓存
```
### 同时,如果页面上有展示日期的,如果没用 timeObject?string("日期格式") 来格式化日期,则需要写成 timeObject?date 或 timeObject? time 或 timeObject? datetime

### 代码生成功能使用代码如下：
```java
    //单数据源
    private static void singleDataSource() {
        DataSourceGeneratorConfig dsgc = new DataSourceGeneratorConfig();
        dsgc.setFileOverride(true);
        dsgc.setCacheEnabled(true);
        CodeGenerator.dataSourceCodeGenerate(dsgc);

        CodeGeneratorConfig cgc = new CodeGeneratorConfig(
                "psm", "com.yang.demo", new String[]{"employee"}
        ).setFileOverride(true).setEnableCache(true).setEnableRedis(true);
        CodeGenerator.baseCodeGenerate(cgc);
    }

    //多数据源
    private static void multipleDataSource() {
        DataSourceGeneratorConfig dsgc = new DataSourceGeneratorConfig();
        dsgc.setMultiple(true).setFileOverride(true).setCacheEnabled(true);
        CodeGenerator.dataSourceCodeGenerate(dsgc);

        //储存每个数据源需要生成的表名
        Map<String, String[]> dataSourceMap = new HashMap<>();
        dataSourceMap.put("psm", new String[]{
                "employee", "hidden_danger"
        });
        dataSourceMap.put("ime511", new String[]{
                "sys_user", "sys_areas"
        });

        dataSourceMap.keySet().forEach(key -> {
            CodeGeneratorConfig cgc = new CodeGeneratorConfig(
                    key, "com.yang.demo", dataSourceMap.get(key)
            ).setFileOverride(true).setEnableCache(true).setEnableRedis(true);

            //psm数据源的包配置
            cgc.setPackageController(cgc.getPackageController() + StringPool.DOT + key);
            cgc.setPackageEntity(cgc.getPackageEntity() + StringPool.DOT + key);
            cgc.setPackageMapper(cgc.getPackageMapper() + StringPool.DOT + key);
            cgc.setPackageService(cgc.getPackageService() + StringPool.DOT + key);
            //默认情况下,mapper.xml文件放在resources目录
            cgc.setMapperPackage(cgc.getMapperPackage() + StringPool.DOT + key);
            //将xml放到mapper.java下的xml目录下的psm文件夹.替换上面一句即可
    //        cgc.setMapperInResource(false);
    //        cgc.setMapperPackage("mapper.xml." + key);
            CodeGenerator.baseCodeGenerate(cgc);
        });
    }
```
### DataSourceGeneratorConfig有以下几个属性,均可通过set设置,其他属性建议不更改
```java
    //全局地开启或关闭配置文件中的所有映射器已经配置的任何缓存:true - 启用,false - 不启用.作用同mybatis plus的cacheEnabled
    private Boolean cacheEnabled = false;
    //是否为多数据源:true - 是,使用multipleYmlFile对应文件解析,false - 否,使用singleYmlFile解析
    private Boolean multiple = false;    
    //单数据源配置文件
    private String singleYmlFile = "application-single-datasource.yml";
    //多数据源配置文件
    private String multipleYmlFile = "application-multiple-datasource.yml";
    //生成数据源配置的模板名称,有需要可自行修改
    private String templateDatasource = "templates/datasource.java.ftl";
    //文件存在时是否覆盖.
    private Boolean fileOverride = false;
```
**fileOverride - 属性请一定注意.此属性建议项目刚开始时使用true,之后要补充新表相关mapper,service等的时候改为false,避免之前的代码被覆盖
###### 单数据源application-single-datasource.yml配置参考
```yaml
    #单数据源，与多数据源列表二选一。如果既设置了单数据源，又设置了多数据源，则以多数据源为准
    #如果要启用,则在application.yml中spring.profiles.active添加一个single-datasource列表对象
    datasource:
      #存放Config.java的配置文件的包
      config-package: com.yang.demo.config
      #此属性表示单数据源属性
      datasource-properties:
        #数据库连接地址
        url: jdbc:mysql://localhost:3306/psm?useUnicode=true&useSSL=false&characterEncoding=utf8
        #用户名
        username: root
        #密码
        password: root
        driver-class-name: com.mysql.jdbc.Driver
        #存放对应实体的包
        type-aliases-package: com.yang.demo.entity
        #存放对应mapper.java的包
        mapper-package: com.yang.demo.mapper
        #存放对应mapper.xml的包
        mapper-locations: classpath:mybatis/mapper
```
###### 多数据源application-multiple-datasource.yml配置参考
```yaml
    #多数据源列表，与单数据源二选一。如果既设置了单数据源，又设置了多数据源，则以多数据源为准
    #如果要启用,则在application.yml中spring.profiles.active添加一个multiple-datasource列表对象
    datasource:
      #存放Config.java的配置文件的包
      config-package: com.yang.demo.config
      #此属性表示多数据源属性,yaml中的列表写法
      datasource-properties-list:
      #数据源名称,必填
      - name: psm
      #是否主数据源,必须有一个为true
        primary: true
      #以下属性参考单数据源  
        url: jdbc:mysql://localhost:3306/psm?useUnicode=true&useSSL=false&characterEncoding=utf8
        username: root
        password: root
        driver-class-name: com.mysql.jdbc.Driver
        type-aliases-package: com.yang.demo.entity
        mapper-package: com.yang.demo.mapper
        mapper-locations: classpath:mybatis/mapper
      - name: ime511
        url: jdbc:mysql://localhost:3306/ime511?useUnicode=true&useSSL=false&characterEncoding=utf8
        username: root
        password: root
        driver-class-name: com.mysql.jdbc.Driver
        type-aliases-package: com.yang.demo.entity
        mapper-package: com.yang.demo.mapper
        mapper-locations: classpath:mybatis/mapper
```
### CodeGeneratorConfig有以下一些属性,均通过set设置,其他属性不建议修改。具体可以参考[CodeGeneratorConfig源码](src/main/java/com/github/yhl452493373/generator/CodeGeneratorConfig.java)
```java
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
        //以下属性,如无特殊需求,不建议修改
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
```
