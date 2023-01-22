import {useIntl} from "umi";
import React from "react";
import {Col, Row} from "antd";
import {ProCard} from "@ant-design/pro-components";
import TemplateEditor from "@/pages/Project/Workspace/Kubernetes/Template/Detail/YAML/TemplateEditor";
import DefaultTemplateEditor from "@/pages/Project/Workspace/Kubernetes/Template/Detail/YAML/DefaultTemplateEditor";

const DeploymentTemplateYAML: React.FC = () => {
  const intl = useIntl();

  return (
    <div>
      <Row>
        <Col span={12}>
          <ProCard title={"Template"}>
            <TemplateEditor/>
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

export default DeploymentTemplateYAML;
