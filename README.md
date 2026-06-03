# 网上通讯录 - 课程大作业

## 项目简介
这是一个基于 Java Web 的网上通讯录管理系统，用于课程大作业。

## 开发环境要求

```text
+--------+-------------------------+
| 环境   | 版本                    |
+--------+-------------------------+
| JDK    | 1.8 及以上              |
| Tomcat | 9.0                     |
| Maven  | 3.6 及以上              |
| IDE    | IntelliJ IDEA           |
+--------+-------------------------+
```

## 如何运行项目

### 1. 克隆项目到本地
```bash
git clone https://github.com/tigerordragon/WangShangTongXunLu.git
cd WangShangTongXunLu
```

### 2. 打包项目
项目目录中的 Web 应用在 `online-address-book` 下，先进入该目录并打包：

```bash
cd online-address-book
mvn clean package
```

打包成功后会生成：

```text
online-address-book/target/online-address-book.war
```

### 3. Windows 启动 Tomcat 并运行页面
安装 `JDK`、`Maven` 和 `Tomcat 9` 后，假设 `Tomcat` 安装目录为：

```text
C:\apache-tomcat-9
```

在项目目录打包：

```bat
cd online-address-book
mvn clean package
```

复制 `war` 包到 `Tomcat` 的 `webapps` 目录：

```bat
copy target\online-address-book.war C:\apache-tomcat-9\webapps\
```

启动 `Tomcat`：

```bat
C:\apache-tomcat-9\bin\startup.bat
```

浏览器访问：

```text
http://localhost:8080/online-address-book/
```

如果 `8080` 已被占用，修改 `C:\apache-tomcat-9\conf\server.xml` 中的端口，例如改成 `8081`，然后重新启动 `Tomcat`：

```xml
<Connector port="8081" protocol="HTTP/1.1"
           connectionTimeout="20000"
           redirectPort="8443" />
```

新的访问地址：

```text
http://localhost:8081/online-address-book/
```

停止 `Tomcat`：

```bat
C:\apache-tomcat-9\bin\shutdown.bat
```

### 4. macOS 启动 Tomcat 并运行页面
推荐使用 `Homebrew` 安装 `Tomcat 9`：

```bash
brew install tomcat@9
```

如果本机 `8080` 端口空闲，可以直接使用默认端口。复制 `war` 包到 `Tomcat` 的 `webapps` 目录：

```bash
cp target/online-address-book.war /opt/homebrew/opt/tomcat@9/libexec/webapps/
```

启动 `Tomcat`：

```bash
/opt/homebrew/opt/tomcat@9/libexec/bin/startup.sh
```

如果 `Tomcat` 提示找不到 Java，需要先设置 `JAVA_HOME`：

```bash
export JAVA_HOME=$(/usr/libexec/java_home)
/opt/homebrew/opt/tomcat@9/libexec/bin/startup.sh
```

浏览器访问：

```text
http://localhost:8080/online-address-book/
```

如果 `8080` 已被占用，修改 `/opt/homebrew/opt/tomcat@9/libexec/conf/server.xml` 中的端口，例如改成 `8081`：

```xml
<Connector port="8081" protocol="HTTP/1.1"
           connectionTimeout="20000"
           redirectPort="8443" />
```

修改后重新启动 `Tomcat`，访问：

```text
http://localhost:8081/online-address-book/
```

停止 `Tomcat`：

```bash
/opt/homebrew/opt/tomcat@9/libexec/bin/shutdown.sh
```

### 5. Linux 启动 Tomcat 并运行页面
安装 `JDK`、`Maven` 和 `Tomcat 9` 后，假设 `Tomcat` 安装目录为：

```text
/opt/apache-tomcat-9
```

在项目目录打包：

```bash
cd online-address-book
mvn clean package
```

复制 `war` 包到 `Tomcat` 的 `webapps` 目录：

```bash
cp target/online-address-book.war /opt/apache-tomcat-9/webapps/
```

启动 `Tomcat`：

```bash
/opt/apache-tomcat-9/bin/startup.sh
```

浏览器访问：

```text
http://localhost:8080/online-address-book/
```

如果 `8080` 已被占用，修改 `/opt/apache-tomcat-9/conf/server.xml` 中的端口，例如改成 `8081`，然后重新启动 `Tomcat`：

```xml
<Connector port="8081" protocol="HTTP/1.1"
           connectionTimeout="20000"
           redirectPort="8443" />
```

新的访问地址：

```text
http://localhost:8081/online-address-book/
```

停止 `Tomcat`：

```bash
/opt/apache-tomcat-9/bin/shutdown.sh
```
