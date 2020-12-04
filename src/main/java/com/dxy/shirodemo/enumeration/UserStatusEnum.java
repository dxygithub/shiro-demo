package com.dxy.shirodemo.enumeration;

import cloud.gouyiba.core.enumation.IEnum;

/**
 * @ClassName UserStatusEnum 用户状态
 * @Author duxiaoyu
 * @Date 2020/12/2 14:33
 * @Version 1.0
 */
public enum UserStatusEnum implements IEnum<Integer> {

    /**
     * 正常(启用)
     */
    ENABLE(1),

    /**
     * 冻结(停用)
     */
    DISABLE(2);

    private Integer value;

    UserStatusEnum(Integer value){
        this.value=value;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
