import React from "react";
import {Col, Row} from "antd";
import {ProCard} from "@ant-design/pro-components";
import FlinkKubernetesTemplateYAMLStepEditor
  from "@/pages/Project/Workspace/Kubernetes/Template/Steps/New/YAML/TemplateEditor";
import FlinkKubernetesTemplateYAMLStepDefaultEditor
  from "@/pages/Project/Workspace/Kubernetes/Template/Steps/New/YAML/DefaultTemplateEditor";

const FlinkKubernetesTemplateYAMLStep: React.FC = () => {
  return (
    <ProCard>
      <Row>
        <Col span={12}>
          <ProCard title={"Template"}>
            <FlinkKubernetesTemplateYAMLStepEditor/>
          </ProCard>
        </Col>
        <Col span={12}>
          <ProCard title={"Template & Defaults"}>
            <FlinkKubernetesTemplateYAMLStepDefaultEditor/>
          </ProCard>
        </Col>
      </Row>
    </ProCard>
  );
}

export default FlinkKubernetesTemplateYAMLStep;
