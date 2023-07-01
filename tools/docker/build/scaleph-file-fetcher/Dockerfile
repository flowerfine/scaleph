FROM eclipse-temurin:11-focal as release

ENV TZ Asia/Shanghai
ENV SCALEPH_HOME /opt/scaleph-file-fetcher

RUN mkdir -p $SCALEPH_HOME

WORKDIR $SCALEPH_HOME

COPY ./scaleph-file-fetcher/target/scaleph-file-fetcher*.jar $SCALEPH_HOME/scaleph-file-fetcher.jar

ENTRYPOINT ["sh","-c","java -jar /opt/scaleph/scaleph-file-fetcher.jar ${ENV_PARAMS}"]