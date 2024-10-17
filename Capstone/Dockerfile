FROM openjdk:17-jdk

# Gradle 빌드 결과물 JAR 파일 복사
COPY build/libs/Capstone-0.0.1-SNAPSHOT.jar app.jar

ENV FAST_API_URL=http://115.85.180.192:8000
ENV DB_URL=jdbc:mysql://localhost:33308/kwu-devel?createDatabaseIfNotExist=false&useUnicode=true
ENV DB_USERNAME=kwu
ENV DB_PASSWORD=32tz6ylkxdgndhz6wmh75vj3

# JAR 파일 실행 명령어
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "app.jar"]
