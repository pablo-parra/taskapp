FROM openjdk:11

COPY /target/springboot-taskapp.jar /app.jar

EXPOSE 8080

CMD java -jar /app.jar --spring.profiles.active="local"