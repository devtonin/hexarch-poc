FROM adoptopenjdk/openjdk11:alpine-jre
WORKDIR /opt/app
COPY target/*.jar template.jar
ENTRYPOINT ["java","-jar","/opt/app/template.jar"]
