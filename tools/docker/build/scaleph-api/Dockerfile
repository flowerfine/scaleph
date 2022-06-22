FROM maven:3.8-openjdk-8 as build

COPY ./ /scaleph
RUN cd /scaleph && mvn -B -U clean package -DskipTests -Dfast -am --projects scaleph-api


FROM ghcr.io/flowerfine/scaleph_seatunnel:2.1.2 as release

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
 --file-system.endPoint=$MINIO_ENDPOINT \
 --file-system.accessKey=$MINIO_ACCESS_KEY \
 --file-system.secretKey=$MINIO_SECRET_KEY \
 "

RUN mkdir -p $SCALEPH_HOME

WORKDIR $SCALEPH_HOME

COPY --from=build /scaleph/scaleph-api/target/scaleph-api*.jar $SCALEPH_HOME/scaleph.jar

EXPOSE 8080 8080

ENTRYPOINT ["sh","-c","java -jar /opt/scaleph/scaleph.jar ${ENV_PARAMS}"]