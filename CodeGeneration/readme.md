# SpringBootCodeGeneration

<i>Springboot框架代码生成</i>

## 生成内容

1. 基本工具类DateUtils
2. Entity类
3. Mapper和xml映射
4. Service接口和实现类
5. Controller
6. 分页所需工具类

目录结构：

```
java
  ├──com.example.demo
  │      ├─bean
  │      ├─builder
  │      └─utils
  └──resources
         └─templates

> 生成类在builder，如果要添加模板作为工具类可以将固定内容写定在txt文件放入template中，然后在builder/BuildBase.java中添加