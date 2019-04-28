FROM java:8
EXPOSE 8090
ARG JAR_FILE
ADD target/${JAR_FILE} /study-java-app.jar
ENTRYPOINT ["java", "-jar", "/study-java-app.jar"]