# TikaX

TikaX 是一个基于 Apache Tika 的智能文档处理平台，能够识别、解析多种文档格式并提取纯文本内容，
将解析后的文本使用滑动窗口算法进行分块处理并持久化存储，后续可以直接进行向量化处理。

## 主要功能

1. **多格式文档解析**：
   - 支持 PDF、Word 文档(.docx)、Markdown、TXT 等多种常见文档格式
   - 支持图片识别
   - 使用 Apache Tika 进行文件类型检测和内容解析

2. **文件管理**：
   - 文件上传与存储（使用 MinIO 对象存储）
   - 文件删除与下载（Minio 预签名 URL）

3. **文本处理**：
   - 提取文档中的纯文本内容
   - 文本分块处理（滑动窗口算法）

4. **用户认证**：
   - 用户注册与登录
   - 基于 Spring Security 的安全控制

## 技术架构

### 后端技术栈

- **核心框架**：Spring Boot 4.0.0
- **安全框架**：Spring Security
- **ORM 框架**：MyBatis 4.0.0
- **数据库**：MySQL
- **缓存**：Redis
- **对象存储**：MinIO
- **文档解析**：Apache Tika 3.2.2
- **辅助工具**：
  - Lombok：简化 Java 代码
  - tess4j：OCR 文字识别
  - commonmark：Markdown 解析
  - Apache POI：Office 文档处理

    
## 功能实现流程

### 文件上传流程

1. 通过接口上传文件
2. 接收文件并进行基础验证
3. 文件上传至 MinIO
4. 使用 Apache Tika 检测文件 MIME 类型
5. 根据 MIME 类型选择合适的解析器提取文本
6. 对提取的文本进行分块处理
7. 将文件元信息和分块分别存储到数据库
8. 返回处理结果

### 文档解析流程

1. 系统根据文件 MIME 类型在解析器注册表中查找对应解析器
2. 调用相应的解析器实现（PDF、DOCX、TXT 等）
3. 解析器从文件中提取纯文本内容
4. 对提取的文本进行后续处理和存储

### 文本分块流程

1. 使用滑动窗口算法对长文本进行分割
2. 保持语义完整性，在句子边界处切割
3. 添加重叠区域确保上下文连贯性
4. 批量存储文本块到数据库

## 项目依赖

主要第三方依赖包括：

- **Spring 生态**：
  - spring-boot-starter-webmvc：Web MVC 框架
  - spring-boot-starter-security：安全框架
  - spring-boot-starter-data-redis：Redis 集成
  - spring-boot-starter-aop：面向切面编程支持

- **数据存储**：
  - mysql-connector-j：MySQL 数据库驱动
  - mybatis-spring-boot-starter：MyBatis 集成
  - minio：对象存储客户端

- **文档处理**：
  - org.apache.tika:tika-core：Tika 核心库
  - org.apache.tika:tika-parsers-standard-package：标准解析器包
  - org.apache.tika:tika-parser-pdf-module：PDF 解析模块
  - org.apache.poi:poi-ooxml：Office 文档处理
  - org.commonmark:commonmark：Markdown 解析
  - net.sourceforge.tess4j:tess4j：OCR 文字识别

- **工具类**：
  - org.projectlombok:lombok：简化代码工具
  - commons-pool2：对象池化工具

## 配置要求

- Java 17+
- MySQL 5.7+
- Redis 3.2.1+
- MinIO 对象存储服务器
- Tesseract OCR 引擎（可选，用于图像文字识别）
