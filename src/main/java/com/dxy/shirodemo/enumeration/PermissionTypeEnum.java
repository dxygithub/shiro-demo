package com.dxy.shirodemo.enumeration;

import cloud.gouyiba.core.enumation.IEnum;

/**
 * @ClassName PermissionTypeEnum 权限类型
 * @Author duxiaoyu
 * @Date 2020/12/2 14:29
 * @Version 1.0
 */
public enum PermissionTypeEnum implements IEnum<Integer> {

    /**
     * 目录
     */
    DIRECTORY(0),

    /**
     * 菜单
     */
    MENU(1),

    /**
     * 按钮
     */
    BUTTON(2);

    private Integer value;

    PermissionTypeEnum(Integer value){
        this.value=value;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
