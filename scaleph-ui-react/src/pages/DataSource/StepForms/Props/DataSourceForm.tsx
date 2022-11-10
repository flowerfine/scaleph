import {useModel} from "umi";
import JdbcForm from "@/pages/DataSource/StepForms/Props/Jdbc";
import {DsType} from "@/services/datasource/typings";
import FtpForm from "@/pages/DataSource/StepForms/Props/Ftp";
import OSSForm from "@/pages/DataSource/StepForms/Props/OSS";
import S3Form from "@/pages/DataSource/StepForms/Props/S3";

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
        default:
          return <div>开发中</div>
      }
    }
    return <div>动态渲染数据源表单失败</div>
  }

  return (<div>{formView(dsType)}</div>);
}

export default DataSourceForm;
