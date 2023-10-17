FROM artifacts.developer.gov.bc.ca/docker-remote/maven:3.8.7-openjdk-18 AS build
WORKDIR /workspace/app

COPY api/pom.xml .
COPY api/src src
RUN mvn clean package -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM build AS vulnscan
COPY --from=artifacts.developer.gov.bc.ca/docker-remote/aquasec/trivy:latest /usr/local/bin/trivy /usr/local/bin/trivy
RUN trivy filesystem --severity CRITICAL --exit-code 0 --no-progress /

FROM artifacts.developer.gov.bc.ca/docker-remote/openjdk:18.0.2.1-jdk-oracle AS pen-myed
RUN useradd -ms /bin/bash spring
RUN mkdir -p /logs
RUN chown -R spring:spring /logs
RUN chmod 755 /logs
USER spring
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-Duser.name=PEN_MYED_API","-Xms300m","-Xmx300m","-noverify","-XX:TieredStopAtLevel=1","-XX:+UseParallelGC","-XX:MinHeapFreeRatio=20","-XX:MaxHeapFreeRatio=40","-XX:GCTimeRatio=4","-XX:AdaptiveSizePolicyWeight=90","-XX:MaxMetaspaceSize=120m","-XX:ParallelGCThreads=1","-Djava.util.concurrent.ForkJoinPool.common.parallelism=4","-XX:CICompilerCount=2","-XX:+ExitOnOutOfMemoryError","-Djava.security.egd=file:/dev/./urandom","-Dspring.backgroundpreinitializer.ignore=true","-cp","app:app/lib/*","ca.bc.gov.educ.api.pen.myed.PenMyEdAPIApplication"]

