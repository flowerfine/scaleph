import JobClusterConfigOptions from '@/pages/DEV/JobConfig/components/ClusterConfigOptions';
import JobBase from '@/pages/DEV/JobConfig/components/JobBase';
import JobJar from '@/pages/DEV/JobConfig/components/JobJar';
import {FlinkClusterConfigService} from '@/services/dev/flinkClusterConfig.service';
import {FlinkJobService} from "@/pages/DEV/Job/FlinkJobService";
import {ProCard, ProFormInstance, StepsForm} from '@ant-design/pro-components';
import {message} from 'antd';
import {useRef} from 'react';
import {history, useIntl, useLocation} from 'umi';
import {FlinkJob, FlinkJobForJar} from "@/pages/DEV/Job/typings";

const JobConfigJarOptions: React.FC = () => {
  const urlParams = useLocation();
  const intl = useIntl();
  const formRef = useRef<ProFormInstance>();

  const params = urlParams.state as FlinkJobForJar;

  const jobConfig = new Map(Object.entries(params?.jobConfig ? params?.jobConfig : {}));
  const args: Array<any> = [];
  jobConfig.forEach((value: any, key: string) => {
    args.push({parameter: key, value: value});
  });
  const data = {
    name: params?.name,
    flinkArtifactId: params?.flinkArtifactJar?.flinkArtifact?.id,
    flinkArtifactJarId: params?.flinkArtifactJar?.id,
    entryClass: params?.flinkArtifactJar?.entryClass,
    args: args,
    deployMode: params?.flinkClusterConfig?.deployMode?.value,
    flinkClusterInstance: params?.flinkClusterInstance?.id,
    flinkClusterConfig: params?.flinkClusterConfig?.id,
    remark: params?.remark,
  };
  const flinkConfig = FlinkClusterConfigService.setData(
    new Map(Object.entries(params?.flinkConfig ? params?.flinkConfig : {})),
  );

  return (
    <ProCard className={'step-form-submitter'}>
      <StepsForm
        formRef={formRef}
        formProps={{
          initialValues: {...data, ...flinkConfig},
          grid: true,
        }}
        submitter={{
          render: (props, doms) => {
            return doms;
          },
        }}
        onFinish={async (values) => {
          const jobConfig = new Map<string, any>();
          values.args?.forEach(function (item: Record<string, any>) {
            jobConfig[item.parameter] = item.value;
          });
          const param: FlinkJob = {
            type: '0',
            code: params?.code,
            name: values['name'],
            flinkArtifactId: values['flinkArtifactJarId'],
            jobConfig: jobConfig,
            flinkClusterConfigId: values['flinkClusterConfig'],
            flinkClusterInstanceId: values['flinkClusterInstance'],
            flinkConfig: FlinkClusterConfigService.getData(values),
            remark: values['remark'],
          };
          return params?.code
            ? FlinkJobService.update(param)
              .then((d) => {
                if (d.success) {
                  message.success(intl.formatMessage({id: 'app.common.operate.edit.success'}));
                  history.back();
                }
              })
            : FlinkJobService.add(param)
              .then((d) => {
                if (d.success) {
                  message.success(intl.formatMessage({id: 'app.common.operate.new.success'}));
                  history.back();
                }
              })
        }}
      >
        <StepsForm.StepForm
          name="base"
          title={intl.formatMessage({id: 'pages.dev.job.base'})}
          layout={'horizontal'}
          style={{
            width: 1000,
          }}
        >
          <JobBase/>
        </StepsForm.StepForm>
        <StepsForm.StepForm
          name="artifact"
          title={intl.formatMessage({id: 'pages.dev.job.artifact'})}
          layout={'horizontal'}
          style={{
            width: 1000,
          }}
        >
          <JobJar data={params}/>
        </StepsForm.StepForm>
        <StepsForm.StepForm
          name="configOptions"
          title={intl.formatMessage({id: 'pages.dev.job.configOptions'})}
          layout={'horizontal'}
          style={{
            width: 1000,
          }}
        >
          <JobClusterConfigOptions/>
        </StepsForm.StepForm>
      </StepsForm>
    </ProCard>
  );
};

export default JobConfigJarOptions;
