import React from "react";
import {ProCard, ProFormDigit, ProFormSelect, ProFormText, ProFormTextArea} from "@ant-design/pro-components";
import {useIntl} from "umi";
import {DICT_TYPE} from "@/constants/dictType";
import {DictDataService} from "@/services/admin/dictData.service";
import {InfoCircleOutlined} from "@ant-design/icons";

const FlinkKubernetesTemplateBase: React.FC = () => {
  const intl = useIntl();

  return (
    <ProCard>
      <ProFormDigit name={"id"} hidden/>
      <ProFormText
        name={"name"}
        label={intl.formatMessage({id: 'pages.project.flink.kubernetes.template.name'})}
        rules={[{required: true}]}
      />
      <ProFormSelect
        name={"deploymentKind"}
        label={intl.formatMessage({id: 'pages.project.flink.kubernetes.template.deploymentKind'})}
        rules={[{required: true}]}
        request={() => DictDataService.listDictDataByType2(DICT_TYPE.deploymentKind)}
      />
      <ProFormText
        name={"namespace"}
        label={intl.formatMessage({id: 'pages.project.flink.kubernetes.template.namespace'})}
        tooltip={{
          title: intl.formatMessage({id: 'pages.project.flink.kubernetes.template.namespace.tooltip'}),
          icon: <InfoCircleOutlined/>,
        }}
        rules={[{required: true}]}
      />
      <ProFormTextArea
        name={"remark"}
        label={intl.formatMessage({id: 'app.common.data.remark'})}
      />
    </ProCard>
  );
}

export default FlinkKubernetesTemplateBase;
