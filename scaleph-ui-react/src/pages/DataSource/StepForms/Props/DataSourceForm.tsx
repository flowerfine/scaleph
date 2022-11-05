import {useModel} from "umi";
import MySQLForm from "@/pages/DataSource/StepForms/Props/MySQL";
import {DsType} from "@/services/datasource/typings";

const DataSourceForm: React.FC = () => {

  const {dsType} = useModel('dataSourceType', (model) => ({
    dsType: model.dsType
  }));

  const formView = (type?: DsType) => {
    if (type?.type.value) {
      switch (type?.type.value) {
        case 'MySQL':
          return <MySQLForm/>
        default:
          return <div>开发中</div>
      }
    }
    return <div>动态渲染数据源表单失败</div>
  }

  return (<div>{formView(dsType)}</div>);
}

export default DataSourceForm;
