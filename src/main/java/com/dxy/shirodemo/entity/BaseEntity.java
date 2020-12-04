package com.dxy.shirodemo.entity;

import cloud.gouyiba.core.annotation.Column;
import cloud.gouyiba.core.annotation.Create;
import cloud.gouyiba.core.annotation.Update;
import cloud.gouyiba.core.typehandler.IEnumTypeHandler;
import com.dxy.shirodemo.enumeration.DelFlagEnum;
import lombok.Data;

/**
 * @ClassName BaseEntity 公用字段实体类
 * @Author duxiaoyu
 * @Date 2020/12/2 14:47
 * @Version 1.0
 */
@Data
public class BaseEntity {

    private Integer id;

    @Column(typeHandler = IEnumTypeHandler.class)
    private DelFlagEnum delFlag;

    private Long createTime;

    private String createUser;

    private Long modifyTime;

    /**
     * 新增操作，指定默认值
     */
    @Create
    public void insert() {
        this.createTime = System.currentTimeMillis() / 1000;
        this.createUser = "写代码的猴子";
        this.delFlag = DelFlagEnum.NO;
    }

    /**
     * 修改操作，指定默认值
     */
    @Update
    public void update() {
        this.modifyTime = System.currentTimeMillis() / 1000;
    }
}
