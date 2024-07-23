package com.codegeneration.bean;

public class FieldInfo {
    // 字段名
    private String FieldName;
    // bean属性名
    private String propertyName;

    private String sqlType;
    // 字段在java中的类型
    private String javaType;
    // 字段备注
    private String comment;
    // 是否自增
    private Boolean isAutoIncrease;

    public String getFieldName() {
        return FieldName;
    }

    public void setFieldName(String fieldName) {
        FieldName = fieldName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getSqlType() {
        return sqlType;
    }

    public void setSqlType(String sqlType) {
        this.sqlType = sqlType;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean getAutoIncrease() {
        return isAutoIncrease;
    }

    public void setAutoIncrease(Boolean autoIncrease) {
        isAutoIncrease = autoIncrease;
    }
}
