import {ProCard, ProFormGroup, ProFormText} from "@ant-design/pro-components";

const K8sResourceOptions: React.FC = () => {
  return (
    <ProCard title={'Kubernetes Resource'} headerBordered collapsible={true}>
      <ProFormGroup>
        <ProFormText
          name="namespace"
          label={"namespace"}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}, {max: 128}]}/>
        <ProFormText
          name="jobmanager.cpu"
          label={"jobmanager.cpu"}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}, {max: 128}]}/>

        <ProFormText
          name="jobmanager.memory"
          label={"jobmanager.memory"}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}, {max: 128}]}/>

        <ProFormText
          name="jobmanager.replicas"
          label={"jobmanager.replicas"}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}, {max: 128}]}/>
        <ProFormText
          name="taskmanager.cpu"
          label={"taskmanager.cpu"}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}, {max: 128}]}/>
        <ProFormText
          name="taskmanager.memory"
          label={"taskmanager.memory"}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}, {max: 128}]}/>
        <ProFormText
          name="taskmanager.replicas"
          label={"taskmanager.replicas"}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}, {max: 128}]}/>

        <ProFormText
          name="taskmanager.Parallelism"
          label={"taskmanager.Parallelism"}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}, {max: 128}]}/>
      </ProFormGroup>
    </ProCard>
  );

}

export default K8sResourceOptions;
