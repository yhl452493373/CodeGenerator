package com.github.yhl452493373.generator;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import com.github.yhl452493373.bean.DataSourceGroup;
import com.github.yhl452493373.bean.DataSourceProperties;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Mybatis代码生成器
 */
@SuppressWarnings({"FieldCanBeLocal", "Duplicates", "unused", "UnusedReturnValue", "WeakerAccess"})
public class CodeGenerator {
    private static Logger logger = LoggerFactory.getLogger(CodeGenerator.class);

    /**
     * 基本代码生成,多数据源则调用多次
     *
     * @param cgc 代码生成配置
     */
    public static void baseCodeGenerate(CodeGeneratorConfig cgc) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setEnableCache(cgc.getEnableCache());
        gc.setFileOverride(cgc.getFileOverride());
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor(cgc.getAuthor());
        gc.setOpen(cgc.getOpenGenerateDir());
        gc.setServiceName("%sService");
        gc.setBaseColumnList(cgc.getMapperColumnList());
        gc.setBaseResultMap(cgc.getMapperResultMap());
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(cgc.getDataSourceUrl());
        dsc.setDriverName(cgc.getDataSourceDriver());
        dsc.setUsername(cgc.getDataSourceUsername());
        dsc.setPassword(cgc.getDataSourcePassword());
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent(cgc.getPackageParent());
        if (StringUtils.isNotEmpty(cgc.getMapperPackage()))
            pc.setXml(cgc.getMapperPackage());
        if (StringUtils.isNotEmpty(cgc.getPackageModule()))
            pc.setModuleName(cgc.getPackageModule());
        if (StringUtils.isNotEmpty(cgc.getPackageController()))
            pc.setController(cgc.getPackageController());
        if (StringUtils.isNotEmpty(cgc.getPackageEntity()))
            pc.setEntity(cgc.getPackageEntity());
        if (StringUtils.isNotEmpty(cgc.getPackageMapper()))
            pc.setMapper(cgc.getPackageMapper());
        if (StringUtils.isNotEmpty(cgc.getPackageService())) {
            pc.setService(cgc.getPackageService());
            pc.setServiceImpl(cgc.getPackageService() + StringPool.DOT + "impl");
        }
        mpg.setPackageInfo(pc);

        //根据自定义的Controller模板设置模板引擎

