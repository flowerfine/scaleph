FROM openjdk:8-jdk
#FROM openjdk:8-jdk-alpine
MAINTAINER gleiyu

ENV FLINK_HOME /opt/flink
RUN mkdir -p $FLINK_HOME ; cd $FLINK_HOME ; \
    curl -LsO https://archive.apache.org/dist/flink/flink-1.13.6/flink-1.13.6-bin-scala_2.11.tgz ; \
    tar -zxf flink-1.13.6-bin-scala_2.11.tgz --strip 1 -C . ; \
    rm flink-1.13.6-bin-scala_2.11.tgz

ENV SEATUNNEL_HOME /opt/seatunnel

RUN mkdir -p $SEATUNNEL_HOME ; cd $SEATUNNEL_HOME ; \
    curl -LsO https://archive.apache.org/dist/incubator/seatunnel/2.1.1/apache-seatunnel-incubating-2.1.1-bin.tar.gz ; \
    tar -zxf apache-seatunnel-incubating-2.1.1-bin.tar.gz --strip 1 -C . ; \
    rm apache-seatunnel-incubating-2.1.1-bin.tar.gz

ENV MYSQL_URL="jdbc:mysql://mysql:3306/scaleph?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai"
ENV MYSQL_USERNAME=root
ENV MYSQL_PASSWORD=123456
ENV REDIS_HOST=redis
ENV REDIS_PORT=6379
ENV REDIS_PASSWORD=123456
ENV MINIO_ENDPOINT=http://minio:9000
ENV MINIO_ACCESS_KEY=admin
ENV MINIO_SECRET_KEY=password

ENV TZ Asia/Shanghai
ENV SCALEPH_HOME /opt/scaleph

ENV ENV_PARAMS=" --spring.datasource.master.jdbc-url=$MYSQL_URL \
 --spring.datasource.master.username=$MYSQL_USERNAME \
 --spring.datasource.master.password=$MYSQL_PASSWORD \
 --spring.datasource.log.jdbc-url=$MYSQL_URL \
 --spring.datasource.log.username=$MYSQL_USERNAME \
 --spring.datasource.log.password=$MYSQL_PASSWORD \
 --spring.quartz.properties.org.quartz.dataSource.quartzDS.URL=$MYSQL_URL \
 --spring.quartz.properties.org.quartz.dataSource.quartzDS.user=$MYSQL_USERNAME \
 --spring.quartz.properties.org.quartz.dataSource.quartzDS.password=$MYSQL_PASSWORD \
 --spring.redis.host=$REDIS_HOST \
 --spring.redis.port=$REDIS_PORT \
 --spring.redis.password=$REDIS_PASSWORD \
 --app.resource.minio.endPoint=$MINIO_ENDPOINT \
 --app.resource.minio.accessKey=$MINIO_ACCESS_KEY \
 --app.resource.minio.secretKey=$MINIO_SECRET_KEY \
 "

RUN mkdir -p $SCALEPH_HOME

WORKDIR $SCALEPH_HOME

ADD ./target/scaleph-api*.jar $SCALEPH_HOME/scaleph.jar

EXPOSE 8080 8080

ENTRYPOINT ["sh","-c","java -jar /opt/scaleph/scaleph.jar ${ENV_PARAMS}"]