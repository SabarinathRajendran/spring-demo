FROM amazoncorretto:17-alpine as corretto-jdk

# hadolint ignore=DL3018,SC2046
RUN apk add --no-cache binutils && \
    jlink \
     --verbose \
     --add-modules ALL-MODULE-PATH \
     --strip-debug \
     --no-man-pages \
     --no-header-files \
     --compress=2 \
     --output /jre

# hadolint ignore=DL3007
FROM alpine:latest
ENV JAVA_HOME=/jre
ENV PATH="${JAVA_HOME}/bin:${PATH}"

COPY --from=corretto-jdk /jre $JAVA_HOME

EXPOSE 8092
COPY target/Delivery-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]