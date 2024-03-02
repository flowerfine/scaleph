import {Node} from "@antv/xflow";
import {ModalFormProps} from "@/typings";
import SourceFakeStepForm from "./source/source-fake-step";
import SinkConsoleStepForm from "./sink/sink-console-step";
import SourceLocalFileStepForm from "./source/source-local-file-step";
import SinkLocalFileStepForm from "./sink/sink-local-file-step";
import SourceFtpFileStepForm from "./source/source-ftp-file-step";
import SinkFtpFileStepForm from "./sink/sink-ftp-file-step";
import SourceSftpFileStepForm from "./source/source-sftp-file-step";
import SinkSftpFileStepForm from "./sink/sink-sftp-file-step";
import SourceHdfsFileStepForm from "./source/source-hdfs-file-step";
import SinkHdfsFileStepForm from "./sink/sink-hdfs-file-step";
import SourceOssFileStep from "./source/source-oss-file-step";
import SinkOSSFileStepForm from "./sink/sink-oss-file-step";
import SourceOSSJindoFileStepForm from "./source/source-ossjindo-file-step";
import SinkOSSJindoFileStepForm from "./sink/sink-ossjindo-file-step";
import SourceS3FileStepForm from "./source/source-s3-file-step";
import SinkS3FileStepForm from "./sink/sink-s3-file-step";
import SourceJdbcStepForm from "./source/source-jdbc-step";
import SinkJdbcStepForm from "./sink/sink-jdbc-step";
import SourceHudiStepForm from "./source/source-hudi-step";
import SourceIcebergStepForm from "./source/source-iceberg-step";
import SourcePaimonStepForm from "./source/source-paimon-step";
import SinkPaimonStepForm from "./sink/sink-paimon-step";
import SourceHttpStepForm from "./source/source-http-step";
import SinkHttpStepForm from "./sink/sink-http-step";
import SinkFeishuStepForm from "./sink/sink-feishu-step";
import SinkWeChatStepForm from "./sink/sink-wechat-step";
import SinkDingTalkStepForm from "./sink/sink-dingtalk-step";
import SinkEmailStepForm from "./sink/sink-email-step";
import SourceSocketStepForm from "./source/source-socket-step";
import SinkSocketStepForm from "./sink/sink-socket-step";
import SourceDorisStepForm from "./source/source-doris-step";
import SinkDorisStepForm from "./sink/sink-doris-step";
import SourceStarRocksStepForm from "./source/source-starrocks-step";
import SinkStarRocksStepForm from "./sink/sink-starrocks-step";
import SourceClickHouseStepForm from "./source/source-clickhouse-step";
import SinkClickHouseStepForm from "./sink/sink-clickhouse-step";
import SourceHiveStepForm from "./source/source-hive-step";
import SinkHiveStepForm from "./sink/sink-hive-step";
import SourceKuduStepForm from "./source/source-kudu-step";
import SinkKuduStepForm from "./sink/sink-kudu-step";
import SourceMaxComputeStepForm from "./source/source-maxcompute-step";
import SinkMaxComputeStepForm from "./sink/sink-maxcompute-step";
import SourceKafkaStepForm from "./source/source-kafka-step";
import SinkKafkaStepForm from "./sink/sink-kafka-step";
import SourceRocketMQStepForm from "./source/source-rocketmq-step";
import SinkRocketMQStepForm from "./sink/sink-rocketmq-step";
import SourceIoTDBStepForm from "./source/source-iotdb-step";
import SinkIoTDBStepForm from "./sink/sink-iotdb-step";
import SourceMongoDBStepForm from "./source/source-mongodb-step";
import SinkMongoDBStepForm from "./sink/sink-mongodb-step";
import SourceCassandraStepForm from "./source/source-cassandra-step";
import SinkCassandraStepForm from "./sink/sink-cassandra-step";
import SourceRedisStepForm from "./source/source-redis-step";
import SinkRedisStepForm from "./sink/sink-redis-step";
import SourcePulsarStepForm from "./source/source-pulsar-step";
import SinkDatahubStepForm from "./sink/sink-datahub-step";
import SourceElasticsearchStepForm from "./source/source-elasticsearch-step";
import SinkElasticsearchStepForm from "./sink/sink-elasticsearch-step";
import SourceNeo4jStepForm from "./source/source-neo4j-step";
import SinkNeo4jStepForm from "./sink/sink-neo4j-step";
import SinkSentryStepForm from "./sink/sink-sentry-step";
import SourceInfluxDBStepForm from "./source/source-influxdb-step";
import SinkInfluxDBStepForm from "./sink/sink-influxdb-step";
import SourceAmazonDynamodbStepForm from "./source/source-dynamodb-step";
import SinkAmazonDynamodbStepForm from "./sink/sink-dynamodb-step";
import SinkS3RedshiftStepForm from "./sink/sink-s3redshift-step";
import SourceOpenMLDBStepForm from "./source/source-openmldb-step";
import SourceCDCMySQLStepForm from "./source/source-cdc-mysql-step";
import SourceCDCSqlServerStepForm from "./source/source-cdc-sqlserver-step";
import SourceCDCOracleStepForm from "./source/source-cdc-oracle-step";
import SourceCDCPostgreSQLStepForm from "./source/source-cdc-postgresql-step";
import SourceCDCMongoDBStepForm from "./source/source-cdc-mongodb-step";
import SinkHbaseStepForm from "./sink/sink-hbase-step";
import TransformCopyStepForm from "./transform/transform-copy-step";
import TransformFieldMapperStepForm from "./transform/transform-field-mapper-step";
import TransformFilterRowKindStepForm from "./transform/transform-filter-row-kind-step";
import TransformFilterStepForm from "./transform/transform-filter-step";
import TransformSplitStepForm from "./transform/transform-split-step";
import TransformSqlStepForm from "./transform/transform-sql-step";
import TransformReplaceStepForm from "./transform/transform-replace-step";

