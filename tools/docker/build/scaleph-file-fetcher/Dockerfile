FROM eclipse-temurin:11-focal as release

ENV MINIO_ENDPOINT=http://minio:9000
ENV MINIO_ACCESS_KEY=admin
ENV MINIO_SECRET_KEY=password

ENV TZ Asia/Shanghai
ENV SCALEPH_HOME /opt/scaleph-file-fetcher

RUN mkdir -p $SCALEPH_HOME
WORKDIR $SCALEPH_HOME

COPY ./scaleph-file-fetcher/target/scaleph-file-fetcher*.tar.gz $SCALEPH_HOME/scaleph-file-fetcher.tar.gz
RUN tar -zxf $SCALEPH_HOME/scaleph-file-fetcher.tar.gz --strip-components 1 -C $SCALEPH_HOME
RUN rm $SCALEPH_HOME/scaleph-file-fetcher.tar.gz

ENV CLASSPATH $CLASSPATH:$SCALEPH_HOME/libs/*:$SCALEPH_HOME/conf/*

# shell script
ENTRYPOINT ["sh","-c","java cn.sliew.scaleph.Application \"$0\" \"$@\" --file-system.endPoint=$MINIO_ENDPOINT --file-system.accessKey=$MINIO_ACCESS_KEY --file-system.secretKey=$MINIO_SECRET_KEY"]