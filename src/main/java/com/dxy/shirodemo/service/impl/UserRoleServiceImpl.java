package com.dxy.shirodemo.service.impl;

import cloud.gouyiba.core.constructor.QueryWrapper;
import com.dxy.shirodemo.entity.Role;
import com.dxy.shirodemo.entity.UserRole;
import com.dxy.shirodemo.mapper.RoleMapper;
import com.dxy.shirodemo.mapper.UserRoleMapper;
import com.dxy.shirodemo.service.UserRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName UseRoleServiceImpl
 * @Author duxiaoyu
 * @Date 2020/12/3 13:44
 * @Version 1.0
 */
@Service
public class UserRoleServiceImpl implements UserRoleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRoleServiceImpl.class);

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public List<Role> getUserRoleByUserId(Integer userId) {
        List<UserRole> userRoles = userRoleMapper.selectList(new QueryWrapper<>().where("user_id", userId));
        if (CollectionUtils.isEmpty(userRoles)) {
            throw new NullPointerException("获取用户角色失败...");
        }
        List<Integer> roleIds = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());
        List<Role> roleList = roleMapper.selectList(new QueryWrapper<>().in("id", roleIds.toArray()));
        return roleList;
    }
}
