import {ProCard, ProFormGroup, ProFormList, ProFormSelect, ProFormText} from "@ant-design/pro-components";
import {useIntl} from "umi";
import {Form} from "antd";
import {FlinkArtifactListParam} from "@/services/dev/typings";
import {list} from "@/services/dev/flinkArtifact.service";

const JobJar: React.FC = () => {
  const intl = useIntl();
  const form = Form.useFormInstance();

  const handleArtifactChange = (value: any, option: any) => {
    if (option) {
      form.setFieldValue('entryClass', option.item.entryClass)
    }
  };

  return (<ProCard>
    <ProFormSelect
      name={"flinkArtifactId"}
      label={intl.formatMessage({id: 'pages.dev.artifact'})}
      rules={[{required: true}]}
      showSearch={true}
      fieldProps={{
        onChange: handleArtifactChange
      }}
      request={(params) => {
        const param: FlinkArtifactListParam = {
          name: params.keyword
        }
        return list(param).then((response) => {
          return response.data.map((item) => {
            return {label: item.name, value: item.id, item: item}
          })
        })
      }}
    />
    <ProFormText
      name="entryClass"
      label={intl.formatMessage({id: 'pages.dev.artifact.entryClass'})}
      rules={[{required: true}, {max: 128}]}
      readonly
    />
      <ProFormList
        name="args"
        copyIconProps={false}
        creatorButtonProps={{
          creatorButtonText: '添加 main args',
          type: "text"
        }}>
        <ProFormGroup>
          <ProFormText name="parameter" label={'Parameter'}/>
          <ProFormText name="value" label={'Value'}/>
        </ProFormGroup>

      </ProFormList>
  </ProCard>);
}

export default JobJar;
