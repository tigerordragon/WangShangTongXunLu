# 数据源配置文件重命名变更记录

**日期**：2026-06-04  
**目的**：避免将 `jdbc.properties` 误解为「JdbcTemplate / 纯 JDBC 持久层」；项目为 **SSM**，该文件仅存放 **DataSource 连接参数**。

**未改动**：Controller、Service、Repository、MyBatis Mapper、前端、`pom.xml` 等。

---

## 1. 新增文件

| 路径 | 行号范围 | 说明 |
|------|----------|------|
| `online-address-book/src/main/resources/datasource.properties` | 1–6 | 原 `jdbc.properties` 内容迁移；键前缀 `jdbc.*` → `datasource.*` |
| `online-address-book/src/main/resources/datasource.properties.example` | 1–6 | 原 `jdbc.properties.example` 迁移；复制说明改为 `datasource.properties` |

## 2. 删除文件

| 路径 | 原行号范围 | 说明 |
|------|------------|------|
| `online-address-book/src/main/resources/jdbc.properties` | 1–6 | 由 `datasource.properties` 替代 |
| `online-address-book/src/main/resources/jdbc.properties.example` | 1–6 | 由 `datasource.properties.example` 替代 |

## 3. 修改文件

### `online-address-book/src/main/java/com/addressbook/config/DatabaseConfig.java`

| 行号范围 | 变更内容 |
|----------|----------|
| 13 | `@PropertySource("classpath:jdbc.properties")` → `@PropertySource("classpath:datasource.properties")` |
| 18–22 | `@Value("${jdbc.*}")` → `@Value("${datasource.*}")`（driver / url / username / password / pool.maximumPoolSize） |

### `docs/7.MySQL数据库接入.md`

| 行号范围 | 变更内容 |
|----------|----------|
| 9 | 配置路径 `jdbc.properties` → `datasource.properties` |
| 25 | 编辑说明改为 `datasource.properties`，并注明可从 example 复制 |
| 56–57 | 排错说明中的文件名与键名改为 `datasource.*` |

---

## 4. 本地迁移说明

若你本机仍保留旧文件 `jdbc.properties`：

1. 将内容中的键改为 `datasource.driver`、`datasource.url` 等（或从 `datasource.properties.example` 复制后填写密码）。
2. 保存为 `online-address-book/src/main/resources/datasource.properties`。
3. 删除旧的 `jdbc.properties`（避免混淆）。

部署前请执行：`cd online-address-book && mvn test package`。

---

## 5. 未修改（历史文档仍写旧名）

`docs/CODE-CHANGES-MYSQL.md` 为 MySQL/SSM 接入时的历史记录，仍引用 `jdbc.properties`，未改以免篡改历史 diff 说明。以本文档与 `docs/7.MySQL数据库接入.md` 为准。
