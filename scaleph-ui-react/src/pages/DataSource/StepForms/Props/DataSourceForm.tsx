import {useModel} from "umi";
import JdbcForm from "@/pages/DataSource/StepForms/Props/Jdbc";
import {DsType} from "@/services/datasource/typings";
import FtpForm from "@/pages/DataSource/StepForms/Props/Ftp";
import OSSForm from "@/pages/DataSource/StepForms/Props/OSS";
import S3Form from "@/pages/DataSource/StepForms/Props/S3";
import RedisForm from "@/pages/DataSource/StepForms/Props/Redis";
import IoTDBForm from "@/pages/DataSource/StepForms/Props/IoTDB";
import KuduForm from "@/pages/DataSource/StepForms/Props/Kudu";
import ElasticsearchForm from "@/pages/DataSource/StepForms/Props/Elasticsearch";
import MongoDBForm from "@/pages/DataSource/StepForms/Props/MongoDB";
import DataHubForm from "@/pages/DataSource/StepForms/Props/DataHub";

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
        case 'OSS':
          return <OSSForm/>
        case 'S3':
          return <S3Form/>
        case 'Redis':
          return <RedisForm/>
        case 'Elasticsearch':
          return <ElasticsearchForm/>
        case 'MongoDB':
          return <MongoDBForm/>

        case 'DataHub':
          return <DataHubForm/>
        case 'Kudu':
          return <KuduForm/>
        case 'IoTDB':
          return <IoTDBForm/>
        default:
          return <div>开发中</div>
      }
    }
    return <div>动态渲染数据源表单失败</div>
  }

  return (<div>{formView(dsType)}</div>);
}

export default DataSourceForm;
