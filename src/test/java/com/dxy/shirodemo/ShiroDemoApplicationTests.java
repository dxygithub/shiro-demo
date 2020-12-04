package com.dxy.shirodemo;


import cloud.gouyiba.core.constructor.QueryWrapper;
import cn.hutool.json.JSONUtil;
import com.dxy.shirodemo.entity.Permission;
import com.dxy.shirodemo.entity.Role;
import com.dxy.shirodemo.entity.RolePermission;
import com.dxy.shirodemo.entity.User;
import com.dxy.shirodemo.enumeration.IsUsableEnum;
import com.dxy.shirodemo.enumeration.PermissionTypeEnum;
import com.dxy.shirodemo.enumeration.UserStatusEnum;
import com.dxy.shirodemo.mapper.*;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;

import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class ShiroDemoApplicationTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShiroDemoApplicationTests.class);

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    private SimpleAccountRealm simpleAccountRealm=new SimpleAccountRealm();

    private DefaultSecurityManager securityManager=new DefaultSecurityManager();

    {
        simpleAccountRealm.addAccount("admin","123456");
        securityManager.setRealm(simpleAccountRealm);
    }

    @Test
    void shiroTest(){
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject=SecurityUtils.getSubject();
        UsernamePasswordToken token=new UsernamePasswordToken("admin5","123456");
        subject.login(token);

        LOGGER.info("isAccess: {}",subject.isAuthenticated());
    }

    @Test
    void permission() {

        List<Permission> permissions = new ArrayList<>();
        Permission permission1 = new Permission();
        permission1.setName("系统管理");
        permission1.setPermission("system-management");
        permission1.setType(PermissionTypeEnum.MENU);
        permission1.setStatus(IsUsableEnum.USABLE);

        Permission permission2 = new Permission();
        permission2.setName("查看用户列表");
        permission2.setParentId(1);
        permission2.setPermission("user:list");
        permission2.setType(PermissionTypeEnum.MENU);
        permission2.setStatus(IsUsableEnum.USABLE);

        Permission permission3 = new Permission();
        permission3.setName("新增用户");
        permission3.setParentId(2);
        permission3.setPermission("user:add");
        permission3.setType(PermissionTypeEnum.BUTTON);
        permission3.setStatus(IsUsableEnum.USABLE);

        Permission permission4 = new Permission();
        permission4.setName("修改用户");
        permission4.setParentId(2);
        permission4.setPermission("user:update");
        permission4.setType(PermissionTypeEnum.BUTTON);
        permission4.setStatus(IsUsableEnum.USABLE);

        Permission permission5 = new Permission();
        permission5.setName("删除用户");
        permission5.setParentId(2);
        permission5.setPermission("user:del");
        permission5.setType(PermissionTypeEnum.BUTTON);
        permission5.setStatus(IsUsableEnum.USABLE);

        permissions.add(permission1);
        permissions.add(permission2);
        permissions.add(permission3);
        permissions.add(permission4);
        permissions.add(permission5);

        int result = permissionMapper.insertBatch(permissions);
        LOGGER.info(result > 0 ? "执行成功。。。" : "执行失败。。。");
    }

    @Test
    void rolePermission() {
        Role admin=new Role();
        admin.setRole("admin");
        admin.setDescription("系统管理员");
        admin.setStatus(IsUsableEnum.USABLE);

        Role user=new Role();
        user.setRole("user");
        user.setDescription("用户");
        user.setStatus(IsUsableEnum.USABLE);

        List<Role> roles= Arrays.asList(admin,user);

        int result=roleMapper.insertBatch(roles);
        LOGGER.info(result > 0 ? "执行成功。。。" : "执行失败。。。");
    }

    @Test
    void user() {
        User admin=new User();
        admin.setUsername("admin");
        admin.setSalt("shiro_admin");
        admin.setPassword(new Md5Hash("123456", ByteSource.Util.bytes(admin.getSalt())).toString());
        admin.setStatus(UserStatusEnum.ENABLE);
        admin.setPhone("16621295036");
        admin.setEmail("dxy_52it@163.com");

        User user=new User();
        user.setUsername("user");
        user.setSalt("shiro_user");
        user.setPassword(new Md5Hash("654321",ByteSource.Util.bytes(user.getSalt())).toString());
        user.setStatus(UserStatusEnum.ENABLE);
        user.setPhone("18636830708");
        user.setEmail("389228699@qq.com");

        List<User> users=Arrays.asList(admin,user);
        int result=userMapper.insertBatch(users);
        LOGGER.info(result > 0 ? "执行成功。。。" : "执行失败。。。");
    }
}
