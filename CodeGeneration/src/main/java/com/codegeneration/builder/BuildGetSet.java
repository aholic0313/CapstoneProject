package com.codegeneration.builder;

import com.codegeneration.bean.FieldInfo;
import com.codegeneration.utils.StringUtils;

import java.io.BufferedWriter;
import java.util.List;

public class BuildGetSet {
    public static void writeGetSet(BufferedWriter bw, List<FieldInfo> fieldInfoList) throws Exception {
        for (FieldInfo field : fieldInfoList) {
            String propertyName = field.getPropertyName();
            // 首字母大写
            String tempField = StringUtils.UpperCaseFirstLetter(propertyName);
            String javaType = field.getJavaType();
            // setter
            bw.write("\tpublic void set" + tempField + "(" + javaType + " " + propertyName + ") {");
            bw.newLine();
            bw.write("\t\tthis." + propertyName + " = " + propertyName + ";");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();
            bw.newLine();
            // getter
            bw.write("\tpublic " + javaType + " get" + tempField + "() {");
            bw.newLine();
            bw.write("\t\treturn " + propertyName + ";");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();
            bw.newLine();
        }

    }
}
