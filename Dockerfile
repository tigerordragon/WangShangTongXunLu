# syntax=docker/dockerfile:1

FROM maven:3.9-eclipse-temurin-8 AS build

WORKDIR /workspace

COPY online-address-book/pom.xml ./pom.xml
RUN mvn -B -DskipTests dependency:go-offline

COPY online-address-book/src ./src
RUN mvn -B -DskipTests package

FROM tomcat:9.0-jdk8-temurin

RUN rm -rf /usr/local/tomcat/webapps/*

COPY --from=build /workspace/target/online-address-book /usr/local/tomcat/webapps/online-address-book

RUN set -eux; \
    { \
      printf '%s\n' '# MySQL connection for Docker Compose'; \
      printf '%s\n' ''; \
      printf '%s\n' 'datasource.driver=com.mysql.cj.jdbc.Driver'; \
      printf '%s\n' ''; \
      printf '%s\n' 'datasource.url=jdbc:mysql://db:3306/online_address_book?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai&characterEncoding=utf8'; \
      printf '%s\n' ''; \
      printf '%s\n' 'datasource.username=online_address_book'; \
      printf '%s\n' ''; \
      printf '%s\n' 'datasource.password=123456'; \
      printf '%s\n' ''; \
      printf '%s\n' 'datasource.pool.maximumPoolSize=10'; \
    } > /usr/local/tomcat/webapps/online-address-book/WEB-INF/classes/datasource.properties

EXPOSE 8080

CMD ["catalina.sh", "run"]
