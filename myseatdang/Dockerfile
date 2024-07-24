# Base Image
FROM amazoncorretto:17
# Image meta 정보
LABEL authors="myseatdang<dldydwns2020@naver.com>"

# Build 시 사용할 변수 선언
ARG JAR_FILE_PATH=build/libs/*.jar

# 생성할 image의 / 디렉토리에 파일 복사
COPY ${JAR_FILE_PATH} /app.jar

# Container 구동 시 실행할 명령어
ENTRYPOINT ["java", "-jar", "/app.jar"]
