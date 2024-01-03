import React from "react";
import {Divider} from "antd";
import {ProCard} from "@ant-design/pro-components";
import {useIntl} from "@umijs/max";
import DorisInstanceDetailYAMLInstance
  from "@/pages/Project/Workspace/Doris/OperatorInstance/Detail/YAML/DorisInstanceYaml";
import DorisInstanceDetailYAMLStatus
  from "@/pages/Project/Workspace/Doris/OperatorInstance/Detail/YAML/DorisInstanceStatusYaml";

const DorisInstanceDetailYAML: React.FC = () => {
  const intl = useIntl();

  return (
    <ProCard.Group title={intl.formatMessage({id: 'pages.project.doris.instance.detail.yaml'})}
                   direction={'row'}>
      <ProCard title={intl.formatMessage({id: 'pages.project.doris.instance.detail.yaml.instance'})}>
        <DorisInstanceDetailYAMLInstance/>
      </ProCard>
      <Divider type={'vertical'}/>
      <ProCard title={intl.formatMessage({id: 'pages.project.doris.instance.detail.yaml.status'})}>
        <DorisInstanceDetailYAMLStatus/>
      </ProCard>
    </ProCard.Group>
  );
}

export default DorisInstanceDetailYAML;
