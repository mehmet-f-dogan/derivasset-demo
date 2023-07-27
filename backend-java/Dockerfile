# Use the official OpenJDK image as the base image
FROM openjdk:17-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven Wrapper files to the working directory
COPY .mvn ./.mvn

# Copy the Maven project files to the working directory
COPY pom.xml ./
COPY mvnw ./
COPY src ./src/

# Build the Spring Boot application using the Maven Wrapper
RUN ./mvnw package -DskipTests -Dmaven.javadoc.skip=true

# Expose the port on which the Spring Boot application will run
EXPOSE 8080

# Command to run the Spring Boot application
CMD ["java", "-jar", "target/derivasset-demo-0.0.1.jar"]