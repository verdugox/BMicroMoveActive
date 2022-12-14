FROM openjdk:11
VOLUME /tmp
EXPOSE 8086
ADD ./target/MoveActive-0.0.1-SNAPSHOT.jar ms-moveactive.jar
ENTRYPOINT ["java", "-jar","/ms-moveactive.jar"]

