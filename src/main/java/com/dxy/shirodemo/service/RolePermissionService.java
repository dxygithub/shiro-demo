package com.dxy.shirodemo.service;

import com.dxy.shirodemo.entity.Permission;

import java.util.List;

/**
 * @ClassName RolePermissionService
 * @Author duxiaoyu
 * @Date 2020/12/3 11:43
 * @Version 1.0
 */
public interface RolePermissionService {

    List<Permission> getRolePermissionByRoleId(List<Integer> roleIds);
}
