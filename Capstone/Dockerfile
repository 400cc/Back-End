FROM openjdk:17-jdk

# Gradle 빌드 결과물 JAR 파일 복사
COPY build/libs/Capstone-0.0.1-SNAPSHOT.jar app.jar

# JAR 파일 실행 명령어
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "app.jar"]
