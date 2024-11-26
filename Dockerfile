# 1. Base Image 선택 (OpenJDK 17 사용)
FROM openjdk:17-jdk-slim

# 2. Build Arguments
ARG JAR_FILE=build/libs/gahyeonbot-1.0.0.jar

# 3. 작업 디렉토리 생성 및 설정
WORKDIR /app

# 4. 애플리케이션의 JAR 파일 복사
COPY ${JAR_FILE} /app/bot.jar

# 5. .env 파일 복사
COPY .env /app/.env

# 5. 포트 설정
EXPOSE 8080

# 6. 디버깅용 파일 상태 확인
RUN ls -al /app

# 7. Docker 컨테이너 실행 시 명령어
CMD ["sh", "-c", "java -jar bot.jar > /app/log.txt 2>&1 && tail -f /app/log.txt"]