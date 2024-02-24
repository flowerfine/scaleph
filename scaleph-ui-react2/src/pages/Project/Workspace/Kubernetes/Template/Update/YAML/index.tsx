import React from "react";
import {Col, Row} from "antd";
import {ProCard} from "@ant-design/pro-components";
import {Props} from '@/app.d';
import TemplateEditor from "@/pages/Project/Workspace/Kubernetes/Template/Update/YAML/TemplateEditor";
import DefaultTemplateEditor from "@/pages/Project/Workspace/Kubernetes/Template/Update/YAML/DefaultTemplateEditor";
import {WsFlinkKubernetesTemplate} from "@/services/project/typings";

const FlinkKubernetesDeploymentTemplateYAML: React.FC<Props<WsFlinkKubernetesTemplate>> = ({data}) => {
  return (
    <div>
      <Row>
        <Col span={12}>
          <ProCard title={"Template"}>
            <TemplateEditor data={data}/>
          </ProCard>
        </Col>
        <Col span={12}>
          <ProCard title={"Template & Defaults"}>
            <DefaultTemplateEditor/>
          </ProCard>
        </Col>
      </Row>
    </div>
  );
}

export default FlinkKubernetesDeploymentTemplateYAML;
