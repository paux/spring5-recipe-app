# Launch a CentOS container, provide a command
docker run -d centos tail -f /dev/null
# Interactive shell into that container
docker exec -it $container_name bash
# Within CentOS, install Java
yum install java
# (most probably you prefer java-11, though)
# But that won't get persisted, we'll have to compose our own Docker file!
# Build the Dockerfile
docker build -t spring-boot-docker .
# Run the container
docker run -d -p 8080:8080 spring-boot-docker
