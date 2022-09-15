import {ProCard, ProFormSelect, ProFormText} from "@ant-design/pro-components";
import {useIntl} from 'umi';
import {Form} from "antd";
import {listDictDataByType} from "@/services/admin/dictData.service";
import {DICT_TYPE} from "@/constant";

const JobClusterConfigOptions: React.FC = () => {
  const intl = useIntl();
  const form = Form.useFormInstance();

  return (<ProCard
    style={{
      marginBlockEnd: 16,
      minWidth: 800,
      maxWidth: '100%',
    }}
  >
    <ProFormSelect
      name="deployMode"
      label={intl.formatMessage({id: 'pages.dev.clusterConfig.deployMode'})}
      rules={[{required: true}]}
      showSearch={false}
      request={() => {
        return listDictDataByType(DICT_TYPE.flinkDeploymentMode)
      }}
    />
  </ProCard>);
}

export default JobClusterConfigOptions;
