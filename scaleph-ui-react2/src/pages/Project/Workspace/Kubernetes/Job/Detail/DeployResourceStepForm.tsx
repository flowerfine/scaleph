import React from "react";
import {ProFormDigit, ProFormGroup, ProFormText, ProFormTextArea} from "@ant-design/pro-components";
import {useIntl} from "@umijs/max";

const FlinkKubernetesJobDeployResourceStepForm: React.FC = () => {
  const intl = useIntl();

  return (
    <ProFormGroup>
      <ProFormDigit
        name="jobManagerCpu"
        label={intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.deploy.resource.jobManagerCpu'})}
        colProps={{span: 10, offset: 1}}
        rules={[{required: true}]}
        initialValue={1.0}
        fieldProps={{
          min: 0,
          precision: 2
        }}
      />
      <ProFormDigit
        name="taskManagerCpu"
        label={intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.deploy.resource.taskManagerCpu'})}
        colProps={{span: 10, offset: 1}}
        rules={[{required: true}]}
        initialValue={1.0}
        fieldProps={{
          min: 0,
          precision: 2
        }}
      />
      <ProFormText
        name="jobManagerMemory"
        label={intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.deploy.resource.jobManagerMemory'})}
        colProps={{span: 10, offset: 1}}
        rules={[{required: true}]}
        initialValue={"1G"}
      />
      <ProFormText
        name="taskManagerMemory"
        label={intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.deploy.resource.taskManagerMemory'})}
        colProps={{span: 10, offset: 1}}
        rules={[{required: true}]}
        initialValue={"1G"}
      />
      <ProFormDigit
        name="jobManagerReplicas"
        label={intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.deploy.resource.jobManagerReplicas'})}
        colProps={{span: 10, offset: 1}}
        initialValue={1}
        fieldProps={{
          min: 1
        }}
      />
      <ProFormDigit
        name="taskManagerReplicas"
        label={intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.deploy.resource.taskManagerReplicas'})}
        colProps={{span: 10, offset: 1}}
        initialValue={1}
        fieldProps={{
          min: 1
        }}
      />
      <ProFormDigit
        name="parallelism"
        label={intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.deploy.resource.parallelism'})}
        colProps={{span: 10, offset: 1}}
        rules={[{required: true}]}
        initialValue={1}
        fieldProps={{
          min: 1
        }}
      />
      <ProFormTextArea
        name="userFlinkConfiguration"
        label={intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.deploy.resource.userFlinkConfiguration'})}
        colProps={{span: 23, offset: 1}}
      />
    </ProFormGroup>
  );
}

export default FlinkKubernetesJobDeployResourceStepForm;
