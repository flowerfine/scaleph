import {ModalFormProps} from "@/typings";
import SourceFakeStepForm from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/source/source-fake-step";
import SinkConsoleStepForm from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/sink/sink-console-step";
import {Node} from "@antv/xflow";
import SourceLocalFileStepForm
  from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/source/source-local-file-step";
import SinkLocalFileStepForm
  from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/sink/sink-local-file-step";
import SourceFtpFileStepForm
  from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/source/source-ftp-file-step";
import SinkFtpFileStepForm
  from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/sink/sink-ftp-file-step";
import SourceSftpFileStepForm
  from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/source/source-sftp-file-step";
import SinkSftpFileStepForm
  from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/sink/sink-sftp-file-step";
import SourceHdfsFileStepForm
  from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/source/source-hdfs-file-step";
import SinkHdfsFileStepForm
  from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/sink/sink-hdfs-file-step";
import SourceOssFileStep
  from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/source/source-oss-file-step";
import SinkOSSFileStepForm
  from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/sink/sink-oss-file-step";
import SourceOSSJindoFileStepForm
  from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/source/source-ossjindo-file-step";
import SinkOSSJindoFileStepForm
  from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/sink/sink-ossjindo-file-step";
import SourceS3FileStepForm
  from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/source/source-s3-file-step";
import SinkS3FileStepForm from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/sink/sink-s3-file-step";
import SourceJdbcStepForm from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/source/source-jdbc-step";
import SinkJdbcStepForm from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/sink/sink-jdbc-step";
import SourceHudiStepForm from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/source/source-hudi-step";
import SourceIcebergStepForm
  from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/source/source-iceberg-step";
import SourcePaimonStepForm
  from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/source/source-paimon-step";
import SinkPaimonStepForm from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/sink/sink-paimon-step";
import SourceHttpStepForm from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/source/source-http-step";
import SinkHttpStepForm from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/sink/sink-http-step";
import SinkFeishuStepForm from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/sink/sink-feishu-step";
import SinkWeChatStepForm from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/sink/sink-wechat-step";
import SinkDingTalkStepForm
  from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/sink/sink-dingtalk-step";
import SinkEmailStepForm from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/sink/sink-email-step";
import SourceSocketStepForm
  from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/source/source-socket-step";
import SinkSocketStepForm from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/sink/sink-socket-step";
import SinkDorisStepForm from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/sink/sink-doris-step";
import SourceStarRocksStepForm
  from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/source/source-starrocks-step";
import SinkStarRocksStepForm
  from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/sink/sink-starrocks-step";
import SourceClickHouseStepForm
  from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/source/source-clickhouse-step";
import SinkClickHouseStepForm
  from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/sink/sink-clickhouse-step";
import SourceHiveStepForm from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/source/source-hive-step";
import SinkHiveStepForm from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/sink/sink-hive-step";
import SourceKuduStepForm from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/source/source-kudu-step";
import SinkKuduStepForm from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/sink/sink-kudu-step";
import SourceMaxComputeStepForm
  from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/source/source-maxcompute-step";
import SinkMaxComputeStepForm
  from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/sink/sink-maxcompute-step";
import SourceKafkaStepForm
  from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/source/source-kafka-step";
import SinkKafkaStepForm from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/sink/sink-kafka-step";
import SourceRocketMQStepForm
  from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/source/source-rocketmq-step";
import SinkRocketMQStepForm
  from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/sink/sink-rocketmq-step";
import SourceIoTDBStepForm
  from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/source/source-iotdb-step";
import SinkIoTDBStepForm from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/sink/sink-iotdb-step";
import SourceMongoDBStepForm
  from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/source/source-mongodb-step";
import SinkMongoDBStepForm from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/sink/sink-mongodb-step";
import SourceCassandraStepForm
  from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/source/source-cassandra-step";
import SinkCassandraStepForm
  from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/sink/sink-cassandra-step";
import SourceRedisStepForm
  from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/source/source-redis-step";
import SinkRedisStepForm from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/sink/sink-redis-step";
import SourcePulsarStepForm
  from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/source/source-pulsar-step";
import SinkDatahubStepForm from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/sink/sink-datahub-step";
import SourceElasticsearchStepForm
  from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/source/source-elasticsearch-step";
import SinkElasticsearchStepForm
  from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/sink/sink-elasticsearch-step";
import SourceNeo4jStepForm
  from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/source/source-neo4j-step";
import SinkNeo4jStepForm from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/sink/sink-neo4j-step";
import SinkSentryStepForm from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/sink/sink-sentry-step";
import SourceInfluxDBStepForm
  from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/source/source-influxdb-step";
import SinkInfluxDBStepForm
  from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/sink/sink-influxdb-step";
import SourceAmazonDynamodbStepForm
  from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/source/source-dynamodb-step";
import SinkAmazonDynamodbStepForm
  from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/sink/sink-dynamodb-step";
import SinkS3RedshiftStepForm
  from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/sink/sink-s3redshift-step";
import SourceOpenMLDBStepForm
  from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/source/source-openmldb-step";
import SourceCDCMySQLStepForm
  from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/source/source-cdc-mysql-step";
import SourceCDCSqlServerStepForm
  from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/source/source-cdc-sqlserver-step";
import SourceCDCMongoDBStepForm
  from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/source/source-cdc-mongodb-step";
import SinkHbaseStepForm from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/sink/sink-hbase-step";
import TransformCopyStepForm
  from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/transform/transform-copy-step";
import TransformFieldMapperStepForm
  from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/transform/transform-field-mapper-step";
