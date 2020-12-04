package com.dxy.shirodemo.service.impl;

import cloud.gouyiba.core.constructor.QueryWrapper;
import com.dxy.shirodemo.entity.Permission;
import com.dxy.shirodemo.entity.RolePermission;
import com.dxy.shirodemo.mapper.PermissionMapper;
import com.dxy.shirodemo.mapper.RolePermissionMapper;
import com.dxy.shirodemo.service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName RolePermissionServiceImpl
 * @Author duxiaoyu
 * @Date 2020/12/3 11:46
 * @Version 1.0
 */
@Service
public class RolePermissionServiceImpl implements RolePermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    /**
     * 根据角色id获取权限数据
     *
     * @param roleIds
     * @return
     */
    @Override
    public List<Permission> getRolePermissionByRoleId(List<Integer> roleIds) {
        List<RolePermission> rolePermissionList = rolePermissionMapper.selectList(new QueryWrapper<>().in("role_id", roleIds.toArray()));
        if (CollectionUtils.isEmpty(rolePermissionList)) {
            throw new NullPointerException("获取角色权限数据失败...");
        }
        List<Integer> permissionIds = rolePermissionList.stream().map(RolePermission::getPermissionId).collect(Collectors.toList());
        List<Permission> permissionList = permissionMapper.selectList(new QueryWrapper<>().in("id", permissionIds.toArray()));
        return permissionList;
    }
}
