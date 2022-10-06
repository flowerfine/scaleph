import {ProForm, ProFormGroup, ProFormList, ProFormSelect, ProFormText} from "@ant-design/pro-components";
import {useIntl} from "umi";
import {FlinkArtifactJar} from "@/services/dev/typings";

const JobArtifactForJarWeb: React.FC<{ data: FlinkArtifactJar }> = ({data}) => {
  const intl = useIntl();

  console.log('JobArtifactForJarWeb', data)

  return (<ProForm<FlinkArtifactJar>
    initialValues={data}
    layout={'horizontal'}
    readonly={true}
    grid={true}
    submitter={false}
  >
    <ProFormGroup>
      <ProFormText
        name="fileName"
        label={"fileName"}
        colProps={{span: 10, offset: 1}}
      />
      <ProFormText
        name="version"
        label={"version"}
        colProps={{span: 2, offset: 1}}
      />
      <ProFormText
        name="entryClass"
        label={"entryClass"}
        colProps={{span: 10, offset: 1}}
      />
    </ProFormGroup>
    <ProFormList
      name="args"
      copyIconProps={false}
      deleteIconProps={false}
      fieldExtraRender={(fieldAction, meta) => {
        // fieldAction.remove(0)
        // return (<a onClick={()=>fieldAction.add({id:"xx"})}>新增</a>)
      }}
    >
      <ProFormGroup>
        <ProFormText
          name="parameter"
          label={intl.formatMessage({id: 'pages.dev.job.jar.args.key'})}
          colProps={{span: 10, offset: 1}}
        />
        <ProFormText
          name="value"
          label={intl.formatMessage({id: 'pages.dev.job.jar.args.value'})}
          colProps={{span: 10, offset: 1}}
        />
      </ProFormGroup>
    </ProFormList>

  </ProForm>);
}

export default JobArtifactForJarWeb;
