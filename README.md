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