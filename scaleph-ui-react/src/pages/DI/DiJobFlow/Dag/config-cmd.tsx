import { createCmdConfig, DisposableCollection, IApplication, NsEdgeCmd, NsGraph, NsNodeCmd, uuidv4, XFlowEdgeCommands, XFlowGraphCommands } from '@antv/xflow'
// import { MockApi } from './service'
import { commandContributions } from './cmd-extensions'
import { NODE_HEIGHT, NODE_WIDTH } from './constant'
import { XFlowEdge } from '@antv/xflow-extension/es/canvas-dag-extension/x6-extension/edge';
import { XFlowNode } from '@antv/xflow-extension/es/canvas-dag-extension/x6-extension/node';
import { NsAddEdgeEvent } from './config-graph';

export const useCmdConfig = createCmdConfig(config => {
  // 注册全局Command扩展
  config.setCommandContributions(() => commandContributions)
  // 设置hook
  config.setRegisterHookFn(hooks => {
    const list = [
      hooks.graphMeta.registerHook({
        name: 'get graph meta',
        handler: async args => {
          // args.graphMetaService = MockApi.queryGraphMeta
          console.log('get graph meta cmd', args);
        },
      }),
      hooks.saveGraphData.registerHook({
        name: 'save graph data',
        handler: async args => {
          if (!args.saveGraphDataService) {
            console.log('save graph cmd', args);
            // args.saveGraphDataService = MockApi.saveGraphData
          }
        },
      }),
      hooks.addNode.registerHook({
        name: 'add node',
        handler: async args => {
          console.log('add node cmd', args);
          const cellFactory: NsNodeCmd.AddNode.IArgs['cellFactory'] = async nodeConfig => {
            const node = new XFlowNode({
              ...nodeConfig,
            })
            return node
          }
          args.cellFactory = cellFactory;
          args.createNodeService = async args => {
            console.log('add node service running', args);
            const portItems = [
              {
                type: NsGraph.AnchorType.INPUT,
                group: NsGraph.AnchorGroup.TOP,
                tooltip: '123123'
              },
              {
                type: NsGraph.AnchorType.INPUT,
                group: NsGraph.AnchorGroup.BOTTOM,
                tooltip: '243554'
              }
            ] as NsGraph.INodeAnchor[];
            const { id, ports = portItems } = args.nodeConfig;
            const nodeId = id || uuidv4();
            const node: NsNodeCmd.AddNode.IArgs['nodeConfig'] = {
              ...args.nodeConfig,
              id: nodeId,
              width: NODE_WIDTH,
              height: NODE_HEIGHT,
              // ports: (ports as NsGraph.INodeAnchor[]).map(port => {
              //   return { ...port, id: uuidv4() }
              // })
              ports: {
                groups: {
                  top: {
                    position: 'top',
                    attrs: {
                      circle: {
                        r: 4,
                        magnet: true,
                        stroke: '#31d0c6',
                        strokeWidth: 2,
                        fill: '#fff',
                      },
                    },
                  },
                  bottom: {
                    position: 'bottom',
                    attrs: {
                      circle: {
                        r: 4,
                        magnet: true,
                        stroke: '#31d0c6',
                        strokeWidth: 2,
                        fill: '#fff',
                      },
                    },
                  },
                },
                items: createPortItems(args.nodeConfig.data.type, args.nodeConfig.data.name)
              },
            }
            console.log("111111111 node info", node)
            return node;
          }
        },
      }),
      hooks.addEdge.registerHook({
        name: 'dag-add-edge',
        handler: async args => {
          const cellFactory: NsEdgeCmd.AddEdge.IArgs['cellFactory'] = async edgeConfig => {
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
              // attrs: {
              //   line: {
              //     strokeDasharray: '',
              //     targetMarker: '',
              //     stroke: '#d5d5d5',
              //     strokeWidth: 1,
              //   },
              // },
              data: { ...edgeConfig },
            })
            return cell
          }
          args.cellFactory = cellFactory
        },
      }),
      hooks.afterGraphInit.registerHook({
        name: 'call add edge to replace temp edge',
        handler: async handlerArgs => {
          const { commandService, graph } = handlerArgs
          graph.on(NsAddEdgeEvent.EVENT_NAME, (args: NsAddEdgeEvent.IArgs) => {
            console.log('add edge........ run ', args);
            commandService.executeCommand(XFlowEdgeCommands.ADD_EDGE.id, {
              edgeConfig: {
                id: uuidv4(),
                source: args.source,
                target: args.target,
                sourcePortId: args.sourcePortId,
                targetPortId: args.targetPortId,
              }
            } as NsEdgeCmd.AddEdge.IArgs)
            args.edge.remove()
          })
        },
      }),
      hooks.addEdge.registerHook({
        name: 'get edge config from backend api',
        handler: async args => {
          console.log('asdfasdf', args);
          args.createEdgeService = async args => {
            console.log('456467657', args);
            const { edgeConfig } = args;
            console.log("545345634", {
              ...edgeConfig,
              // id: uuidv4(),
            })
            return edgeConfig
            // id: uuidv4(),

          }
        },
      }),
    ]
    const toDispose = new DisposableCollection()
    toDispose.pushAll(list)
    return toDispose
  })
})

