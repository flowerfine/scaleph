import {
  createCmdConfig,
  DisposableCollection,
  IApplication,
  NsEdgeCmd,
  NsNodeCmd,
  uuidv4,
  XFlowEdgeCommands,
  XFlowGraphCommands,
} from '@antv/xflow';
import { DiJob } from '@/services/project/typings';
import { XFlowEdge } from '@antv/xflow-extension/es/canvas-dag-extension/x6-extension/edge';
import { XFlowNode } from '@antv/xflow-extension/es/canvas-dag-extension/x6-extension/node';
import { commandContributions } from './cmd-extensions';
import { NsAddEdgeEvent } from './config-graph';
import { DND_RENDER_ID, NODE_HEIGHT, NODE_WIDTH } from './constant';
import { DagService } from './service';

export const useCmdConfig = createCmdConfig((config) => {
  // 注册全局Command扩展
  config.setCommandContributions(() => commandContributions);
  // 设置hook
  config.setRegisterHookFn((hooks) => {
    const list = [
      hooks.graphMeta.registerHook({
        name: 'get graph meta',
        handler: async (args) => {},
      }),
      hooks.saveGraphData.registerHook({
        name: 'save graph data',
        handler: async (args) => {
          if (!args.saveGraphDataService) {
            args.saveGraphDataService = DagService.saveGraphData;
          }
        },
      }),
      hooks.addNode.registerHook({
        name: 'add node',
        handler: async (args) => {
          const cellFactory: NsNodeCmd.AddNode.IArgs['cellFactory'] = async (nodeConfig) => {
            const node = new XFlowNode({
              ...nodeConfig,
            });
            return node;
          };
          args.cellFactory = cellFactory;
          args.createNodeService = async (args) => {
            const { id } = args.nodeConfig;
            const nodeId = id || uuidv4();
            const node: NsNodeCmd.AddNode.IArgs['nodeConfig'] = {
              ...args.nodeConfig,
              id: nodeId,
              width: NODE_WIDTH,
              height: NODE_HEIGHT,
              renderKey: DND_RENDER_ID,
              ports: DagService.createPorts(args.nodeConfig.data.type),
            };
            return node;
          };
        },
      }),
      hooks.addEdge.registerHook({
        name: 'dag-add-edge',
        handler: async (args) => {
          const cellFactory: NsEdgeCmd.AddEdge.IArgs['cellFactory'] = async (edgeConfig) => {
            const cell = new XFlowEdge({
              ...edgeConfig,
              id: edgeConfig.id,
              source: {
                cell: edgeConfig.source,
                port: edgeConfig.sourcePortId,
              },
              target: {
                cell: edgeConfig.target,
                port: edgeConfig.targetPortId,
              },
              data: { ...edgeConfig },
            });
            return cell;
          };
          args.cellFactory = cellFactory;
        },
      }),
      hooks.afterGraphInit.registerHook({
        name: 'call add edge to replace temp edge',
        handler: async (handlerArgs) => {
          const { commandService, graph } = handlerArgs;
          graph.on(NsAddEdgeEvent.EVENT_NAME, (args: NsAddEdgeEvent.IArgs) => {
            commandService.executeCommand(XFlowEdgeCommands.ADD_EDGE.id, {
              edgeConfig: {
                id: uuidv4(),
                source: args.source,
                target: args.target,
                sourcePortId: args.sourcePortId,
                targetPortId: args.targetPortId,
              },
            } as NsEdgeCmd.AddEdge.IArgs);
            args.edge.remove();
          });
        },
      }),
      hooks.addEdge.registerHook({
        name: 'get edge config from backend api',
        handler: async (args) => {
          args.createEdgeService = async (args) => {
            const { edgeConfig } = args;
            return edgeConfig;
          };
        },
      }),
    ];
    const toDispose = new DisposableCollection();
    toDispose.pushAll(list);
    return toDispose;
  });
});

/** 查询图的节点和边的数据 */
export const initGraphCmds = (app: IApplication, job: DiJob) => {
  app.executeCommandPipeline([
    {
      commandId: XFlowGraphCommands.GRAPH_ZOOM.id,
      getCommandOption: async () => {
        return {
          args: { factor: 'fit', zoomOptions:{ maxScale: 0.9 }},
        };
      },
    },
  ]);
};
