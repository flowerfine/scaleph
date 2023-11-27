import {useIntl} from "umi";
import {ProCard, ProFormDigit, ProFormGroup, ProFormText} from "@ant-design/pro-components";

const DorisFeComponent: React.FC = () => {
  const intl = useIntl();

  return (<ProCard
    title={intl.formatMessage({id: 'pages.project.doris.template.steps.component.cn'})}
    headerBordered
    collapsible={true}
    defaultCollapsed>
    <ProFormGroup>
      <ProFormDigit
        name="cn.replicas"
        label={intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.replicas'})}
        colProps={{span: 10, offset: 1}}
        initialValue={3}
        fieldProps={{
          min: 1
        }}
      />
      <ProFormText
        name="cn.image"
        label={intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.image'})}
        colProps={{span: 10, offset: 1}}
        initialValue={"selectdb/doris.be-ubuntu:2.0.2"}
      />
      <ProFormDigit
        name="cn.requests.cpu"
        label={intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.requests.cpu'})}
        colProps={{span: 10, offset: 1}}
        initialValue={8}
        fieldProps={{
          min: 1
        }}
      />
      <ProFormText
        name="cn.requests.memory"
        label={intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.requests.memory'})}
        colProps={{span: 10, offset: 1}}
        initialValue={"16Gi"}
      />
      <ProFormDigit
        name="cn.limits.cpu"
        label={intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.limits.cpu'})}
        colProps={{span: 10, offset: 1}}
        initialValue={16}
        fieldProps={{
          min: 1
        }}
      />
      <ProFormText
        name="cn.limits.memory"
        label={intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.limits.memory'})}
        colProps={{span: 10, offset: 1}}
        initialValue={"32Gi"}
      />
    </ProFormGroup>
  </ProCard>);
}

export default DorisFeComponent;
