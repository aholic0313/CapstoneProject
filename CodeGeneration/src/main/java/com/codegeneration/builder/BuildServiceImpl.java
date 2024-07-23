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

public class BuildServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(BuildPo.class);

    public static void execute(TableInfo tableInfo) {
        String className = tableInfo.getBeanName() + Constants.SUFFIX_SERVICE_IMPL;
        String interfaceName = tableInfo.getBeanName() + Constants.SUFFIX_SERVICE;
        String beanMapperName = tableInfo.getBeanName() + Constants.SUFFIX_MAPPERS;
        String lowerMapperName = StringUtils.LowerCaseFirstLetter(beanMapperName);

        File folder = new File((Constants.PATH_SERVICE_IMPL));
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
            bw.write("package " + Constants.PACKAGE_SERVICE_IMPL + ";");
            bw.newLine();
            bw.newLine();
            // 导包
            // 判断是否需要导入序列化和反序列化包
            bw.write("import java.util.List;");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_PO + "." + tableInfo.getBeanName() + ";");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_QUERY + "." + tableInfo.getBeanParamName() + ";");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_VO + ".PaginationResultVO;");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_SERVICE + "." + interfaceName + ";");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_QUERY + ".SimplePage;");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_ENUM + ".PageSize;");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_MAPPER + "." + beanMapperName + ";");
            bw.newLine();
            bw.write("import org.springframework.stereotype.Service;");
            bw.newLine();
            bw.write("import javax.annotation.Resource;");
            bw.newLine();

