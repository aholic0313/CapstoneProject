package com.codegeneration.builder;

import com.codegeneration.utils.DateUtils;

import java.io.BufferedWriter;
import java.util.Date;

public class BuildComment {
    public static void writeClassComment(BufferedWriter bw, String classComment) throws Exception {
        bw.write("/**");
        bw.newLine();
        bw.write(" * @Description: " + classComment);
        bw.newLine();
        bw.write(" *");
        bw.newLine();
        bw.write(" * @Date: " + DateUtils.format(new Date(), DateUtils._YYYYMMDD));
        bw.newLine();
        bw.write(" */");
        bw.newLine();
    }

    public static void writeFieldComment(BufferedWriter bw, String fieldComment) throws Exception {
        if (fieldComment == null) {
            fieldComment = "*";
        }
        bw.write("\t/* " + fieldComment + " */");
        bw.newLine();
    }



}
