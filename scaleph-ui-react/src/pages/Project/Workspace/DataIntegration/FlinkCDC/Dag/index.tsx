import React from 'react';
import {PageContainer} from "@ant-design/pro-components";
import {useAccess, useIntl, useLocation} from '@umijs/max';
import {WORKSPACE_CONF} from '@/constants/constant';
import {WsArtifactFlinkCDC} from "@/services/project/typings";
import styles from "@/pages/Project/Workspace/DataIntegration/SeaTunnel/Dag/index.less";
import {CustomToolbar} from "@/pages/Project/Workspace/DataIntegration/SeaTunnel/Dag/components/toolbar";
import {
  Background,
  Clipboard,
  Control,
  ControlEnum,
  Grid,
  History,
  Minimap,
  Snapline,
  XFlow,
  XFlowGraph
} from "@antv/xflow";
import {
  DAG_CONNECTOR,
  DAG_EDGE
} from "@/pages/Project/Workspace/DataIntegration/SeaTunnel/Dag/components/node/canvas-node";
import {Dnd} from "@/pages/Project/Workspace/DataIntegration/FlinkCDC/Dag/components/dnd/dnd";
import {CustomMenubar} from "@/pages/Project/Workspace/DataIntegration/FlinkCDC/Dag/components/menubar";

const DataIntegrationFlinkCDCDagWeb: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();
  const data = useLocation().state as WsArtifactFlinkCDC;
  const projectId = localStorage.getItem(WORKSPACE_CONF.projectId);

  return (
    <PageContainer title={false}>
      <XFlow>
        <div className={styles.page}>
          <div className={styles.container}>
            <div className={styles.left}>
              <Dnd data={data}/>
            </div>
            <div className={styles.center}>
              <div className={styles.toolbar}>
                <CustomToolbar/>
                <CustomMenubar data={data}/>
              </div>

              <div className={styles.graph}>
                <XFlowGraph
                  pannable
                  connectionOptions={{
                    snap: true,
                    allowBlank: false,
                    allowLoop: false,
                    highlight: true,
                    connectionPoint: 'anchor',
                    anchor: 'center',
                    connector: DAG_CONNECTOR,
                    validateMagnet({magnet}) {
                      return magnet.getAttribute('port-group') !== 'top';
                    },
                  }}
                  connectionEdgeOptions={{
                    shape: DAG_EDGE,
                    animated: false,
                    zIndex: -1,
                  }}
                />
                {/*<InitShape data={data}/>*/}

                <Grid type="mesh" options={{color: '#ccc', thickness: 1}}/>
                <Snapline/>
                <Clipboard/>
                <History/>
                <Background/>
                <div className={styles.scaleToolbar}>
                  {/* 颜色样式不对，实际上是有数字的 */}
                  <Control
                    items={[ControlEnum.ZoomToOrigin, ControlEnum.ZoomToFit, ControlEnum.ZoomIn, ControlEnum.ZoomTo, ControlEnum.ZoomOut]}
                    direction={'vertical'}
                  />
                </div>
                <div className={styles.minimap}>
                  <Minimap/>
                </div>
              </div>
            </div>
          </div>
        </div>
      </XFlow>
    </PageContainer>
  );
};

export default DataIntegrationFlinkCDCDagWeb;
