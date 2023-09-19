FROM amazoncorretto:17
EXPOSE 8761
COPY target/EurekaServer-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]