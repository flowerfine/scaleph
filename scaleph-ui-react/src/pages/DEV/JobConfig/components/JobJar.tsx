import {ProForm, ProFormSelect} from "@ant-design/pro-components";
import {useIntl} from "umi";
import {Form} from "antd";

const JobJar: React.FC = () => {
  const intl = useIntl();
  const [form] = Form.useForm();

  return (<ProForm
    form={form}
    layout={"horizontal"}
    grid={true}
    rowProps={{gutter: [16, 8]}}
  >
    <ProFormSelect
      name={"flinkArtifactId"}
      label={intl.formatMessage({id: 'pages.dev.artifact'})}
      rules={[{required: true}]}
      showSearch={true}
      request={async () => {
        return []
      }}
    />

  </ProForm>);
}

export default JobJar;
