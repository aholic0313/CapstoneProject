package com.codegeneration.builder;

import com.codegeneration.bean.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 创建模板类
 */
public class BuildBase {
    private static Logger logger = LoggerFactory.getLogger(BuildBase.class);

    public static void execute() {
        List<String> headerInfoList = new ArrayList<>();

        // 生成dateEnum
        headerInfoList.add("package " + Constants.PACKAGE_ENUM + ";");
        build(headerInfoList, "DateTimeEnum", Constants.PATH_ENUM);

        // 生成DateTimeEnum
        headerInfoList.clear();
        headerInfoList.add("package " + Constants.PACKAGE_ENUM + ";");
        build(headerInfoList, "PageSize", Constants.PATH_ENUM);

        //生成ResponseCodeEnum
        headerInfoList.clear();
        headerInfoList.add("package " + Constants.PACKAGE_ENUM + ";");
        build(headerInfoList, "ResponseCodeEnum", Constants.PATH_ENUM);

        // 生成DateUtils
        headerInfoList.clear();
        headerInfoList.add("package " + Constants.PACKAGE_UTILS + ";");
        build(headerInfoList, "DateUtils", Constants.PATH_UTILS);

        // 生成StringUtils
        headerInfoList.clear();
        headerInfoList.add("package " + Constants.PACKAGE_UTILS + ";");
        build(headerInfoList, "StringUtils", Constants.PATH_UTILS);

        // 生成BaseMapper
        headerInfoList.clear();
        headerInfoList.add("package " + Constants.PACKAGE_MAPPER + ";");
        build(headerInfoList, "BaseMapper", Constants.PATH_MAPPER);

        //生成BaseController
        headerInfoList.clear();
        headerInfoList.add("package " + Constants.PACKAGE_CONTROLLER + ";\n");
        headerInfoList.add("import " + Constants.PACKAGE_VO + ".ResponseVO;\n");
        headerInfoList.add("import " + Constants.PACKAGE_ENUM + ".ResponseCodeEnum;");
        build(headerInfoList, "BaseController", Constants.PATH_CONTROLLER);

        // 生成分页查询参数
        headerInfoList.clear();
        headerInfoList.add("package " + Constants.PACKAGE_QUERY + ";");
        build(headerInfoList, "BaseQueryParam", Constants.PATH_QUERY);

        // 生成分页查询工具
        headerInfoList.clear();
        headerInfoList.add("package " + Constants.PACKAGE_QUERY + ";");
        headerInfoList.add("\n");
        headerInfoList.add("import " + Constants.PACKAGE_ENUM + ".PageSize;");
        headerInfoList.add("\n");
        build(headerInfoList, "SimplePage", Constants.PATH_QUERY);

        // 生成分页VO类
        headerInfoList.clear();
        headerInfoList.add("package " + Constants.PACKAGE_VO + ";");
        build(headerInfoList, "PaginationResultVO", Constants.PATH_VO);

        // 生成ResponseVO
        headerInfoList.clear();
        headerInfoList.add("package " + Constants.PACKAGE_VO + ";");
        build(headerInfoList, "ResponseVO", Constants.PATH_VO);

        // 生成BusinessException
        headerInfoList.clear();
        headerInfoList.add("package " + Constants.PACKAGE_EXCEPTION + ";\n");
        headerInfoList.add("import " + Constants.PACKAGE_ENUM + ".ResponseCodeEnum" + ";");
        build(headerInfoList, "BusinessException", Constants.PATH_EXCEPTION);

        // 生成GlobalExceptionHandlerController
        headerInfoList.clear();
        headerInfoList.add("package " + Constants.PACKAGE_CONTROLLER + ";\n");
        headerInfoList.add("import " + Constants.PACKAGE_VO + ".ResponseVO;\n");
        headerInfoList.add("import " + Constants.PACKAGE_ENUM + ".ResponseCodeEnum;\n");
        headerInfoList.add("import " + Constants.PACKAGE_EXCEPTION + ".BusinessException;");
        build(headerInfoList, "GlobalExceptionHandlerController", Constants.PATH_CONTROLLER);


    }

    private static void build(List<String> headerInfoList, String fileName, String outPutPath) {
        File folder = new File(outPutPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File javaFile = new File(outPutPath, fileName + ".java");
        // 读取一个文件的内容，再将内容输出到另一个文件
        OutputStream outs = null;
        OutputStreamWriter outw = null;
        BufferedWriter bw = null;

        InputStream in = null;
        InputStreamReader inr = null;
        BufferedReader br = null;
        try {
            outs = new FileOutputStream(javaFile);
            outw = new OutputStreamWriter(outs, "utf8");
            bw = new BufferedWriter(outw);

            String templatePath = BuildBase.class.getClassLoader().getResource("template/" + fileName + ".txt").getPath();

            in = new FileInputStream(templatePath);
            inr = new InputStreamReader(in, "utf8");
            br = new BufferedReader(inr);

            String lineInfo;
            for (String head : headerInfoList) {
                bw.write(head);
                if (head.contains("package")) {
                    bw.newLine();
                }
            }
            bw.newLine();
            while ((lineInfo = br.readLine()) != null) {
                bw.write(lineInfo);
                bw.newLine();
            }
            bw.flush();

        } catch (Exception e) {
            logger.error("生成基础类失败" + fileName + e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inr != null) {
                try {
                    inr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outw != null) {
                try {
                    outw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outs != null) {
                try {
                    outs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
