# 1. Base Image 선택 (OpenJDK 17 사용)
FROM openjdk:17-jdk-slim

# 3. 작업 디렉토리 생성 및 설정
WORKDIR /app

# 4. 애플리케이션의 JAR 파일 복사
COPY target/gahyeonbot.1.0.0.jar /app/bot.jar

# 5. Docker 컨테이너 실행 시 명령어
CMD ["sh", "-c", "java $JAVA_OPTS -jar bot.jar > /app/log.txt 2>&1 && tail -f /app/log.txt"]