        if (cgc.getUserTemplateType().endsWith("ftl"))
            mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        else
            mpg.setTemplateEngine(new VelocityTemplateEngine());

        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<>();
                //模块级包名,例如com.apache.tomcat.controller的com.apache.tomcat级
                map.put("packageParent", pc.getParent());
                //redis包名
                map.put("packageRedis", pc.getParent() + StringPool.DOT + cgc.getPackageRedis());
                //是否启用redis
                map.put("enableRedis", cgc.getEnableRedis());
                //Config.java所在包名
                map.put("packageConfig", pc.getParent() + StringPool.DOT + cgc.getPackageConfig());
                //Shiro相关java所在包名,比如realm
                map.put("packageShiro", pc.getParent() + StringPool.DOT + cgc.getPackageShiro());
                //Shiro的redis相关java所在包名
                map.put("packageShiroRedis", pc.getParent() + StringPool.DOT + cgc.getPackageShiroRedis());
                this.setMap(map);
            }
        };

        // 自定义配置
        TemplateConfig tc = new TemplateConfig();
        List<FileOutConfig> focList = new ArrayList<>();
        if (cgc.getEnableCache() && cgc.getEnableRedis()) {
            gc.setEnableCache(true);
            //Mapper.xml模板和输出配置
            focList.add(new FileOutConfig(cgc.getUserTemplateDir() + "/" + cgc.getUserTemplateMapperXml() + "." + cgc.getUserTemplateType()) {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    // 自定义输出文件名称
                    if (cgc.getMapperInResource()) {
                        return projectPath + "/src/main/resources/".replace("/", cgc.getFileSeparator())
                                + pc.getXml().replace(".", cgc.getFileSeparator())
                                + cgc.getFileSeparator() + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
                    } else {
                        return projectPath + "/src/main/java".replace("/", cgc.getFileSeparator()) +
                                (StringUtils.isEmpty(pc.getParent()) ?
                                        "" : cgc.getFileSeparator() + pc.getParent().replace(".", cgc.getFileSeparator())
                                ) +
                                cgc.getFileSeparator() + pc.getXml().replace(".", cgc.getFileSeparator()) +
                                cgc.getFileSeparator() + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
                    }
                }
            });
            tc.setXml(null);

            //Mapper.java模板和输出配置
            focList.add(new FileOutConfig(cgc.getUserTemplateDir() + "/" + cgc.getUserTemplateMapperJava() + "." + cgc.getUserTemplateType()) {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    // 自定义输出文件名称
                    return projectPath + "/src/main/java".replace("/", cgc.getFileSeparator()) +
                            (StringUtils.isEmpty(pc.getParent()) ?
                                    "" : cgc.getFileSeparator() + pc.getParent().replace(".", cgc.getFileSeparator())
                            ) +
                            cgc.getFileSeparator() + pc.getMapper().replace(".", cgc.getFileSeparator()) +
                            cgc.getFileSeparator() + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_JAVA;
                }
            });
            tc.setMapper(null);

            //RedisConfiguration.java模板和输出配置
            focList.add(new FileOutConfig(cgc.getUserTemplateDir() + "/" + cgc.getUserTemplateRedisConfiguration() + "." + cgc.getUserTemplateType()) {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    // 自定义输出文件名称
                    return projectPath + "/src/main/java".replace("/", cgc.getFileSeparator()) +
                            (StringUtils.isEmpty(pc.getParent()) ?
                                    "" : cgc.getFileSeparator() + pc.getParent().replace(".", cgc.getFileSeparator())
                            ) +
                            cgc.getFileSeparator() + cgc.getPackageRedis().replace(".", cgc.getFileSeparator()) +
                            cgc.getFileSeparator() + "RedisConfiguration" + StringPool.DOT_JAVA;
                }
            });

            //RedisCache.java模板和输出配置
            focList.add(new FileOutConfig(cgc.getUserTemplateDir() + "/" + cgc.getUserTemplateRedisCache() + "." + cgc.getUserTemplateType()) {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    // 自定义输出文件名称
                    return projectPath + "/src/main/java".replace("/", cgc.getFileSeparator()) +
                            (StringUtils.isEmpty(pc.getParent()) ?
                                    "" : cgc.getFileSeparator() + pc.getParent().replace(".", cgc.getFileSeparator())
                            ) +
                            cgc.getFileSeparator() + cgc.getPackageRedis().replace(".", cgc.getFileSeparator()) +
                            cgc.getFileSeparator() + "RedisCache" + StringPool.DOT_JAVA;
                }
            });

            //RedisConfig.java模板和输出配置
            focList.add(new FileOutConfig(cgc.getUserTemplateDir() + "/" + cgc.getUserTemplateRedisConfig() + "." + cgc.getUserTemplateType()) {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    // 自定义输出文件名称
                    return projectPath + "/src/main/java".replace("/", cgc.getFileSeparator()) +
                            (StringUtils.isEmpty(pc.getParent()) ?
                                    "" : cgc.getFileSeparator() + pc.getParent().replace(".", cgc.getFileSeparator())
                            ) +
                            cgc.getFileSeparator() + cgc.getPackageConfig().replace(".", cgc.getFileSeparator()) +
                            cgc.getFileSeparator() + "RedisConfig" + StringPool.DOT_JAVA;
                }
            });

            //RedisProperties.java模板和输出配置
            focList.add(new FileOutConfig(cgc.getUserTemplateDir() + "/" + cgc.getUserTemplateRedisProperties() + "." + cgc.getUserTemplateType()) {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    // 自定义输出文件名称
                    return projectPath + "/src/main/java".replace("/", cgc.getFileSeparator()) +
                            (StringUtils.isEmpty(pc.getParent()) ?
                                    "" : cgc.getFileSeparator() + pc.getParent().replace(".", cgc.getFileSeparator())
                            ) +
                            cgc.getFileSeparator() + cgc.getPackageRedis().replace(".", cgc.getFileSeparator()) +
                            cgc.getFileSeparator() + "RedisProperties" + StringPool.DOT_JAVA;
                }
            });

            //RedisLettuceProperties.java模板和输出配置
            focList.add(new FileOutConfig(cgc.getUserTemplateDir() + "/" + cgc.getUserTemplateRedisLettuceProperties() + "." + cgc.getUserTemplateType()) {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    // 自定义输出文件名称
                    return projectPath + "/src/main/java".replace("/", cgc.getFileSeparator()) +
                            (StringUtils.isEmpty(pc.getParent()) ?
                                    "" : cgc.getFileSeparator() + pc.getParent().replace(".", cgc.getFileSeparator())
                            ) +
                            cgc.getFileSeparator() + cgc.getPackageRedis().replace(".", cgc.getFileSeparator()) +
                            cgc.getFileSeparator() + "RedisLettuceProperties" + StringPool.DOT_JAVA;
                }
            });

            //RedisLettucePoolProperties.java模板和输出配置
            focList.add(new FileOutConfig(cgc.getUserTemplateDir() + "/" + cgc.getUserTemplateRedisLettucePoolProperties() + "." + cgc.getUserTemplateType()) {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    // 自定义输出文件名称
                    return projectPath + "/src/main/java".replace("/", cgc.getFileSeparator()) +
                            (StringUtils.isEmpty(pc.getParent()) ?
                                    "" : cgc.getFileSeparator() + pc.getParent().replace(".", cgc.getFileSeparator())
                            ) +
                            cgc.getFileSeparator() + cgc.getPackageRedis().replace(".", cgc.getFileSeparator()) +
                            cgc.getFileSeparator() + "RedisLettucePoolProperties" + StringPool.DOT_JAVA;
                }
            });

            //shiro的RedisShiroCache.java模板和输出配置
            focList.add(new FileOutConfig(cgc.getUserTemplateDir() + "/" + cgc.getUserTemplateRedisShiroCache() + "." + cgc.getUserTemplateType()) {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    // 自定义输出文件名称
                    return projectPath + "/src/main/java".replace("/", cgc.getFileSeparator()) +
                            (StringUtils.isEmpty(pc.getParent()) ?
                                    "" : cgc.getFileSeparator() + pc.getParent().replace(".", cgc.getFileSeparator())
                            ) +
                            cgc.getFileSeparator() + cgc.getPackageShiroRedis().replace(".", cgc.getFileSeparator()) +
                            cgc.getFileSeparator() + "RedisShiroCache" + StringPool.DOT_JAVA;
                }
            });

            //shiro的RedisShiroCacheManager.java模板和输出配置
            focList.add(new FileOutConfig(cgc.getUserTemplateDir() + "/" + cgc.getUserTemplateRedisShiroCacheManager() + "." + cgc.getUserTemplateType()) {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    // 自定义输出文件名称
                    return projectPath + "/src/main/java".replace("/", cgc.getFileSeparator()) +
                            (StringUtils.isEmpty(pc.getParent()) ?
                                    "" : cgc.getFileSeparator() + pc.getParent().replace(".", cgc.getFileSeparator())
                            ) +
                            cgc.getFileSeparator() + cgc.getPackageShiroRedis().replace(".", cgc.getFileSeparator()) +
                            cgc.getFileSeparator() + "RedisShiroCacheManager" + StringPool.DOT_JAVA;
                }
            });

            //shiro的RedisShiroConfiguration.java模板和输出配置
            focList.add(new FileOutConfig(cgc.getUserTemplateDir() + "/" + cgc.getUserTemplateRedisShiroConfiguration() + "." + cgc.getUserTemplateType()) {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    // 自定义输出文件名称
                    return projectPath + "/src/main/java".replace("/", cgc.getFileSeparator()) +
                            (StringUtils.isEmpty(pc.getParent()) ?
                                    "" : cgc.getFileSeparator() + pc.getParent().replace(".", cgc.getFileSeparator())
                            ) +
                            cgc.getFileSeparator() + cgc.getPackageShiroRedis().replace(".", cgc.getFileSeparator()) +
                            cgc.getFileSeparator() + "RedisShiroConfiguration" + StringPool.DOT_JAVA;
                }
            });

            //shiro的RedisShiroSessionDAO.java模板和输出配置
            focList.add(new FileOutConfig(cgc.getUserTemplateDir() + "/" + cgc.getUserTemplateRedisShiroSessionDAO() + "." + cgc.getUserTemplateType()) {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    // 自定义输出文件名称
                    return projectPath + "/src/main/java".replace("/", cgc.getFileSeparator()) +
                            (StringUtils.isEmpty(pc.getParent()) ?
                                    "" : cgc.getFileSeparator() + pc.getParent().replace(".", cgc.getFileSeparator())
                            ) +
                            cgc.getFileSeparator() + cgc.getPackageShiroRedis().replace(".", cgc.getFileSeparator()) +
                            cgc.getFileSeparator() + "RedisShiroSessionDAO" + StringPool.DOT_JAVA;
                }
            });
        }

        //shiro的ShiroConfig.java模板和输出配置
        focList.add(new FileOutConfig(cgc.getUserTemplateDir() + "/" + cgc.getUserTemplateShiroConfig() + "." + cgc.getUserTemplateType()) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名称
                return projectPath + "/src/main/java".replace("/", cgc.getFileSeparator()) +
                        (StringUtils.isEmpty(pc.getParent()) ?
                                "" : cgc.getFileSeparator() + pc.getParent().replace(".", cgc.getFileSeparator())
                        ) +
                        cgc.getFileSeparator() + cgc.getPackageConfig().replace(".", cgc.getFileSeparator()) +
                        cgc.getFileSeparator() + "ShiroConfig" + StringPool.DOT_JAVA;
            }
        });

        //shiro的ShiroRealm.java模板和输出配置
        focList.add(new FileOutConfig(cgc.getUserTemplateDir() + "/" + cgc.getUserTemplateShiroRealm() + "." + cgc.getUserTemplateType()) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名称
                return projectPath + "/src/main/java".replace("/", cgc.getFileSeparator()) +
                        (StringUtils.isEmpty(pc.getParent()) ?
                                "" : cgc.getFileSeparator() + pc.getParent().replace(".", cgc.getFileSeparator())
                        ) +
                        cgc.getFileSeparator() + cgc.getPackageShiro().replace(".", cgc.getFileSeparator()) +
                        cgc.getFileSeparator() + "ShiroRealm" + StringPool.DOT_JAVA;
            }
        });

        //ServiceConfig.java模板和输出配置
        focList.add(new FileOutConfig(cgc.getUserTemplateDir() + "/" + cgc.getUserTemplateServiceConfig() + "." + cgc.getUserTemplateType()) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名称
                return projectPath + "/src/main/java".replace("/", cgc.getFileSeparator()) +
                        (StringUtils.isEmpty(pc.getParent()) ?
                                "" : cgc.getFileSeparator() + pc.getParent().replace(".", cgc.getFileSeparator())
                        ) +
                        cgc.getFileSeparator() + cgc.getPackageConfig().replace(".", cgc.getFileSeparator()) +
                        cgc.getFileSeparator() + "ServiceConfig" + StringPool.DOT_JAVA;
            }
        });

        if (cgc.getMapperInResource() && !cgc.getEnableRedis()) {
            //指定模板以及模板生成文件位置
            //非redis模式,指定xml模板和输出位置
            focList.add(new FileOutConfig("/templates/mapper.xml." + cgc.getUserTemplateType()) {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    // 自定义输出文件名称
                    return projectPath + "/src/main/resources/".replace("/", cgc.getFileSeparator())
                            + pc.getXml().replace(".", cgc.getFileSeparator())
                            + cgc.getFileSeparator() + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
                }
            });
            tc.setXml(null);
        }

        if (StringUtils.isNotEmpty(cgc.getUserTemplateController())) {
            //如果要用自定义的controller,指定其模板位置和输出位置
            focList.add(new FileOutConfig(cgc.getUserTemplateDir() + "/" + cgc.getUserTemplateController() + "." + cgc.getUserTemplateType()) {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    // 自定义输出文件名称
                    return projectPath + "/src/main/java".replace("/", cgc.getFileSeparator()) +
                            (StringUtils.isEmpty(pc.getParent()) ?
                                    "" : cgc.getFileSeparator() + pc.getParent().replace(".", cgc.getFileSeparator())
                            ) +
                            cgc.getFileSeparator() + pc.getController().replace(".", cgc.getFileSeparator()) +
                            cgc.getFileSeparator() + tableInfo.getEntityName() + "Controller" + StringPool.DOT_JAVA;
                }
            });
            tc.setController(null);
        }

        if (!focList.isEmpty()) {
            cfg.setFileOutConfigList(focList);
            mpg.setCfg(cfg);
            mpg.setTemplate(tc);
        }

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(cgc.getLombokModel());
        strategy.setRestControllerStyle(cgc.getRestControllerStyle());
        if (cgc.getTableInclude().length != 0) {
            strategy.setInclude(cgc.getTableInclude());
        } else if (cgc.getTableExclude().length != 0) {
            strategy.setExclude(cgc.getTableExclude());
        }

        //生成实体、controller要继承的公共类，带完整包名