//            if (tableInfo.getHaveDate() || tableInfo.getHaveDateTime()) {
//                bw.write("import java.util.Date;");
//                bw.newLine();
//                // 导入DateUtils和Date枚举的包
//                bw.write("import " + Constants.PACKAGE_ENUM + ".DateTimeEnum;");
//                bw.newLine();
//                bw.write("import " + Constants.PACKAGE_UTILS + ".DateUtils;");
//                bw.newLine();
//                // 序列化和反序列化导包
//                bw.write(Constants.BEAN_DATE_FORMAT_CLASS);
//                bw.newLine();
//                bw.write(Constants.BEAN_DATE_UNFORMAT_CLASS);
//                bw.newLine();
//            }
//            // 判断是否需要导入bigDecimal包
//            if (tableInfo.getHaveBigDecimal()) {
//                bw.write("import java.math.BigDecimal;");
//                bw.newLine();
//            }

            BuildComment.writeClassComment(bw, tableInfo.getComment() + "业务接口");
            bw.write("@Service(\"" + StringUtils.LowerCaseFirstLetter(interfaceName) + "\")");
            bw.newLine();
            bw.write("public class " + className + " implements " + interfaceName + " {");
            bw.newLine();
            bw.newLine();

            bw.write("\t@Resource");
            bw.newLine();
            bw.write("\tprivate " + beanMapperName + "<" + tableInfo.getBeanName() + "," + tableInfo.getBeanParamName() + "> " + lowerMapperName + ";");
            bw.newLine();
            bw.newLine();


            BuildComment.writeFieldComment(bw, "根据条件查询列表");
            bw.write("\t@Override");
            bw.newLine();
            bw.write("\tpublic List<" + tableInfo.getBeanName() + "> findListByParam(" + tableInfo.getBeanParamName() + " query) {");
            bw.newLine();
            bw.write("\t\treturn this." + lowerMapperName + ".selectList(query);");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();
            bw.newLine();

            BuildComment.writeFieldComment(bw, "根据条件查询数量");
            bw.write("\t@Override");
            bw.newLine();
            bw.write("\tpublic Integer findCountByParam(" + tableInfo.getBeanParamName() + " query) {");
            bw.newLine();
            bw.write("\t\treturn this." + lowerMapperName + ".selectCount(query);");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();
            bw.newLine();

            BuildComment.writeFieldComment(bw, "分页查询");
            bw.write("\t@Override\n");
            bw.write("\tpublic PaginationResultVO<" + tableInfo.getBeanName() + "> findListByPage(" + tableInfo.getBeanParamName() + " query) {\n");
            bw.write("\t\tInteger count = this.findCountByParam(query);\n");
            bw.write("\t\tInteger pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();\n\n");
            bw.write("\t\tSimplePage page = new SimplePage(query.getPageNo(),count,pageSize);\n");
            bw.write("\t\tquery.setSimplePage(page);\n");
            bw.write("\t\tList<" + tableInfo.getBeanName() + "> list = this.findListByParam(query);\n");
            bw.write("\t\tPaginationResultVO<" + tableInfo.getBeanName() + "> result = new PaginationResultVO(count,page.getPageSize(),page.getPageNo(),page.getPageTotal(),list);\n");
            bw.write("\t\treturn result;\n");
            bw.write("\t}");
            bw.newLine();
            bw.newLine();

            BuildComment.writeFieldComment(bw, "新增");
            bw.write("\t@Override");
            bw.newLine();
            bw.write("\tpublic Integer add(" + tableInfo.getBeanName() + " bean) {");
            bw.newLine();
            bw.write("\t\treturn this." + lowerMapperName + ".insert(bean);\n");
            bw.write("\t}");
            bw.newLine();
            bw.newLine();

            BuildComment.writeFieldComment(bw, "批量新增");
            bw.write("\t@Override");
            bw.newLine();
            bw.write("\tpublic Integer addBatch(List<" + tableInfo.getBeanName() + "> listBean) {");
            bw.newLine();
            bw.write("\t\tif (listBean == null || listBean.isEmpty()){\n\t\t\treturn 0;\n\t\t}\n");
            bw.write("\t\treturn this." + lowerMapperName + ".insertBatch(listBean);\n");
            bw.write("\t}");
            bw.newLine();
            bw.newLine();

            BuildComment.writeFieldComment(bw, "批量新增或修改");
            bw.write("\t@Override");
            bw.newLine();
            bw.write("\tpublic Integer addOrUpdateBatch(List<" + tableInfo.getBeanName() + "> listBean) {");
            bw.newLine();
            bw.write("\t\tif (listBean == null || listBean.isEmpty()){\n\t\t\treturn 0;\n\t\t}\n");
            bw.write("\t\treturn this." + lowerMapperName + ".insertOrUpdateBatch(listBean);\n");
            bw.write("\t}");
            bw.newLine();
            bw.newLine();

            // 根据主键操作
            Map<String, List<FieldInfo>> keyIdxMap = tableInfo.getKeyIndexMap();
            for (Map.Entry<String, List<FieldInfo>> entry : keyIdxMap.entrySet()) {
                List<FieldInfo> keyFieldList = entry.getValue();
                StringBuilder methodName = new StringBuilder();
                StringBuilder methodPrams = new StringBuilder();
                StringBuilder returnPrams = new StringBuilder();

                Integer index = 0;
                for (FieldInfo fieldInfo : keyFieldList) {
                    index++;
                    // 方法名字符串
                    methodName.append(StringUtils.UpperCaseFirstLetter(fieldInfo.getPropertyName()));
                    if (index < keyFieldList.size()) {
                        methodName.append("And");
                    }

                    // 参数字符串
                    methodPrams.append(fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName());
                    returnPrams.append(fieldInfo.getPropertyName());
                    if (index < keyFieldList.size()) {
                        methodPrams.append(", ");
                        returnPrams.append(", ");
                    }
                }
                BuildComment.writeFieldComment(bw, "根据" + returnPrams + "查询对象");
                bw.write("\t@Override");
                bw.newLine();
                bw.write("\tpublic " + tableInfo.getBeanName() + " get" + tableInfo.getBeanName() + "By" + methodName + "(" + methodPrams + ") {");
                bw.newLine();
                bw.write("\t\treturn this." + lowerMapperName + ".selectBy" + methodName + "(" + returnPrams + ");\n");
                bw.write("\t}");
                bw.newLine();
                bw.newLine();

                BuildComment.writeFieldComment(bw, "根据" + returnPrams + "更新");
                bw.write("\t@Override");
                bw.newLine();
                bw.write("\tpublic Integer update" + tableInfo.getBeanName() + "By" + methodName + "(" + tableInfo.getBeanName() + " bean," + methodPrams + ") {");
                bw.newLine();
                bw.write("\t\treturn this." + lowerMapperName + ".updateBy" + methodName + "(bean," + returnPrams + ");\n");
                bw.write("\t}");
                bw.newLine();
                bw.newLine();

                BuildComment.writeFieldComment(bw, "根据" + returnPrams + "删除");
                bw.write("\t@Override");
                bw.newLine();
                bw.write("\tpublic Integer delete" + tableInfo.getBeanName() + "By" + methodName + "(" + methodPrams + ") {");
                bw.newLine();
                bw.write("\t\treturn this." + lowerMapperName + ".deleteBy" + methodName + "(" + returnPrams + ");\n");
                bw.write("\t}");
                bw.newLine();
                bw.newLine();
            }

            bw.write("}");
            bw.newLine();
            bw.flush();
        } catch (Exception e) {
            logger.error("创建ServiceImpl失败", e);
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
