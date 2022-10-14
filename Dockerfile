FROM gradle:latest

COPY . /project
RUN  cd /project && gradle build

#run the spring boot application
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-jar","/project/build/libs/*.jar"]