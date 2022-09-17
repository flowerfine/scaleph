import {ProCard, ProFormGroup, ProFormList, ProFormSelect, ProFormText} from "@ant-design/pro-components";
import {useIntl} from "umi";
import {Form} from "antd";
import {FlinkArtifact, FlinkArtifactJarListParam, FlinkArtifactListParam} from "@/services/dev/typings";
import {list as listArtifact} from "@/services/dev/flinkArtifact.service";
import {list as listArtifactJar} from "@/services/dev/flinkArtifactJar.service";
import {useState} from "react";
import {TablePageResponse} from "@/app.d";

const JobJar: React.FC = () => {
  const intl = useIntl();
  const form = Form.useFormInstance();

  const [flinkArtifactPage, setFlinkArtifactPage] = useState<TablePageResponse<FlinkArtifact>>({
    pageSize: 10,
    current: 1,
    total: 0,
    data: []
  });

  const handleArtifactJarChange = (value: any, option: any) => {
    if (option) {
      form.setFieldValue('entryClass', option.item.entryClass)
    }
  };

  const handleFlinkArtifactJarScroll = (e: any) => {
    const {scrollTop, scrollHeight, clientHeight} = e.target;
    const isEnd = scrollHeight - scrollTop === clientHeight;
    console.log(e)
  }

  return (<ProCard>
    <ProFormGroup>
      <ProFormSelect
        name={"flinkArtifactId"}
        label={intl.formatMessage({id: 'pages.dev.artifact'})}
        rules={[{required: true}]}
        colProps={{span: 6}}
        fieldProps={{
          showSearch: true,
        }}
        request={(params) => {
          const param: FlinkArtifactListParam = {
            name: params.keyWords,
            type: 0
          }
          return listArtifact(param).then((response) => {
            return response.data.map((item) => {
              return {label: item.name, value: item.id, item: item}
            })
          })
        }}
      />
      <ProFormSelect
        name={"flinkArtifactJarId"}
        colProps={{span: 18}}
        dependencies={["flinkArtifactId"]}
        fieldProps={{
          showSearch: false,
          onChange: handleArtifactJarChange,
          onPopupScroll: handleFlinkArtifactJarScroll
        }}
        request={(params) => {
          if (!params.flinkArtifactId) {
            return Promise.any([])
          }
          const param: FlinkArtifactJarListParam = {
            flinkArtifactId: params.flinkArtifactId
          }
          return listArtifactJar(param).then((response) => {
            return response.data.map((item) => {
              return {label: item.version + '-' + item.fileName, value: item.id, item: item}
            })
          })
        }}
      />
    </ProFormGroup>
    <ProFormText
      name="entryClass"
      label={intl.formatMessage({id: 'pages.dev.artifact.jar.entryClass'})}
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
        <ProFormText name="parameter" label={'Parameter'} colProps={{span: 10, offset: 1}}/>
        <ProFormText name="value" label={'Value'} colProps={{span: 10, offset: 1}}/>
      </ProFormGroup>
    </ProFormList>
  </ProCard>);
}

export default JobJar;
