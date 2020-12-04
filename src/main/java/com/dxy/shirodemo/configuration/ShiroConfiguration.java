package com.dxy.shirodemo.configuration;

import com.dxy.shirodemo.filter.MyPermissionAuthorizationFilter;
import com.dxy.shirodemo.filter.MyRolesAuthorizationFilter;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName ShiroConfiguration
 * @Author duxiaoyu
 * @Date 2020/12/2 15:10
 * @Version 1.0
 */
@Configuration
public class ShiroConfiguration {

    private final int EXPIRE = 1800;


    /**
     * 配置shiro过滤器链
     *
     * @param securityManager
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean filterFactoryBean = new ShiroFilterFactoryBean();
        filterFactoryBean.setSecurityManager(securityManager);

        // 配置自定义过滤器
        Map<String, Filter> customFilterMap = new LinkedHashMap<>();
        customFilterMap.put("myRoles", new MyRolesAuthorizationFilter());
        customFilterMap.put("myPermission", new MyPermissionAuthorizationFilter());
        filterFactoryBean.setFilters(customFilterMap);

        // 配置shiro内置过滤器链： 从上到下，优先级排序：从高到低，按顺序执行，所以此处使用LinkedHashMap配置有序
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/static/**", "anon"); // 静态资源匿名访问
        filterChainDefinitionMap.put("/bootstrap/**", "anon"); // 静态资源匿名访问
        filterChainDefinitionMap.put("/css/**", "anon"); // 静态资源匿名访问
        filterChainDefinitionMap.put("/js/**", "anon"); // 静态资源匿名访问
        filterChainDefinitionMap.put("/img/**", "anon"); // 静态资源匿名访问
        filterChainDefinitionMap.put("/user/login", "anon");// 登录页面匿名访问
        filterChainDefinitionMap.put("/user/doLogin", "anon");// 登录请求匿名访问
        // 配置自定义过滤器链：myRoles[admin],myPermission[user_list] 按照顺序调用执行
        filterChainDefinitionMap.put("/user/queryUserList","authc,myRoles[admin],myPermission[user_list_test]");
        filterChainDefinitionMap.put("/**", "authc");// 其他请求均需要认证，优先级最低，故放到最后，也只能放到最后
        filterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        filterFactoryBean.setLoginUrl("/user/login"); // 登录页面
        filterFactoryBean.setSuccessUrl("/index");// 登录成功后的跳转页面
        filterFactoryBean.setUnauthorizedUrl("/user/error");// 验证失败后的跳转路径
        return filterFactoryBean;
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
     * 配置shiro安全管理器
     *
     * @return
     */
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(shiroRealm());
        return securityManager;
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
