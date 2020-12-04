package com.dxy.shirodemo.entity;

import cloud.gouyiba.core.annotation.Table;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName UserRole 用户角色
 * @Author duxiaoyu
 * @Date 2020/12/2 14:57
 * @Version 1.0
 */
@Data
@Table("sys_user_role")
public class UserRole extends BaseEntity implements Serializable {

    private Integer userId;

    private Integer roleId;
}
