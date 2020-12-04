package com.dxy.shirodemo.entity;

import cloud.gouyiba.core.annotation.Column;
import cloud.gouyiba.core.annotation.Table;
import cloud.gouyiba.core.typehandler.IEnumTypeHandler;
import com.dxy.shirodemo.enumeration.IsUsableEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName Role 角色
 * @Author duxiaoyu
 * @Date 2020/12/2 14:56
 * @Version 1.0
 */
@Data
@Table("sys_role")
public class Role extends BaseEntity implements Serializable {

    private String role;

    private String description;

    @Column(typeHandler = IEnumTypeHandler.class)
    private IsUsableEnum status;
}
