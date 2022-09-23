import JobClusterConfigOptions from '@/pages/DEV/JobConfig/components/ClusterConfigOptions';
import JobBase from '@/pages/DEV/JobConfig/components/JobBase';
import JobJar from '@/pages/DEV/JobConfig/components/JobJar';
import { FlinkClusterConfigService } from '@/services/dev/flinkClusterConfig.service';
import { FlinkJobConfigJarService } from '@/services/dev/flinkJobConfigJar.service';
import { FlinkJobConfigJar } from '@/services/dev/typings';
import { ProCard, ProFormInstance, StepsForm } from '@ant-design/pro-components';
import { message } from 'antd';
import { useRef } from 'react';
import { history, useIntl, useLocation } from 'umi';

const JobConfigJarOptions: React.FC = () => {
  const urlParams = useLocation();
  const intl = useIntl();
  const formRef = useRef<ProFormInstance>();

  const params = urlParams.state as FlinkJobConfigJar;

  const jobConfig = new Map(Object.entries(params?.jobConfig ? params?.jobConfig : {}));
  const args: Array<any> = [];
  jobConfig.forEach((value: any, key: string) => {
    args.push({ parameter: key, value: value });
  });
  const data = {
    name: params?.name,
    flinkArtifactId: params?.flinkArtifactJar?.flinkArtifact.id,
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
          initialValues: { ...data, ...flinkConfig },
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
          const param: FlinkJobConfigJar = {
            id: params?.id,
            name: values['name'],
            flinkArtifactJar: { id: values['flinkArtifactJarId'] },
            flinkClusterConfig: { id: values['flinkClusterConfig'] },
            flinkClusterInstance: { id: values['flinkClusterInstance'] },
            jobConfig: jobConfig,
            flinkConfig: FlinkClusterConfigService.getData(values),
            remark: values['remark'],
          };
          return params?.id
            ? FlinkJobConfigJarService.update(param)
                .then((d) => {
                  if (d.success) {
                    message.success(intl.formatMessage({ id: 'app.common.operate.new.success' }));
                  }
                })
                .catch(() => {
                  message.error(intl.formatMessage({ id: 'app.common.operate.new.failure' }));
                })
                .finally(() => {
                  history.back();
                })
            : FlinkJobConfigJarService.add(param)
                .then((d) => {
                  if (d.success) {
                    message.success(intl.formatMessage({ id: 'app.common.operate.new.success' }));
                  }
                })
                .catch(() => {
                  message.error(intl.formatMessage({ id: 'app.common.operate.new.failure' }));
                })
                .finally(() => {
                  history.back();
                });
        }}
      >
        <StepsForm.StepForm
          name="base"
          title={intl.formatMessage({ id: 'pages.dev.job.base' })}
          layout={'horizontal'}
          style={{
            width: 1000,
          }}
        >
          <JobBase />
        </StepsForm.StepForm>
        <StepsForm.StepForm
          name="artifact"
          title={intl.formatMessage({ id: 'pages.dev.job.artifact' })}
          layout={'horizontal'}
          style={{
            width: 1000,
          }}
        >
          <JobJar data={params} />
        </StepsForm.StepForm>
        <StepsForm.StepForm
          name="configOptions"
          title={intl.formatMessage({ id: 'pages.dev.job.configOptions' })}
          layout={'horizontal'}
          style={{
            width: 1000,
          }}
        >
          <JobClusterConfigOptions />
        </StepsForm.StepForm>
      </StepsForm>
    </ProCard>
  );
};

export default JobConfigJarOptions;
