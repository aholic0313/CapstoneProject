package com.codegeneration.builder;

import com.codegeneration.bean.Constants;
import com.codegeneration.bean.FieldInfo;
import com.codegeneration.bean.TableInfo;
import com.codegeneration.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.Map;

public class BuildController {
    private static final Logger logger = LoggerFactory.getLogger(BuildPo.class);

    public static void execute(TableInfo tableInfo) {
        File folder = new File((Constants.PATH_CONTROLLER));
        String className = tableInfo.getBeanName() + "Controller";
        String upperBeanServiceName = tableInfo.getBeanName() + Constants.SUFFIX_SERVICE;
        String lowerBeanServiceName = StringUtils.LowerCaseFirstLetter(upperBeanServiceName);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File poFile = new File(folder, className + ".java");

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
            bw.write("package " + Constants.PACKAGE_CONTROLLER + ";");
            bw.newLine();
            bw.newLine();
            // 导包
            bw.write("import java.util.List;\n");
            bw.write("import " + Constants.PACKAGE_PO + "." + tableInfo.getBeanName() + ";\n");
            bw.write("import " + Constants.PACKAGE_QUERY + "." + tableInfo.getBeanParamName() + ";\n");
            bw.write("import " + Constants.PACKAGE_VO + ".ResponseVO;\n");
            bw.write("import " + Constants.PACKAGE_SERVICE + "." + upperBeanServiceName + ";\n");
            bw.write("import org.springframework.web.bind.annotation.RequestMapping;\n");
            bw.write("import org.springframework.web.bind.annotation.RestController;\n");
            bw.write("import org.springframework.web.bind.annotation.RequestBody;\n");
            bw.write("import javax.annotation.Resource;\n");

            BuildComment.writeClassComment(bw, tableInfo.getComment() + "业务控制层");
            bw.write("@RestController\n");
            bw.write("@RequestMapping(\"/" + StringUtils.LowerCaseFirstLetter(tableInfo.getBeanName()) + "\")\n");
            bw.write("public class " + className + " extends BaseController {");
            bw.newLine();
            bw.newLine();

            bw.write("\t@Resource\n");
            bw.write("\tprivate " + upperBeanServiceName + " " + lowerBeanServiceName + ";\n");
            bw.newLine();

            BuildComment.writeFieldComment(bw, "根据条件分页查询");
            bw.write("\t@RequestMapping(\"loadDataList\")\n");
            bw.write("\tpublic ResponseVO loadDataList(" + tableInfo.getBeanParamName() + " query) {\n");
            bw.write("\t\treturn getSuccessResponseVO(" + lowerBeanServiceName + ".findListByPage(query));\n\t}");
            bw.newLine();
            bw.newLine();

            BuildComment.writeFieldComment(bw, "新增");
            bw.write("\t@RequestMapping(\"add\")\n");
            bw.write("\tpublic ResponseVO add(" + tableInfo.getBeanName() + " bean) {\n");
            bw.write("\t\t" + lowerBeanServiceName + ".add(bean);\n");
            bw.write("\t\treturn getSuccessResponseVO(null);\n\t}");
            bw.newLine();
            bw.newLine();

            BuildComment.writeFieldComment(bw, "批量新增");
            bw.write("\t@RequestMapping(\"addBatch\")\n");
            bw.write("\tpublic ResponseVO addBatch(@RequestBody List<" + tableInfo.getBeanName() + "> listBean) {\n");
            bw.write("\t\t" + lowerBeanServiceName + ".addBatch(listBean);\n");
            bw.write("\t\treturn getSuccessResponseVO(null);\n\t}");
            bw.newLine();
            bw.newLine();

            BuildComment.writeFieldComment(bw, "批量新增或修改");
            bw.write("\t@RequestMapping(\"addOrUpdateBatch\")\n");
            bw.write("\tpublic ResponseVO addOrUpdateBatch(@RequestBody List<" + tableInfo.getBeanName() + "> listBean) {\n");
            bw.write("\t\t" + lowerBeanServiceName + ".addOrUpdateBatch(listBean);\n");
            bw.write("\t\treturn getSuccessResponseVO(null);\n\t}");
            bw.newLine();
            bw.newLine();

            // 根据主键操作
            Map<String, List<FieldInfo>> keyIdxMap = tableInfo.getKeyIndexMap();
            for (Map.Entry<String, List<FieldInfo>> entry : keyIdxMap.entrySet()) {
                List<FieldInfo> keyFieldList = entry.getValue();
                StringBuilder upperPrimaryKey = new StringBuilder();
                StringBuilder methodParamsFull = new StringBuilder();
                StringBuilder methodParamsName = new StringBuilder();

                Integer index = 0;
                for (FieldInfo fieldInfo : keyFieldList) {
                    index++;
                    // 方法名字符串
                    upperPrimaryKey.append(StringUtils.UpperCaseFirstLetter(fieldInfo.getPropertyName()));
                    methodParamsName.append(fieldInfo.getPropertyName());
                    if (index < keyFieldList.size()) {
                        upperPrimaryKey.append("And");
                    }

                    // 参数字符串
                    methodParamsFull.append(fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName());
                    if (index < keyFieldList.size()) {
                        methodParamsFull.append(", ");
                        methodParamsName.append(", ");
                    }
                }

                String methodNameGetBy = "get" + tableInfo.getBeanName() + "By" + upperPrimaryKey;
                String methodNameDelBy = "delete" + tableInfo.getBeanName() + "By" + upperPrimaryKey;
                String methodNameUpdateBy = "update" + tableInfo.getBeanName() + "By" + upperPrimaryKey;

                BuildComment.writeFieldComment(bw, "根据" + upperPrimaryKey + "查询对象");
                bw.write("\t@RequestMapping(\"" + methodNameGetBy + "\")\n");
                bw.write("\tpublic ResponseVO " + methodNameGetBy + "(" + methodParamsFull + ") {\n ");
                bw.write("\t\treturn getSuccessResponseVO(" + lowerBeanServiceName + "." + methodNameGetBy + "(" + methodParamsName + "));\n");
                bw.write("\t}\n");
                bw.newLine();
                bw.newLine();

                BuildComment.writeFieldComment(bw, "根据" + upperPrimaryKey + "修改对象");
                bw.write("\t@RequestMapping(\"" + methodNameUpdateBy + "\")\n");
                bw.write("\tpublic ResponseVO " + methodNameUpdateBy + "(" + tableInfo.getBeanName() + " bean, " + methodParamsFull + ") {\n ");
                bw.write("\t\t" + lowerBeanServiceName + "." + methodNameUpdateBy + " (bean," + methodParamsName + ");\n");
                bw.write("\t\treturn getSuccessResponseVO(null);\n");
                bw.write("\t}\n");
                bw.newLine();
                bw.newLine();

                BuildComment.writeFieldComment(bw, "根据" + upperPrimaryKey + "删除对象");
                bw.write("\t@RequestMapping(\"" + methodNameDelBy + "\")\n");
                bw.write("\tpublic ResponseVO " + methodNameDelBy + "(" + methodParamsFull + ") {\n ");
                bw.write("\t\t" + lowerBeanServiceName + "." + methodNameDelBy + "(" + methodParamsName + ");\n");
                bw.write("\t\treturn getSuccessResponseVO(null);\n");
                bw.write("\t}\n");
                bw.newLine();
                bw.newLine();
//
//                BuildComment.writeFieldComment(bw, "根据" + methodName + "更新");
//                bw.write("\tInteger updateBy" + methodName + "(" + tableInfo.getBeanName() + " bean," + methodPrams + ");");
//                bw.newLine();
//                bw.newLine();
//
//                BuildComment.writeFieldComment(bw, "根据" + methodName + "删除");
//                bw.write("\tInteger deleteBy" + methodName + "(" + methodPrams + ");");
//                bw.newLine();
//                bw.newLine();
            }

            bw.write("}");
            bw.newLine();

            bw.flush();
        } catch (Exception e) {
            logger.error("创建Controller失败", e);
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
