package com.dxy.shirodemo.enumeration;

import cloud.gouyiba.core.enumation.IEnum;

/**
 * @ClassName IsUsableEnum
 * @Author duxiaoyu
 * @Date 2020/12/2 14:40
 * @Version 1.0
 */
public enum IsUsableEnum implements IEnum<Integer> {

    /**
     * 不可用
     */
    NO_USABLE(1),

    /**
     * 可用
     */
    USABLE(2);

    private Integer value;

    IsUsableEnum(Integer value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
