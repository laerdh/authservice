FROM anapsix/alpine-java
LABEL maintainer="laerdh@gmail.com"
COPY ./build/libs/authservice-*.jar /services/authservice.jar
CMD ["java", "-jar", "/services/authservice.jar"]