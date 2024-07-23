package com.codegeneration.builder;

import com.codegeneration.bean.Constants;
import com.codegeneration.bean.FieldInfo;
import com.codegeneration.bean.TableInfo;
import com.codegeneration.utils.DateUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class BuildPo {
    private static final Logger logger = LoggerFactory.getLogger(BuildPo.class);

    public static void execute(TableInfo tableInfo) {
        File folder = new File((Constants.PATH_PO));
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File poFile = new File(folder, tableInfo.getBeanName() + ".java");

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
            bw.write("package " + Constants.PACKAGE_PO + ";");
            bw.newLine();
            // 导包
            bw.newLine();
            bw.write("import java.io.Serializable;");
            bw.newLine();
            // 判断是否需要导入序列化和反序列化包
            if (tableInfo.getHaveDate() || tableInfo.getHaveDateTime()) {
                bw.write("import java.util.Date;");
                bw.newLine();
                // 导入DateUtils和Date枚举的包
                bw.write("import " + Constants.PACKAGE_ENUM + ".DateTimeEnum;");
                bw.newLine();
                bw.write("import " + Constants.PACKAGE_UTILS + ".DateUtils;");
                bw.newLine();
                // 序列化和反序列化导包
                bw.write(Constants.BEAN_DATE_FORMAT_CLASS);
                bw.newLine();
                bw.write(Constants.BEAN_DATE_UNFORMAT_CLASS);
                bw.newLine();
            }
            // 判断是否需要导入bigDecimal包
            if (tableInfo.getHaveBigDecimal()) {
                bw.write("import java.math.BigDecimal;");
                bw.newLine();
            }
            // 判断是否需要导入ignoreBean包
            for (FieldInfo field : tableInfo.getFieldInfoList()) {
                if (ArrayUtils.contains(Constants.IGNORE_BEAN_TOJSON_FIELD.split(","), field.getFieldName())) {
                    bw.write(Constants.IGNORE_BEAN_TOJSON_CLASS);
                    bw.newLine();
                    break;
                }
            }
            bw.newLine();
            // 写类注释和类
            BuildComment.writeClassComment(bw, tableInfo.getComment());
            bw.write("public class " + tableInfo.getBeanName() + " implements Serializable {");
            bw.newLine();
            /*
             * package和类名结尾
             */
            /*
             * 属性开始
             */
            for (FieldInfo field : tableInfo.getFieldInfoList()) {
                BuildComment.writeFieldComment(bw, field.getComment());
                // 判断是否需要写注解【date和datetime两个的pattern是不一样的，要区分开】
                if (ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, field.getSqlType())) {
                    // 序列化注解和反序列化注解
                    bw.write("\t" + String.format(Constants.BEAN_DATE_FORMAT_EXPRESSION, DateUtils.YYYY_MM_DD_HH_MM_SS));
                    bw.newLine();
                    bw.write("\t" + String.format(Constants.BEAN_DATE_UNFORMAT_EXPRESSION, DateUtils.YYYY_MM_DD_HH_MM_SS));
                    bw.newLine();
                }
                if (ArrayUtils.contains(Constants.SQL_DATE_TYPES, field.getSqlType())) {
                    // 序列化注解和反序列化注解
                    bw.write("\t" + String.format(Constants.BEAN_DATE_FORMAT_EXPRESSION, DateUtils.YYYY_MM_DD));
                    bw.newLine();
                    bw.write("\t" + String.format(Constants.BEAN_DATE_UNFORMAT_EXPRESSION, DateUtils.YYYY_MM_DD));
                    bw.newLine();
                }
                // 需要忽略的属性
                if (ArrayUtils.contains(Constants.IGNORE_BEAN_TOJSON_FIELD.split(","), field.getFieldName())) {
                    bw.write("\t" + Constants.IGNORE_BEAN_TOJSON_EXPRESSION);
                    bw.newLine();
                }
                bw.write("\tprivate " + field.getJavaType() + " " + field.getPropertyName() + ";");
                bw.newLine();
                bw.newLine();
            }
            /*
             * 属性结尾
             */

            /*
             * getter/setter/toString方法开始
             */
            BuildGetSet.writeGetSet(bw, tableInfo.getFieldInfoList());
            // 重写toString方法
            StringBuffer toString = new StringBuffer();
            for (FieldInfo field : tableInfo.getFieldInfoList()) {
                toString.append("\"" + field.getComment() + ":\" + " + "(" + field.getPropertyName() + " == null ? \"空\" : ");
                // 如果是datetime需要转成人看得懂的日期
                if (ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, field.getSqlType())) {
                    toString.append("DateUtils.format(" + field.getPropertyName() + ", DateTimeEnum.YYYY_MM_DD_HH_MM_SS.getPattern())" + ")");
                } else if (ArrayUtils.contains(Constants.SQL_DATE_TYPES, field.getSqlType())) {
                    toString.append("DateUtils.format(" + field.getPropertyName() + ", DateTimeEnum.YYYY_MM_DD.getPattern())" + ")");
                } else {
                    toString.append(field.getPropertyName() + ")");
                }
                toString.append("+ \" | \"  + ");

            }
            toString.append("\" , \" + ");
            String toStringStr = toString.toString();
            toStringStr = toStringStr.substring(0, toStringStr.lastIndexOf(" + \" , \" +"));
            bw.write("\t@Override");
            bw.newLine();
            bw.write("\tpublic String toString() {");
            bw.newLine();
            bw.write("\t\treturn " + toStringStr + ";");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();
            /*
              getter/setter/toString方法结尾
             */
            /*
              文件结尾
             */
            bw.write("}");
            bw.flush();
        } catch (Exception e) {
            logger.error("创建PO失败", e);
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
