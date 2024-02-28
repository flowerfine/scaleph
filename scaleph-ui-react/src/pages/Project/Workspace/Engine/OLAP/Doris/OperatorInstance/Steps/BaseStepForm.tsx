import React from "react";
import {InfoCircleOutlined} from "@ant-design/icons";
import {ProCard, ProFormDigit, ProFormSelect, ProFormText, ProFormTextArea} from "@ant-design/pro-components";
import {useIntl} from "@umijs/max";
import {WORKSPACE_CONF} from "@/constants/constant";
import {ClusterCredentialListParam} from "@/services/resource/typings";
import {ClusterCredentialService} from "@/services/resource/clusterCredential.service";
import {WsDorisOperatorTemplate} from "@/services/project/typings";
import {WsDorisOperatorTemplateService} from "@/services/project/WsDorisOperatorTemplateService";

const DorisInstanceBase: React.FC = () => {
  const intl = useIntl();
  const localProjectId = localStorage.getItem(WORKSPACE_CONF.projectId);

  return (
    <ProCard>
      <ProFormDigit name={"id"} hidden/>
      <ProFormText
        name={"name"}
        label={intl.formatMessage({id: 'pages.project.doris.instance.name'})}
        rules={[{required: true}]}
      />
      <ProFormSelect
        name="clusterCredentialId"
        label={intl.formatMessage({id: 'pages.project.doris.instance.steps.base.cluster'})}
        rules={[{required: true}]}
        tooltip={{
          title: intl.formatMessage({id: 'pages.project.doris.instance.steps.base.cluster.tooltip'}),
          icon: <InfoCircleOutlined/>,
        }}
        allowClear={false}
        request={((params, props) => {
          const param: ClusterCredentialListParam = {
            name: params.keyWords,
          };
          return ClusterCredentialService.list(param).then((response) => {
            return response.data.map((item) => {
              return {label: item.name, value: item.id, item: item}
            })
          })
        })}
      />
      <ProFormText
        name={"namespace"}
        label={intl.formatMessage({id: 'pages.project.doris.instance.namespace'})}
        rules={[{required: true}]}
        tooltip={{
          title: intl.formatMessage({id: 'pages.project.doris.instance.namespace.tooltip'}),
          icon: <InfoCircleOutlined/>,
        }}
        initialValue={"doris"}
      />
      <ProFormSelect
        name="templateId"
        label={intl.formatMessage({id: 'pages.project.doris.instance.steps.base.template'})}
        rules={[{required: true}]}
        allowClear={false}
        request={((params, props) => {
          const param: WsDorisOperatorTemplate = {
            projectId: localProjectId,
            name: params.keyWords,
          };
          return WsDorisOperatorTemplateService.list(param).then((response) => {
            return response.data.map((item) => {
              return {label: item.name, value: item.id, item: item}
            })
          })
        })}
      />
      <ProFormTextArea
        name={"remark"}
        label={intl.formatMessage({id: 'app.common.data.remark'})}
      />
    </ProCard>
  );
}

export default DorisInstanceBase;
