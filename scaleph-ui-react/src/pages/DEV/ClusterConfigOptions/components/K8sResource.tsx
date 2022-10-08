import {ProCard, ProFormDigit, ProFormGroup, ProFormText} from "@ant-design/pro-components";
import {useIntl} from "umi";

const K8sResourceOptions: React.FC = () => {
  const intl = useIntl();

  return (
    <ProCard title={'Kubernetes Resource'} headerBordered collapsible={true}>
      <ProFormGroup>
        <ProFormText
          name="namespace"
          label={(intl.formatMessage({id: 'pages.dev.clusterConfig.namespace'}))}
          colProps={{span: 10, offset: 1}}
          rules={[{required: true}]}
          fieldProps={{
            defaultValue: "default"
          }}
        />
        <ProFormDigit
          name="jobManagerCPU"
          label={(intl.formatMessage({id: 'pages.dev.clusterConfig.jobManagerCPU'}))}
          colProps={{span: 10, offset: 1}}
          rules={[{required: true}]}
          fieldProps={{
            min: 0.1,
            precision: 1
          }}
        />
        <ProFormDigit
          name="taskManagerCPU"
          label={(intl.formatMessage({id: 'pages.dev.clusterConfig.taskManagerCPU'}))}
          colProps={{span: 10, offset: 1}}
          rules={[{required: true}]}
          fieldProps={{
            min: 0.1,
            precision: 1
          }}
        />
        <ProFormText
          name="jobManagerMemory"
          label={(intl.formatMessage({id: 'pages.dev.clusterConfig.jobManagerMemory'}))}
          colProps={{span: 10, offset: 1}}
          rules={[{required: true}]}/>
        <ProFormText
          name="taskManagerMemory"
          label={(intl.formatMessage({id: 'pages.dev.clusterConfig.taskManagerMemory'}))}
          colProps={{span: 10, offset: 1}}
          rules={[{required: true}, {max: 128}]}/>
        <ProFormDigit
          name="jobManagerReplicas"
          label={(intl.formatMessage({id: 'pages.dev.clusterConfig.jobManagerReplicas'}))}
          colProps={{span: 10, offset: 1}}
          rules={[{required: true}]}
          min={1}
        />
        <ProFormDigit
          name="taskManagerReplicas"
          label={(intl.formatMessage({id: 'pages.dev.clusterConfig.taskManagerReplicas'}))}
          colProps={{span: 10, offset: 1}}
          rules={[{required: true}]}
          min={1}
        />
      </ProFormGroup>
    </ProCard>
  );

}

export default K8sResourceOptions;
