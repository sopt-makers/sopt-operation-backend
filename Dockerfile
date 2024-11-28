FROM gradle:8.10.2 as builder

# mkdir /app-build && cd /app-build
WORKDIR /app-build

# docker cp . gradle:app-build
COPY . /app-build

# create .jar
RUN gradle build -x test

# Run-Time Image Setting
FROM openjdk:21-jdk-slim as production

# mkdir /app-run && cd /app-run
WORKDIR /app-run

# copy .jar to Run-Time Image
COPY --from=builder /app-build/build/libs/*.jar /app-run/operation.jar


EXPOSE 8080
ENTRYPOINT ["java"]
CMD ["-jar", "operation.jar"]