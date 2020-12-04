package com.dxy.shirodemo.entity;

import cloud.gouyiba.core.annotation.Column;
import cloud.gouyiba.core.annotation.Table;
import cloud.gouyiba.core.typehandler.IEnumTypeHandler;
import com.dxy.shirodemo.enumeration.IsUsableEnum;
import com.dxy.shirodemo.enumeration.PermissionTypeEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName Permission 权限
 * @Author duxiaoyu
 * @Date 2020/12/2 14:35
 * @Version 1.0
 */
@Data
@Table("sys_permission")
public class Permission extends BaseEntity implements Serializable{

    private Integer parentId;

    private String name;

    private String permission;

    @Column(typeHandler = IEnumTypeHandler.class)
    private PermissionTypeEnum type;

    private String url;

    @Column(typeHandler = IEnumTypeHandler.class)
    private IsUsableEnum status;
}
