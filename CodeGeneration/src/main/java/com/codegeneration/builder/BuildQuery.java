package com.codegeneration.builder;

import com.codegeneration.bean.Constants;
import com.codegeneration.bean.FieldInfo;
import com.codegeneration.bean.TableInfo;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BuildQuery {
    private static final Logger logger = LoggerFactory.getLogger(BuildQuery.class);

    public static void execute(TableInfo tableInfo) {
        File folder = new File((Constants.PATH_QUERY));
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File poFile = new File(folder, tableInfo.getBeanName() + Constants.SUFFIX_BEAN_QUERY + ".java");

        OutputStream outs = null;
        OutputStreamWriter outsw = null;
        BufferedWriter bw = null;
        try {
            outs = new FileOutputStream(poFile);
            outsw = new OutputStreamWriter(outs, "utf8");
            bw = new BufferedWriter(outsw);
            /**
             * package和类名开始
             */
            bw.write("package " + Constants.PACKAGE_QUERY + ";");
            bw.newLine();
            // 导包
            bw.newLine();
            // 判断是否需要导入Date包
            if (tableInfo.getHaveDate() || tableInfo.getHaveDateTime()) {
                bw.write("import java.util.Date;");
                bw.newLine();
                // 导入DateUtils和Date枚举的包
            }
            // 判断是否需要导入bigDecimal包
            if (tableInfo.getHaveBigDecimal()) {
                bw.write("import java.math.BigDecimal;");
                bw.newLine();
            }
            // 写类注释和类
            BuildComment.writeClassComment(bw, tableInfo.getComment());
            bw.write("public class " + tableInfo.getBeanName() + Constants.SUFFIX_BEAN_QUERY + " extends BaseQueryParam {");
            bw.newLine();
            /*
             * package和类名结尾
             */
            /*
             * 属性开始
             */
            List<FieldInfo> extendList = new ArrayList();
            for (FieldInfo field : tableInfo.getFieldInfoList()) {
                // 注释
                BuildComment.writeFieldComment(bw, field.getComment() + "查询对象");

                bw.write("\tprivate " + field.getJavaType() + " " + field.getPropertyName() + ";");
                bw.newLine();
                bw.newLine();
                // String和Date类型参数
                if (ArrayUtils.contains(Constants.SQL_STRING_TYPE, field.getSqlType())) {
                    String propertyName = field.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_FUZZY;
                    bw.write("\tprivate " + field.getJavaType() + " " + propertyName + ";");
                    bw.newLine();
                    bw.newLine();
                } else if (ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, field.getSqlType()) || ArrayUtils.contains(Constants.SQL_DATE_TYPES, field.getSqlType())) {
                    bw.write("\tprivate String" + " " + field.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_TIME_START + ";");
                    bw.newLine();
                    bw.newLine();
                    bw.write("\tprivate String" + " " + field.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_TIME_END + ";");
                    bw.newLine();
                    bw.newLine();
                }
            }
            /*
             * 属性结尾
             */

            /*
             * getter/setter/toString方法开始
             */
            BuildGetSet.writeGetSet(bw, tableInfo.getFieldInfoList());
            BuildGetSet.writeGetSet(bw, tableInfo.getExtendFieldInfoList());
            /*
              getter/setter/toString方法结尾
             */
            /*
              文件结尾
             */
            bw.write("}");
            bw.flush();
        } catch (Exception e) {
            logger.error("创建QUERY失败", e);
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
}
