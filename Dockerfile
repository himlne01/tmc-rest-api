FROM public.ecr.aws/docker/library/maven:latest as build
COPY pom.xml ./pom.xml
COPY src ./src
RUN mvn clean package

FROM public.ecr.aws/docker/library/maven:eclipse-temurin
COPY --from=build target/*.jar ./ 
ENTRYPOINT java -jar ./Too-Many-Cooks-1.0-SNAPSHOT.jar