package com.codegeneration.bean;

import com.codegeneration.utils.PropertiesUtils;

public class Constants {
    // 需要忽略的前缀
    public static Boolean IGNORE_TABLE_PREFIX;
    // BEAN查询后缀
    public static String SUFFIX_BEAN_QUERY;
    public static String SUFFIX_BEAN_QUERY_FUZZY;
    public static String SUFFIX_BEAN_QUERY_TIME_START;
    public static String SUFFIX_BEAN_QUERY_TIME_END;
    public static String SUFFIX_MAPPERS;
    public static String SUFFIX_SERVICE;
    public static String SUFFIX_SERVICE_IMPL;

    // 需要忽略的属性
    public static String IGNORE_BEAN_TOJSON_FIELD;
    public static String IGNORE_BEAN_TOJSON_EXPRESSION;
    public static String IGNORE_BEAN_TOJSON_CLASS;
    // 日期格式序列化
    public static String BEAN_DATE_FORMAT_EXPRESSION;
    public static String BEAN_DATE_FORMAT_CLASS;
    // 日期格式反序列化
    public static String BEAN_DATE_UNFORMAT_EXPRESSION;
    public static String BEAN_DATE_UNFORMAT_CLASS;

    // 基路径
    public static String PATH_BASE;
    public static String PATH_BASE_JAVA;
    public static String PATH_BASE_RESOURCE;

    //各种实体路径【即基路径/java（或source）/包路径/实体/各种po和vo等等】
    public static String PATH_PO;
    public static String PATH_VO;
    public static String PATH_QUERY;
    public static String PATH_UTILS;
    public static String PATH_ENUM;
    public static String PATH_MAPPER;
    public static String PATH_SERVICE;
    public static String PATH_CONTROLLER;
    public static String PATH_EXCEPTION;
    public static String PATH_SERVICE_IMPL;
    public static String PATH_MAPPERS_XML;

    // java/resource路径
    public static String PATH_JAVA = "java";
    public static String PATH_RESOURCE = "resources";

    // 包路径
    public static String PACKAGE_BASE;
    public static String PACKAGE_PO;
    public static String PACKAGE_VO;
    public static String PACKAGE_QUERY;
    public static String PACKAGE_UTILS;
    public static String PACKAGE_ENUM;
    public static String PACKAGE_MAPPER;
    public static String PACKAGE_SERVICE;
    public static String PACKAGE_CONTROLLER;
    public static String PACKAGE_EXCEPTION;
    public static String PACKAGE_SERVICE_IMPL;
    public static String PACKAGE_MAPPERS_XML;

    // 对应的sql类型
    public final static String[] SQL_DATE_TIME_TYPES = new String[]{"datetime", "timestamp"};
    public final static String[] SQL_DATE_TYPES = new String[]{"date"};
    public static final String[] SQL_DECIMAL_TYPE = new String[]{"decimal", "double", "float"};
    public static final String[] SQL_STRING_TYPE = new String[]{"char", "varchar", "text", "mediumtext", "longtext"};
    public static final String[] SQL_INTEGER_TYPE = new String[]{"int", "tinyint"};
    public static final String[] SQL_LONG_TYPE = new String[]{"bigint"};


