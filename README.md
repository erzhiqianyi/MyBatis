# MyBatis
Myabits  source read note and sample

## 源码阅读的意义
### 为什么要阅读源码
- 理解MyBatis实现原理
- 学习优秀的的设计和技巧
- 提示自我

### 如何阅读源码
- 调试追踪
- 归类总结
- 上下文整合
- 合理利用工具

### 为什么阅读MyBatis源码
- MyBatis 应用广泛
- 设计知识面广
- 代码量合适

### 源码阅读流程
- 掌握项目使用方法
- 把握核心功能，重点关注核心功能相关代码

## Mybatis概述
### JDBC如何处理数据库操作
- 连接流程  
建立连接  → 拼装和执行sql语句 → 转化操作结果
  [示例代码](JDBCDemo)
### ORM框架
对象关系映射(Object Relational Mapping ,ORM)

### MyBatis特点
- 核心映射
  Java方法与SQL语句关联，SQL语句的参数或结果和对象关联，屏蔽关系型数据库，面向对象的方式进行数据库读写。
- 缓存功能
- 懒加载功能
- 主键自增功能
- 多数据集处理

### MyBatis配置方式
- 完全代码配置
- 基于XMl配置
- 外部框架配置

### MyBatis 使用示例
[官方教程](https://mybatis.org/mybatis-3/zh/getting-started.html)
#### 单独使用
[示例代码](MyBatisDemo)
##### 引入 MyBatis 依赖
使用 maven ,引入依赖
```xml
<dependency>
  <groupId>org.mybatis</groupId>
  <artifactId>mybatis</artifactId>
  <version>x.x.x</version>
</dependency>
```
##### 构建  SqlSessionFactory
示例代码 [MyBatisDemo](MyBatisDemo)
- 从XML中构建  
  从 XML 配置文件或一个预先配置的 Configuration 实例来构建出 SqlSessionFactory 实例。
- 用Java代码直接构建  
  使用 ```Configuration ``` 进行配置
  
#### 在Spring Boot 中使用
结合Spring Boot 使用  
示例代码 [SpringMyBatis](SpringMyBatis)

### MyBatis核心业务功能
简化 JDBC 操作流程，去除重复性操作，将面向过程的操作变成面向对象的操作。
- 将输入语句转换成纯粹的SQL语句
- 将数据库操作节点和映射接口中的抽象方法进行绑定，在抽象方法被调用时执行数据库操作
- 将输入参数对象转化为数据库操作语句中的参数
- 将数据库操作语句的返回结果转化为对象

## MyBatis 跟踪调试
对MyBatis 源码进行跟踪调试，了解源码的运行流程。
[示例代码](SpringMyBatisDebugDemo)
### 类加载流程
加载 -> 验证 -> 准备 -> 解析 -> 初始化
#### 加载 
把class 字节码文件从各个来源通过类加载器载入内存
- 字节码来源
  - 编译生成的.class文件
  - jar包中的.class文件
  - 远程动态代理实时编译
- 类加载器
  - 启动类加载器
  - 扩展类加载器
  - 应用类加载器
  - 自定义类加载器
#### 验证
- 验证文件格式
- 元数据验证
- 字节码验证
- 符号引用验证
#### 准备
为类变量分配内存，赋初值,根据不同类型的默认初始值为变量赋初值 
- 基本内型:默认0 
- 引用类型:null
- 常量: 设置的初始值

#### 解析
将常量池内的符号引用替换为直接引用的过程。

#### 初始化
这个阶段主要是对类变量初始化，是执行类构造器的过程。

通过类加载器读取外部资源
- 应用
通过类加载器来读取资源文件
### 配置文件加载流程
#### 利用Resources读取配置文件
```Resources.getResourceAsStream()``` 得到资源
利用类加载器读取外部资源。
- 存在多个类加资器如何处理
将类加载器包装起来，当作一个类加载器使用 ,参考 ```org.apache.ibatis.io.ClassLoaderWrapper```.
