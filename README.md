## Java代码练习

一个以 **`com.weixf`** 为基础包的 Java / Spring 学习与实践仓库，涵盖从 Java 基础（注解、集合、JDBC、并发）到 Spring Boot、安全框架、规则引擎、定时任务、RPC、Web Service、文档导出等主题。 **共
18 个模块、约 877 个 Java 文件**（含测试）。

仓库定位为 **个人学习 / 实验性质**，多数模块是可独立运行的 Demo，少数为可复用库 / 参考实现。

---

## 技术基线

| 项           | 取值                                                                                                                     |
|--------------|--------------------------------------------------------------------------------------------------------------------------|
| 语言         | **Java 8**（`maven.compiler.source/target = 1.8`）                                                                       |
| 主体框架     | **Spring Boot 2.5.14**                                                                                                   |
| 历史分支框架 | `spring-study` 系列使用 **Spring Boot 2.4.8**                                                                            |
| 基础包       | `com.weixf`                                                                                                              |
| 构建工具     | **Maven**（各模块独立 `pom.xml`）                                                                                        |
| 常用依赖     | `lombok`、`fastjson`、`druid`、`mybatis` / `mybatis-plus`、`hutool`、`mysql-connector-java`、`commons-*`、`jdom2`、`poi` |

> **工程结构说明**：
> - **没有根 `pom.xml`**（非聚合多模块工程），各模块可独立 `mvn` 构建运行。
> - 只有两个 **聚合模块**自带子模块：`spring-boot-study`（10 个子模块）与 `spring-study`（3 个子模块）。
> - `jdbcLearn` 与 `web-event-demo` 刻意 **不依赖 Spring Boot**，用于学习原生 JDBC / Servlet 基础。

---

## 模块总览

| 模块                       | 主题                                          | 技术栈                                 | 运行方式               |
|----------------------------|-----------------------------------------------|----------------------------------------|------------------------|
| `annotation`               | 自定义注解 + 反射读取                         | 纯 Java（SB 父工程）                   | 含 `main`              |
| `collection-study`         | 手写 List / 数据结构                          | 纯 Java                                | 库 / 学习              |
| `jdbcLearn`                | JDBC 基础 → 连接池 → 泛型 DAO                 | Druid、HikariCP、MySQL                 | `App`                  |
| `thread-study`             | 并发 / JUC 全系列                             | 纯 Java（SB 父工程）                   | `ThreadApp`            |
| `spring-study` ⭐          | Spring 源码 / XML Security / PDM 代码生成     | SB 2.4.8、Spring MVC、Freemarker       | 聚合（3 子模块）       |
| `spring-boot-study` ⭐     | 10 个 Spring Boot 子 Demo                     | SB 2.5.14                              | 聚合（10 子模块）      |
| `rpc-study`                | Forest HTTP / RPC 客户端                      | SB、Forest、Jsoup                      | `RpcApp`               |
| `webservice-demo`          | Spring Web Services / JAX-WS                  | SB、`spring-boot-starter-web-services` | `Main` / `@WebService` |
| `web-event-demo`           | 基于 Servlet 的事件 / 监听器框架              | 纯 Servlet + JSTL                      | 需 Servlet 容器        |
| `security-springboot-demo` | Spring Security + JWT + MyBatis-Plus RBAC     | SB、Security、JWT、MP                  | `SecurityDemoApp`      |
| `shiro-springboot-demo`    | Apache Shiro + MyBatis-Plus + Thymeleaf       | SB、Shiro、Ehcache                     | `ShiroDemoApp`         |
| `liteflow-learn`           | LiteFlow 规则引擎 / 流程编排                  | SB、`liteflow-spring-boot-starter`     | `LiteFlowApp`          |
| `quartz-springboot-demo`   | Quartz 动态定时任务                           | SB、Quartz、MyBatis、c3p0              | `QuartzDemoApp`        |
| `commons-utils`            | 通用工具库（缓存/加解密/Excel/树）            | SB、`guava`、`poi`、`hutool`           | 库                     |
| `code-generator`           | 代码生成脚手架起点                            | SB                                     | `GeneratorApp`         |
| `code-test-module`         | 第三方库实验（drools/disruptor/jedis/axis2…） | SB、多种                               | 多 Demo                |
| `word-export`              | POI-TL 模板 Word/PDF 导出                     | SB、`poi-tl`、documents4j              | `WordExportApp`        |
| `design-pattern`           | GoF + J2EE 设计模式全集（258 文件）           | SB、`lombok`                           | 各模式 Demo            |

> ⭐ 表示聚合模块（目录下含 `pom.xml` 父工程 + 子模块）。

---

## 分主题详解

### 一、Java 基础

- **`annotation`**：定义自定义注解 `A` / `B`，用反射（`Method.getAnnotation`）读取注解元数据。
- **`collection-study`**：手写 `MyList` 等数据结构，理解集合底层实现。
- **`jdbcLearn`**：从原生 JDBC（`JDBCQuick` / `Prepared` / `Operation` / `Injection`）到 Druid / HikariCP 连接池，再到基于 `Column` / `Table` 注解的通用 `BaseDAO`，附 `init.sql`。
- **`thread-study`**：覆盖线程创建、守护 / 优先级、`synchronized`、死锁、`Lock` 卖票、`BlockingQueue`、`CountDownLatch` / `CyclicBarrier` / `Semaphore`、`CompletableFuture`、`ForkJoin`
  、线程池、读写锁、生产者-消费者等。

