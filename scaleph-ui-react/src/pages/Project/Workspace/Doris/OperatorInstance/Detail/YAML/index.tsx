import React from "react";
import {ProCard} from "@ant-design/pro-components";
import DorisInstanceDetailYAMLInstance
  from "@/pages/Project/Workspace/Doris/OperatorInstance/Detail/YAML/DorisInstanceYaml";
import DorisInstanceDetailYAMLStatus
  from "@/pages/Project/Workspace/Doris/OperatorInstance/Detail/YAML/DorisInstanceStatusYaml";
import {useIntl} from "umi";
import {Divider} from "antd";

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
