FROM openjdk:8-jdk-alpine as build
WORKDIR /workspace/xiang-qi

RUN mkdir application
RUN mkdir library

COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

COPY application/pom.xml ./application/
COPY application/src ./application/src
COPY library/pom.xml ./library/
COPY library/src ./library/src

RUN ./mvnw install -DskipTests
RUN mkdir -p application/target/dependency && (cd application/target/dependency; jar -xf ../*.jar)

FROM openjdk:8-jre-alpine
VOLUME /tmp
ARG DEPENDENCY=/workspace/xiang-qi/application/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

ENTRYPOINT ["java","-cp","app:app/lib/*","me.portfolio.application.Application"]