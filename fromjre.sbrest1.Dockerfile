FROM openjdk:8-jre-alpine
ADD /target/sb-rest-1-0.0.1-SNAPSHOT.jar /app/sbrest1.jar
ENTRYPOINT [ "java", "-jar", "/app/sbrest1.jar" ]
EXPOSE 8090