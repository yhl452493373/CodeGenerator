package com.github.yhl452493373.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.github.yhl452493373.bean.CaptchaProperties;
import com.github.yhl452493373.shiro.ShiroCaptcha;
import com.github.yhl452493373.bean.DataSourceGroup;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class CommonConfig {
    @ConfigurationProperties(prefix = "datasource")
    @Bean
    public DataSourceGroup dataSourceGroup() {
        return new DataSourceGroup();
    }

    @ConfigurationProperties(prefix = "captcha")
    @Bean
    public CaptchaProperties captchaProperties() {
        return new CaptchaProperties();
    }

    @Bean
    public ShiroCaptcha shiroCaptcha() {
        return new ShiroCaptcha(captchaProperties());
    }

    /**
     * fastjson
     */
    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        FastJsonHttpMessageConverter fastConvert = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteDateUseDateFormat,
                SerializerFeature.DisableCircularReferenceDetect);
        fastJsonConfig.setDateFormat("yyyy-MM-dd hh:mm:ss");
        //处理中文乱码问题
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        fastConvert.setSupportedMediaTypes(fastMediaTypes);
        fastConvert.setFastJsonConfig(fastJsonConfig);
        return new HttpMessageConverters((HttpMessageConverter<?>) fastConvert);
    }

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor= new PaginationInterceptor();
        paginationInterceptor.setDialectType("mysql");
        return paginationInterceptor;
    }
}
