package ${cfg.packageConfig};

<#if (enableCache&&cfg.enableRedis)>
import ${cfg.packageShiroRedis}.RedisShiroConfiguration;
import ${cfg.packageShiroRedis}.RedisShiroSessionDAO;
import ${cfg.packageShiroRedis}.RedisShiroCacheManager;
</#if>
import ${cfg.packageShiro}.ShiroRealm;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.ShiroHttpSession;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
<#if (enableCache&&cfg.enableRedis)>
import org.springframework.beans.factory.annotation.Qualifier;
</#if>
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
<#if (enableCache&&cfg.enableRedis)>
import org.springframework.data.redis.core.RedisTemplate;
</#if>

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * ShiroFilterFactoryBean 处理拦截资源文件问题。
     * 注意：单独一个ShiroFilterFactoryBean配置是或报错的，以为在
     * 初始化ShiroFilterFactoryBean的时候需要注入：SecurityManager
     * <p>
     * Filter Chain定义说明 1、一个URL可以配置多个Filter，使用逗号分隔 2、当设置多个过滤器时，全部验证通过，才视为通过
     * 3、部分过滤器可指定参数，如perms，roles
     */
    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 必须设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        shiroFilterFactoryBean.setLoginUrl("/login");
        shiroFilterFactoryBean.setUnauthorizedUrl("/login");

        //TODO 根据项目配置拦截器
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

        //配置记住我或认证通过可以访问的地址(配置不会被拦截的链接 顺序判断)
        filterChainDefinitionMap.put("/static/**", "anon");
        filterChainDefinitionMap.put("/login", "anon");
        filterChainDefinitionMap.put("/captcha", "anon");

        // 配置退出过滤器,其中的具体的退出代码Shiro已经替我们实现了.退出不用写.直接访问/logout就行
        filterChainDefinitionMap.put("/logout", "logout");

        // <!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
        filterChainDefinitionMap.put("/**", "authc");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    <#if (enableCache&&cfg.enableRedis)>
    @Bean
    public RedisShiroConfiguration redisShiroConfiguration() {
        return new RedisShiroConfiguration();
    }
    </#if>

    <#if (enableCache&&cfg.enableRedis)>
    @Bean
    public RedisShiroCacheManager redisShiroCacheManager(RedisShiroConfiguration redisShiroConfiguration, @Qualifier("lettuceRedisTemplate") RedisTemplate<Object, Object> shiroRedisTemplate) {
        logger.debug("ShiroConfig.redisShiroCacheManager()");
        return new RedisShiroCacheManager(redisShiroConfiguration, shiroRedisTemplate);
    }
    </#if>

    <#if (enableCache&&cfg.enableRedis)>
    @Bean
    public RedisShiroSessionDAO redisShiroSessionDAO(RedisShiroConfiguration redisShiroConfiguration, @Qualifier("lettuceRedisTemplate") RedisTemplate<Object, Object> shiroRedisTemplate) {
        logger.debug("ShiroConfig.redisShiroSessionDAO()");
        return new RedisShiroSessionDAO(redisShiroConfiguration, shiroRedisTemplate);
    }
    </#if>

    @Bean
    public SessionManager sessionManager(<#if (enableCache&&cfg.enableRedis)>RedisShiroSessionDAO redisShiroSessionDAO</#if>) {
        logger.debug("ShiroConfig.sessionManager()");
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        <#if enableCache && cfg.enableRedis>
        sessionManager.setSessionDAO(redisShiroSessionDAO);//单redis的session存储
        </#if>
        //单位为毫秒（1秒=1000毫秒） 3600000毫秒为1个小时
        sessionManager.setSessionValidationInterval(3600000 * 12);
        //3600000 milliseconds = 1 hour
        sessionManager.setGlobalSessionTimeout(3600000 * 12);
        //是否删除无效的，默认也是开启
        sessionManager.setDeleteInvalidSessions(true);
        //是否开启 检测，默认开启
        sessionManager.setSessionValidationSchedulerEnabled(true);
        //创建会话Cookie
        Cookie cookie = new SimpleCookie(ShiroHttpSession.DEFAULT_SESSION_ID_NAME);
        sessionManager.setSessionIdCookie(cookie);
        return sessionManager;
    }

    @Bean
    public SecurityManager securityManager(<#if (enableCache&&cfg.enableRedis)>RedisShiroCacheManager redisShiroCacheManager, SessionManager sessionManager ,</#if>RememberMeManager rememberMeManager) {
        logger.debug("ShiroConfig.securityManager()");
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        securityManager.setRealm(shiroRealm());
        securityManager.setSubjectFactory(new DefaultWebSubjectFactory());
        //记住我管理器
        securityManager.setRememberMeManager(rememberMeManager);
        <#if enableCache && cfg.enableRedis>
        //注入缓存管理器;
        securityManager.setCacheManager(redisShiroCacheManager);
        //自定义sessionDAO
        securityManager.setSessionManager(sessionManager);
        </#if>
        return securityManager;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public ShiroRealm shiroRealm() {
        return new ShiroRealm();
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        return new AuthorizationAttributeSourceAdvisor();
    }

    @Bean
    public SimpleCookie rememberMeCookie() {
        //这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        //TODO 根据需要设置记住我cookie生效时间30天 ,单位秒
        simpleCookie.setMaxAge(30 * 24 * 60 * 60);
        return simpleCookie;
    }

    @Bean
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        return cookieRememberMeManager;
    }
}
