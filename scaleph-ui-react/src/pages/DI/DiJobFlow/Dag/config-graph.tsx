import type { Edge } from '@antv/x6';
import type { EventArgs } from '@antv/x6/lib/graph/events';
import {
  createGraphConfig,
  createHookConfig,
  DisposableCollection,
  NsGraph,
  NsNodeCmd,
  XFlowNodeCommands,
} from '@antv/xflow';
import { XFlowEdge } from '@antv/xflow-extension/es/canvas-dag-extension/x6-extension/edge';
import { CustomCommands, DND_RENDER_ID, ZOOM_OPTIONS } from './constant';
import { BaseNode } from './react-node/base-node';

export namespace NsAddEdgeEvent {
  export const EVENT_NAME = 'ADD_EDGE_CMD_EVENT';
  export interface IArgs {
    targetPortId: string;
    sourcePortId: string;
    source: string;
    target: string;
    edge: Edge;
  }
}

/** graph hook config */
export const useGraphHookConfig = createHookConfig((config, proxy) => {
  // 获取 Props
  const props = proxy.getValue();

  config.setRegisterHook((hooks) => {
    const disposableList = [
      // register react Node Render
      hooks.reactNodeRender.registerHook({
        name: 'add react node',
        handler: async (renderMap) => {
          renderMap.set(DND_RENDER_ID, BaseNode);
        },
      }),

      // 注册增加 graph event
      hooks.x6Events.registerHook({
        name: 'node_moved',
        handler: async (events) => {
          events.push({
            eventName: 'node:moved',
            callback: (e, cmds) => {
              const { node } = e;
              cmds.executeCommand<NsNodeCmd.MoveNode.IArgs>(XFlowNodeCommands.MOVE_NODE.id, {
                id: node.id,
                position: node.getPosition(),
              });
            },
          } as NsGraph.IEvent<'node:moved'>);
        },
      }),
      hooks.x6Events.registerHook({
        name: 'edge_delete',
        handler: async (events) => {
          events.push({
            eventName: 'edge:removed',
            callback: (e, cmds) => {},
          });
        },
      }),
      hooks.x6Events.registerHook({
        name: 'node_dbclick',
        handler: async (events) => {
          events.push({
            eventName: 'node:dblclick',
            callback: (e, cmds) => {
              const { node } = e;
              cmds.executeCommand(CustomCommands.NODE_EDIT.id, { nodeConfig: node });
            },
          });
        },
      }),
    ];
    const toDispose = new DisposableCollection();
    toDispose.pushAll(disposableList);
    return toDispose;
  });
});

export const useGraphCOnfig = createGraphConfig((config) => {
  config.setX6Config({
    grid: {
      size: 10,
      visible: true,
      type: 'doubleMesh',
      args: [
        {
          color: '#E7E8EA',
          thickness: 1,
        },
        {
          color: '#CBCED3',
          thickness: 1,
          factor: 4,
        },
      ],
    },
    scaling: { min: ZOOM_OPTIONS.minScale, max: ZOOM_OPTIONS.maxScale },
    mousewheel: { enabled: true, zoomAtMousePosition: true },
    history: {
      enabled: true,
    },
    background: {
      color: '#F8F8FA', // 设置画布背景颜色
    },
    clipboard: {
      enabled: true,
    },
    panning: {
      enabled: true,
      modifiers: 'shift',
    },
    selecting: {
      enabled: true,
      rubberband: true,
      multiple: true,
      movable: true,
      strict: true,
      showNodeSelectionBox: true,
      showEdgeSelectionBox: true,
    },
    connecting: {
      snap: true,
      allowBlank: false,
      allowMulti: false,
      allowLoop: false,
      allowNode: false,
      allowEdge: false,
      connector: 'smooth',
      highlight: true,
      createEdge(this, args) {
        const graph = this;
        const edge = new XFlowEdge({});
        const addEdge = (args: EventArgs['edge:connected']) => {
          const { isNew } = args;
          const edgeCell = args.edge;
          if (isNew && edgeCell.isEdge() && edgeCell === edge) {
            const portId = edgeCell.getTargetPortId();
            const targetNode = edgeCell.getTargetCell();
            if (targetNode && targetNode.isNode()) {
              targetNode.setPortProp(portId + '', 'connected', false);
              const targetPortId = edgeCell.getTargetPortId();
              const sourcePortId = edgeCell.getSourcePortId();
              const sourceCellId = edgeCell.getSourceCellId();
              const targetCellId = edgeCell.getTargetCellId();
              graph.trigger(NsAddEdgeEvent.EVENT_NAME, {
                targetPortId,
                sourcePortId,
                source: sourceCellId,
                target: targetCellId,
                edge: edge,
              } as NsAddEdgeEvent.IArgs);
            }
          }
        };
        graph.once('edge:connected', addEdge);
        return edge;
      },
      validateEdge: (args) => {
        const { edge } = args;
        return !!(edge?.target as any)?.port;
      },
      validateConnection({
        edge,
        edgeView,
        sourceView,
        targetView,
        sourcePort,
        targetPort,
        sourceMagnet,
        targetMagnet,
        sourceCell,
        targetCell,
        type,
      }) {
        if (sourceView === targetView) {
          return false;
        } else if (!sourceMagnet || !targetMagnet) {
          return false;
        } else {
          const sourceNode = sourceView?.cell as any;
          const sourcePortId = sourceMagnet.getAttribute('port');
          if (!sourcePortId) {
            return false;
          }
          const sPort = sourceNode.getPort(sourcePortId);
          if (sPort.type !== NsGraph.AnchorType.OUTPUT) {
            return false;
          }
          const targetNode = targetView?.cell as any;
          const targetPortId = targetMagnet.getAttribute('port');
          if (!targetPortId) {
            return false;
          }
          const tPort = targetNode.getPort(targetPortId);
          if (tPort.type !== NsGraph.AnchorType.INPUT) {
            return false;
          }
          return true;
        }
      },
    },
  });
});
