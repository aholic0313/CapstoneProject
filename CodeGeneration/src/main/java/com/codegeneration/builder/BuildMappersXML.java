package com.codegeneration.builder;

import com.codegeneration.bean.Constants;
import com.codegeneration.bean.FieldInfo;
import com.codegeneration.bean.TableInfo;
import com.codegeneration.utils.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuildMappersXML {
    private static final Logger logger = LoggerFactory.getLogger(BuildMappers.class);

    private static final String BASE_COLUMN_LIST = "base_column_list";
    private static final String BASE_QUERY_CONDITION = "base_query_condition";
    private static final String BASE_QUERY_CONDITION_EXTEND = "base_query_condition_extend";
    private static final String QUERY_CONDITION = "query_condition";

    public static void execute(TableInfo tableInfo) {
        File folder = new File((Constants.PATH_MAPPERS_XML));
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String className = tableInfo.getBeanName() + Constants.SUFFIX_MAPPERS;
        String poClass = Constants.PACKAGE_PO + "." + tableInfo.getBeanName();
        File mapperFile = new File(folder, className + ".xml");


        OutputStream outs = null;
        OutputStreamWriter outsw = null;
        BufferedWriter bw = null;
        try {
            outs = new FileOutputStream(mapperFile);
            outsw = new OutputStreamWriter(outs, "utf8");
            bw = new BufferedWriter(outsw);

            /**
             * 固定头Start
             */
            bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
            bw.newLine();
            bw.write("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"");
            bw.newLine();
            bw.write("\t\t\"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">");
            bw.newLine();
            // <mapper>
            bw.write("<mapper namespace=\"" + Constants.PACKAGE_MAPPER + "." + className + "\">");
            bw.newLine();
            bw.newLine();
            /**
             * 固定头End
             */

            /**
             * 实体映射Start
             */
            bw.write("\t<!-- 实体映射 -->");
            bw.newLine();
            bw.write("\t<resultMap id=\"base_result_map\" type=\"" + poClass + "\">");
            bw.newLine();
            bw.newLine();

            Map<String, List<FieldInfo>> keyIdxMap = tableInfo.getKeyIndexMap();
            FieldInfo fieldID = null;
            for (Map.Entry<String, List<FieldInfo>> entry : keyIdxMap.entrySet()) {
                if ("PRIMARY".equals(entry.getKey())) {
                    List<FieldInfo> fieldInfoList = entry.getValue();
                    if (fieldInfoList.size() == 1) {
                        fieldID = fieldInfoList.get(0);
                        break;
                    }
                }
            }

            for (FieldInfo fieldInfo : tableInfo.getFieldInfoList()) {
                bw.write("\t\t<!-- " + fieldInfo.getComment() + " -->");
                bw.newLine();
                String key = "";
                if (fieldID != null && fieldInfo.getPropertyName().equals(fieldID.getPropertyName())) {
                    key = "id";
                } else {
                    key = "result";
                }
                bw.write("\t\t<" + key + " property=\"" + fieldInfo.getPropertyName() + "\" column=\"" + fieldInfo.getFieldName() + "\" />");
                bw.newLine();
                bw.newLine();
            }

            bw.write("\t</resultMap>");
            bw.newLine();
            bw.newLine();
            /**
             * 实体映射End
             */

            /**
             * 通用查询列Start
             */
            bw.write("\t<!-- 通用查询列 -->");
            bw.newLine();

            bw.write("\t<sql id=\"" + BASE_COLUMN_LIST + "\">");
            bw.newLine();
            StringBuilder columnBuilder = new StringBuilder();
            for (FieldInfo fieldInfo : tableInfo.getFieldInfoList()) {
                columnBuilder.append(fieldInfo.getFieldName()).append(",");
            }
            String columnBuilderStr = columnBuilder.substring(0, columnBuilder.lastIndexOf(","));
            bw.write("\t\t" + columnBuilderStr);
            bw.newLine();
            bw.write("\t</sql>");
            bw.newLine();
            bw.newLine();
            /**
             * 通用查询列End
             */

            /**
             * 基础查询条件Start
             */
            bw.write("\t<!-- 基础查询条件 -->");
            bw.newLine();

            bw.write("\t<sql id=\"" + BASE_QUERY_CONDITION + "\">");
            bw.newLine();
            bw.write("\t\t<where>");
            bw.newLine();

            for (FieldInfo fieldInfo : tableInfo.getFieldInfoList()) {
                String moreStringQuery = "";
                if (ArrayUtils.contains(Constants.SQL_STRING_TYPE, fieldInfo.getSqlType())) {
                    moreStringQuery = "and query." + fieldInfo.getPropertyName() + " != \'\'";
                }
                bw.write("\t\t\t<if test=\"query." + fieldInfo.getPropertyName() + " != null " + moreStringQuery + "\">");
                bw.newLine();
                bw.write("\t\t\t\tand id = #{query." + fieldInfo.getPropertyName() + "}");
                bw.newLine();
                bw.write("\t\t\t</if>");
                bw.newLine();
            }
            bw.write("\t\t</where>");
            bw.newLine();
            bw.write("\t</sql>");
            bw.newLine();
            bw.newLine();
            /**
             * 基础查询条件End
             */

            /**
             * 扩展查询条件Start
             */
            bw.write("\t<!-- 扩展查询条件 -->");
            bw.newLine();
            bw.write("\t<sql id=\"" + BASE_QUERY_CONDITION_EXTEND + "\">");
            bw.newLine();
            bw.write("\t\t<where>");
            bw.newLine();

            for (FieldInfo fieldInfo : tableInfo.getExtendFieldInfoList()) {
                bw.write("\t\t\t<if test=\"query." + fieldInfo.getPropertyName() + " != null and query." + fieldInfo.getPropertyName() + " != ''\">");
                bw.newLine();
                // DateStart/End 语句的共同前缀
                String dateStEn = "\t\t\t\t<![CDATA[ and " + fieldInfo.getFieldName();

                if (ArrayUtils.contains(Constants.SQL_STRING_TYPE, fieldInfo.getSqlType())) {
                    bw.write("\t\t\t\tand " + fieldInfo.getFieldName() + " like concat('%', #{query." + fieldInfo.getPropertyName() + "}, '%')");
                    bw.newLine();
                } else if (ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, fieldInfo.getSqlType()) || ArrayUtils.contains(Constants.SQL_DATE_TYPES, fieldInfo.getSqlType())) {
                    if (fieldInfo.getPropertyName().endsWith(Constants.SUFFIX_BEAN_QUERY_TIME_START)) {
                        bw.write(dateStEn + " >= str_to_date(#{query." + fieldInfo.getPropertyName() + "}, '%Y-%m-%d') ]]>");
                        bw.newLine();
                    } else if (fieldInfo.getPropertyName().endsWith(Constants.SUFFIX_BEAN_QUERY_TIME_END)) {
                        bw.write(dateStEn + " < date_sub(str_to_date(#{query." + fieldInfo.getPropertyName() + "}, '%Y-%m-%d'), interval -1 day) ]]>");
                        bw.newLine();
                    }
                }

                bw.write("\t\t\t</if>");
                bw.newLine();
            }

            bw.write("\t\t</where>");
            bw.newLine();
            bw.write("\t</sql>");
            bw.newLine();
            bw.newLine();
            /**
             * 扩展查询条件End
             */

            /**
             * 通用查询条件Start
             */
            bw.write("\t<!-- 通用查询条件 -->");
            bw.newLine();
            bw.write("\t<sql id=\"" + QUERY_CONDITION + "\">");
            bw.newLine();

            bw.write("\t\t\t<include refid=\"" + BASE_QUERY_CONDITION + "\"/>");
            bw.newLine();
            bw.write("\t\t\t<include refid=\"" + BASE_QUERY_CONDITION_EXTEND + "\"/>");
            bw.newLine();

            bw.write("\t</sql>");
            bw.newLine();
            /**
             * 通用查询条件End
             */

            /**
             * 查询列表Start
             */
            bw.write("\t<!-- 查询列表 -->");
            bw.newLine();
            bw.write("\t<select id=\"selectList\" resultMap=\"base_result_map\">");
            bw.newLine();
            bw.write("\t\tSELECT <include refid=\"" + BASE_COLUMN_LIST + "\"/> FROM " + tableInfo.getTableName() + " <include refid=\"" + QUERY_CONDITION + "\"/>");
            bw.newLine();

            bw.write("\t\t<if test=\"query.orderBy != null\">order by ${query.orderBy}</if>");
            bw.newLine();
            bw.write("\t\t<if test=\"query.simplePage != null\">limit #{query.simplePage.start},#{query.simplePage.end}</if>");
            bw.newLine();

            bw.write("\t</select>");
            bw.newLine();
            bw.newLine();
            /**
             * 查询列表End
             */

            /**
             * 查询数量Start
             */
            bw.write("\t<!-- 查询数量 -->");
            bw.newLine();
            bw.write("\t<select id=\"selectCount\" resultType=\"java.lang.Integer\">");
            bw.newLine();
            bw.write("\t\tSELECT count(1) FROM " + tableInfo.getTableName() + "<include refid=\"" + QUERY_CONDITION + "\"/>");
            bw.newLine();
            bw.write("\t</select>");
            bw.newLine();
            bw.newLine();
            /**
             * 查询数量End
             */

            /**
             * 单条插入Start
             */
            bw.write("\t<!-- 单条插入 -->");
            bw.newLine();
            bw.write("\t<insert id=\"insert\" parameterType=\"" + poClass + "\">");
            bw.newLine();

            FieldInfo autoIncreaField = null;
            for (FieldInfo field : tableInfo.getFieldInfoList()) {
                if (field.getAutoIncrease() != null && field.getAutoIncrease()) {
                    autoIncreaField = field;
                    break;
                }
            }

            if (autoIncreaField != null) {
                bw.write("\t\t<selectKey keyProperty=\"bean." + autoIncreaField.getFieldName() + "\" resultType=\"" + autoIncreaField.getJavaType() + "\" order=\"AFTER\">SELECT LAST_INSERT_ID()</selectKey>");
                bw.newLine();
            }

            bw.write("\t\tINSERT INTO " + tableInfo.getTableName());
            bw.newLine();

            writeInsertTrimTag(bw, tableInfo);

            bw.write("\t</insert>");
            bw.newLine();
            bw.newLine();
            /**
             * 单条插入End
             */

            /**
             * 单条插入或更新（匹配有值的字段）Start
             */
            bw.write("\t<!-- 单条插入或更新（匹配有值的字段） -->");
            bw.newLine();
            bw.write("\t<insert id=\"insertOrUpdate\" parameterType=\"" + poClass + "\">");
            bw.newLine();
            bw.write("\t\tINSERT INTO " + tableInfo.getTableName());
            bw.newLine();

            writeInsertTrimTag(bw, tableInfo);

            bw.write("\t\ton DUPLICATE key update");
            bw.newLine();
            bw.write("\t\t<trim prefix=\"\" suffix=\"\" suffixOverrides=\",\">");
            bw.newLine();
            // 标记为主键索引的需要过滤掉（不允许修改）
            Map<String, String> tempMap = new HashMap<>();
            for (Map.Entry<String, List<FieldInfo>> entry : keyIdxMap.entrySet()) {
                List<FieldInfo> fieldInfoList = entry.getValue();
                for (FieldInfo item : fieldInfoList) {
                    tempMap.put(item.getFieldName(), item.getFieldName());
                }
            }
            for (FieldInfo field : tableInfo.getFieldInfoList()) {
                // 过滤
                if (tempMap.get(field.getFieldName()) != null) {
                    continue;
                }
                bw.write("\t\t\t<if test=\"bean." + field.getPropertyName() + " != null\">");
                bw.newLine();
                bw.write("\t\t\t\t" + field.getFieldName() + " = VALUES(" + field.getFieldName() + "),");
                bw.newLine();
                bw.write("\t\t\t</if>");
                bw.newLine();
            }
            bw.write("\t\t</trim>");
            bw.newLine();
            bw.write("\t</insert>");
            bw.newLine();
            bw.newLine();
            /**
             * 单条插入或更新（匹配有值的字段）End
             */

            /**
             * 批量插入Start
             */
            bw.write("\t<!-- 批量插入 -->");
            bw.newLine();
            bw.write("\t<insert id=\"insertBatch\" parameterType=\"" + poClass + "\">");
            bw.newLine();

            writeInsertBatchFor(bw, tableInfo);

            bw.write("\t</insert>");
            bw.newLine();
            bw.newLine();
            /**
             * 批量插入End
             */

            /**
             * 批量插入或更新Start
             */
            bw.write("\t<!-- 批量插入或更新(操作前需要把原数据取出，然后与新数据合并，否则会把原数据字段冲掉) -->");
            bw.newLine();
            bw.write("\t<insert id=\"insertOrUpdateBatch\" parameterType=\"" + poClass + "\">");
            bw.newLine();

            writeInsertBatchFor(bw, tableInfo);

            bw.write("\t\ton DUPLICATE key update");
            bw.newLine();
            StringBuffer duplicateField = new StringBuffer();
            for (FieldInfo fieldInfo : tableInfo.getFieldInfoList()) {
//                if (fieldInfo.getAutoIncrease() != null && fieldInfo.getAutoIncrease()) {
//                    continue;
//                }
                duplicateField.append("\t\t").append(fieldInfo.getFieldName()).append(" = VALUES(").append(fieldInfo.getFieldName()).append("),\n");
            }
            String duplicateFieldStr = duplicateField.substring(0, duplicateField.lastIndexOf(",\n"));
            bw.write(duplicateFieldStr);
            bw.newLine();

            bw.write("\t</insert>");
            bw.newLine();
            bw.newLine();
            /**
             * 批量插入End
             */

            /**
             * 根据主键操作（查询/删除/修改） Start
             */
            for (Map.Entry<String, List<FieldInfo>> entry : keyIdxMap.entrySet()) {
                List<FieldInfo> keyFieldList = entry.getValue();

                StringBuilder methodName = new StringBuilder();

                Integer index = 0;
                StringBuffer paramName = new StringBuffer();
                for (FieldInfo fieldInfo : keyFieldList) {
                    index++;
                    // 方法名字符串
                    methodName.append(StringUtils.UpperCaseFirstLetter(fieldInfo.getPropertyName()));
                    paramName.append(fieldInfo.getFieldName() + "=#{" + fieldInfo.getPropertyName() + "}");
                    if (index < keyFieldList.size()) {
                        methodName.append("And");
                        paramName.append(" and ");
                    }
                }
                bw.write("\t<!-- 根据" + methodName + "查询 -->");
                bw.newLine();
                bw.write("\t<select id=\"selectBy" + methodName + "\" resultMap=\"base_result_map\">");
                bw.newLine();
                bw.write("\t\tselect");
                bw.newLine();
                bw.write("\t\t<include refid=\"" + BASE_COLUMN_LIST + "\"/>");
                bw.newLine();
                bw.write("\t\tfrom " + tableInfo.getTableName() + " where " + paramName);
                bw.newLine();
                bw.write("\t</select>");
                bw.newLine();
                bw.newLine();

                bw.write("\t<!-- 根据" + methodName + "更新 -->");
                bw.newLine();
                bw.write("\t<update id=\"updateBy" + methodName + "\" parameterType=\"" + poClass + "\">");
                bw.newLine();
                bw.write("\t\tUPDATE " + tableInfo.getTableName());
                bw.newLine();
                bw.write("\t\t<set>");
                bw.newLine();
                for (FieldInfo fieldInfo : tableInfo.getFieldInfoList()) {
//                    if (fieldInfo.getAutoIncrease() != null && fieldInfo.getAutoIncrease()) {
//                        continue;
//                    }
                    bw.write("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() + " != null\">");
                    bw.newLine();
                    bw.write("\t\t\t\t" + fieldInfo.getFieldName() + " = #{bean." + fieldInfo.getPropertyName() + "},");
                    bw.newLine();
                    bw.write("\t\t\t</if>");
                    bw.newLine();
                }
                bw.write("\t\t</set>");
                bw.newLine();
                bw.write("\tWHERE " + paramName);
                bw.newLine();
                bw.write("\t</update>");
                bw.newLine();
                bw.newLine();

                bw.write("\t<!-- 根据" + methodName + "删除 -->");
                bw.newLine();
                bw.write("\t<delete id=\"deleteBy" + methodName + "\">");
                bw.newLine();
                bw.write("\t\tdelete from " + tableInfo.getTableName() + " where " + paramName);
                bw.newLine();
                bw.write("\t</delete>");
                bw.newLine();
                bw.newLine();
            }
            /**
             * 根据主键操作（查询/删除/修改）End
             */

            // </mapper>
            bw.write("</mapper>");
            bw.newLine();
            bw.newLine();
            bw.flush();
        } catch (Exception e) {
            logger.error("创建MappersXML失败", e);
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
            if (outsw != null) {
                try {
                    outsw.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
            if (outs != null) {
                try {
                    outs.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }
    }

    private static void writeInsertBatchFor(BufferedWriter bw, TableInfo tableInfo) throws Exception {
        StringBuffer tempFieldName = new StringBuffer();
        for (FieldInfo field : tableInfo.getFieldInfoList()) {
            if (field.getAutoIncrease() != null && field.getAutoIncrease()) {
                continue;
            }
            tempFieldName.append(field.getFieldName()).append(",");
        }
        String fieldNameStr = tempFieldName.substring(0, tempFieldName.lastIndexOf(","));
        bw.write("\t\tINSERT INTO " + tableInfo.getTableName() + "(" + fieldNameStr + ")values");
        bw.newLine();
        bw.write("\t\t<foreach collection=\"list\" item=\"item\" separator=\",\" >");
        bw.newLine();
        StringBuffer foreachLine = new StringBuffer();
        foreachLine.append("\t\t\t(");
        for (FieldInfo field : tableInfo.getFieldInfoList()) {
            if (field.getAutoIncrease() != null && field.getAutoIncrease()) {
                continue;
            }
            foreachLine.append("#{item.").append(field.getPropertyName()).append("},");
        }
        String foreachLineStr = foreachLine.substring(0, foreachLine.lastIndexOf(","));
        bw.write(foreachLineStr + ")");
        bw.newLine();
        bw.write("\t\t</foreach>");
        bw.newLine();
    }

    private static void writeInsertTrimTag(BufferedWriter bw, TableInfo tableInfo) throws Exception {
        bw.write("\t\t<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
        bw.newLine();

        for (FieldInfo fieldInfo : tableInfo.getFieldInfoList()) {
//            if (fieldInfo.getAutoIncrease() != null && fieldInfo.getAutoIncrease()) {
//                continue;
//            }
            bw.write("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() + " != null\">");
            bw.newLine();
            bw.write("\t\t\t\t" + fieldInfo.getFieldName() + ",");
            bw.newLine();
            bw.write("\t\t\t</if>");
            bw.newLine();
        }

        bw.write("\t\t</trim>");
        bw.newLine();
        bw.newLine();

        bw.write("\t\t<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\">");
        bw.newLine();

        for (FieldInfo fieldInfo : tableInfo.getFieldInfoList()) {
//            if (fieldInfo.getAutoIncrease() != null && fieldInfo.getAutoIncrease()) {
//                continue;
//            }
            bw.write("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() + " != null\">");
            bw.newLine();
            bw.write("\t\t\t\t#{bean." + fieldInfo.getPropertyName() + "},");
            bw.newLine();
            bw.write("\t\t\t</if>");
            bw.newLine();
        }

        bw.write("\t\t</trim>");
        bw.newLine();
    }
}
