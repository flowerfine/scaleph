import {useIntl, useLocation} from "umi";
import React from "react";
import {Tabs} from "antd";
import {WsFlinkKubernetesJob} from "@/services/project/typings";
import FlinkKubernetesJobDetailYAMLWeb from "@/pages/Project/Workspace/Kubernetes/Job/Detail/YAML";

const FlinkKubernetesJobDetailWeb: React.FC = () => {
  const intl = useIntl();
  const data = useLocation().state as WsFlinkKubernetesJob

  const items = [
    {
      label: intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.yaml'}),
      key: 'yaml',
      children: <FlinkKubernetesJobDetailYAMLWeb data={data}/>
    },
  ]
  return (
    <Tabs items={items}/>
  );
}

export default FlinkKubernetesJobDetailWeb;
