FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD build/libs/shorturl-0.0.1-SNAPSHOT.jar build/docker/app.jar
#RUN bash -c 'touch build/docker/app.jar'
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=local","build/docker/app.jar"]