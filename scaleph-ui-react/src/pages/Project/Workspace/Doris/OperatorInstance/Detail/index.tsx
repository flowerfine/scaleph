import {useIntl, useLocation} from "umi";
import React from "react";
import {WsDorisOperatorInstance} from "@/services/project/typings";
import {PageContainer} from "@ant-design/pro-components";
import {Divider} from "antd";
import DorisInstanceDetailComponent
  from "@/pages/Project/Workspace/Doris/OperatorInstance/Detail/DorisInstanceComponent";
import DorisInstanceDetailYAML from "@/pages/Project/Workspace/Doris/OperatorInstance/Detail/DorisInstanceYaml";
import DorisInstanceDetailAction from "@/pages/Project/Workspace/Doris/OperatorInstance/Detail/DorisInstanceAction";
import DorisInstanceDetailAccess from "@/pages/Project/Workspace/Doris/OperatorInstance/Detail/DorisInstanceAccess";

const DorisInstanceDetailWeb: React.FC = () => {
  const intl = useIntl();
  const data = useLocation().state as WsDorisOperatorInstance

  return (
    <PageContainer title={intl.formatMessage({id: 'pages.project.doris.instance.detail'})}>
      <DorisInstanceDetailAction data={data}/>
      <Divider type={'horizontal'}/>
      <DorisInstanceDetailComponent data={data}/>
      <Divider type={'horizontal'}/>
      <DorisInstanceDetailAccess data={data}/>
      <Divider type={'horizontal'}/>
      <DorisInstanceDetailYAML data={data}/>
    </PageContainer>
  );
}

export default DorisInstanceDetailWeb;
