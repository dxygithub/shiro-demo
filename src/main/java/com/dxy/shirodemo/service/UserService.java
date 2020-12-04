package com.dxy.shirodemo.service;

import com.dxy.shirodemo.entity.User;

import java.util.List;

/**
 * @ClassName UserService
 * @Author duxiaoyu
 * @Date 2020/12/3 11:39
 * @Version 1.0
 */
public interface UserService {

    User getUserByAccount(String account);

    List<User> getAllUserList();
}
