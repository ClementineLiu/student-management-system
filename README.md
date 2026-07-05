# 学生管理系统（管理员端）

基于 Spring Boot + Vue.js + Element UI + MySQL 的 Web 学生管理系统。

## 环境要求

| 软件 | 版本 | 说明 |
|---|---|---|
| JDK | 17 | `D:\jdk\17\` |
| MySQL | 8.0+ | 端口 3306 |
| Maven | 3.6+ | 构建后端（项目含 mvnw wrapper 可免安装） |
| Node.js | 16+ | 构建前端（可选，dist 已包含在项目中） |
| 浏览器 | Chrome / Edge | 推荐 Chrome |

## 快速开始

### 第一步：创建数据库

打开 MySQL 命令行，执行：

```
CREATE DATABASE IF NOT EXISTS student_management DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

然后导入建表 SQL：

```
mysql -u root -p student_management < db\student_management.sql
```

> 如果你用 Navicat：右键"数据库" → "新建数据库" → 名称填 `student_management` → 确定 → 右键该库 → "运行 SQL 文件" → 选择 `db/student_management.sql`

### 第二步：修改数据库密码

用记事本打开 `src\main\resources\application.yml`，找到这两行，改成你的 MySQL 用户名和密码：

```yaml
    username: root      # 改成你的
    password: 123456    # 改成你的
```

### 第三步：构建前端（如果 dist 目录为空）

```bash
cd src\main\resources\admin\admin
npm install
npm run build
```

> 构建产物会输出到 `src/main/resources/admin/admin/dist/`，Spring Boot 启动后会自动加载。

### 第四步：启动程序

```bash
cd D:\JavaProject\StudentManagementWeb
mvnw spring-boot:run
```

> 等待控制台出现 `学生管理系统启动成功！访问地址：http://localhost:8080/student`

### 第五步：打开浏览器

地址栏输入：

```
http://localhost:8080/student
```

### 第六步：登录

| 用户名 | 密码 |
|---|---|
| `admin` | `admin123` |

登录成功后进入管理后台首页。

---

## 功能说明

### 系统首页
- 4 个统计卡片：学生总数、班级总数、课程总数、平均成绩
- 3 个图表：各班级人数饼图、各课程平均分柱状图、成绩区间分布图

### 学生信息管理
- 表格展示所有学生，支持分页
- 搜索：按学号 / 姓名模糊搜索，按班级筛选，按性别筛选
- 新增/编辑：弹窗表单，包含学号、姓名、性别、年龄、手机、邮箱、班级、住址、入学日期
- 批量删除：勾选多行后一键删除

### 班级管理
- 增删改查班级信息
- 字段：班级名称、所属院系/专业、年级

### 课程管理
- 增删改查课程信息
- 字段：课程名称、学分、任课教师、课程描述

### 成绩管理
- 表格关联显示：学生姓名 + 学号 + 班级 + 课程名 + 分数 + 学期 + 考试类型
- 筛选：按学生、课程、学期筛选
- 录入成绩时选择学生和课程（下拉搜索）

---

## 项目文件位置

所有文件均在 D 盘：

| 内容 | 路径 |
|---|---|
| 项目代码 | `D:\JavaProject\StudentManagementWeb\` |
| 后端源码 | `src/main/java/com/student/` |
| 配置文件 | `src/main/resources/application.yml` |
| 前端页面 | `src/main/resources/admin/admin/dist/` |
| 建表 SQL | `db/student_management.sql` |
| 使用说明 | `README.md` |

---

## 常见问题

### Q: 启动报错 "Access denied for user"
> 数据库用户名或密码不对。检查 `application.yml` 里的 `username` 和 `password`。

### Q: 启动报错 "Unknown database 'student_management'"
> 还没建库。先执行第一步的 `CREATE DATABASE`。

### Q: 页面能打开但接口报 401
> Token 过期或未登录。重新登录即可（Token 有效期 1 小时）。

### Q: 页面样式错乱 / 空白
> 清除浏览器缓存后刷新（Ctrl + F5）。

### Q: 想修改前端页面
> 前端源码在 `src/main/resources/admin/admin/src/`。修改后在该目录下执行：
> ```bash
> npm run build
> ```
> 然后重启后端即可看到效果。
