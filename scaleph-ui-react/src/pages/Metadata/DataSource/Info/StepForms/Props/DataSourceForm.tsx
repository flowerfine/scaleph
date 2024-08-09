import {DataSourceProps} from "@/services/datasource/typings";
import JdbcForm from "@/pages/Metadata/DataSource/Info/StepForms/Props/Jdbc";
import FtpForm from "@/pages/Metadata/DataSource/Info/StepForms/Props/Ftp";
import OSSForm from "@/pages/Metadata/DataSource/Info/StepForms/Props/OSS";
import S3Form from "@/pages/Metadata/DataSource/Info/StepForms/Props/S3";
import RedisForm from "@/pages/Metadata/DataSource/Info/StepForms/Props/Redis";
import IoTDBForm from "@/pages/Metadata/DataSource/Info/StepForms/Props/IoTDB";
import KuduForm from "@/pages/Metadata/DataSource/Info/StepForms/Props/Kudu";
import ElasticsearchForm from "@/pages/Metadata/DataSource/Info/StepForms/Props/Elasticsearch";
import MongoDBForm from "@/pages/Metadata/DataSource/Info/StepForms/Props/MongoDB";
import DataHubForm from "@/pages/Metadata/DataSource/Info/StepForms/Props/DataHub";
import HDFSForm from "@/pages/Metadata/DataSource/Info/StepForms/Props/HDFS";
import SocketForm from "@/pages/Metadata/DataSource/Info/StepForms/Props/Socket";
import KafkaForm from "@/pages/Metadata/DataSource/Info/StepForms/Props/Kafka";
import HttpForm from "@/pages/Metadata/DataSource/Info/StepForms/Props/Http";
import PulsarForm from "@/pages/Metadata/DataSource/Info/StepForms/Props/Pulsar";
import HiveForm from "@/pages/Metadata/DataSource/Info/StepForms/Props/Hive";
import ClickHouseForm from "@/pages/Metadata/DataSource/Info/StepForms/Props/ClickHouse";
import Neo4jForm from "@/pages/Metadata/DataSource/Info/StepForms/Props/Neo4j";
import InfluxDBForm from "@/pages/Metadata/DataSource/Info/StepForms/Props/InfluxDB";
import SftpForm from "@/pages/Metadata/DataSource/Info/StepForms/Props/Sftp";
import OSSJindoForm from "@/pages/Metadata/DataSource/Info/StepForms/Props/OSSJindo";
import CassandraForm from "@/pages/Metadata/DataSource/Info/StepForms/Props/Cassandra";
import DorisForm from "@/pages/Metadata/DataSource/Info/StepForms/Props/Doris";
import StarRocksForm from "@/pages/Metadata/DataSource/Info/StepForms/Props/StarRocks";
import MaxComputeForm from "@/pages/Metadata/DataSource/Info/StepForms/Props/MaxCompute";
import GenericForm from "@/pages/Metadata/DataSource/Info/StepForms/Props/Generic";

const DataSourceForm: React.FC<DataSourceProps> = ({prefix, type}) => {

  const formView = () => {
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
          return <JdbcForm prefix={prefix} type={type}/>
        case 'Ftp':
          return <FtpForm prefix={prefix} type={type}/>
        case 'Sftp':
          return <SftpForm prefix={prefix} type={type}/>
        case 'OSS':
          return <OSSForm prefix={prefix} type={type}/>
        case 'OSSJindo':
          return <OSSJindoForm prefix={prefix} type={type}/>
        case 'S3':
          return <S3Form prefix={prefix} type={type}/>
        case 'HDFS':
          return <HDFSForm prefix={prefix} type={type}/>
        case 'Hive':
          return <HiveForm prefix={prefix} type={type}/>
        case 'Redis':
          return <RedisForm prefix={prefix} type={type}/>
        case 'Elasticsearch':
          return <ElasticsearchForm prefix={prefix} type={type}/>
        case 'MongoDB':
          return <MongoDBForm prefix={prefix} type={type}/>
        case 'Cassandra':
          return <CassandraForm prefix={prefix} type={type}/>
        case 'Kafka':
          return <KafkaForm prefix={prefix} type={type}/>
        case 'Pulsar':
          return <PulsarForm prefix={prefix} type={type}/>
        case 'DataHub':
          return <DataHubForm prefix={prefix} type={type}/>
        case 'Doris':
          return <DorisForm prefix={prefix} type={type}/>
        case 'StarRocks':
          return <StarRocksForm prefix={prefix} type={type}/>
        case 'ClickHouse':
          return <ClickHouseForm prefix={prefix} type={type}/>
        case 'Kudu':
          return <KuduForm prefix={prefix} type={type}/>
        case 'MaxCompute':
          return <MaxComputeForm prefix={prefix} type={type}/>
        case 'IoTDB':
          return <IoTDBForm prefix={prefix} type={type}/>
        case 'Neo4j':
          return <Neo4jForm prefix={prefix} type={type}/>
        case 'InfluxDB':
          return <InfluxDBForm prefix={prefix} type={type}/>
        case 'Socket':
          return <SocketForm prefix={prefix} type={type}/>
        case 'Http':
          return <HttpForm prefix={prefix} type={type}/>
        default:
          return <GenericForm prefix={prefix} type={type}/>
      }
    }
    return <div>动态渲染数据源表单失败</div>
  }

  return (<div>{formView()}</div>);
}

export default DataSourceForm;
