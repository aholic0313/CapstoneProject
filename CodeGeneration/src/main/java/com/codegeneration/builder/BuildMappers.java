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

public class BuildMappers {
    private static final Logger logger = LoggerFactory.getLogger(BuildMappers.class);

    public static void execute(TableInfo tableInfo) {
        File folder = new File((Constants.PATH_MAPPER));
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File mapperFile = new File(folder, tableInfo.getBeanName() + Constants.SUFFIX_MAPPERS + ".java");

        OutputStream outs = null;
        OutputStreamWriter outsw = null;
        BufferedWriter bw = null;
        try {
            outs = new FileOutputStream(mapperFile);
            outsw = new OutputStreamWriter(outs, "utf8");
            bw = new BufferedWriter(outsw);
            /**
             * package和类名开始
             */
            bw.write("package " + Constants.PACKAGE_MAPPER + ";");
            bw.newLine();
            bw.newLine();
            bw.write("import org.apache.ibatis.annotations.Param;");
            bw.newLine();
            bw.newLine();
            // 写类注释和类
            BuildComment.writeClassComment(bw, tableInfo.getComment());
            bw.write("public interface " + tableInfo.getBeanName() + Constants.SUFFIX_MAPPERS + "<T, P> extends BaseMapper {");
            bw.newLine();
            /*
             * package和类名结尾
             */


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
                    methodPrams.append("@Param(\"" + fieldInfo.getPropertyName() + "\") " + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName());
                    if (index < keyFieldList.size()) {
                        methodPrams.append(", ");
                    }
                }
                BuildComment.writeFieldComment(bw, "根据" + methodName + "查询");
                bw.write("\tT selectBy" + methodName + "(" + methodPrams + ");");
                bw.newLine();
                bw.newLine();

                BuildComment.writeFieldComment(bw, "根据" + methodName + "更新");
                bw.write("\tInteger updateBy" + methodName + "(@Param(\"bean\") T t, " + methodPrams + ");");
                bw.newLine();
                bw.newLine();

                BuildComment.writeFieldComment(bw, "根据" + methodName + "删除");
                bw.write("\tInteger deleteBy" + methodName + "(" + methodPrams + ");");
                bw.newLine();
                bw.newLine();
            }

            /*
              文件结尾
             */
            bw.write("}");
            bw.newLine();
            bw.flush();
        } catch (Exception e) {
            logger.error("创建Mappers失败", e);
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
