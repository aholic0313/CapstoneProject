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

public class BuildService {
    private static final Logger logger = LoggerFactory.getLogger(BuildPo.class);

    public static void execute(TableInfo tableInfo) {
        File folder = new File((Constants.PATH_SERVICE));
        String className = tableInfo.getBeanName() + Constants.SUFFIX_SERVICE;
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
            bw.write("package " + Constants.PACKAGE_SERVICE + ";");
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
            bw.write("public interface " + className + " {");
            bw.newLine();
            bw.newLine();

            BuildComment.writeFieldComment(bw, "根据条件查询列表");
            bw.write("\tList<" + tableInfo.getBeanName() + "> findListByParam(" + tableInfo.getBeanParamName() + " query);");
            bw.newLine();
            bw.newLine();

            BuildComment.writeFieldComment(bw, "根据条件查询数量");
            bw.write("\tInteger findCountByParam(" + tableInfo.getBeanParamName() + " query);");
            bw.newLine();
            bw.newLine();

            BuildComment.writeFieldComment(bw, "分页查询");
            bw.write("\tPaginationResultVO<" + tableInfo.getBeanName() + "> findListByPage(" + tableInfo.getBeanParamName() + " query);");
            bw.newLine();
            bw.newLine();

            BuildComment.writeFieldComment(bw, "新增");
            bw.write("\tInteger add(" + tableInfo.getBeanName() + " bean);");
            bw.newLine();
            bw.newLine();

            BuildComment.writeFieldComment(bw, "批量新增");
            bw.write("\tInteger addBatch(List<" + tableInfo.getBeanName() + "> listBean);");
            bw.newLine();
            bw.newLine();

            BuildComment.writeFieldComment(bw, "批量新增或修改");
            bw.write("\tInteger addOrUpdateBatch(List<" + tableInfo.getBeanName() + "> listBean);");
            bw.newLine();
            bw.newLine();

            // 根据主键操作
            Map<String, List<FieldInfo>> keyIdxMap = tableInfo.getKeyIndexMap();
            for (Map.Entry<String, List<FieldInfo>> entry : keyIdxMap.entrySet()) {
                List<FieldInfo> keyFieldList = entry.getValue();
                StringBuilder methodName = new StringBuilder();
                StringBuilder methodPrams = new StringBuilder();

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
                    if (index < keyFieldList.size()) {
                        methodPrams.append(", ");
                    }
                }
                BuildComment.writeFieldComment(bw, "根据" + methodName + "查询对象");
                bw.write("\t" + tableInfo.getBeanName() + " get" + tableInfo.getBeanName() + "By" + methodName + "(" + methodPrams + ");");
                bw.newLine();
                bw.newLine();

                BuildComment.writeFieldComment(bw, "根据" + methodName + "更新");
                bw.write("\tInteger update" + tableInfo.getBeanName() + "By" + methodName + "(" + tableInfo.getBeanName() + " bean," + methodPrams + ");");
                bw.newLine();
                bw.newLine();

                BuildComment.writeFieldComment(bw, "根据" + methodName + "删除");
                bw.write("\tInteger delete" + tableInfo.getBeanName() + "By" + methodName + "(" + methodPrams + ");");
                bw.newLine();
                bw.newLine();
            }

            bw.write("}");
            bw.newLine();

            bw.flush();
        } catch (Exception e) {
            logger.error("创建Service失败", e);
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
