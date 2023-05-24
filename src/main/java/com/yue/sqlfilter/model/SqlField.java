package com.yue.sqlfilter.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author YueYue
 */
@Data
public class SqlField implements Serializable {
    /**
     * 源属性字段
     */
    private String field1;

    /**
     * 新属性字段
     */
    private String field2;

    /**
     * 主键属性 如：id
     */
    private String idField;
    private static final long serialVersionUID = 1L;

}