//        strategy.setSuperEntityColumns("id");
//        strategy.setSuperEntityClass(pc.getParent() + StringPool.DOT + "common" + StringPool.DOT + "BaseEntity");
//        strategy.setSuperControllerClass(pc.getParent() + StringPool.DOT + "common" + StringPool.DOT + "BaseController");

        strategy.setControllerMappingHyphenStyle(false);
        if (cgc.getTablePrefix() != null && cgc.getTablePrefix().length != 0) {
            strategy.setTablePrefix(cgc.getTablePrefix());
        }
        mpg.setStrategy(strategy);
        mpg.execute();
        logger.warn("\n");
        if (cgc.getEnableRedis())
            logger.warn("请在application.yml的spring.profiles.active中指定application-redis.yml为激活配置,此文件为redis配置文件");
        logger.warn("请配置SpringBoot启动注解@ComponentScan,增加扫描路径\n" +
                        "{\"{}\",\n" +
                        " \"{}\",\n" +
                        " \"{}\",\n" +
                        " \"{}\",\n" +
                        " \"{}\"}",
                "com.github.yhl452493373.config",
                pc.getParent() + StringPool.DOT + cgc.getPackageConfig(),
                pc.getParent() + StringPool.DOT + pc.getMapper(),
                pc.getParent() + StringPool.DOT + pc.getService(),
                pc.getParent() + StringPool.DOT + pc.getController());
        logger.warn("\n");
    }

    /**
     * 多数据源配置生成
     *
     * @param dsgc 数据源配置生成策略配置
     */
    public static void dataSourceCodeGenerate(DataSourceGeneratorConfig dsgc) {
        Set<DataSourceProperties> dataSourcePropertiesSet = new HashSet<>();
        Yaml yaml = new Yaml();
        LinkedHashMap yamlMap = null;
        String ymlFile = dsgc.getMultiple() ? dsgc.getMultipleYmlFile() : dsgc.getSingleYmlFile();
        try {
            yamlMap = yaml.load(new PathMatchingResourcePatternResolver().getResource(ymlFile).getInputStream());
        } catch (IOException e) {
            logger.error("资源文件{}不存在:{}", ymlFile, e);
        }
        JSONObject yamlJSON = JSONObject.parseObject(JSON.toJSONString(yamlMap));
        DataSourceGroup dataSourceGroup = new DataSourceGroup();
        boolean multiple = false;
        if (yamlJSON.containsKey("datasource")) {
            JSONObject datasourceJSON = yamlJSON.getJSONObject("datasource");
            dataSourceGroup = datasourceJSON.toJavaObject(DataSourceGroup.class);
            DataSourceProperties dataSourceProperties = dataSourceGroup.getDataSourceProperties();
            List<DataSourceProperties> dataSourcePropertiesList = dataSourceGroup.getDataSourcePropertiesList();
            if (dataSourcePropertiesList != null && !dataSourcePropertiesList.isEmpty()) {
                multiple = true;
                AtomicBoolean hasPrimary = new AtomicBoolean(true);
                dataSourcePropertiesList.forEach(temp -> {
                    temp.setMultiple(true);
                    if (!temp.getPrimary())
                        hasPrimary.set(false);
                });
                //如果没有主数据源，则设置第一个为主数据源
                if (!hasPrimary.get())
                    dataSourcePropertiesList.get(0).setPrimary(true);
                dataSourcePropertiesSet.addAll(dataSourcePropertiesList);
            } else if (dataSourceProperties != null) {
                dataSourceProperties.setName(null);
                dataSourceProperties.setMultiple(false);
                dataSourcePropertiesSet.add(dataSourceProperties);
            }
        }
        if (StringUtils.isEmpty(dataSourceGroup.getConfigPackage()))
            throw new NullPointerException("请指定.java格式配置文件所在包");
        //取项目路径
        String projectPath = System.getProperty("user.dir");
        //创建freeMarker配置实例
        Configuration configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        configuration.setDefaultEncoding(ConstVal.UTF8);
        configuration.setClassForTemplateLoading(FreemarkerTemplateEngine.class, StringPool.SLASH);
        String configPackage = dataSourceGroup.getConfigPackage();
        logger.info("==========================准备生成文件...==========================");
        for (DataSourceProperties dataSourceProperties : dataSourcePropertiesSet) {
            dataSourceProperties.setCacheEnabled(dsgc.getCacheEnabled());
            dataSourceProperties.setConfigPackage(configPackage);
            //生成文件路径
            String filePath = projectPath + "/src/main/java".replace("/", dsgc.getFileSeparator()) +
                    dsgc.getFileSeparator() + configPackage.replace(".", dsgc.getFileSeparator()) +
                    dsgc.getFileSeparator() + (
                    multiple ?
                            DataSourceProperties.firstCharToUpper(dataSourceProperties.getName()
                            ) : "") + "DataSourceConfig" + StringPool.DOT_JAVA;
            File configFile = new File(filePath);
            try {
                //加载模版文件
                Template template = configuration.getTemplate(dsgc.getTemplateDatasource());
                if (dsgc.getFileOverride() || !configFile.exists()) {
                    Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(configFile)));
                    //生成文件
                    template.process(dataSourceProperties, out);
                    logger.info("{}生成成功-->{}", configFile.getName(), configFile.getAbsolutePath());
                } else {
                    logger.info("{}已存在,跳过", configFile.getName());
                }
            } catch (IOException | TemplateException e) {
                logger.info("{}生成失败:{}", configFile.getName(), e);
            }
        }
        logger.info("==========================文件生成完成！！！==========================");
        logger.warn("");
        if (multiple)
            logger.warn("请在application.yml的spring.profiles.active中指定application-multiple-datasource.yml为激活配置,此文件为多数据源配置");
        else
            logger.warn("请在application.yml的spring.profiles.active中指定application-single-datasource.yml为激活配置,此文件为单数据源配置");
        logger.warn("请删除application.yml中的spring.datasource节点");
        logger.warn("请删除application.yml中的mybatis和mybatis-plus节点");
        logger.warn("请配置SpringBoot启动注解为:@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})");
        logger.warn("");
    }
}
