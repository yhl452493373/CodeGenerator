package ${configPackage};

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.h3w.bean.DataSourceGroup;
import com.h3w.bean.DataSourceProperties;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@SuppressWarnings("WeakerAccess")
@Configuration
@MapperScan(basePackages = ${name!?cap_first!""}DataSourceConfig.PACKAGE, sqlSessionFactoryRef = "${name!}${name???string("S","s")}qlSessionFactory")
public class ${name!?cap_first!""}DataSourceConfig {
    <#if name??>
    static final String PACKAGE = "${mapperPackage}.${name}";
        <#if mapperLocations?contains("classpath")>
    static final String MAPPER_LOCATION = "${mapperLocations}/${name}/*.xml";
        <#else>
    static final String MAPPER_LOCATION = "classpath:${mapperLocations?replace(".","/")}/${name}/*.xml";
        </#if>
    <#else>
    static final String PACKAGE = "${mapperPackage}";
        <#if mapperLocations?contains("classpath")>
    static final String MAPPER_LOCATION = "${mapperLocations}/*.xml";
        <#else>
    static final String MAPPER_LOCATION = "classpath:${mapperLocations?replace(".","/")}/*.xml";
        </#if>
    </#if>

    @Bean
    public DataSource ${name!}${name???string("D","d")}ataSource(DataSourceGroup dataSourceGroup) {
        DataSourceProperties dataSourceProperties = null;
        if (dataSourceGroup.getDataSourceProperties() != null) {
            dataSourceProperties = dataSourceGroup.getDataSourceProperties();
        } <#if name??> else if (dataSourceGroup.getDataSourcePropertiesList() != null) {
            for (DataSourceProperties tempDataSourceProperties : dataSourceGroup.getDataSourcePropertiesList()) {
                if (tempDataSourceProperties.getName().equalsIgnoreCase("${name}")) {
                    dataSourceProperties = tempDataSourceProperties;
                    break;
                }
            }
        }</#if>
        if (dataSourceProperties == null)
            throw new NullPointerException("${name!}数据源未配置");
        return dataSourceProperties;
    }

    @Bean
    <#if primary??>
    @Primary
    </#if>
    public DataSourceTransactionManager ${name!}${name???string("T","t")}ransactionManager(DataSourceGroup dataSourceGroup) {
        return new DataSourceTransactionManager(${name!}${name???string("D","d")}ataSource(dataSourceGroup));
    }

    @Bean
    <#if primary??>
    @Primary
    </#if>
    public SqlSessionFactory ${name!}${name???string("S","s")}qlSessionFactory(DataSource ${name!}${name???string("D","d")}ataSource, PaginationInterceptor paginationInterceptor) throws Exception {
        final MybatisSqlSessionFactoryBean sessionFactory = new MybatisSqlSessionFactoryBean();
        sessionFactory.setDataSource(${name!}${name???string("D","d")}ataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(${name!?cap_first!""}DataSourceConfig.MAPPER_LOCATION));
        sessionFactory.setGlobalConfig(new GlobalConfig().setRefresh(true));
        MybatisConfiguration configuration = new MybatisConfiguration();
        <#if cacheEnabled>
        configuration.setCacheEnabled(true);
        <#else>
        configuration.setCacheEnabled(false);
        </#if>
        sessionFactory.setConfiguration(configuration);
        sessionFactory.setPlugins(new Interceptor[]{
                paginationInterceptor
        });
        return sessionFactory.getObject();
    }
}