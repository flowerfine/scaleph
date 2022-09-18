import {
  createGraphConfig,
  createHookConfig,
  DisposableCollection,
  NsNodeCmd,
  NsGraph,
  XFlowNodeCommands, /* , XFlowEdgeCommands */
  NsEdgeCmd,
  XFlowEdgeCommands,
} from '@antv/xflow';
import { XFlowEdge } from '@antv/xflow-extension/es/canvas-dag-extension/x6-extension/edge';
import type { EventArgs } from '@antv/x6/lib/graph/events';
import type { Edge, Graph, Node } from '@antv/x6';
import { CONNECTION_PORT_TYPE, DND_RENDER_ID, ZOOM_OPTIONS } from './constant';
import { BaseNode } from './react-node/base-node';

export namespace NsAddEdgeEvent {
  export const EVENT_NAME = 'ADD_EDGE_CMD_EVENT'
  export interface IArgs {
    targetPortId: string
    sourcePortId: string
    source: string
    target: string
    edge: Edge
  }
}

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
          // renderMap.set(GROUP_NODE_RENDER_ID, GroupNode);
        },
      }),

      // 注册增加 graph event
      hooks.x6Events.registerHook({
        name: 'node_moved',
        handler: async events => {
          events.push({
            eventName: 'node:moved',
            callback: (e, cmds) => {
              console.log("event:node:moved", e);
              console.log("event:node:moved cmd", cmds);
              const { node } = e
              cmds.executeCommand<NsNodeCmd.MoveNode.IArgs>(XFlowNodeCommands.MOVE_NODE.id, {
                id: node.id,
                position: node.getPosition(),
              })
            },
          } as NsGraph.IEvent<'node:moved'>)
        },
      }),
      // hooks.x6Events.registerHook({
      //   name: 'edge_connected',
      //   handler: async events => {
      //     events.push({
      //       eventName: 'edge:connected',
      //       callback: (e, cmds) => {
      //         console.log("event:edge:connected", e);
      //         console.log("event:edge:connected cmd", cmds);
      //         const { edge } = e;
      //         cmds.executeCommand<NsEdgeCmd.AddEdge.IArgs>(XFlowEdgeCommands.ADD_EDGE.id, {
      //           edgeConfig: { ...edge }
      //         })
      //       }
      //     });
      //   }
      // }),
      hooks.x6Events.registerHook({
        name: 'edge_delete',
        handler: async events => {
          events.push({
            eventName: 'edge:removed',
            callback: (e, cmds) => {
              console.log("event:edge:removed", e);
              console.log("event:edge:removed cmd", cmds);
            }
          });
        }
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
          console.log('edge cell:', edgeCell);
          if (isNew && edgeCell.isEdge() && edgeCell === edge) {
            const portId = edgeCell.getTargetPortId();
            const targetNode = edgeCell.getTargetCell();
            if (targetNode && targetNode.isNode()) {
              targetNode.setPortProp(portId + '', 'connected', false);
              // edgeCell.attr({
              //   line: {
              //     strokeDasharray: '',
              //     targetMarker: '',
              //     stroke: '#d5d5d5',
              //   },
              // })
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
        }
        graph.once('edge:connected', addEdge);
        console.log('edge:connected3131', edge);
        return edge
      },
      validateEdge: args => {
        const { edge } = args
        return !!(edge?.target as any)?.port
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
          if (!sourcePortId) { return false }
          const sPort = sourceNode.getPort(sourcePortId);
          if (sPort.type !== NsGraph.AnchorType.OUTPUT) { return false }
          const targetNode = targetView?.cell as any;
          const targetPortId = targetMagnet.getAttribute('port');
          if (!targetPortId) { return false; }
          const tPort = targetNode.getPort(targetPortId);
          if (tPort.type !== NsGraph.AnchorType.INPUT) {
            return false;
          }
          console.log('TPORT', tPort);
          return true;
        }
      },
    },
  });
});
