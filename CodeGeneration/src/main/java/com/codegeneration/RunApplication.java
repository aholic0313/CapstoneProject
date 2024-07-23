package com.codegeneration;

import com.codegeneration.bean.TableInfo;
import com.codegeneration.builder.*;

import java.util.List;

public class RunApplication {
    public static void main(String[] args) {
        List<TableInfo> tableInfoList = BuildTable.getTables();

        BuildBase.execute();
        for (TableInfo tableInfo:tableInfoList) {
            BuildPo.execute(tableInfo);
            BuildQuery.execute(tableInfo);
            BuildMappers.execute(tableInfo);
            BuildMappersXML.execute(tableInfo);
            BuildService.execute(tableInfo);
            BuildServiceImpl.execute(tableInfo);
            BuildController.execute(tableInfo);
        }
    }
}
