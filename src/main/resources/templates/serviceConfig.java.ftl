package ${cfg.packageConfig};

import ShiroCaptcha;
<#if tableInfoList??>
    <#list tableInfoList as tempTableInfo>
import ${package.Service}.${tempTableInfo.serviceName};
    </#list>
</#if>
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * 这个配置文件用于配置所有需要注入的bean,其余地方通过ServiceConfig.serviceConfig.xxxxService方式调用
 * 降低因同一个service在多个文件中分别注入引起的时间消耗
 */
@Configuration
public class ServiceConfig {
    public static ServiceConfig serviceConfig;

    @Autowired
    public ShiroCaptcha shiroCaptcha;
    <#if tableInfoList??>
    <#list tableInfoList as tempTableInfo>
    @Autowired
    public ${tempTableInfo.serviceName} ${tempTableInfo.serviceName?uncap_first};
    </#list>
    </#if>

    @PostConstruct
    public void init() {
        serviceConfig = this;
    }
}
