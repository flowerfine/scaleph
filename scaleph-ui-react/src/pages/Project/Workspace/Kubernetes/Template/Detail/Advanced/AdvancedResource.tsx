import {ProCard, ProFormDigit, ProFormGroup, ProFormText} from "@ant-design/pro-components";

const AdvancedBasic: React.FC = () => {
  return (<ProCard
    title={'Resource'}
    headerBordered
    collapsible={true}
    defaultCollapsed>
    <ProFormGroup>
      <ProFormDigit
        name="spec.jobManager.resource.cpu"
        label={'JobManager CPU'}
        colProps={{span: 10, offset: 1}}
        initialValue={1.0}
        fieldProps={{
          min: 0,
          precision: 2
        }}
      />
      <ProFormDigit
        name="spec.taskManager.resource.cpu"
        label={'TaskManager CPU'}
        colProps={{span: 10, offset: 1}}
        initialValue={1.0}
        fieldProps={{
          min: 0,
          precision: 2
        }}
      />
      <ProFormText
        name="spec.jobManager.resource.memory"
        label={'JobManager Memory'}
        colProps={{span: 10, offset: 1}}
        initialValue={"1G"}
      />
      <ProFormText
        name="spec.taskManager.resource.memory"
        label={'TaskManager Memory'}
        colProps={{span: 10, offset: 1}}
        initialValue={"1G"}
      />
      <ProFormDigit
        name="spec.jobManager.replicas"
        label={'JobManager Replicas'}
        colProps={{span: 10, offset: 1}}
        initialValue={1}
        fieldProps={{
          min: 1
        }}
      />
      <ProFormDigit
        name="spec.taskManager.replicas"
        label={'TaskManager Replicas'}
        colProps={{span: 10, offset: 1}}
        initialValue={1}
        fieldProps={{
          min: 1
        }}
      />
    </ProFormGroup>
  </ProCard>);
}

export default AdvancedBasic;
