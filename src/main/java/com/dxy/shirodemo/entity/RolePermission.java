package com.dxy.shirodemo.entity;

import cloud.gouyiba.core.annotation.Table;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName RolePermission 角色权限
 * @Author duxiaoyu
 * @Date 2020/12/2 14:57
 * @Version 1.0
 */
@Data
@Table("sys_role_permission")
public class RolePermission extends BaseEntity implements Serializable {

    private Integer roleId;

    private Integer permissionId;
}