    static {
        // 忽略属性
        IGNORE_TABLE_PREFIX = Boolean.valueOf(PropertiesUtils.getString("ignore.table.prefix"));

        // Query后缀
        SUFFIX_BEAN_QUERY = PropertiesUtils.getString("suffix.bean.query");
        SUFFIX_BEAN_QUERY_TIME_END = PropertiesUtils.getString("suffix.bean.query.time.end");
        SUFFIX_BEAN_QUERY_TIME_START = PropertiesUtils.getString("suffix.bean.query.time.start");
        SUFFIX_BEAN_QUERY_FUZZY = PropertiesUtils.getString("suffix.bean.query.fuzzy");
        SUFFIX_MAPPERS = PropertiesUtils.getString("suffix.mappers");
        SUFFIX_SERVICE = PropertiesUtils.getString("suffix.service");
        SUFFIX_SERVICE_IMPL = PropertiesUtils.getString("suffix.service.impl");

        // 需要忽略的前缀
        IGNORE_BEAN_TOJSON_FIELD = PropertiesUtils.getString("ignore.bean.tojson.field");
        IGNORE_BEAN_TOJSON_EXPRESSION = PropertiesUtils.getString("ignore.bean.tojson.expression");
        IGNORE_BEAN_TOJSON_CLASS = PropertiesUtils.getString("ignore.bean.tojson.class");
        // 序列化
        BEAN_DATE_FORMAT_EXPRESSION = PropertiesUtils.getString("bean.date.format.expression");
        BEAN_DATE_FORMAT_CLASS = PropertiesUtils.getString("bean.date.format.expression.class");
        // 反序列化
        BEAN_DATE_UNFORMAT_EXPRESSION = PropertiesUtils.getString("bean,date.unformat.expression");
        BEAN_DATE_UNFORMAT_CLASS = PropertiesUtils.getString("bean,date.unformat.expression.class");

        PACKAGE_BASE = PropertiesUtils.getString("package.base");
        PACKAGE_PO = PACKAGE_BASE + "." + PropertiesUtils.getString("package.po");
        PACKAGE_VO = PACKAGE_BASE + "." + PropertiesUtils.getString("package.vo");
        PACKAGE_UTILS = PACKAGE_BASE + "." + PropertiesUtils.getString("package.utils");
        PACKAGE_ENUM = PACKAGE_BASE + "." + PropertiesUtils.getString("package.enum");
        PACKAGE_MAPPER = PACKAGE_BASE + "." + PropertiesUtils.getString("package.mapper");
        PACKAGE_SERVICE = PACKAGE_BASE + "." + PropertiesUtils.getString("package.service");
        PACKAGE_SERVICE_IMPL = PACKAGE_BASE + "." + PropertiesUtils.getString("package.service.impl");
        PACKAGE_CONTROLLER = PACKAGE_BASE + "." + PropertiesUtils.getString("package.controller");
        PACKAGE_EXCEPTION = PACKAGE_BASE + "." + PropertiesUtils.getString("package.exception");
        PACKAGE_MAPPERS_XML = PACKAGE_BASE + "." + PropertiesUtils.getString("package.xml");
        PACKAGE_QUERY = PACKAGE_BASE + "." + PropertiesUtils.getString("package.query");

        PATH_BASE = PropertiesUtils.getString("path.base");
        PATH_BASE_JAVA = PATH_BASE + "/" + PATH_JAVA + "/" + PACKAGE_BASE.replace(".", "/");
        PATH_BASE_RESOURCE = PATH_BASE + "/" + PATH_RESOURCE + "/" + PACKAGE_BASE.replace(".", "/");

        PATH_PO = PATH_BASE_JAVA + "/" + PropertiesUtils.getString("package.po").replace(".", "/");
        PATH_VO = PATH_BASE_JAVA + "/" + PropertiesUtils.getString("package.vo").replace(".", "/");
        PATH_QUERY = PATH_BASE_JAVA + "/" + PropertiesUtils.getString("package.query").replace(".", "/");
        PATH_UTILS = PATH_BASE_JAVA + "/" + PropertiesUtils.getString("package.utils");
        PATH_ENUM = PATH_BASE_JAVA + "/" + PropertiesUtils.getString("package.enum");
        PATH_MAPPER = PATH_BASE_JAVA + "/" + PropertiesUtils.getString("package.mapper");
        PATH_SERVICE = PATH_BASE_JAVA + "/" + PropertiesUtils.getString("package.service");
        PATH_EXCEPTION = PATH_BASE_JAVA + "/" + PropertiesUtils.getString("package.exception");
        PATH_SERVICE_IMPL = PATH_BASE_JAVA + "/" + PropertiesUtils.getString("package.service.impl").replace(".", "/");
        PATH_CONTROLLER = PATH_BASE_JAVA + "/" + PropertiesUtils.getString("package.controller");

        PATH_MAPPERS_XML = PATH_BASE_RESOURCE + "/" + PropertiesUtils.getString("package.mapper");

    }

    public static void main(String[] args) {
        System.out.println(PACKAGE_BASE);
        System.out.println(PACKAGE_PO);
        System.out.println(PATH_CONTROLLER);
        System.out.println(PACKAGE_CONTROLLER);
//        System.out.println(PACKAGE_MAPPERS_XML);
    }
}
