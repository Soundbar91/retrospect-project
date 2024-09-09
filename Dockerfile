# base-image
FROM amazoncorretto:17-alpine-jdk

# continer path
WORKDIR /app

# jar copy
COPY ./build/libs/retrospect-project-0.0.1-SNAPSHOT.jar /app/app.jar

# java app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]
