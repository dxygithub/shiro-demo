package com.dxy.shirodemo.service.impl;

import cloud.gouyiba.core.constructor.QueryWrapper;
import com.dxy.shirodemo.entity.User;
import com.dxy.shirodemo.mapper.UserMapper;
import com.dxy.shirodemo.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName UserServiceImpl
 * @Author duxiaoyu
 * @Date 2020/12/3 13:50
 * @Version 1.0
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUserByAccount(String account) {
        if (StringUtils.isBlank(account)) {
            throw new NullPointerException("员工账号为空...");
        }
        User user = userMapper.selectOne(new QueryWrapper<>().where("username", account));
        return user;
    }

    @Override
    public List<User> getAllUserList() {
        List<User> userList=userMapper.selectList(new QueryWrapper<>().where("del_flag",2).where("status",1));
        return userList;
    }
}