### 二、Spring / Spring Boot

- **`spring-study`**（聚合，SB 2.4.8）：
  - `spring-source-learn`（93 文件）：通过 **mini-spring（手写 Spring）** 学习 IoC/AOP 源码，含自动代理、循环依赖、事件监听、属性占位符等大量 XML 测试。
  - `spring-security-test`：传统 **XML 配置** 的 Spring Security + MVC（web.xml、JSP）。
  - `pdm-code-generate`：PowerDesigner PDM → 代码生成（Freemarker + MyBatis-Plus + Oracle）。
- **`spring-boot-study`**（聚合，SB 2.5.14，10 子模块）：`card-analysis`（Web+Thymeleaf）、`dynamic-datasource-demo`（Druid 动态数据源 + AOP）、`encrypt-spring-boot-starter`（自定义 Starter）、
  `grace-controller`（校验/优雅 controller）、`schema-maker`（Druid+Oracle 建库）、`security-auth-menu` / `security-web-demo`（Security 鉴权）、`shiro-test`（Shiro+Redis）、
  `sms-spring-boot-starter`（自定义短信 Starter）、`web-demo`（AOP+Redis）。

### 三、Web / 远程调用

- **`rpc-study`**：基于 **Forest** 声明式 HTTP 客户端调用外部 API（部门 / 员工 / 车辆等 DTO）。
- **`webservice-demo`**：Spring Web Services / **JAX-WS**（`@WebService`），含 jdom2 XML 解析。
- **`web-event-demo`**：脱离框架，手写基于 Servlet 的事件发布 / 监听器机制（`EventPublisher` / `EventListener` / `LoginEventListener`），演示监听器最佳实践。

### 四、安全 / 权限（高频主题）

- **`security-springboot-demo`**：Spring Security + **JWT** + MyBatis-Plus 的 RBAC 完整 Demo（`JWTAuthenticationTokenFilter`、5 个 Handler、`SysUser/SysRole/SysMenu`、SQL 脚本）。
- **`shiro-springboot-demo`**：Apache **Shiro** + MyBatis-Plus + Thymeleaf 认证授权（`ShiroConfig`、`MyRealm`、`ehcache-shiro.xml`）。
- 鉴权主题在 `spring-boot-study`（`security-auth-menu`、`security-web-demo`、`shiro-test`）与 `spring-study`（`spring-security-test`）中亦有大量复现。

### 五、数据 / 流程 / 调度

- **`liteflow-learn`**： **LiteFlow** 规则引擎，`flow.el.xml` 编排 `ACmp`…`NCmp` 组件，带自定义 `MyContext`。
- **`quartz-springboot-demo`**： **Quartz** 动态任务管理（`QuartzUtils`、`SchedulerConfig`、REST 接口启停任务）。

### 六、工具 / 实用

- **`commons-utils`**（42 文件）：可复用工具库 —— 本地缓存、HTTP、加解密（`AESUtil` / `RSAUtil`）、Excel 导入导出（POI + 注解）、树构建等。
- **`code-generator`**：代码生成 Spring Boot 应用起点（仅入口类）。
- **`code-test-module`**（48 文件）：多第三方库实验场 —— LMAX **Disruptor**、 **Drools** 规则引擎、Redis（Jedis/Lettuce）、SOAP（Axis2）、邮件、FreeMarker 条码、iText/PDF、JNA 等。
- **`word-export`**（32 文件）：基于 **POI-TL** 的 Word/PDF 导出，含表格 / 高亮 / Markdown 自定义渲染策略（`CustomTableRenderPolicy` 等）。
- **`design-pattern`**（258 文件，最大模块）：创建型 / 结构型 / 行为型 / J2EE 共数十种 GoF 设计模式实现，每种配 `readme.txt` 与 `*PatternDemo` 入口。

---

## 构建与运行

各模块独立构建（无根聚合 POM）：

```bash
# 进入任意模块
cd spring-boot-study/security-auth-menu

# 编译
mvn clean compile

# 运行（Spring Boot 模块）
mvn spring-boot:run
# 或直接运行对应的 @SpringBootApplication 主类
```

> - JDK 需 **Java 8**。
> - 涉及数据库（MySQL / Oracle）的模块需先执行模块内 `*.sql` 初始化脚本并配置 `application.yml` 数据源。
> - `web-event-demo` 需部署到 Servlet 容器（或运行 `App.main` 观察事件机制）。

---

## 代码统计（概览）

| 模块                                          | Java 文件数 |
|-----------------------------------------------|-------------|
| `design-pattern`                              | 258         |
| `spring-study`（含 `spring-source-learn` 93） | 159         |
| `spring-boot-study`（10 子模块合计）          | 145         |
| `code-test-module`                            | 48          |
| `commons-utils`                               | 42          |
| `security-springboot-demo`                    | 39          |
| `thread-study`                                | 37          |
| `word-export`                                 | 32          |
| 其余模块                                      | 2 ~ 24 不等 |

> 合计约 **877** 个 Java 文件（`src/main/java` 约 813 个，其余为测试）。

---

## 仓库信息

- Git 远程：`git@github.com:wxingfu/maven-test.git`
- 首次提交：`2024-03-10`，最近活跃：`2025-11`
- 忽略规则见 `.gitignore`（统一排除各模块 `target/` 与 `.idea/*.iml`）
