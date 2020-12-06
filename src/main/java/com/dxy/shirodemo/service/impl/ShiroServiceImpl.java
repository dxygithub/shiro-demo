package com.dxy.shirodemo.service.impl;

import com.dxy.shirodemo.mapper.PermissionMapper;
import com.dxy.shirodemo.mapper.RoleMapper;
import com.dxy.shirodemo.mapper.UserMapper;
import com.dxy.shirodemo.service.ShiroService;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author tony -_-
 * @date 2020/12/6/006/16:07
 */
@Service
public class ShiroServiceImpl implements ShiroService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private RoleMapper roleMapper;

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

        return null;
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
