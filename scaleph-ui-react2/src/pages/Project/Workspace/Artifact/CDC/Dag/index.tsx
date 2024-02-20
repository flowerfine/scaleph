import React from 'react';
import {PageContainer} from "@ant-design/pro-components";
import {useAccess, useIntl, useLocation} from '@umijs/max';
import {WORKSPACE_CONF} from '@/constants/constant';
import {WsArtifactFlinkCDC} from "@/services/project/typings";

const FlinkArtifactCDCDagWeb: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();
  const data = useLocation().state as WsArtifactFlinkCDC;
  const projectId = localStorage.getItem(WORKSPACE_CONF.projectId);

  return (
    <PageContainer title={false}>
      Flink CDC Dag Web: {data.wsFlinkArtifact?.name}
    </PageContainer>
  );
};

export default FlinkArtifactCDCDagWeb;
