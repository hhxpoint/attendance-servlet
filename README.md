> 课程团队项目，原始成员与分工见 [CONTRIBUTORS.md](CONTRIBUTORS.md)。本仓库由 hhxpoint 整理、修复构建并补充文档。

# 职工考勤管理系统

## 项目简介

本项目是一个基于 Java Servlet 的职工考勤管理系统，采用传统的 JavaWeb 技术栈实现。

## 技术架构

| 分类 | 技术 |
|------|------|
| 后端 | Java Servlet + JDBC |
| 前端 | HTML + CSS + JavaScript |
| 数据库 | MySQL 8.0 |
| 构建工具 | Maven |
| 服务器 | Tomcat 8.5/9.0 |

## 项目结构

```
attendance/
├── sql/
│   └── init.sql              # 数据库初始化脚本
├── src/main/java/com/attendance/
│   ├── model/                # 实体类
│   │   ├── User.java         # 用户
│   │   ├── Department.java   # 部门
│   │   ├── Attendance.java   # 考勤
│   │   ├── LeaveApply.java   # 请假申请
│   │   ├── Announcement.java # 公告
│   │   └── SysLog.java       # 系统日志
│   ├── dao/                  # 数据访问层
│   │   ├── UserDAO.java
│   │   ├── DepartmentDAO.java
│   │   ├── AttendanceDAO.java
│   │   ├── LeaveApplyDAO.java
│   │   ├── AnnouncementDAO.java
│   │   └── SysLogDAO.java
│   ├── service/              # 业务逻辑层
│   │   ├── UserService.java
│   │   ├── DepartmentService.java
│   │   ├── AttendanceService.java
│   │   ├── LeaveApplyService.java
│   │   ├── AnnouncementService.java
│   │   └── SysLogService.java
│   ├── servlet/               # 控制层
│   │   ├── UserServlet.java
│   │   ├── DepartmentServlet.java
│   │   ├── AttendanceServlet.java
│   │   ├── LeaveServlet.java
│   │   ├── AnnouncementServlet.java
│   │   └── SysLogServlet.java
│   ├── filter/                # 过滤器
│   │   ├── CharacterEncodingFilter.java  # 字符编码
│   │   └── LoginFilter.java              # 登录验证
│   ├── listener/              # 监听器
│   │   ├── AppListener.java   # 应用启动监听
│   │   └── SessionListener.java # 会话监听
│   └── util/                  # 工具类
│       ├── DBUtil.java        # 数据库工具
│       ├── MD5Util.java       # MD5加密
│       ├── DateUtil.java      # 日期工具
│       └── Result.java         # 统一响应
├── src/main/webapp/
│   ├── WEB-INF/web.xml        # Web配置
│   ├── static/                # 静态资源
│   │   ├── login.html         # 登录页
│   │   ├── pages/index.html   # 主页
│   │   ├── css/style.css      # 样式
│   │   └── js/api.js          # API工具
│   └── index.jsp
├── src/main/resources/
│   └── db.properties          # 数据库配置
└── pom.xml                    # Maven配置
```

## 团队成员

| 姓名 | 角色 | 分工 |
|------|------|------|
| **汪轩增** | 队长 | 整体架构、核心开发、数据库设计 |
| 梁宏博 | 队员 | 考勤管理、请假管理模块 |
| 贾兮雯 | 队员 | DAO层实现、前端联调 |
| 周童 | 队员 | 过滤器/监听器、日志模块 |
| 刘鹏志 | 队员 | 部门管理、公告管理模块 |

## 知识点覆盖

- [x] Javaweb开发环境搭建
- [x] HTML基础
- [x] JavaScript
- [x] Java Servlet处理表单
- [x] Java Servlet交互
- [x] JSP内置对象
- [x] 过滤器（Filter）
- [x] 监听器（Listener）
- [x] 文件上传与下载
- [x] JDBC数据库操作
- [x] JSTL
- [x] EL表达式
- [x] 中央控制器（Servlet设计模式）

## 快速开始

### 1. 环境要求

- JDK 1.8+
- MySQL 8.0+
- Tomcat 8.5/9.0
- Maven 3.6+

### 2. 数据库配置

修改 `src/main/resources/db.properties`：

```properties
db.driver=com.mysql.cj.jdbc.Driver
db.url=jdbc:mysql://localhost:3306/attendance_system?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
db.username=root
db.password=123456
```

### 3. 初始化数据库

```bash
mysql -u root -p < sql/init.sql
```

### 4. 运行项目

**方式一：Maven Tomcat 插件**

```bash
mvn tomcat7:run
```

**方式二：IDEA 配置 Tomcat**

1. File -> Project Structure -> Artifacts -> Web Application: Exploded
2. Add Configuration -> Tomcat Server -> Local
3. Deployment -> 添加 artifacts

### 5. 访问系统

地址：http://localhost:8081/attendance/

## 登录账号

| 用户名 | 密码 | 角色 |
|--------|------|------|
| admin | 123456 | 管理员 |
| wangxz | 123456 | 技术总监/队长 |
| lianghb | 123456 | 部门经理 |
| jiaxw | 123456 | 部门经理 |
| zhout | 123456 | 部门经理 |
| liupz | 123456 | 部门经理 |

## 功能模块

### 1. 用户管理
- 用户列表
- 新增/编辑用户
- 删除用户
- 重置密码
- 修改密码

### 2. 考勤管理
- 今日考勤打卡
- 考勤记录查询
- 考勤统计

### 3. 请假管理
- 发起请假申请
- 请假审批
- 请假记录查询

### 4. 部门管理
- 部门列表
- 新增/编辑部门
- 删除部门

### 5. 公告管理
- 发布公告
- 公告列表
- 公告置顶

### 6. 系统日志
- 操作日志查询
- 日志统计

## API 接口

| 接口 | 方法 | 说明 |
|------|------|------|
| `/user/login` | POST | 用户登录 |
| `/user/logout` | POST | 用户登出 |
| `/user/current` | GET | 获取当前用户 |
| `/user/list` | GET | 用户列表 |
| `/attendance/today` | GET | 今日考勤 |
| `/attendance/clockIn` | POST | 上班打卡 |
| `/attendance/clockOut` | POST | 下班打卡 |
| `/leave/apply` | POST | 申请请假 |
| `/leave/pending` | GET | 待审批列表 |
| `/dept/list` | GET | 部门列表 |
| `/announcement/list` | GET | 公告列表 |

## License

MIT License
