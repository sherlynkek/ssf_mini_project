FROM openjdk:23-jdk AS compiler

ARG COMPILE_DIR=/code_folder

WORKDIR ${COMPILE_DIR}

COPY .mvn .mvn
COPY src src
COPY pom.xml .
COPY mvnw .
COPY mvnw.cmd .

RUN chmod a+x ./mvnw

RUN ./mvnw clean package -Dmaven.tests.skip=true

ENV SERVER_PORT 3000

EXPOSE ${SERVER_PORT}

# run application using ENTRYPOINT or CMD
ENTRYPOINT java -jar target/Mini-Project-0.0.1-SNAPSHOT.jar

FROM openjdk:23-jdk

ARG DEPLOY_DIR=/app

WORKDIR ${DEPLOY_DIR}

COPY --from=compiler /code_folder/target/Mini_project-0.0.1-SNAPSHOT.jar Mini-Project.jar

ENV SERVER_PORT=3000

EXPOSE ${SERVER_PORT}

ENTRYPOINT [ "java", "-jar", "Mini-Project.jar" ]