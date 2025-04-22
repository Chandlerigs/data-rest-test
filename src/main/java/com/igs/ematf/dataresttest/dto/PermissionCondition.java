package com.igs.ematf.dataresttest.dto;


import lombok.Data;

import java.util.List;


@Data
public class PermissionCondition {

    private String id;

    /**
     * 要比较的表，sql左表达式最后一个小数点之前的内容
     */
    private String tableName;
    /**
     * 要比较的字段名
     */
    private String fieldName;
    /**
     * 逻辑运算符
     */
    private String logicalOperator;
    /**
     * 比较运算符
     */
    private String comparisonOperator;
    /**
     * 比较运算符是否为否定状态
     */
    private Boolean not;
    /**
     * 比较的值
     */
    private String comparisonData;
    /**
     * 比较的值的类型
     */
    private Class<?> comparisonDataType;

    private List<PermissionCondition> children;
    public PermissionCondition() {
    }

    public PermissionCondition(String fieldName, String compOpt, String compData, Class<?> compDataType) {
        this.fieldName = fieldName;
        this.comparisonOperator = compOpt;
        this.comparisonData = compData;
        this.comparisonDataType = compDataType;
    }
}
