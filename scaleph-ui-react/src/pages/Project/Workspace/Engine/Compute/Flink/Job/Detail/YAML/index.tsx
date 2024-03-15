import React from "react";
import {Divider} from "antd";
import {ProCard} from "@ant-design/pro-components";
import {connect, useIntl} from "@umijs/max";
import {Props} from '@/typings';
import {WsFlinkKubernetesJob} from "@/services/project/typings";
import FlinkKubernetesJobDetailDeployYAMLWeb
  from "@/pages/Project/Workspace/Engine/Compute/Flink/Job/Detail/YAML/FlinkKubernetesJobDetailDeployYaml";
import FlinkKubernetesJobDetailStatusYAMLWeb
  from "@/pages/Project/Workspace/Engine/Compute/Flink/Job/Detail/YAML/FlinkKubernetesJobDetailStatusYaml";

const FlinkKubernetesJobDetailYAMLWeb: React.FC<Props<WsFlinkKubernetesJob>> = (props: any) => {
  const intl = useIntl();

  return (
    <ProCard.Group title={false} direction={'row'}>
      <ProCard title={intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.steps.yaml.instance'})}>
        <FlinkKubernetesJobDetailDeployYAMLWeb/>
      </ProCard>
      <Divider type={'vertical'}/>
      <ProCard title={intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.steps.yaml.status'})}>
        <FlinkKubernetesJobDetailStatusYAMLWeb/>
      </ProCard>
    </ProCard.Group>
  );
}


const mapModelToProps = ({flinkKubernetesJobDetail}: any) => ({flinkKubernetesJobDetail})
export default connect(mapModelToProps)(FlinkKubernetesJobDetailYAMLWeb);