const createPortItems = (type: string, label: string) => {
  if (type === 'source') {
    const items: NsGraph.INodeAnchor[] = [
      {
        id: uuidv4(),
        group: NsGraph.AnchorGroup.BOTTOM,
        type: NsGraph.AnchorType.OUTPUT
      }
    ];
    return items;
  } else if (type === 'trans') {
    const items: NsGraph.INodeAnchor[] = [
      {
        id: uuidv4(),
        group: NsGraph.AnchorGroup.BOTTOM,
        type: NsGraph.AnchorType.OUTPUT
      },
      {
        id: uuidv4(),
        group: NsGraph.AnchorGroup.TOP,
        type: NsGraph.AnchorType.INPUT
      }
    ];
    return items;
  } else if (type === 'sink') {
    const items: NsGraph.INodeAnchor[] = [
      {
        id: uuidv4(),
        group: NsGraph.AnchorGroup.TOP,
        type: NsGraph.AnchorType.INPUT
      }
    ];
    return items;
  } else {
    return [];
  }
}

/** 查询图的节点和边的数据 */
export const initGraphCmds = (app: IApplication) => {
  app.executeCommandPipeline([
    /** 1. 从服务端获取数据 */
    // {
    //   commandId: XFlowGraphCommands.LOAD_DATA.id,
    //   getCommandOption: async () => {
    //     return {
    //       args: {
    //         loadDataService: MockApi.loadGraphData,
    //       },
    //     }
    //   },
    // },
    // /** 2. 执行布局算法 */
    // {
    //   commandId: XFlowGraphCommands.GRAPH_LAYOUT.id,
    //   getCommandOption: async ctx => {
    //     const { graphData } = ctx.getResult()
    //     return {
    //       args: {
    //         layoutType: 'dagre',
    //         layoutOptions: {
    //           type: 'dagre',
    //           /** 布局方向 */
    //           rankdir: 'TB',
    //           /** 节点间距 */
    //           nodesep: 60,
    //           /** 层间距 */
    //           ranksep: 30,
    //         },
    //         graphData,
    //       },
    //     }
    //   },
    // },
    // /** 3. 画布内容渲染 */
    // {
    //   commandId: XFlowGraphCommands.GRAPH_RENDER.id,
    //   getCommandOption: async ctx => {
    //     const { graphData } = ctx.getResult()
    //     return {
    //       args: {
    //         graphData,
    //       },
    //     }
    //   },
    // },
    /** 4. 缩放画布 */
    {
      commandId: XFlowGraphCommands.GRAPH_ZOOM.id,
      getCommandOption: async () => {
        return {
          args: { factor: 'fit', zoomOptions: { maxScale: 0.9 } },
        }
      },
    },
  ])
}
