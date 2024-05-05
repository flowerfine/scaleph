import React, {useEffect} from 'react';
import {PageContainer} from '@ant-design/pro-components';
import {useLocation} from '@umijs/max';
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
import {WsArtifactSeaTunnel} from "@/services/project/typings";
import {DAG_CONNECTOR, DAG_EDGE} from "./components/node/canvas-node";
import {InitShape} from "./components/node/init-node";
import {Dnd} from "./components/dnd/dnd";
import {CustomMenubar} from "./components/menubar";
import {CustomToolbar} from "./components/toolbar";
import styles from './index.less';

const DataIntegrationSeaTunnelDagWeb: React.FC = () => {
  const data = useLocation().state as WsArtifactSeaTunnel;

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
                <InitShape data={data}/>

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

export default DataIntegrationSeaTunnelDagWeb;
