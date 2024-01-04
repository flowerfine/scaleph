import React from 'react';
import {PageContainer} from '@ant-design/pro-components';
import {useLocation} from '@umijs/max';
import {
    Background,
    Clipboard,
    Control,
    Grid,
    History,
    Minimap,
    Snapline,
    Transform,
    XFlow,
    XFlowGraph
} from "@antv/xflow";
import styles from './index.less';
import {Toolbar} from "@/pages/Xflow/dag/toolbar";
import {DAG_CONNECTOR, DAG_EDGE} from "@/pages/Xflow/dag/shape";
import {InitShape} from "@/pages/Xflow/dag/node";
import {Connect} from "@/pages/Xflow/dag/connect";
import {Dnd} from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/dnd/dnd";
import {WsDiJob} from "@/services/project/typings";

interface DiJobFlowPorps {
    data: WsDiJob;
    meta: { flowId?: string; origin?: WsDiJob };
}

const DiJobFlow: React.FC = () => {
    const props = useLocation().state as DiJobFlowPorps;

    return (
        <PageContainer title={false}>
            <XFlow>
                <div className={styles.page}>
                    <div className={styles.container}>
                        <div className={styles.left}>
                            <Dnd data={props.data}/>
                        </div>
                        <div className={styles.center}>
                            <div className={styles.toolbar}>
                                <Toolbar/>
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
                                        animated: true,
                                        zIndex: -1,
                                    }}
                                />
                                <InitShape/>
                                <Connect/>

                                <Grid type="mesh" options={{ color: '#ccc', thickness: 1 }} />
                                <Snapline/>
                                <Clipboard/>
                                <History/>
                                <Background/>
                                <div className={styles.scaleToolbar}>
                                    <Control
                                        items={['zoomOut', 'zoomTo', 'zoomIn', 'zoomToFit', 'zoomToOrigin']}
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

export default DiJobFlow;
