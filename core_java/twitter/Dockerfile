# Use an official Java runtime as a parent image
FROM openjdk:8-alpine
# Copy the executable JAR file to the container
COPY target/twitter*.jar /usr/local/app/twitter/lib/twitter.jar
# Set environment variables
ENV API_KEY=<your_api_key>
ENV API_SECRET=<your_api_secret>
ENV ACCESS_TOKEN=<your_access_token>
ENV ACCESS_TOKEN_SECRET=<your_access_token_secret>
# Run the JAR file when the container starts
ENTRYPOINT ["java","-jar","/usr/local/app/twitter/lib/twitter.jar"]
