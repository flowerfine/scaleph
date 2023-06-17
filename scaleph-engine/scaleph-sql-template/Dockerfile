ARG JAVA_VERSION
ARG FLINK_VERSION

# Buold jar
FROM maven:3-eclipse-temurin-$JAVA_VERSION as builder

ARG JAVA_VERSION
ARG FLINK_VERSION

RUN mkdir -p /usr/src/scaleph-sql-template
COPY src pom.xml /usr/src/scaleph-sql-template/
# Use custom maven settings for testing
# COPY settings.xml /usr/share/maven/conf/settings.xml
WORKDIR /usr/src/scaleph-sql-template
RUN mvn clean package -DskipTests -Dflink.version=$FLINK_VERSION -Djava.version=$JAVA_VERSION

# Build flink
FROM flink:${FLINK_VERSION}-java${JAVA_VERSION} as image
RUN mkdir -p /opt/flink/usrlib
COPY --from=builder /usr/src/scaleph-sql-template/target/sql-runner.jar /opt/flink/usrlib/