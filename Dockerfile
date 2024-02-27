FROM openjdk:17-oracle
EXPOSE 8081
ADD /target/CloudService-0.0.1-SNAPSHOT.jar CloudService.jar
CMD ["java", "-jar", "CloudService.jar"]