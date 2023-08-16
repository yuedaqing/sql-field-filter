<a name="NC8dt"></a>
# 无需任何配置、启动即用
<a name="WJT71"></a>
## 项目来由：

- 由于工作时经常需要补录数据，但两边数据库定义的表规范不一致，一边是大驼峰，另一边是全小写。
- 每次插入数据都要将每个字段进行批量转换，于是抽点时间造了这个轮子。
<a name="CX1cy"></a>
## 使用教程：

1. 在原字段输入框中：输入原SQL语句的全部字段
2. 新字段指：新字段中必须有原字段中的任意(一个或多个)属性，且字段属性使用英文逗号分割
3. 主键字段：根据你输入的主键字段(也可以是其它字段)，在下载的SQL文件中会返回全部的主键字段及数量
4. 原表名：上传的SQL文件中的表名
5. 新表名：下载的SQL文件中的表名
6. 字段风格：下划线  小驼峰  全小写
7. 上传你的SQL文件
<a name="jCgHs"></a>
## 项目架构

- 前端：Vue3+VueRouter+Antdesign+axios
- 后端：springBoot2.7.6+knife4j+Hutool-all
<a name="JKxYM"></a>
## 后端目录结构
```
├─src
│  ├─main
│  │  ├─java
│  │  │  └─com
│  │  │      └─yue
│  │  │          └─sqlfilter
│  │  │              │  SqlFilterApplication.java 启动类
│  │  │              │  
│  │  │              ├─common
│  │  │              │      BaseResponse.java 通用返回类
│  │  │              │      ErrorCode.java 自定义错误码
│  │  │              │      ResultUtils.java 返回工具类
│  │  │              │      
│  │  │              ├─config
│  │  │              │      CorsConfig.java 全局跨域配置
│  │  │              │      JsonConfig.java Spring MVC Json 配置
│  │  │              │      Knife4jConfig.java Knife4j 接口文档配置
│  │  │              │      
│  │  │              ├─controller
│  │  │              │      SqlFilterController.java
│  │  │              │      
│  │  │              ├─exception
│  │  │              │      BusinessException.java 自定义异常类
│  │  │              │      GlobalExceptionHandler.java 全局异常处理器
│  │  │              │      ThrowUtils.java 抛异常工具类
│  │  │              │      
│  │  │              ├─model
│  │  │              │      SqlField.java 处理SQL实体类
│  │  │              │      
│  │  │              ├─service
│  │  │              │  │  SqlFilterService.java 定义接口
│  │  │              │  │  
│  │  │              │  └─imp
│  │  │              │          SqlFilterImpl.java 接口实现类
│  │  │              │          
│  │  │              └─utils
│  │  │                      SqlFieldUtil.java SQL工具类
│  │  │                      
│  │  └─resources
│  │          application.yml 配置文件
```
