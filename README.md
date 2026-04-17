# 美味轩线上点餐系统

一个基于 `Vue 3 + Spring Boot + Spring Data JPA + MySQL + JWT` 的前后端分离在线点餐系统，包含用户端点餐流程与管理员后台管理功能。

## 项目简介

本项目围绕餐厅线上订餐场景实现了完整业务闭环，覆盖菜单浏览、购物车管理、订单创建、订单查询、管理员登录、菜品管理、分类管理、订单管理、用户管理和数据统计等核心功能。

项目采用前后端分离架构：

- 前端：`Vue 3 + Vite + Pinia + Vue Router + Element Plus`
- 后端：`Spring Boot + Spring Data JPA + MySQL`
- 鉴权：`JWT + 拦截器`

该项目适合作为 Java Web / Spring Boot / 前后端联调类项目展示。

## 技术栈

### 前端

- Vue 3
- Vite
- Vue Router 4
- Pinia
- Axios
- Element Plus
- ECharts

### 后端

- Java 11
- Spring Boot 2.7
- Spring MVC
- Spring Data JPA
- MySQL
- JWT
- BCrypt

## 核心功能

### 用户端

- 首页轮播和菜品推荐展示
- 菜单浏览与分类筛选
- 购物车增删改查
- 提交订单
- 订单列表与订单详情查看
- 用户注册、登录、个人信息维护

### 管理端

- 管理员登录
- 分类管理
- 菜品管理
- 订单管理与状态流转
- 用户管理
- 数据统计展示

## 业务流程

### 用户下单主流程

1. 用户注册并登录系统
2. 浏览菜品并加入购物车
3. 填写配送信息并提交订单
4. 系统根据购物车生成订单与订单明细
5. 用户在订单中心查看订单状态
6. 管理员在后台更新订单状态
7. 用户确认收货，订单完成

### 后台管理流程

1. 管理员登录后台
2. 查看统计面板
3. 管理分类、菜品、订单和用户信息
4. 对订单进行状态更新

## 项目结构

```text
.
├── back                        # Spring Boot 后端
│   ├── pom.xml
│   └── src/main
│       ├── java/com/meiweixuan
│       │   ├── common         # 统一响应体
│       │   ├── config         # MVC / JWT / CORS / 密码配置
│       │   ├── controller     # 控制层
│       │   ├── dao            # JPA Repository
│       │   ├── dto            # 请求响应 DTO
│       │   ├── entity         # 实体类
│       │   ├── exception      # 全局异常处理
│       │   ├── interceptor    # JWT 拦截器
│       │   ├── service        # 业务层
│       │   └── util           # Token / 密码工具
│       └── resources
│           ├── application.properties
│           ├── meiweixuan.sql
│           └── static/images
├── front                       # Vue 3 前端
│   ├── package.json
│   ├── vite.config.js
│   └── src
│       ├── api                # 接口封装
│       ├── assets             # 静态资源
│       ├── components         # 公共组件
│       ├── router             # 路由配置
│       ├── store              # Pinia 状态管理
│       └── views              # 页面
└── 运行截图
```

## 系统设计说明

### 前后端分离

- 前端负责页面渲染、状态管理、路由控制和接口调用
- 后端负责业务逻辑、数据库访问、权限校验和统一异常处理

### 统一响应规范

