package com.dxy.shirodemo.service;

import com.dxy.shirodemo.entity.Permission;
import com.dxy.shirodemo.entity.Role;

import java.util.List;

/**
 * @ClassName RolePermissionService
 * @Author duxiaoyu
 * @Date 2020/12/3 11:43
 * @Version 1.0
 */
public interface RolePermissionService {

    List<Permission> getRolePermissionByRoleId(List<Integer> roleIds);

    Integer addRole(Role role,String permissionIds);

    Integer updateRole(Role role,String permissionIds);

    Integer delRole(Integer id);

}
