package com.dxy.shirodemo.service.impl;

import cloud.gouyiba.core.constructor.QueryWrapper;
import com.dxy.shirodemo.entity.Permission;
import com.dxy.shirodemo.entity.Role;
import com.dxy.shirodemo.entity.RolePermission;
import com.dxy.shirodemo.mapper.*;
import com.dxy.shirodemo.service.ShiroService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author tony -_-
 * @date 2020/12/6/006/16:07
 */
@Service
public class ShiroServiceImpl implements ShiroService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShiroServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    /**
     * 初始化全部权限
     *
     * @return
     */
    @Override
    public Map<String, String> loadFilterChainDefinitionMap() {
        /**
         * TODO 根据所有权限动态配置
         * 1. 查询每个权限对应的所有角色信息
         * 2. 动态拼接角色字符串
         * 3. 组成完整的权限过滤器字符串: /api/request/** : authc,myRoles[admin,user,root],myPermission[user_list]
         */

        // 配置shiro内置过滤器链： 从上到下，优先级排序：从高到低，按顺序执行，所以此处使用LinkedHashMap配置有序
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/static/**", "anon"); // 静态资源匿名访问
        filterChainDefinitionMap.put("/bootstrap/**", "anon"); // 静态资源匿名访问
        filterChainDefinitionMap.put("/css/**", "anon"); // 静态资源匿名访问
        filterChainDefinitionMap.put("/js/**", "anon"); // 静态资源匿名访问
        filterChainDefinitionMap.put("/img/**", "anon"); // 静态资源匿名访问
        filterChainDefinitionMap.put("/user/login", "anon");// 登录页面匿名访问
        filterChainDefinitionMap.put("/user/doLogin", "anon");// 登录请求匿名访问
        filterChainDefinitionMap.put("/user/logout","anon");// 退出匿名访问

        List<Permission> permissionList = permissionMapper.selectList(new QueryWrapper<>().where("status", 2).where("del_flag", 2).isNotNull("url"));
        for (Permission permission : permissionList) {
            List<RolePermission> rolePermissions = rolePermissionMapper.selectList(new QueryWrapper<>().where("permission_id", permission.getId()).where("del_flag", 2));
            Set<Integer> roleIds = rolePermissions.stream().map(RolePermission::getRoleId).collect(Collectors.toSet());
            List<Role> roles = roleMapper.selectList(new QueryWrapper<>().in("id", roleIds.toArray()));

            // 开始拼接当前权限的所有角色
            StringJoiner roleSj = new StringJoiner(",", "myRoles[", "],");
            for (Role role : roles) {
                if (StringUtils.isNotBlank(role.getRole())) {
                    roleSj.add(role.getRole());
                }
            }

            StringBuilder sb = new StringBuilder("authc,");
            sb.append(roleSj.toString());
            sb.append(String.format("myPermission[%s]", permission.getPermission()));
            filterChainDefinitionMap.put(permission.getUrl(), sb.toString());
        }

        filterChainDefinitionMap.put("/**", "authc");// 其他请求均需要认证，优先级最低，故放到最后，也只能放到最后
        return filterChainDefinitionMap;
    }

    /**
     * 修改权限
     *
     * @param shiroFilterFactoryBean
     * @param roleId
     * @param isRemoveSession:
     */
    @Override
    public void updatePermission(ShiroFilterFactoryBean shiroFilterFactoryBean, Integer roleId, Boolean isRemoveSession) {
        /**
         * TODO 当动态修改权限角色时，增删改都需要调用此方法进行接口权限更新
         * 清空当前ShiroFilterFactoryBean中的权限存储，然后重新从数据库中查询最新的权限，进行更新
         */
        AbstractShiroFilter abstractShiroFilter;
        try {
            abstractShiroFilter = (AbstractShiroFilter) shiroFilterFactoryBean.getObject();
        } catch (Exception e) {
            throw new ClassCastException("get ShiroFilter from shiroFilterFactoryBean error!");
        }
        PathMatchingFilterChainResolver filterChainResolver = (PathMatchingFilterChainResolver) abstractShiroFilter.getFilterChainResolver();
        DefaultFilterChainManager manager = (DefaultFilterChainManager) filterChainResolver.getFilterChainManager();

        // 清空过滤管理器中的存储
        manager.getFilterChains().clear();
        // 清空过滤器工厂的存储，必须清空，否则会将原来的继续添加到DefaultFilterChainManager中
        // ps:如果仅仅是更新的话,可以根据这里的 map 遍历数据修改,重新整理好权限再一起添加
        shiroFilterFactoryBean.getFilterChainDefinitionMap().clear();

        // 重新获取所有权限
        Map<String, String> filterChainDefinitionMap = this.loadFilterChainDefinitionMap();
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        // 重新构建过滤器链
        for (Map.Entry<String, String> filter : filterChainDefinitionMap.entrySet()) {
            manager.createChain(filter.getKey(), filter.getValue());
        }

        LOGGER.info("=================== 重新构建权限过滤器成功 ===================");
    }

    /**
     * 根据角色修改权限：动态赋权
     *
     * @param roleId
     * @param isRemoveSession:
     */
    @Override
    public void updatePermissionByRoleId(Integer roleId, Boolean isRemoveSession) {
        /**
         * TODO 删除shiro中的用户缓存信息，强制重新授权，达到动态更新用户权限的目的
         */
    }
}
