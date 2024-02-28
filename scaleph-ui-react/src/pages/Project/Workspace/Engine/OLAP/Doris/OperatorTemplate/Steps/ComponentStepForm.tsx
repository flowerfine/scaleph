import React from "react";
import {ProCard} from "@ant-design/pro-components";
import DorisFeComponent from "./Component/DorisFeComponent";
import DorisBeComponent from "./Component/DorisBeComponent";
import DorisCnComponent from "./Component/DorisCnComponent";

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
