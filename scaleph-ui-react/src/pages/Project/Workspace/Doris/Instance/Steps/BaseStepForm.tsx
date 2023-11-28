import React from "react";
import {ProCard, ProFormDigit, ProFormSelect, ProFormText} from "@ant-design/pro-components";
import {useIntl} from "umi";
import {ClusterCredentialListParam} from "@/services/resource/typings";
import {ClusterCredentialService} from "@/services/resource/clusterCredential.service";
import {WsDorisTemplate} from "@/services/project/typings";
import {WsDorisTemplateService} from "@/services/project/WsDorisTemplateService";
import {WORKSPACE_CONF} from "@/constant";

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
      />
      <ProFormSelect
        name="templateId"
        label={intl.formatMessage({id: 'pages.project.doris.instance.steps.base.template'})}
        rules={[{required: true}]}
        allowClear={false}
        request={((params, props) => {
          const param: WsDorisTemplate = {
            projectId: localProjectId,
            name: params.keyWords,
          };
          return WsDorisTemplateService.list(param).then((response) => {
            return response.data.map((item) => {
              return {label: item.name, value: item.id, item: item}
            })
          })
        })}
      />
      <ProFormText
        name={"remark"}
        label={intl.formatMessage({id: 'app.common.data.remark'})}
      />
    </ProCard>
  );
}

export default DorisInstanceBase;
