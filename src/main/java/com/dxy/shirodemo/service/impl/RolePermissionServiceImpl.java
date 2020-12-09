package com.dxy.shirodemo.service.impl;

import cloud.gouyiba.core.constructor.QueryWrapper;
import com.dxy.shirodemo.entity.Permission;
import com.dxy.shirodemo.entity.Role;
import com.dxy.shirodemo.entity.RolePermission;
import com.dxy.shirodemo.enumeration.IsUsableEnum;
import com.dxy.shirodemo.mapper.PermissionMapper;
import com.dxy.shirodemo.mapper.RoleMapper;
import com.dxy.shirodemo.mapper.RolePermissionMapper;
import com.dxy.shirodemo.service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
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

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public Integer addRole(Role role, String permissionIds) {
        role.setStatus(IsUsableEnum.USABLE);
        roleMapper.insert(role);
        Role newRole = roleMapper.selectOne(new QueryWrapper<>().orderBy("id", "desc").limit(0, 1));
        String[] permissionArr=permissionIds.split(",");
        List<RolePermission> rolePermissionList=new ArrayList<>();
        for (String item:permissionArr){
            RolePermission rolePermission=new RolePermission();
            rolePermission.setRoleId(newRole.getId());
            rolePermission.setPermissionId(Integer.valueOf(item));
            rolePermissionList.add(rolePermission);
        }
        rolePermissionMapper.insertBatch(rolePermissionList);
        return 1;
    }

    @Override
    public Integer updateRole(Role role, String permissionIds) {
        return null;
    }

    @Override
    public Integer delRole(Integer id) {
        return null;
    }

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
