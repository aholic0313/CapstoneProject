package com.codegeneration.builder;

import com.codegeneration.bean.Constants;
import com.codegeneration.bean.FieldInfo;
import com.codegeneration.bean.TableInfo;
import com.codegeneration.utils.PropertiesUtils;
import com.codegeneration.utils.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuildTable {
    private static final Logger logger = LoggerFactory.getLogger(BuildTable.class);
    private static Connection conn = null;

    private static String SQL_SHOW_TABLE_STATUS = "show table STATUS;";
    private static String SQL_SHOW_TABLE_FIELDS = "show full fields from %s;";
    private static String SQL_SHOW_TABLE_INDEX = "show index from %s;";

    static {
        String driverName = PropertiesUtils.getString("db.driver.name");
        String url = PropertiesUtils.getString("db.url");
        String user = PropertiesUtils.getString("db.username");
        String password = PropertiesUtils.getString("db.password");
        try {
            Class.forName(driverName);
            conn = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            logger.error("数据库连接失败", e);
        }

    }

    /**
     * 获取tableStatus查询结果
     */
    public static List<TableInfo> getTables() {
        PreparedStatement ps = null;
        ResultSet tableResult = null;
        List<TableInfo> tableInfoList = new ArrayList();

        try {
            // 连接数据库，load查询语句
            ps = conn.prepareStatement(SQL_SHOW_TABLE_STATUS);
            // 执行query，把返回的结果存在tableResult里
            tableResult = ps.executeQuery();
            while (tableResult.next()) {
                // 获取内容
                String tableName = tableResult.getString("name");
                String comment = tableResult.getString("comment");
                String beanName = tableName;
                // 如果表名有无意义前缀（如tb_user_info中的tb_无意义），则去掉
                if (Constants.IGNORE_TABLE_PREFIX) {
                    beanName = tableName.substring(beanName.indexOf("_") + 1);
                }
                // 把user_info改为userInfo
                beanName = processField(beanName, true);

                // 开始写入tableInfo对象内（一些不用加工的数据可以先存）
                TableInfo tableInfo = new TableInfo();
                tableInfo.setTableName(tableName);
                tableInfo.setBeanName(beanName);
                tableInfo.setComment(comment);
                tableInfo.setBeanParamName(beanName + Constants.SUFFIX_BEAN_QUERY);

                // 获取字段信息
                readFieldInfo(tableInfo);
                //获取索引信息
                getKeyIndexInfo(tableInfo);
//                logger.info("tableinfo:{}",JsonUtils.convertObj2Json(tableInfo));
                tableInfoList.add(tableInfo);
            }
        } catch (Exception e) {
            logger.error("读取表失败");
        } finally {
            if (tableResult != null) {
                try {
                    tableResult.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        // 函数调用返回
        return tableInfoList;
    }

    /**
     * 获取字段信息
     *
     * @param tableInfo
     * @return
     */
    private static void readFieldInfo(TableInfo tableInfo) {
        PreparedStatement ps = null;
        ResultSet filedRes = null;
        List<FieldInfo> fieldInfoList = new ArrayList();
        // Fuzzy，dateStart和dateEnd扩展字段
        List<FieldInfo> fieldExtendList = new ArrayList();
        Boolean haveDateTime = false;
        Boolean haveDate = false;
        Boolean haveBigDecimal = false;
        try {
            ps = conn.prepareStatement(String.format(SQL_SHOW_TABLE_FIELDS, tableInfo.getTableName()));
            filedRes = ps.executeQuery();
            while (filedRes.next()) {
                String field = filedRes.getString("field");
                String type = filedRes.getString("type");
                String extra = filedRes.getString("extra");
                String comment = filedRes.getString("comment");

                // 把sqltype中的类型长度（varchar(30)）去掉
                if (type.indexOf("(") > 0) {
                    type = type.substring(0, type.indexOf("("));
                }
                String propertyName = processField(field, false);

                FieldInfo fieldInfo = new FieldInfo();

                fieldInfoList.add(fieldInfo);
                fieldInfo.setFieldName(field);
                fieldInfo.setComment(comment);
                fieldInfo.setSqlType(type);
                // extra字段内存在auto_increment字符串代表是自增的
                fieldInfo.setAutoIncrease("auto_increment".equalsIgnoreCase(extra) ? true : false);
                fieldInfo.setPropertyName(propertyName);
                fieldInfo.setJavaType(processJaveType(type));

                if (ArrayUtils.contains(Constants.SQL_DATE_TYPES, type)) {
                    haveDate = true;
                }   // date和datetime在后面加注解是不一样的，需要区分开
                if (ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, type)) {
                    haveDateTime = true;
                }
                if (ArrayUtils.contains(Constants.SQL_DECIMAL_TYPE, type)) {
                    haveBigDecimal = true;
                }

                // String扩展属性
                if (ArrayUtils.contains(Constants.SQL_STRING_TYPE, type)) {
                    FieldInfo fuzzyField = new FieldInfo();
                    fuzzyField.setJavaType(fieldInfo.getJavaType());
                    fuzzyField.setPropertyName(propertyName + Constants.SUFFIX_BEAN_QUERY_FUZZY);
                    fuzzyField.setFieldName(fieldInfo.getFieldName());
                    fuzzyField.setSqlType(type);
                    fieldExtendList.add(fuzzyField);
                }
                // Date扩展属性
                if (ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, type) || ArrayUtils.contains(Constants.SQL_DATE_TYPES, type)) {
                    FieldInfo dateStart = new FieldInfo();
                    dateStart.setJavaType("String");
                    dateStart.setPropertyName(propertyName + Constants.SUFFIX_BEAN_QUERY_TIME_START);
                    dateStart.setSqlType(type);
                    fieldExtendList.add(dateStart);
                    dateStart.setFieldName(fieldInfo.getFieldName());

                    FieldInfo dateEnd = new FieldInfo();
                    dateEnd.setJavaType("String");
                    dateEnd.setPropertyName(propertyName + Constants.SUFFIX_BEAN_QUERY_TIME_END);
                    dateEnd.setSqlType(type);
                    dateEnd.setFieldName(fieldInfo.getFieldName());
                    fieldExtendList.add(dateEnd);
                }
            }
            tableInfo.setHaveDateTime(haveDateTime);
            tableInfo.setHaveDate(haveDate);
            tableInfo.setHaveBigDecimal(haveBigDecimal);
            // 直接把fieldinfo写入tableinfo对象内，该函数不需要返回值
            tableInfo.setFieldInfoList(fieldInfoList);
            tableInfo.setExtendFieldInfoList(fieldExtendList);

        } catch (Exception e) {
            logger.error("读取字段失败");
        } finally {
            if (filedRes != null) {
                try {
                    filedRes.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取索引信息
     *
     * @param tableInfo
     * @return
     */
    private static List<FieldInfo> getKeyIndexInfo(TableInfo tableInfo) {
        PreparedStatement ps = null;
        ResultSet filedRes = null;
        List<FieldInfo> fieldInfoList = new ArrayList();

        try {

            Map<String, FieldInfo> tempMap = new HashMap();
            for (FieldInfo fieldInfo : tableInfo.getFieldInfoList()) {
                tempMap.put(fieldInfo.getFieldName(), fieldInfo);
            }

            ps = conn.prepareStatement(String.format(SQL_SHOW_TABLE_INDEX, tableInfo.getTableName()));
            filedRes = ps.executeQuery();
            while (filedRes.next()) {
                String keyName = filedRes.getString("key_name");
                Integer nonUnique = filedRes.getInt("non_unique");
                String columnName = filedRes.getString("column_name");
                if (nonUnique == 1) {
                    continue;
                }
                List<FieldInfo> keyFieldList = tableInfo.getKeyIndexMap().get(keyName);
                if (null == keyFieldList) {
                    keyFieldList = new ArrayList();
                    tableInfo.getKeyIndexMap().put(keyName, keyFieldList);
                }
                keyFieldList.add(tempMap.get(columnName));

            }
        } catch (Exception e) {
            logger.error("读取索引失败");
        } finally {
            if (filedRes != null) {
                try {
                    filedRes.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return fieldInfoList;
    }

    /**
     * 解析字段，将下划线驼峰名转为大小写驼峰
     *
     * @param field
     * @param upperCaseFirstLetter
     * @return
     */
    private static String processField(String field, Boolean upperCaseFirstLetter) {
        StringBuffer stringBuffer = new StringBuffer();
        String[] fields = field.split("_");
        stringBuffer.append(upperCaseFirstLetter ? StringUtils.UpperCaseFirstLetter(fields[0]) : fields[0]);
        for (int i = 1, len = fields.length; i < len; i++) {
            stringBuffer.append(StringUtils.UpperCaseFirstLetter(fields[i]));
        }
        return stringBuffer.toString();
    }

    /**
     * 将mysql的类型转换为java的类型
     *
     * @param type mysql类型
     * @return javaType
     */
    private static String processJaveType(String type) {
        if (ArrayUtils.contains(Constants.SQL_INTEGER_TYPE, type)) {
            return "Integer";
        } else if (ArrayUtils.contains(Constants.SQL_LONG_TYPE, type)) {
            return "Long";
        } else if (ArrayUtils.contains(Constants.SQL_STRING_TYPE, type)) {
            return "String";
        } else if (ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, type) || ArrayUtils.contains(Constants.SQL_DATE_TYPES, type)) {
            return "Date";
        } else if (ArrayUtils.contains(Constants.SQL_DECIMAL_TYPE, type)) {
            return "BigDecimal";
        } else {
            throw new RuntimeException("类型无法识别" + type);
        }
    }
}
