# IDEA中运行项目说明

## 项目概述

- **项目名称**: 职工考勤管理系统
- **技术栈**: Java Servlet + JDBC + HTML/JavaScript (原生)
- **开发团队**: 汪轩增（队长）、梁宏博、贾兮雯、周童、刘鹏志

## 环境要求

- **JDK**: 1.8 或更高版本
- **MySQL**: 8.0 或更高版本
- **Maven**: 3.6 或更高版本
- **IDEA**: 2020.1 或更高版本（推荐 Ultimate 版）

---

## 方法一：使用Maven Tomcat插件运行（推荐，最简单）

### 1. 导入项目

1. 打开 IDEA
2. 选择 `File` -> `Open`
3. 选择项目目录 `d:/C++/attendance`（或你的项目路径）
4. 选择 `Open as Project`
5. 等待 Maven 自动导入依赖（右下角有进度条）

### 2. 配置JDK

1. `File` -> `Project Structure` (Ctrl+Alt+Shift+S)
2. `Project` -> `Project SDK` -> 选择你的 JDK 1.8
3. `Project language level` -> 选择 `8`
4. 点击 `Apply` -> `OK`

### 3. 配置数据库

1. 确保 MySQL 服务已启动
2. 创建数据库并导入初始数据：

```bash
# 在命令行执行（需要mysql命令）
mysql -u root -p < sql/init.sql
```

3. 修改数据库配置 `src/main/resources/db.properties`：

```properties
db.driver=com.mysql.cj.jdbc.Driver
db.url=jdbc:mysql://localhost:3306/attendance_system?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
db.username=root
db.password=你的MySQL密码
```

### 4. 运行项目

**方式A：使用Maven面板**

1. 打开右侧 **Maven** 面板（如果没有，点击左下角方块图标或按 `Alt+9`）
2. 展开 `attendance-system` -> `Plugins` -> `tomcat7`
3. 双击 `tomcat7:run`
4. 等待控制台显示 `启动成功` 信息

**方式B：使用终端**

```bash
mvn tomcat7:run
```

### 5. 访问系统

打开浏览器访问：`http://localhost:8084/attendance/`

---

## 方法二：使用IDEA配置Tomcat运行

### 1. 导入项目（同上）

### 2. 配置项目结构

1. `File` -> `Project Structure` (Ctrl+Alt+Shift+S)
2. `Project`:
   - `Project SDK`: 选择 JDK 1.8
   - `Project language level`: 8
3. `Modules`:
   - 确保 `src/main/java` 标记为 `Sources`（蓝色）
   - 确保 `src/main/resources` 标记为 `Resources`（黄色）
   - 确保 `src/main/webapp` 标记为 `Web`（地球图标）
4. `Artifacts`:
   - 点击 `+` -> `Web Application: Exploded` -> `From Modules`
   - 选择 `attendance-system` 模块
   - 点击 `OK`

### 3. 配置Tomcat服务器

1. 点击右上角 `Add Configuration`（或 `Run` -> `Edit Configurations`）
2. 点击 `+` -> `Tomcat Server` -> `Local`
3. **Server** 标签页：
   - `Name`: 输入 `Tomcat 9`
   - `Application server`: 点击 `Configure`，选择你的 Tomcat 安装目录
   - `URL`: `http://localhost:8082/attendance/`
   - `HTTP port`: `8082`
   - `JMX port`: `1099`
4. **Deployment** 标签页：
   - 点击 `+` -> `Artifact`
   - 选择 `attendance:war exploded`
   - `Application context`: `/attendance`
5. 点击 `Apply` -> `OK`

### 4. 配置数据库（同上）

### 5. 运行项目

1. 点击右上角运行按钮 `▶`（或按 `Shift+F10`）
2. 等待 Tomcat 启动完成
3. 浏览器会自动打开或手动访问 `http://localhost:8082/attendance/`

---

## 方法三：打包WAR部署到独立Tomcat

### 1. 打包项目

```bash
mvn clean package
```

### 2. 部署到Tomcat

1. 复制 `target/attendance.war` 到 Tomcat 的 `webapps` 目录
2. 启动 Tomcat：`bin/startup.bat`（Windows）或 `bin/startup.sh`（Linux/Mac）
3. 访问：`http://localhost:8080/attendance/`

---

## 登录账号

| 用户名 | 密码 | 角色 | 姓名 |
|--------|------|------|------|
| admin | 123456 | 管理员 | 系统管理员 |
| wangxz | 123456 | 部门经理 | 汪轩增（队长） |
| lianghb | 123456 | 部门经理 | 梁宏博 |
| jiaxw | 123456 | 部门经理 | 贾兮雯 |
| zhout | 123456 | 部门经理 | 周童 |
| liupz | 123456 | 部门经理 | 刘鹏志 |

---

