import { IAppLoad, NsGraphCmd, XFlowGraphCommands } from '@antv/xflow';
import React, { useRef, useEffect, useCallback } from 'react';
/** 交互组件 */
import {
  NsGraph,
  /** XFlow核心组件 */
  XFlow,
  /** 流程图画布组件 */
  FlowchartCanvas,
  /** 流程图配置扩展 */
  FlowchartExtension,
  /** 通用组件：快捷键 */
  KeyBindings,
  /** 通用组件：画布缩放 */
  CanvasScaleToolbar,
  /** 通用组件：右键菜单 */
  CanvasContextMenu,
  /** 通用组件：工具栏 */
  CanvasToolbar,
  /** 通用组件：对齐线 */
  CanvasSnapline,
  /** 通用组件：节点连接桩 */
  CanvasNodePortTooltip,
} from '@antv/xflow';
import type { Graph } from '@antv/x6';
/** 配置Command*/
import { useCmdConfig } from './config-cmd';
/** 配置Menu */
import { useMenuConfig } from './config-menu';
/** 配置Toolbar */
import { useToolbarConfig } from './config-toolbar';
/** 配置快捷键 */
import { useKeybindingConfig } from './config-keybinding';
/** 流程图节点组件 */
import CustomNodeCollapsePanel from './CustomNodeCollapsePanel';
/** 配置Dnd组件面板 */
import '@antv/xflow/dist/index.css';
import './index.less';
import { PageContainer } from '@ant-design/pro-layout';
import { useGraphConfig } from './CustomNodeCollapsePanel/config-dnd-panel';

export interface IProps {
  meta: { flowId: string };
}

export const Demo: React.FC<IProps> = (props) => {
  const { meta } = props;
  const toolbarConfig = useToolbarConfig();
  const menuConfig = useMenuConfig();
  const keybindingConfig = useKeybindingConfig();
  const graphRef = useRef<Graph>();
  const commandConfig = useCmdConfig();
  /**
   * @param app 当前XFlow工作空间
   * @param extensionRegistry 当前XFlow配置项
   */

  const onLoad: IAppLoad = async (app) => {
    graphRef.current = await app.getGraphInstance();
    const graphData: NsGraph.IGraphData = JSON.parse(
      localStorage.getItem('graphData')!,
    ) || { nodes: [], edges: [] };
    await app.executeCommand<NsGraphCmd.GraphRender.IArgs>(
      XFlowGraphCommands.GRAPH_RENDER.id,
      {
        graphData: graphData,
      },
    );
    // 居中
    await app.executeCommand<NsGraphCmd.GraphZoom.IArgs>(
      XFlowGraphCommands.GRAPH_ZOOM.id,
      {
        factor: 'real',
      },
    );
    graphBind();
  };

  const graphBind = useCallback(() => {
    if (graphRef.current) {
      graphRef.current.on('node:click', (...arg) => {
        console.log(arg);
      });
    }
    console.log(graphRef);
  }, [graphRef]);

  return (
    <PageContainer>
      <XFlow
        className="flow-user-custom-clz"
        commandConfig={commandConfig}
        onLoad={onLoad}
        meta={meta}
      >
        <FlowchartExtension />
        <CustomNodeCollapsePanel />
        <CanvasToolbar
          className="xflow-workspace-toolbar-top"
          layout="horizontal"
          config={toolbarConfig}
          position={{ top: 0, left: 0, right: 0, bottom: 0 }}
        />
        <FlowchartCanvas
          useConfig={useGraphConfig}
          position={{ top: 40, left: 0, right: 0, bottom: 0 }}
        >
          <CanvasScaleToolbar
            layout="horizontal"
            position={{ top: -40, right: 0 }}
            style={{
              width: 150,
              left: 'auto',
              height: 39,
            }}
          />
          <CanvasContextMenu config={menuConfig} />
          <CanvasSnapline color="#faad14" />
          <CanvasNodePortTooltip />
        </FlowchartCanvas>
        <KeyBindings config={keybindingConfig} />
      </XFlow>
    </PageContainer>
  );
};

export default Demo;
