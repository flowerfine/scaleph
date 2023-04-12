import React, {useState} from 'react';
import {useLocation} from 'umi';
import {PageContainer} from '@ant-design/pro-components';
import {
  CompressOutlined,
  DeleteOutlined,
  EditOutlined,
  EyeOutlined,
  FullscreenExitOutlined,
  FullscreenOutlined,
  FundProjectionScreenOutlined,
  InfoCircleOutlined,
  PlaySquareOutlined,
  ProfileOutlined,
  SaveOutlined,
  SendOutlined,
  StopOutlined,
  ZoomInOutlined,
  ZoomOutOutlined,
} from '@ant-design/icons';
import {
  CanvasContextMenu,
  CanvasMiniMap,
  CanvasNodePortTooltip,
  CanvasScaleToolbar,
  CanvasSnapline,
  CanvasToolbar,
  IApplication,
  IAppLoad,
  IconStore,
  KeyBindings,
  NodeCollapsePanel,
  NsGraph,
  XFlow,
  XFlowCanvas,
} from '@antv/xflow';
/** config graph */
import {useGraphConfig, useGraphHookConfig} from './Dag/config-graph';
/** config command */
import {initGraphCmds, useCmdConfig} from './Dag/config-cmd';
/** config key bind */
import {useKeybindingConfig} from './Dag/config-keybinding';
/** config menu */
import {useMenuConfig} from './Dag/config-menu';
/** config toolbar */
import {useScaleToolbarConfig, useToolbarConfig} from './Dag/config-toolbar';
/** config dnd panel */
import '@antv/xflow/dist/index.css';
import './index.less';
import * as dndPanelConfig from './Dag/config-dnd-panel';
import {WsDiJob} from '@/services/project/typings';
import {DagService} from './Dag/service';

interface DiJobFlowPorps {
  data: WsDiJob;
  meta: { flowId?: string; origin?: WsDiJob };
}

const DiJobFlow: React.FC<DiJobFlowPorps> = () => {
  const props = useLocation().state as DiJobFlowPorps;
  const graphConfig = useGraphConfig(props);
  const graphHookConfig = useGraphHookConfig(props);
  const commandConfig = useCmdConfig();
  const toolbarConfig = useToolbarConfig();
  const scaleToolbarConfig = useScaleToolbarConfig();
  const menuConfig = useMenuConfig();
  const keybindingConfig = useKeybindingConfig();
  const [graphData, setGraphData] = useState<NsGraph.IGraphData>({nodes: [], edges: []});
  const {data, meta} = props;

  /**register icons */
  IconStore.set('DeleteOutlined', DeleteOutlined);
  IconStore.set('EditOutlined', EditOutlined);
  IconStore.set('PlaySquareOutlined', PlaySquareOutlined);
  IconStore.set('StopOutlined', StopOutlined);
  IconStore.set('SaveOutlined', SaveOutlined);
  IconStore.set('SendOutlined', SendOutlined);
  IconStore.set('EyeOutlined', EyeOutlined);
  IconStore.set('FundProjectionScreenOutlined', FundProjectionScreenOutlined);
  IconStore.set('ProfileOutlined', ProfileOutlined);
  IconStore.set('FullscreenOutlined', FullscreenOutlined);
  IconStore.set('FullscreenExitOutlined', FullscreenExitOutlined);
  IconStore.set('ZoomInOutlined', ZoomInOutlined);
  IconStore.set('ZoomOutOutlined', ZoomOutOutlined);
  IconStore.set('CompressOutlined', CompressOutlined);
  IconStore.set('InfoCircleOutlined', InfoCircleOutlined);

  const cache = React.useMemo<{ app: IApplication | null }>(
    () => ({
      app: null,
    }),
    [],
  );
  const onLoad: IAppLoad = async (app) => {
    cache.app = app;
    initGraphCmds(cache.app, meta.origin || {id: data.id});
  };

  React.useEffect(() => {
    if (cache.app) {
      initGraphCmds(cache.app, meta.origin || {id: data.id});
    }
    refreshJobGraph();
  }, [meta]);

  const refreshJobGraph = async () => {
    DagService.loadJobInfo(meta.origin?.id as number).then((resp) => {
      setGraphData(resp);
    });
  };

  return (
    <PageContainer title={false}>
      <XFlow
        className="dag-user-custom-clz"
        hookConfig={graphHookConfig}
        commandConfig={commandConfig}
        onLoad={onLoad}
        graphData={graphData}
        meta={meta}
      >
        <NodeCollapsePanel
          className="xflow-node-panel"
          position={{width: 240, top: 0, bottom: 0, left: 0}}
          bodyPosition={{top: 40, bottom: 0, left: 0}}
          footerPosition={{height: 0}}
          nodeDataService={dndPanelConfig.nodeDataService}
          onNodeDrop={dndPanelConfig.onNodeDrop}
          searchService={dndPanelConfig.searchService}
        />
        <CanvasToolbar
          className="xflow-workspace-toolbar-top"
          layout="horizontal"
          config={toolbarConfig}
          position={{top: 0, left: 240, right: 0, bottom: 0}}
        />
        <XFlowCanvas config={graphConfig} position={{top: 40, left: 240, right: 0, bottom: 0}}>
          <CanvasScaleToolbar layout="vertical" position={{top: 12, right: 12}}/>
          <CanvasContextMenu config={menuConfig}/>
          <CanvasSnapline color="#faad14"/>
          <CanvasMiniMap minimapOptions={{width: 200, height: 120}}/>
          <CanvasNodePortTooltip/>
        </XFlowCanvas>
        <KeyBindings config={keybindingConfig}/>
      </XFlow>
    </PageContainer>
  );
};

export default DiJobFlow;
