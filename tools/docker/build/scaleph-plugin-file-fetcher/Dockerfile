FROM maven:3.8-eclipse-temurin-11 as build

COPY ./ /scaleph
RUN cd /scaleph && \
    mvn -B -U -T 4 clean package -Pdist -DskipTests -Dfast && \
    tar -zxf /scaleph/scaleph-dist/target/scaleph-*-bin.tar.gz -C /scaleph/scaleph-dist/target  && \
    mkdir /dist && \
    cp -r /scaleph/scaleph-dist/target/scaleph-*/* /dist


FROM eclipse-temurin:11-focal as release

ENV TZ Asia/Shanghai
ENV SCALEPH_HOME /opt/scaleph-plugin-file-fetcher

RUN mkdir -p $SCALEPH_HOME

WORKDIR $SCALEPH_HOME

COPY --from=build /dist $SCALEPH_HOME/

EXPOSE 8080 8080

ENTRYPOINT ["sh","-c","java -cp /opt/scaleph/libs/* cn.sliew.scaleph.plugin.file.fetcher.CliEntrypoint ${ENV_PARAMS}"]