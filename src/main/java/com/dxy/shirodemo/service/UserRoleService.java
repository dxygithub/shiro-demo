package com.dxy.shirodemo.service;

import com.dxy.shirodemo.entity.Role;

import java.util.List;

/**
 * @ClassName UseRoleService
 * @Author duxiaoyu
 * @Date 2020/12/3 11:41
 * @Version 1.0
 */
public interface UserRoleService {

    List<Role> getUserRoleByUserId(Integer userId);
}
