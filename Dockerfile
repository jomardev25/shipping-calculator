FROM maven:3.8.4-openjdk-17 AS build

ARG WORK_SPACE=/usr/app
WORKDIR $WORK_SPACE
COPY pom.xml .
COPY src ./src
RUN mvn clean install


FROM openjdk:17-alpine
WORKDIR $WORK_SPACE
COPY --from=build /usr/app/target/shipping-calculator-0.0.1-SNAPSHOT.jar ./shipping-calculator.jar
EXPOSE 8080
CMD ["java", "-jar", "shipping-calculator.jar"]