后端统一使用 `Result<T>` 作为接口返回格式：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {}
}
```

前端在 `front/src/api/http.js` 中统一处理成功响应、业务异常和登录失效跳转。

### 权限控制

- 普通用户接口：使用用户 JWT 访问
- 管理员接口：使用管理员 JWT 访问
- 后端通过 `JwtInterceptor` 拦截请求并解析 `userId`、`username`、`userType`
- 前端路由通过 `meta.requiresAuth` 和 `meta.requiresAdmin` 进行页面访问控制

### 密码安全

- 新注册用户密码使用 `BCrypt` 加密存储
- 历史明文密码账号支持兼容校验
- 老账号首次成功登录后会自动升级为加密密码

## 数据库说明

数据库脚本位于：

- `back/src/main/resources/meiweixuan.sql`

当前项目核心表包括：

- `yonghu`：用户表
- `guanliyuan`：管理员表
- `caipinfenlei`：菜品分类表
- `caipin`：菜品表
- `gouwuche`：购物车表
- `orders`：订单表
- `order_items`：订单明细表

## 接口说明

### 公开接口

- `GET /api/caipin`：获取菜品列表
- `GET /api/caipinfenlei`：获取分类列表
- `GET /api/banner`：获取首页轮播图
- `POST /api/yonghu/login`：用户登录
- `POST /api/yonghu/register`：用户注册
- `POST /api/admin/login`：管理员登录

### 用户接口

- `GET /api/cart`：获取购物车
- `POST /api/cart/add`：添加购物车
- `PUT /api/cart/update`：修改购物车数量
- `DELETE /api/cart/item/{id}`：删除购物车项
- `DELETE /api/cart/clear`：清空购物车
- `POST /api/orders`：创建订单
- `GET /api/orders`：获取当前用户订单列表
- `GET /api/orders/{id}`：获取订单详情
- `PUT /api/orders/{id}/cancel`：取消订单
- `PUT /api/orders/{id}/confirm`：确认收货
- `GET /api/yonghu/info`：获取用户信息
- `PUT /api/yonghu/update`：更新用户信息

### 管理员接口

- `GET /api/admin/info`：获取管理员信息
- `GET /api/admin/categories`：分类列表
- `POST /api/admin/categories`：新增分类
- `PUT /api/admin/categories/{id}`：修改分类
- `DELETE /api/admin/categories/{id}`：删除分类
- `GET /api/admin/dishes`：菜品列表
- `POST /api/admin/dishes`：新增菜品
- `PUT /api/admin/dishes/{id}`：修改菜品
- `DELETE /api/admin/dishes/{id}`：删除菜品
- `GET /api/admin/orders`：订单列表
- `GET /api/admin/orders/{id}`：订单详情
- `PUT /api/admin/orders/{id}/status`：更新订单状态
- `GET /api/admin/users`：用户列表
- `PUT /api/admin/users/{id}`：修改用户
- `DELETE /api/admin/users/{id}`：删除用户
- `GET /api/admin/stats/overview`：数据统计总览

## 本地运行

### 环境要求

- JDK 11
- Maven 3.6+
- Node.js 16+
- MySQL 8.x

### 1. 初始化数据库

创建数据库：

```sql
CREATE DATABASE meiweixuan DEFAULT CHARACTER SET utf8mb4;
```

导入脚本：

```bash
mysql -uroot -p meiweixuan < back/src/main/resources/meiweixuan.sql
```

### 2. 配置后端

修改文件：

- `back/src/main/resources/application.properties`

重点配置：

- `spring.datasource.url`
- `spring.datasource.username`
- `spring.datasource.password`

默认端口：

- 后端：`8082`
- 项目前缀：`/api`

### 3. 启动后端

```bash
cd back
mvn spring-boot:run
```

或：

```bash
mvn clean package
java -jar target/meiweixuan-backend-0.0.1-SNAPSHOT.jar
```

### 4. 启动前端

```bash
cd front
npm install
npm run dev
```

默认访问地址：

- 前端开发环境：[http://localhost:5173](http://localhost:5173)

## 默认账号说明

数据库初始脚本中包含演示账号：

### 普通用户

- 用户名：`user1`
- 密码：`123456`

### 管理员

- 用户名：`admin`
- 密码：`123456`

说明：

- 首次使用旧脚本导入的明文密码账号登录后，系统会自动升级为加密密码。

## 项目亮点

- 使用 `JWT + 拦截器` 实现用户端和管理员端双角色鉴权
- 使用 `Result<T> + 全局异常处理` 实现统一接口返回和错误处理
- 使用 `Spring Data JPA` 完成订单、菜品、购物车、用户等业务模块开发
- 使用 `BCrypt` 提升账号密码存储安全性
- 订单模块支持从购物车生成订单明细，形成完整业务闭环
- 前端封装统一 `Axios` 请求层，支持登录失效处理与消息提示

## 截图展示

仓库 `运行截图` 目录下包含首页、菜单、购物车、订单中心、后台管理等页面截图，可用于项目展示和简历材料补充。
