import {
  createGraphConfig,
  createHookConfig,
  DisposableCollection,
  XFlowNodeCommands /* , XFlowEdgeCommands */,
} from '@antv/xflow';
import { CONNECTION_PORT_TYPE, DND_RENDER_ID, ZOOM_OPTIONS } from './constant';
import { BaseNode } from './react-node/base-node';
// import { AlgoNode } from './react-node/algo-node';
// import { GroupNode } from './react-node/group';

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

      // register graph event
      hooks.x6Events.registerHook({
        name: 'add',
        handler: async (events) => {
          events.push({
            eventName: 'node:moved',
            callback: (e, cmds) => {
              const { node } = e;
              cmds.executeCommand(XFlowNodeCommands.MOVE_NODE.id, {
                id: node.id,
                position: node.getPosition(),
              });
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
        if (
          sourcePort === CONNECTION_PORT_TYPE.source &&
          targetPort === CONNECTION_PORT_TYPE.target
        ) {
          return true;
        } else {
          return false;
        }
      },
    },
  });
});
