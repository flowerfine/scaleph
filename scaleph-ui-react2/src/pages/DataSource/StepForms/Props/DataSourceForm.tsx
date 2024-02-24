import {useModel} from "umi";
import {DsType} from "@/services/datasource/typings";
import JdbcForm from "@/pages/DataSource/StepForms/Props/Jdbc";
import FtpForm from "@/pages/DataSource/StepForms/Props/Ftp";
import OSSForm from "@/pages/DataSource/StepForms/Props/OSS";
import S3Form from "@/pages/DataSource/StepForms/Props/S3";
import RedisForm from "@/pages/DataSource/StepForms/Props/Redis";
import IoTDBForm from "@/pages/DataSource/StepForms/Props/IoTDB";
import KuduForm from "@/pages/DataSource/StepForms/Props/Kudu";
import ElasticsearchForm from "@/pages/DataSource/StepForms/Props/Elasticsearch";
import MongoDBForm from "@/pages/DataSource/StepForms/Props/MongoDB";
import DataHubForm from "@/pages/DataSource/StepForms/Props/DataHub";
import HDFSForm from "@/pages/DataSource/StepForms/Props/HDFS";
import SocketForm from "@/pages/DataSource/StepForms/Props/Socket";
import KafkaForm from "@/pages/DataSource/StepForms/Props/Kafka";
import HttpForm from "@/pages/DataSource/StepForms/Props/Http";
import PulsarForm from "@/pages/DataSource/StepForms/Props/Pulsar";
import HiveForm from "@/pages/DataSource/StepForms/Props/Hive";
import ClickHouseForm from "@/pages/DataSource/StepForms/Props/ClickHouse";
import Neo4jForm from "@/pages/DataSource/StepForms/Props/Neo4j";
import InfluxDBForm from "@/pages/DataSource/StepForms/Props/InfluxDB";
import SftpForm from "@/pages/DataSource/StepForms/Props/Sftp";
import OSSJindoForm from "@/pages/DataSource/StepForms/Props/OSSJindo";
import CassandraForm from "@/pages/DataSource/StepForms/Props/Cassandra";
import DorisForm from "@/pages/DataSource/StepForms/Props/Doris";
import StarRocksForm from "@/pages/DataSource/StepForms/Props/StarRocks";
import MaxComputeForm from "@/pages/DataSource/StepForms/Props/MaxCompute";

const DataSourceForm: React.FC = () => {

  const {dsType} = useModel('dataSourceType', (model) => ({
    dsType: model.dsType
  }));

  const formView = (type?: DsType) => {
    if (type?.type.value) {
      switch (type?.type.value) {
        case 'MySQL':
        case 'Oracle':
        case 'PostgreSQL':
        case 'SQLServer':
        case 'DmDB':
        case 'GBase8a':
        case 'Greenplum':
        case 'Phoenix':
          return <JdbcForm/>
        case 'Ftp':
          return <FtpForm/>
        case 'Sftp':
          return <SftpForm/>
        case 'OSS':
          return <OSSForm/>
        case 'OSSJindo':
          return <OSSJindoForm/>
        case 'S3':
          return <S3Form/>
        case 'HDFS':
          return <HDFSForm/>
        case 'Hive':
          return <HiveForm/>
        case 'Redis':
          return <RedisForm/>
        case 'Elasticsearch':
          return <ElasticsearchForm/>
        case 'MongoDB':
          return <MongoDBForm/>
        case 'Cassandra':
          return <CassandraForm/>
        case 'Kafka':
          return <KafkaForm/>
        case 'Pulsar':
          return <PulsarForm/>
        case 'DataHub':
          return <DataHubForm/>
        case 'Doris':
          return <DorisForm/>
        case 'StarRocks':
          return <StarRocksForm/>
        case 'ClickHouse':
          return <ClickHouseForm/>
        case 'Kudu':
          return <KuduForm/>
        case 'MaxCompute':
          return <MaxComputeForm/>
        case 'IoTDB':
          return <IoTDBForm/>
        case 'Neo4j':
          return <Neo4jForm/>
        case 'InfluxDB':
          return <InfluxDBForm/>
        case 'Socket':
          return <SocketForm/>
        case 'Http':
          return <HttpForm/>
        default:
          return <div>开发中</div>
      }
    }
    return <div>动态渲染数据源表单失败</div>
  }

  return (<div>{formView(dsType)}</div>);
}

export default DataSourceForm;
