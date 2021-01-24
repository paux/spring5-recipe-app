FROM centos

RUN yum install -y java-11

VOLUME /tmp
ADD /target/spring5-recipe-app-0.0.1-SNAPSHOT.jar myapp.jar
RUN sh -c 'touch /myapp.jar'
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/myapp.jar"]
