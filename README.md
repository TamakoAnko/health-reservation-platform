# 健康体检预约管理平台

一个基于 Spring Boot、Dubbo、MySQL、Redis 和 Spring AI Alibaba 的健康体检预约管理平台，覆盖体检套餐维护、会员管理、预约容量、预约记录和健康知识问答等业务。

> 项目定位：学习与作品展示项目，不是已经面向真实用户稳定运营的商业系统。
>
> 仓库中的云服务密钥、数据库密码和 Token 均已替换为环境变量，运行前需要按照 `.env.example` 配置。

## 项目功能

- 检查项管理：新增、编辑、删除、分页查询和条件筛选。
- 检查组管理：维护检查组与检查项之间的多对多关系。
- 体检套餐管理：套餐新增、修改、删除、图片上传、检查组关联和详情查询。
- 会员管理：会员注册、登录、信息维护和手机号验证。
- 预约容量管理：按日期配置可预约人数，查看月度预约情况。
- 在线预约：校验预约日期、剩余容量、会员身份和重复预约状态。
- 预约记录：保存预约信息，支持按照预约 ID 查询详情。
- Redis 缓存：保存登录态、验证码、套餐和预约相关热点数据。
- 定时任务：执行过期数据清理、缓存维护和状态同步。
- AI 健康助手：基于 RAG 检索健康知识文档，并通过 SSE 流式返回回答。

## 技术栈

| 分类 | 技术 |
| --- | --- |
| 开发语言 | Java 8、Java 17（AI 模块） |
| 基础框架 | Spring Boot、Spring MVC、Spring Security |
| 服务调用 | Dubbo、ZooKeeper |
| 数据访问 | MyBatis、PageHelper、MySQL |
| 缓存 | Redis |
| 模板与静态页 | FreeMarker、Nginx |
| AI | Spring AI Alibaba、DashScope、RAG、SSE |
| 构建工具 | Maven |

## 系统架构

```text
后台管理端 health_backend
        │
        │ Dubbo RPC
        ▼
业务服务 health_service_provider ──────── MySQL
        ▲                                  │
        │ Dubbo RPC                         │
移动端接口 health_mobile                  Redis
        │                                  │
        │                                  ZooKeeper
        ▼
AI 助手 AIChatAssistant ─────── DashScope / 向量检索

定时任务 health_jobs
```

## 模块说明

| 模块 | 作用 |
| --- | --- |
| `health_common` | 公共实体、工具类、统一返回和基础设施代码 |
| `health_interface` | Dubbo 服务接口和参数契约 |
| `health_service_provider` | 核心业务实现、MyBatis Mapper 和数据库访问 |
| `health_backend` | 后台管理端接口和页面资源 |
| `health_mobile` | 移动端注册登录、套餐查询和预约接口 |
| `health_jobs` | Quartz 定时任务 |
| `AIChatAssistant` | Spring AI 健康问答和 RAG 检索模块 |
| `spring_security_demo` | Spring Security 示例模块 |

## 核心设计

### 1. 预约流程

预约主流程分为四步：

1. 按预约日期查询容量配置，确认当天已经开放预约。
2. 比较已预约人数和可预约人数，判断是否还有名额。
3. 查询会员、套餐和日期是否已经存在有效预约，防止重复预约。
4. 保存预约记录，并更新预约人数和必要的会员信息。

### 2. 并发防超约改进

当前版本中的预约人数更新属于“先查询、再判断、后更新”，在并发请求下存在超约风险。

生产化改造建议使用数据库条件更新：

```sql
UPDATE order_setting
SET reservations = reservations + 1
WHERE order_date = ?
  AND reservations < number;
```

影响行数为 `0` 时直接返回名额已满。对于同一会员、同一套餐和同一日期的重复预约，还应增加唯一约束作为数据库层兜底。

### 3. Redis 缓存

- 验证码按“手机号 + 业务类型”保存，避免登录和预约验证码互相覆盖。
- 登录成功后保存服务端会话，移动端请求通过会话判断登录状态。
- 套餐和预约数据使用 Cache Aside 思路，写操作优先更新数据库，再删除缓存。

### 4. AI 问答

- 离线阶段对健康知识文档进行切分、向量化和入库。
- 在线阶段检索相关片段，组装 Prompt 后交给大模型。
- 使用 SSE 流式返回，降低用户首字等待时间。
- 实时业务数据应通过受控工具查询，不能依赖模型记忆生成。

## 本地运行

### 1. 环境要求

- JDK 8，用于传统业务模块。
- JDK 17，用于 `AIChatAssistant`。
- Maven 3.8+。
- MySQL 8。
- Redis 6+。
- ZooKeeper 3.6+。

### 2. 初始化数据库

仓库暂未包含完整的数据库初始化 SQL，需要根据实体和 Mapper 创建 `health` 数据库及对应表结构。

### 3. 配置环境变量

```bash
cp .env.example .env
```

至少需要设置：

- `DB_PASSWORD`
- `REDIS_HOST`
- `ZOOKEEPER_HOST`
- `DASHSCOPE_API_KEY`
- `QINIU_ACCESS_KEY`
- `QINIU_SECRET_KEY`
- `QINIU_BUCKET`

短信功能还需要配置阿里云或腾讯云短信参数。

### 4. 启动后端

先启动 ZooKeeper 和 Redis，再按顺序启动：

```bash
mvn -pl health_service_provider -am spring-boot:run
mvn -pl health_backend -am spring-boot:run
mvn -pl health_mobile -am spring-boot:run
mvn -pl health_jobs -am spring-boot:run
mvn -pl AIChatAssistant -am spring-boot:run
```

默认端口：

| 模块 | 端口 |
| --- | ---: |
| `health_service_provider` | 81 |
| `health_backend` | 82 |
| `health_mobile` | 80 |
| `health_jobs` | 88 |
| `AIChatAssistant` | 91 |

## 接口示例

```text
GET  /setmeal/getAllSetmeal.do
GET  /setmeal/findById.do?id={id}
POST /member/login.do
POST /order/submit.do
GET  /order/findById.do?id={id}
GET  /ordersetting/getOrderSettingByMonth.do?date=2026-10
GET  /rag4aiops?message={question}
```

## 我的工作

本项目主要作为课程与实训实践项目完成，我参与的工作包括：

- 参与检查项、检查组、套餐、会员和预约模块的接口开发与联调。
- 梳理套餐、检查组、检查项以及预约记录之间的数据关系。
- 参与 Dubbo 服务接口拆分、Redis 缓存设计和定时任务配置。
- 参与 AI 问答模块的 RAG 检索与流式返回实现。
- 使用 AI 工具辅助生成脚手架、重复代码和排查建议，并进行人工审查和接口验证。

## 已知问题

- 预约人数更新尚未改成数据库原子更新，并发场景需要加固。
- 缺少完整数据库初始化脚本和自动化测试。
- 部分配置包含本机路径，需要在其他环境中调整。
- AI 模块依赖外部大模型，需要网络、API Key 和额度。
- 项目没有真实线上流量数据。

## 开源说明

本仓库用于学习和作品展示。仓库目前未声明额外开源许可证，如需复用请先联系作者并确认课程或实训材料的授权范围。

