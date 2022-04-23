FROM openjdk:8-jdk-alpine
MAINTAINER gleiyu

ENV MYSQL_URL="jdbc:mysql://mysql:3306/breeze?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai"
ENV MYSQL_USERNAME=root
ENV MYSQL_PASSWORD=123456
ENV REDIS_HOST=redis
ENV REDIS_PORT=6379
ENV REDIS_PASSWORD=123456

ENV TZ Asia/Shanghai
ENV BREEZE_HOME /opt/breeze

ENV ENV_PARAMS=" --spring.datasource.druid.master.url=$MYSQL_URL \
 --spring.datasource.druid.master.username=$MYSQL_USERNAME \
 --spring.datasource.druid.master.password=$MYSQL_PASSWORD \
 --spring.datasource.druid.log.url=$MYSQL_URL \
 --spring.datasource.druid.log.username=$MYSQL_USERNAME \
 --spring.datasource.druid.log.password=$MYSQL_PASSWORD \
 --spring.quartz.properties.org.quartz.dataSource.quartzDS.url=$MYSQL_URL \
 --spring.quartz.properties.org.quartz.dataSource.quartzDS.username=$MYSQL_USERNAME \
 --spring.quartz.properties.org.quartz.dataSource.quartzDS.password=$MYSQL_PASSWORD \
 --spring.redis.host=$REDIS_HOST \
 --spring.redis.port=$REDIS_PORT \
 --spring.redis.password=$REDIS_PASSWORD \
 "

RUN mkdir -p $BREEZE_HOME

WORKDIR $BREEZE_HOME

ADD ./target/breeze-api*.jar $BREEZE_HOME/breeze.jar

EXPOSE 8080 8080

ENTRYPOINT ["sh","-c","java -jar /opt/breeze/breeze.jar ${ENV_PARAMS}"]