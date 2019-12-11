package com.example.demo;

import lombok.Data;

import java.io.Serializable;

/**
 * 工厂建模  position - nodeDataArray 数据信息
 */
@Data
public class FEamFactoryModePosition implements Serializable {
    private static final long serialVersionUID = -4903658174985558220L;
    /* 产线标志 true */
    private Boolean isGroup;
    /* 产线uuid */
    private String unid;
    /* 种类 */
    private String category;
    /* 唯一key */
    private Integer key;
    /* 地址引用 */
    private String source;
    /* 设备编号 */
    private String equipment_id;
    /* 名称 */
    private String name;
    /* 坐标 */
    private String loc;
    /* 产线key */
    private Integer group;
    /* 运行状态 */
    private String status;
    /* 启用状态 1启用  0停用 */
    private Integer useFlag;
    /* 瓶颈设备标识  0（默认）非瓶颈  1瓶颈 */
    private Integer isBottleneck;
}