const SeaTunnnelConnectorForm: React.FC<ModalFormProps<Node>> = ({visible, onVisibleChange, onCancel, onOK, data}) => {

  const switchStep = (node: Node) => {
    const name = node.data.meta.name;
    const type = node.data.meta.type;
    if (type === 'source' && name === 'LocalFile') {
      return (<SourceLocalFileStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel}
                                       onOK={onOK}/>);
    } else if (type === 'sink' && name === 'LocalFile') {
      return (
        <SinkLocalFileStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'FtpFile') {
      return (
        <SourceFtpFileStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'FtpFile') {
      return (
        <SinkFtpFileStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'SftpFile') {
      return (<SourceSftpFileStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel}
                                      onOK={onOK}/>);
    } else if (type === 'sink' && name === 'SftpFile') {
      return (
        <SinkSftpFileStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'HdfsFile') {
      return (<SourceHdfsFileStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel}
                                      onOK={onOK}/>);
    } else if (type === 'sink' && name === 'HdfsFile') {
      return (
        <SinkHdfsFileStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'OssFile') {
      return (
        <SourceOssFileStep visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'OssFile') {
      return (
        <SinkOSSFileStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'OssJindoFile') {
      return (<SourceOSSJindoFileStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel}
                                          onOK={onOK}/>);
    } else if (type === 'sink' && name === 'OssJindoFile') {
      return (<SinkOSSJindoFileStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel}
                                        onOK={onOK}/>);
    } else if (type === 'source' && name === 'S3File') {
      return (
        <SourceS3FileStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'S3File') {
      return (
        <SinkS3FileStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'Jdbc') {
      return (
        <SourceJdbcStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'Jdbc') {
      return (
        <SinkJdbcStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'Hudi') {
      return (
        <SourceHudiStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'Iceberg') {
      return (
        <SourceIcebergStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'Paimon') {
      return (
        <SourcePaimonStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'Paimon') {
      return (
        <SinkPaimonStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'FakeSource') {
      return (
        <SourceFakeStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'Console') {
      return (
        <SinkConsoleStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'Http') {
      return (
        <SourceHttpStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'Http') {
      return (
        <SinkHttpStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'Feishu') {
      return (
        <SinkFeishuStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'WeChat') {
      return (
        <SinkWeChatStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'DingTalk') {
      return (
        <SinkDingTalkStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'EmailSink') {
      return (
        <SinkEmailStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'Socket') {
      return (
        <SourceSocketStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'Socket') {
      return (
        <SinkSocketStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'Doris') {
      return (
        <SourceDorisStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    }  else if (type === 'sink' && name === 'Doris') {
      return (
        <SinkDorisStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'StarRocks') {
      return (<SourceStarRocksStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel}
                                       onOK={onOK}/>);
    } else if (type === 'sink' && name === 'StarRocks') {
      return (
        <SinkStarRocksStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'Clickhouse') {
      return (<SourceClickHouseStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel}
                                        onOK={onOK}/>);
    } else if (type === 'sink' && name === 'Clickhouse') {
      return (<SinkClickHouseStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel}
                                      onOK={onOK}/>);
    } else if (type === 'source' && name === 'Hive') {
      return (
        <SourceHiveStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'Hive') {
      return (
        <SinkHiveStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'Kudu') {
      return (
        <SourceKuduStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'Kudu') {
      return (
        <SinkKuduStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'Maxcompute') {
      return (<SourceMaxComputeStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel}
                                        onOK={onOK}/>);
    } else if (type === 'sink' && name === 'Maxcompute') {
      return (<SinkMaxComputeStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel}
                                      onOK={onOK}/>);
    } else if (type === 'source' && name === 'Kafka') {
      return (
        <SourceKafkaStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'Kafka') {
      return (
        <SinkKafkaStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'Rocketmq') {
      return (<SourceRocketMQStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel}
                                      onOK={onOK}/>);
    } else if (type === 'sink' && name === 'Rocketmq') {
      return (
        <SinkRocketMQStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'IoTDB') {
      return (
        <SourceIoTDBStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'IoTDB') {
      return (
        <SinkIoTDBStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'MongoDB') {
      return (
        <SourceMongoDBStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'MongoDB') {
      return (
        <SinkMongoDBStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'Cassandra') {
      return (<SourceCassandraStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel}
                                       onOK={onOK}/>);
    } else if (type === 'sink' && name === 'Cassandra') {
      return (
        <SinkCassandraStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'Redis') {
      return (
        <SourceRedisStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'Redis') {
      return (
        <SinkRedisStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'Pulsar') {
      return (
        <SourcePulsarStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'DataHub') {
      return (
        <SinkDatahubStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'Elasticsearch') {
      return (<SourceElasticsearchStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel}
                                           onOK={onOK}/>);
    } else if (type === 'sink' && name === 'Elasticsearch') {
      return (<SinkElasticsearchStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel}
                                         onOK={onOK}/>);
    } else if (type === 'source' && name === 'Neo4j') {
      return (
        <SourceNeo4jStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'Neo4j') {
      return (
        <SinkNeo4jStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'Sentry') {
      return (
        <SinkSentryStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'InfluxDB') {
      return (<SourceInfluxDBStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel}
                                      onOK={onOK}/>);
    } else if (type === 'sink' && name === 'InfluxDB') {
      return (
        <SinkInfluxDBStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'AmazonDynamodb') {
      return (<SourceAmazonDynamodbStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel}
                                            onOK={onOK}/>);
    } else if (type === 'sink' && name === 'AmazonDynamodb') {
      return (<SinkAmazonDynamodbStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel}
                                          onOK={onOK}/>);
    } else if (type === 'sink' && name === 'S3Redshift') {
      return (<SinkS3RedshiftStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel}
                                      onOK={onOK}/>);
    } else if (type === 'source' && name === 'OpenMldb') {
      return (<SourceOpenMLDBStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel}
                                      onOK={onOK}/>);
    } else if (type === 'source' && name === 'MySQL-CDC') {
      return (<SourceCDCMySQLStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel}
                                      onOK={onOK}/>);
    } else if (type === 'source' && name === 'SqlServer-CDC') {
      return (<SourceCDCSqlServerStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel}
                                          onOK={onOK}/>);
    } else if (type === 'source' && name === 'Oracle-CDC') {
      return (<SourceCDCOracleStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel}
                                          onOK={onOK}/>);
    } else if (type === 'source' && name === 'Postgres-CDC') {
      return (<SourceCDCPostgreSQLStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel}
                                       onOK={onOK}/>);
    } else if (type === 'source' && name === 'MongoDB-CDC') {
      return (<SourceCDCMongoDBStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel}
                                        onOK={onOK}/>);
    } else if (type === 'sink' && name === 'Hbase') {
      return (
        <SinkHbaseStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'transform' && name === 'Copy') {
      return (
        <TransformCopyStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'transform' && name === 'FieldMapper') {
      return (<TransformFieldMapperStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel}
                                            onOK={onOK}/>);
    } else if (type === 'transform' && name === 'FilterRowKind') {
      return (<TransformFilterRowKindStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel}
                                              onOK={onOK}/>);
    } else if (type === 'transform' && name === 'Filter') {
      return (<TransformFilterStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel}
                                       onOK={onOK}/>);
    } else if (type === 'transform' && name === 'Replace') {
      return (<TransformReplaceStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel}
                                        onOK={onOK}/>);
    } else if (type === 'transform' && name === 'Split') {
      return (<TransformSplitStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel}
                                      onOK={onOK}/>);
    } else if (type === 'transform' && name === 'Sql') {
      return (
        <TransformSqlStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else {
      return <></>;
    }
  };

  return (<div>{switchStep(data)}</div>);
}

export default SeaTunnnelConnectorForm;