import TransformFilterRowKindStepForm
  from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/transform/transform-filter-row-kind-step";
import TransformFilterStepForm
  from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/transform/transform-filter-step";
import TransformSplitStepForm
  from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/transform/transform-split-step";
import TransformSqlStepForm
  from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/transform/transform-sql-step";
import TransformReplaceStepForm
  from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/transform/transform-replace-step";

const SeaTunnnelConnectorForm: React.FC<ModalFormProps<Node>> = ({visible, onVisibleChange, onCancel, onOK, data}) => {

  const switchStep = (node: Node) => {
    const name = node.data.meta.name;
    const type = node.data.meta.type;
    if (type === 'source' && name === 'LocalFile') {
      return (<SourceLocalFileStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'LocalFile') {
      return (<SinkLocalFileStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'FtpFile') {
      return (<SourceFtpFileStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'FtpFile') {
      return (<SinkFtpFileStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'SftpFile') {
      return (<SourceSftpFileStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'SftpFile') {
      return (<SinkSftpFileStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'HdfsFile') {
      return (<SourceHdfsFileStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'HdfsFile') {
      return (<SinkHdfsFileStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'OssFile') {
      return (<SourceOssFileStep visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'OssFile') {
      return (<SinkOSSFileStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'OssJindoFile') {
      return (<SourceOSSJindoFileStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'OssJindoFile') {
      return (<SinkOSSJindoFileStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'S3File') {
      return (<SourceS3FileStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'S3File') {
      return (<SinkS3FileStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'Jdbc') {
      return (<SourceJdbcStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'Jdbc') {
      return (<SinkJdbcStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'Hudi') {
      return (<SourceHudiStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'Iceberg') {
      return (<SourceIcebergStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'Paimon') {
      return (<SourcePaimonStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'Paimon') {
      return (<SinkPaimonStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'FakeSource') {
      return (<SourceFakeStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'Console') {
      return (<SinkConsoleStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'Http') {
      return (<SourceHttpStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'Http') {
      return (<SinkHttpStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'Feishu') {
      return (<SinkFeishuStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'WeChat') {
      return (<SinkWeChatStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'DingTalk') {
      return (<SinkDingTalkStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'EmailSink') {
      return (<SinkEmailStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'Socket') {
      return (<SourceSocketStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'Socket') {
      return (<SinkSocketStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'Doris') {
      return (<SinkDorisStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'StarRocks') {
      return (<SourceStarRocksStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'StarRocks') {
      return (<SinkStarRocksStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'Clickhouse') {
      return (<SourceClickHouseStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'Clickhouse') {
      return (<SinkClickHouseStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'Hive') {
      return (<SourceHiveStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'Hive') {
      return (<SinkHiveStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'Kudu') {
      return (<SourceKuduStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'Kudu') {
      return (<SinkKuduStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'Maxcompute') {
      return (<SourceMaxComputeStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'Maxcompute') {
      return (<SinkMaxComputeStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'Kafka') {
      return (<SourceKafkaStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'Kafka') {
      return (<SinkKafkaStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'Rocketmq') {
      return (<SourceRocketMQStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'Rocketmq') {
      return (<SinkRocketMQStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'IoTDB') {
      return (<SourceIoTDBStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'IoTDB') {
      return (<SinkIoTDBStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'MongoDB') {
      return (<SourceMongoDBStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'MongoDB') {
      return (<SinkMongoDBStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'Cassandra') {
      return (<SourceCassandraStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'Cassandra') {
      return (<SinkCassandraStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'Redis') {
      return (<SourceRedisStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'Redis') {
      return (<SinkRedisStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'Pulsar') {
      return (<SourcePulsarStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'DataHub') {
      return (<SinkDatahubStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'Elasticsearch') {
      return (<SourceElasticsearchStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'Elasticsearch') {
      return (<SinkElasticsearchStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'Neo4j') {
      return (<SourceNeo4jStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'Neo4j') {
      return (<SinkNeo4jStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'Sentry') {
      return (<SinkSentryStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'InfluxDB') {
      return (<SourceInfluxDBStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'InfluxDB') {
      return (<SinkInfluxDBStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'AmazonDynamodb') {
      return (<SourceAmazonDynamodbStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'AmazonDynamodb') {
      return (<SinkAmazonDynamodbStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'S3Redshift') {
      return (<SinkS3RedshiftStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'OpenMldb') {
      return (<SourceOpenMLDBStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'MySQL-CDC') {
      return (<SourceCDCMySQLStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'SqlServer-CDC') {
      return (<SourceCDCSqlServerStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'source' && name === 'MongoDB-CDC') {
      return (<SourceCDCMongoDBStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'Hbase') {
      return (<SinkHbaseStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'transform' && name === 'Copy') {
      return (<TransformCopyStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'transform' && name === 'FieldMapper') {
      return (<TransformFieldMapperStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'transform' && name === 'FilterRowKind') {
      return (<TransformFilterRowKindStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'transform' && name === 'Filter') {
      return (<TransformFilterStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'transform' && name === 'Replace') {
      return (<TransformReplaceStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'transform' && name === 'Split') {
      return (<TransformSplitStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'transform' && name === 'Sql') {
      return (<TransformSqlStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else {
      return <></>;
    }
  };

  return (<div>{switchStep(data)}</div>);
}

export default SeaTunnnelConnectorForm;
