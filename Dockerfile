#FROM openjdk:11
FROM maven
RUN mkdir app
WORKDIR app
COPY . .
RUN mvn clean install -DskipTests
EXPOSE 80
ENTRYPOINT curl ${CONFIG_URL} -o ./src/main/resources/application-prod.yml ; java -jar -Xmx3g -Xms1g -Dspring.profiles.active=prod -Dspring.config.location=src/main/resources/application-prod.yml -Djdk.tls.client.protocols=TLSv1.2 target/springcoretemplate.jar
