import React from "react";
import {ProCard} from "@ant-design/pro-components";
import DorisFeComponent from "@/pages/Project/Workspace/Doris/OperatorTemplate/Steps/Component/DorisFeComponent";
import DorisBeComponent from "@/pages/Project/Workspace/Doris/OperatorTemplate/Steps/Component/DorisBeComponent";
import DorisCnComponent from "@/pages/Project/Workspace/Doris/OperatorTemplate/Steps/Component/DorisCnComponent";

const DorisTemplateComponent: React.FC = () => {
  return (
    <ProCard>
      <DorisFeComponent/>
      <DorisBeComponent/>
      <DorisCnComponent/>
    </ProCard>
  );
}

export default DorisTemplateComponent;
