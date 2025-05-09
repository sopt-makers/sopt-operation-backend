FROM openjdk:17-jdk-slim as builder

ARG PROFILE=test

# mkdir /app-build && cd /app-build
WORKDIR /app-build

# docker cp . gradle:app-build
COPY . /app-build

# create .jar
RUN echo "Build with PROFILE=${PROFILE}" && ./gradlew build -Pprofile=${PROFILE} --no-daemon

# Run-Time Image Setting
FROM openjdk:17-jdk-slim as production

# mkdir /app-run && cd /app-run
WORKDIR /app-run

# copy .jar to Run-Time Image
COPY --from=builder /app-build/build/libs/operation.jar /app-run/operation.jar

# 환경 변수로 프로파일을 설정할 수 있도록 함
ENV PROFILE=${PROFILE}


EXPOSE 8080
ENTRYPOINT ["java"]
CMD ["-Dfile.encoding=UTF-8", "-jar", "operation.jar"]
