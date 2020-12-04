package com.dxy.shirodemo.enumeration;

import cloud.gouyiba.core.enumation.IEnum;

/**
 * @ClassName DelFlagEnum 删除标记
 * @Author duxiaoyu
 * @Date 2020/12/2 14:25
 * @Version 1.0
 */
public enum DelFlagEnum implements IEnum<Integer> {

    /**
     * 未删除
     */
    NO(2),

    /**
     * 已删除
     */
    YES(1);

    private Integer value;

    DelFlagEnum(Integer value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
