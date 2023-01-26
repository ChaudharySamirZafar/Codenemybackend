#
# Set Up, Clean & Build stage
#
FROM maven:3.8.3-openjdk-17 as build
COPY src /home/app/src
COPY results_samirzafartest.txt /home/app
COPY TestFile.java /home/app
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

#
# Package stage
# Download JDK-17
# Download Python
# Copy the Jar file to a file
# Expose 8080 port
# Start the jar up.
#
FROM amazoncorretto:17
RUN yum -y install python3 python3-pip
COPY --from=build /home/app/target/api-0.0.1-SNAPSHOT.jar /usr/local/lib/demo.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/demo.jar"]