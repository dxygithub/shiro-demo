package com.dxy.shirodemo.entity;

import cloud.gouyiba.core.annotation.Column;
import cloud.gouyiba.core.annotation.Table;
import cloud.gouyiba.core.typehandler.IEnumTypeHandler;
import com.dxy.shirodemo.enumeration.UserStatusEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName User 用户
 * @Author duxiaoyu
 * @Date 2020/12/2 14:57
 * @Version 1.0
 */
@Data
@Table("sys_user")
public class User extends BaseEntity implements Serializable {

    private String username;

    private String password;

    private String salt;

    private String phone;

    private String email;

    @Column(typeHandler = IEnumTypeHandler.class)
    private UserStatusEnum status;
}
