package com.dxy.shirodemo.configuration;

import com.dxy.shirodemo.entity.Permission;
import com.dxy.shirodemo.entity.Role;
import com.dxy.shirodemo.entity.User;
import com.dxy.shirodemo.enumeration.UserStatusEnum;
import com.dxy.shirodemo.service.RolePermissionService;
import com.dxy.shirodemo.service.UserRoleService;
import com.dxy.shirodemo.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @ClassName ShiroRealm
 * @Author duxiaoyu
 * @Date 2020/12/3 11:37
 * @Version 1.0
 */
public class ShiroRealm extends AuthorizingRealm {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShiroRealm.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RolePermissionService rolePermissionService;

    /**
     * 用户授权
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        LOGGER.info("开始授权校验===>");
        User user = (User) principalCollection.getPrimaryPrincipal();
        List<Role> roleList = userRoleService.getUserRoleByUserId(user.getId());
        List<Integer> roleIds = roleList.stream().map(Role::getId).collect(Collectors.toList());
        List<Permission> permissionList = rolePermissionService.getRolePermissionByRoleId(roleIds);

        Set<String> roleSet = roleList.stream().map(Role::getRole).collect(Collectors.toSet());
        Set<String> permissionSet = permissionList.stream().map(Permission::getPermission).collect(Collectors.toSet());
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(roleSet);
        authorizationInfo.setStringPermissions(permissionSet);

        LOGGER.info("角色权限授权成功...");
        return authorizationInfo;
    }

    /**
     * 用户认证
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        LOGGER.info("开始认证===>");
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String account = token.getUsername();
        User user = userService.getUserByAccount(account);
        // 当前用户不存在
        if (Objects.isNull(user)) {
            return null;
        }
        // 当前用户已被冻结
        if (user.getStatus() == UserStatusEnum.DISABLE) {
            throw new LockedAccountException();
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user, user.getPassword(), ByteSource.Util.bytes(user.getSalt()), this.getName());
        return authenticationInfo;
    }
}