## 常见问题解决

### 1. Maven依赖下载失败

**现象**：控制台显示 `Could not find artifact` 或下载卡住

**解决**：
1. 检查网络连接
2. 更换Maven镜像源，修改 `~/.m2/settings.xml`：
```xml
<mirrors>
    <mirror>
        <id>aliyun</id>
        <name>Aliyun Maven</name>
        <url>https://maven.aliyun.com/repository/public</url>
        <mirrorOf>central</mirrorOf>
    </mirror>
</mirrors>
```
3. 右键 `pom.xml` -> `Maven` -> `Reload Project`

### 1.1 缺少Servlet API报错

**现象**：IDEA中报错 `Cannot resolve symbol 'servlet'`

**解决**：
1. `File` -> `Project Structure` -> `Modules`
2. 点击 `+` -> `Library` -> `Java`
3. 选择Tomcat安装目录下的 `lib/servlet-api.jar`
4. 或者直接在pom.xml中已配置依赖，执行 `mvn clean install` 重新下载依赖

### 2. 端口被占用（Address already in use）

**现象**：控制台显示 `Address already in use: bind`，如8085端口冲突

**解决**：
```bash
# Windows查找占用端口的进程（以8085为例）
netstat -ano | findstr :8085

# 结束进程（将<PID>替换为实际的进程ID）
taskkill /PID <PID> /F
```

或在 `pom.xml` 中修改端口：
```xml
<configuration>
    <port>8084</port>  <!-- 改为其他未被占用的端口 -->
</configuration>
```

**注意**：修改端口后，访问地址也要相应改变，如 `http://localhost:8084/attendance/`

### 3. 数据库连接失败

**现象**：控制台显示 `Communications link failure` 或 `Access denied`

**解决**：
1. 检查 MySQL 服务是否启动
2. 检查 `db.properties` 中的用户名密码是否正确
3. 检查数据库 `attendance_system` 是否存在
4. 如果是MySQL 8.0，确保使用 `com.mysql.cj.jdbc.Driver` 驱动

### 4. 中文乱码

**现象**：页面显示乱码或数据库中文显示为问号

**解决**：
1. 确保数据库字符集为 `utf8mb4`：
```sql
ALTER DATABASE attendance_system CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```
2. 确保 `db.properties` 包含 `characterEncoding=utf-8`
3. IDEA设置：`File` -> `Settings` -> `Editor` -> `File Encodings` -> 全部设置为 `UTF-8`

### 5. 静态资源404

**现象**：CSS、JS文件加载失败，页面无样式

**解决**：
1. 确保 `web.xml` 中配置了正确的欢迎页面
2. 检查访问路径是否正确（应包含 `/attendance/` 上下文）
3. 清理浏览器缓存

### 6. 登录失败但账号密码正确

**现象**：提示用户名或密码错误

**解决**：
1. 检查数据库中用户状态 `status` 是否为 1（启用）
2. 检查密码是否为 MD5 加密存储（123456 的MD5是 `e10adc3949ba59abbe56e057f20f883e`）

### 7. IDEA无法识别Servlet

**现象**：Servlet类显示红色错误，无法导入 `javax.servlet`

**解决**：
1. `File` -> `Project Structure` -> `Modules`
2. 点击 `+` -> `Library` -> `Java`
3. 选择 Tomcat 安装目录下的 `lib/servlet-api.jar`
4. 点击 `Apply` -> `OK`

---

## 项目结构说明

```
attendance/
├── sql/
│   └── init.sql              # 数据库初始化脚本
├── src/main/java/com/attendance/
│   ├── model/                # 实体类
│   ├── dao/                  # 数据访问层
│   ├── service/              # 业务逻辑层
│   ├── servlet/              # 控制层
│   ├── filter/               # 过滤器
│   ├── listener/             # 监听器
│   └── util/                 # 工具类
├── src/main/webapp/
│   ├── static/               # 静态资源
│   │   ├── login.html        # 登录页
│   │   ├── css/style.css     # 样式
│   │   ├── js/api.js         # API工具
│   │   └── pages/index.html  # 主页面
│   └── WEB-INF/web.xml       # Web配置
├── src/main/resources/
│   └── db.properties         # 数据库配置
└── pom.xml                   # Maven配置
```

---

## 启动成功标志

控制台显示：
```
========================================
   职工考勤管理系统 启动成功！
   访问地址: http://localhost:8084/attendance/
========================================
```

然后打开浏览器访问即可！

---

## 技术栈

- **后端**: Java Servlet + JDBC
- **前端**: HTML5 + CSS3 + JavaScript (原生)
- **数据库**: MySQL 8.0
- **构建工具**: Maven
- **服务器**: Tomcat 8.5/9.0

---

**文档版本**: V1.1  
**最后更新**: 2026年4月26日
