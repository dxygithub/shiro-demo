package com.dxy.shirodemo.configuration;

import com.dxy.shirodemo.filter.MyPermissionAuthorizationFilter;
import com.dxy.shirodemo.filter.MyRolesAuthorizationFilter;
import com.dxy.shirodemo.service.ShiroService;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.cache.MapCache;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName ShiroConfiguration
 * @Author duxiaoyu
 * @Date 2020/12/2 15:10
 * @Version 1.0
 */
@Configuration
public class ShiroConfiguration {

    /**
     * 配置shiro过滤器工厂
     *
     * @param securityManager
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager, ShiroService shiroService) {
        ShiroFilterFactoryBean filterFactoryBean = new ShiroFilterFactoryBean();
        filterFactoryBean.setSecurityManager(securityManager);

        // 自定义过滤器
        Map<String, Filter> customFilterMap = new LinkedHashMap<>();
        customFilterMap.put("myRoles", new MyRolesAuthorizationFilter());
        customFilterMap.put("myPermission", new MyPermissionAuthorizationFilter());
        filterFactoryBean.setFilters(customFilterMap);

        // 检查是否有

        // TODO 动态加载全部权限
        Map<String, String> dynamicPermissionMap = shiroService.loadFilterChainDefinitionMap();
        filterFactoryBean.setFilterChainDefinitionMap(dynamicPermissionMap);

        filterFactoryBean.setLoginUrl("/user/login"); // 登录页面
        filterFactoryBean.setSuccessUrl("/index");// 登录成功后的跳转页面
        filterFactoryBean.setUnauthorizedUrl("/user/error");// 验证失败后的跳转路径
        return filterFactoryBean;
    }

    /**
     * 配置shiro安全管理器
     *
     * @return
     */
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(shiroRealm());
        securityManager.setSessionManager(customSessionManager());
        securityManager.setCacheManager(redisCacheManager());
        return securityManager;
    }

    /**
     * 注入自定义realm
     *
     * @return
     */
    @Bean
    public ShiroRealm shiroRealm() {
        ShiroRealm shiroRealm = new ShiroRealm();
        shiroRealm.setCredentialsMatcher(new HashedCredentialsMatcher("MD5"));
        return shiroRealm;
    }

    /**
     * 自定义CustomSessionManager
     *
     * @return
     */
    @Bean
    public CustomSessionManager customSessionManager() {
        CustomSessionManager customSessionManager = new CustomSessionManager();
        customSessionManager.setSessionDAO(redisSessionDao());
        return customSessionManager;
    }

    /**
     * 自定义RedisSessionDao
     *
     * @return
     */
    @Bean
    public RedisSessionDao redisSessionDao() {
        return new RedisSessionDao();
    }

    /**
     * 自定义RedisCacheManager
     * @return
     */
    @Bean
    public RedisCacheManager redisCacheManager(){
        return new RedisCacheManager();
    }

    @Bean
    public MapCache<Object, AuthorizationInfo> mapCache(){
        return new MapCache<>("shiro",new ConcurrentHashMap<>());
    }

    /**
     * 配置DefaultWebSessionManager
     *
     * @return
     */
    // @Bean
    public DefaultWebSessionManager defaultWebSessionManager() {
        DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
        defaultWebSessionManager.setSessionDAO(redisSessionDao());
        return defaultWebSessionManager;
    }

    /**
     * 开启shiro权限注解支持
     *
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor sourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        sourceAdvisor.setSecurityManager(securityManager);
        return sourceAdvisor;
    }

    /**
     * 配置Shiro生命周期处理器
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 自动创建代理类，若不添加，Shiro的注解可能不会生效。
     */
    @Bean
    @DependsOn({"lifecycleBeanPostProcessor"})
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new
                DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }


}
