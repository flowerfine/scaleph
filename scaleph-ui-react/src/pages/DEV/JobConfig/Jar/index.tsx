import {ProCard, StepsForm,} from '@ant-design/pro-components';
import JobJar from "@/pages/DEV/JobConfig/components/JobJar";
import JobClusterConfigOptions from "@/pages/DEV/JobConfig/components/ClusterConfigOptions";
import {history, useIntl, useLocation} from "umi";
import {Form, message} from "antd";
import {FlinkJobConfigJar} from "@/services/dev/typings";
import {getData, setData} from "@/services/dev/flinkClusterConfig.service";
import {add, update} from "@/services/dev/flinkJobConfigJar.service";

const JobConfigJarOptions: React.FC = () => {
  const urlParams = useLocation();
  const intl = useIntl();
  const [form] = Form.useForm();

  const params = urlParams.state as FlinkJobConfigJar;

  const jobConfig = new Map(Object.entries(params?.jobConfig ? params?.jobConfig : {}))
  const args: Array<any> = []
  jobConfig.forEach((value: any, key: string) => {
    args.push({parameter: key, value: value})
  })
  const data = {
    flinkArtifactId: params?.flinkArtifactJar?.flinkArtifact.id,
    flinkArtifactJarId: params?.flinkArtifactJar?.id,
    entryClass: params?.flinkArtifactJar?.entryClass,
    args: args,
    deployMode: params?.flinkClusterConfig?.deployMode?.value,
    flinkClusterInstance: params?.flinkClusterInstance?.id,
    flinkClusterConfig: params?.flinkClusterConfig?.id
  }
  const flinkConfig = new Map(Object.entries(params?.flinkConfig ? params?.flinkConfig : {}))
  setData(form, flinkConfig)

  return (<ProCard>
    <StepsForm
      formProps={{
        form: form,
        initialValues: data,
        grid: true,
        wrapperCol: {span: 24}
      }}
      submitter={{
        render: (props, doms) => {
          return doms
        }
      }}
      onFinish={async (values) => {
        const jobConfig = new Map<string, any>();
        values.args?.forEach(function (item: Record<string, any>) {
          jobConfig[item.parameter] = item.value
        })
        const param: FlinkJobConfigJar = {
          id: params?.id,
          name: 'name',
          flinkArtifactJar: {id: values['flinkArtifactJarId']},
          flinkClusterConfig: {id: values['flinkClusterConfig']},
          flinkClusterInstance: {id: values['flinkClusterInstance']},
          jobConfig: jobConfig,
          flinkConfig: getData(values),
          remark: 'remark'
        }
        return params?.id ?
          update(param).then((d) => {
            if (d.success) {
              message.success(intl.formatMessage({id: 'app.common.operate.new.success'}));
            }
          })
            .catch(() => {
              message.error(intl.formatMessage({id: 'app.common.operate.new.failure'}));
            })
            .finally(() => {
              history.back();
            })
          : add(param).then((d) => {
            if (d.success) {
              message.success(intl.formatMessage({id: 'app.common.operate.new.success'}));
            }
          })
            .catch(() => {
              message.error(intl.formatMessage({id: 'app.common.operate.new.failure'}));
            })
            .finally(() => {
              history.back();
            });
      }}
    >
      <StepsForm.StepForm
        name="artifact"
        title="Artifact"
        layout={"horizontal"}
        style={{
          width: 1000
        }}>
        <JobJar/>
      </StepsForm.StepForm>
      <StepsForm.StepForm
        name="configOptions"
        title="Config Options"
        layout={"horizontal"}
        style={{
          width: 1000
        }}>
        <JobClusterConfigOptions/>
      </StepsForm.StepForm>
    </StepsForm>
  </ProCard>);
};

export default JobConfigJarOptions;
