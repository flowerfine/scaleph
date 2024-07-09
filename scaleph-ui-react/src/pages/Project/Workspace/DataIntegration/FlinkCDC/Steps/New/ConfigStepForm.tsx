import React from "react";
import {ProCard} from "@ant-design/pro-components";
import DataIntegrationFlinkCDCStepConfigDataSource
  from "@/pages/Project/Workspace/DataIntegration/FlinkCDC/Steps/New/Config/ConfigStepDataSource";
import DataIntegrationFlinkCDCStepConfigRoute
  from "@/pages/Project/Workspace/DataIntegration/FlinkCDC/Steps/New/Config/ConfigStepRoute";
import DataIntegrationFlinkCDCStepConfigTransform
  from "@/pages/Project/Workspace/DataIntegration/FlinkCDC/Steps/New/Config/ConfigStepTransform";

const DataIntegrationFlinkCDCStepConfig: React.FC = () => {

  return (
    <ProCard.Group direction={"column"}>
      <DataIntegrationFlinkCDCStepConfigDataSource/>
      <DataIntegrationFlinkCDCStepConfigRoute/>
      <DataIntegrationFlinkCDCStepConfigTransform/>
    </ProCard.Group>
  );
}

export default DataIntegrationFlinkCDCStepConfig;
