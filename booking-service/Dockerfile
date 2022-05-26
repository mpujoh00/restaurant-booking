FROM openjdk:11
ADD booking-service-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-Djava.secuyarity.egd=file:/dev/./urandom","-Dspring.profiles.active=dev","-jar","/app.jar"]