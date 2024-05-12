ARG BASE_RELEASE_IMAGE=eclipse-temurin:17-focal
FROM $BASE_RELEASE_IMAGE as release

ADD scaleph-dist/target/scaleph-*-bin.tar.gz /opt

ENV MYSQL_HOST=mysql
ENV MYSQL_PORT=3306
ENV MYSQL_USERNAME=root
ENV MYSQL_PASSWORD=123456
ENV REDIS_HOST=redis
ENV REDIS_PORT=6379
ENV REDIS_PASSWORD=123456
ENV MINIO_ENDPOINT=http://minio:9000
ENV MINIO_ACCESS_KEY=admin
ENV MINIO_SECRET_KEY=password
ENV GRAVITINO_URL=http://gravitino:8090

ENV TZ Asia/Shanghai
ENV SCALEPH_HOME /opt/scaleph

RUN mkdir -p $SCALEPH_HOME

WORKDIR $SCALEPH_HOME

EXPOSE 8080 8080

ENTRYPOINT [ "sh", "-c", "/opt/scaleph/bin/scaleph.sh start-foreground" ]