import {useIntl} from "umi";
import {ProCard, ProFormDigit, ProFormGroup, ProFormSelect, ProFormText} from "@ant-design/pro-components";
import {DictDataService} from "@/services/admin/dictData.service";
import {DICT_TYPE} from "@/constants/dictType";

const DorisFeComponent: React.FC = () => {
  const intl = useIntl();

  return (<ProCard
    title={intl.formatMessage({id: 'pages.project.doris.template.steps.component.fe'})}
    headerBordered
    collapsible={true}>
    <ProFormGroup>
      <ProFormDigit
        name="fe.replicas"
        label={intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.replicas'})}
        colProps={{span: 10, offset: 1}}
        initialValue={3}
        fieldProps={{
          min: 1
        }}
      />
      <ProFormText
        name="fe.image"
        label={intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.image'})}
        colProps={{span: 10, offset: 1}}
        initialValue={"selectdb/doris.fe-ubuntu:2.0.2"}
      />
      <ProFormDigit
        name="fe.requests.cpu"
        label={intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.requests.cpu'})}
        colProps={{span: 10, offset: 1}}
        initialValue={8}
        fieldProps={{
          min: 0,
          precision: 2
        }}
      />
      <ProFormText
        name="fe.requests.memory"
        label={intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.requests.memory'})}
        colProps={{span: 10, offset: 1}}
        initialValue={"16Gi"}
      />
      <ProFormDigit
        name="fe.limits.cpu"
        label={intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.limits.cpu'})}
        colProps={{span: 10, offset: 1}}
        initialValue={16}
        fieldProps={{
          min: 0,
          precision: 2
        }}
      />
      <ProFormText
        name="fe.limits.memory"
        label={intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.limits.memory'})}
        colProps={{span: 10, offset: 1}}
        initialValue={"32Gi"}
      />
      <ProFormSelect
        name="fe.service.type"
        label={intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.service.type'})}
        colProps={{span: 10, offset: 1}}
        request={() => DictDataService.listDictDataByType2(DICT_TYPE.flinkServiceExposedType)}
      />
    </ProFormGroup>
  </ProCard>);
}

export default DorisFeComponent;
