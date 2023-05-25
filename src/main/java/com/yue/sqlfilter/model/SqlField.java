package com.yue.sqlfilter.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author YueYue
 */
@Data
public class SqlField implements Serializable {
    /**
     * 原字段
     */
    private String field1;

    /**
     * 新字段
     */
    private String field2;

    /**
     * 主键属性 如：id
     */
    private String idField;

    /**
     * 旧表名
     */
    private String oldTableName;

    /**
     * 新表名
     */
    private String newTableName;

    /**
     * SQL字段小驼峰或下划线
     * 默认 0下划线，1小驼峰 2全小写
     */
    private Integer convertType;
    private static final long serialVersionUID = 1L;

}
