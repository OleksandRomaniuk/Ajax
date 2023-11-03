FROM openjdk:17-slim
COPY build/libs/AjaxProject-0.0.1-SNAPSHOT.jar ajax-app.jar

ENV SPRING_PROFILES_ACTIVE=dev

ENTRYPOINT ["java", "-jar", "ajax-app.jar"]
