package com.generalnetdisk.enums;

public enum DateTimeEnum{
    YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss"),YYYY_MM_DD("yyyy-MM-dd");

    private String pattern;

    DateTimeEnum(String pattern){
        this.pattern = pattern;
    }

    public String getPattern(){
        return pattern;
    }
}
