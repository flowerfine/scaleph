import {ProForm, ProFormSelect, ProFormText} from "@ant-design/pro-components";
import {useIntl} from "umi";
import {Form} from "antd";
import {FlinkArtifactListParam} from "@/services/dev/typings";
import {list} from "@/services/dev/flinkArtifact.service";

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
      request={(params) => {
        const param: FlinkArtifactListParam = {
          name: params.keyword
        }
        return list(param).then((response) => {
          return response.data.map((item) => {
            return {label: item.name, value: item.id}
          })
        })
      }}
    />

    <ProFormText
      name="entryClass"
      label={intl.formatMessage({id: 'pages.dev.artifact.entryClass'})}
      colProps={{span: 10, offset: 1}}
      rules={[{ required: true }, { max: 64 }]}
    />

  </ProForm>);
}

export default JobJar;
