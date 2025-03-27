# 使用带Maven的基础镜像（开发阶段需要构建工具）
FROM maven:3.8-openjdk-17 AS builder

# 复制源码和构建文件
WORKDIR /app
COPY pom.xml .
COPY src ./src

# 构建应用（依赖缓存优化）
RUN mvn dependency:go-offline
RUN mvn package -DskipTests

# 运行阶段
FROM openjdk:17-slim
WORKDIR /app
COPY  ./target/*.jar app.jar

# 开发时允许调试端口
EXPOSE 8080 5005

# 以调试模式启动（适合IDE连接）
CMD ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-jar", "app.jar"]