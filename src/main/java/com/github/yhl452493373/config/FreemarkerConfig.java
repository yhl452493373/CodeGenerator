package com.github.yhl452493373.config;

import com.jagregory.shiro.freemarker.ShiroTags;
import freemarker.template.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Configuration
public class FreemarkerConfig {
    @Value("${spring.freemarker.template-loader-path:classpath:/templates/}")
    private String templateLoaderPath;

    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer() throws IOException, TemplateException {
        FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
        freeMarkerConfigurer.setTemplateLoaderPath(templateLoaderPath);
        freemarker.template.Configuration configuration = freeMarkerConfigurer.createConfiguration();
        configuration.setDefaultEncoding("UTF-8");
        configuration.setSharedVariable("shiro", new ShiroTags());
        configuration.setObjectWrapper(new CustomObjectWrapper());
        configuration.setNumberFormat("#");
        configuration.setDateTimeFormat("yyyy-MM-dd HH:mm:ss");
        configuration.setDateFormat("yyyy-MM-dd");
        configuration.setTimeFormat("HH:mm:ss");
        freeMarkerConfigurer.setConfiguration(configuration);
        return freeMarkerConfigurer;
    }

    @SuppressWarnings("deprecation")
    private class CustomObjectWrapper extends DefaultObjectWrapper {
        @Override
        public TemplateModel wrap(Object obj) throws TemplateModelException {
            if (obj instanceof LocalDateTime) {
                Timestamp timestamp = Timestamp.valueOf((LocalDateTime) obj);
                return new SimpleDate(timestamp);
            }
            if (obj instanceof LocalDate) {
                Date date = Date.valueOf((LocalDate) obj);
                return new SimpleDate(date);
            }
            if (obj instanceof LocalTime) {
                Time time = Time.valueOf((LocalTime) obj);
                return new SimpleDate(time);
            }
            return super.wrap(obj);
        }
    }
}