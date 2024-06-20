import React from 'react';
import {PageContainer} from "@ant-design/pro-components";
import {useAccess, useIntl, useLocation} from '@umijs/max';
import {WsArtifactFlinkCDC} from "@/services/project/typings";
import {WORKSPACE_CONF} from "@/constants/constant";

const DataIntegrationFlinkCDCUpdateSteps: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();
  const data = useLocation().state as WsArtifactFlinkCDC;
  const projectId = localStorage.getItem(WORKSPACE_CONF.projectId);

  return (
    <PageContainer title={false}>
      开发中
    </PageContainer>
  );
};

export default DataIntegrationFlinkCDCUpdateSteps;
